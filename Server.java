package com.javarush.test.level30.lesson15.big01;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server class basic
 * 17.05.2016
 * Autor by TheZalesskie
 */
public class Server {

    // name key customer and the value - the connection with him
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();


    /** MAIN **/
    public static void main(String[] args) throws IOException {
        //Require server port
        ConsoleHelper.writeMessage("Enter the server port: ");
        int serverPort = ConsoleHelper.readInt();

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {

            ConsoleHelper.writeMessage("The server is running");

            while (true) {
                //Listening
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                //Start handler
                handler.start();
            }
        }

    }


    /** Messages sent to all **/
    public static void sendBroadcastMessage(Message message) {

        try {

            for (Connection connection : connectionMap.values()) {
                connection.send(message);
            }

        } catch (Exception e){
            e.printStackTrace();
            ConsoleHelper.writeMessage("Send by message");
        }

    }


    /**Handler Handler , which will occur with the messaging client **/
    private static class Handler extends Thread {

        private Socket socket;

        //Constructor
        public Handler(Socket socket) {

            this.socket = socket;
        }


        @Override
        public void run() {

            ConsoleHelper.writeMessage("Establishing a connection with the address " + socket.getRemoteSocketAddress());
            String clientName = null;
            //created Connection
            try (Connection connection = new Connection(socket)) {
                //Show a message that a new connection to a remote location
                ConsoleHelper.writeMessage("Connecting to the port: " + connection.getRemoteSocketAddress());
                //Calling the method implements a handshake with the client , keeping the name of a new customer
                clientName = serverHandshake(connection);
                //Sent to all participants of the chat information on behalf of the acceding Member ( message type USER_ADDED)
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, clientName));
                //Notify new member of the existing participating
                sendListOfUsers(connection, clientName);
                //Run the main message loop server
                serverMainLoop(connection, clientName);


            } catch (IOException e) {
                ConsoleHelper.writeMessage("An error occurred while communicating with a remote location");
            } catch (ClassNotFoundException e) {
                ConsoleHelper.writeMessage("An error occurred while communicating with a remote location");
            }

            //Once all exceptions are processed , remove the entry from connectionMap
            connectionMap.remove(clientName);
            //and otpravlyalem message other users
            sendBroadcastMessage(new Message(MessageType.USER_REMOVED, clientName));

            ConsoleHelper.writeMessage("Connecting to a remote location closed");

        }

        /** Handshake **/
        private String serverHandshake(Connection connection) throws IOException,
                ClassNotFoundException {

            while (true) {
                // To generate and send a command to request a user name
                connection.send(new Message(MessageType.NAME_REQUEST));
                //Get customer response
                Message message = connection.receive();

                // Check that the command is received from the user's name
                if (message.getType() == MessageType.USER_NAME) {

                    //Get the name , check out the answer, that it is not empty
                    if (message.getData() != null && !message.getData().isEmpty()) {

                        // and a user with the same name is not already connected (use connectionMap)
                        if (connectionMap.get(message.getData()) == null) {

                            // Add a new user and a connection to him connectionMap
                            connectionMap.put(message.getData(), connection);
                            // Send a command to the client is informed that his name is accepted
                            connection.send(new Message(MessageType.NAME_ACCEPTED));

                            // Return accepted name as the return value
                            return message.getData();
                        }
                    }
                }
            }
        }


        /** Sending a list of all users **/
        private void sendListOfUsers(Connection connection, String userName) throws IOException {

            for (String key : connectionMap.keySet()) {
                Message message = new Message(MessageType.USER_ADDED, key);

                if (!key.equals(userName)) {
                    connection.send(message);
                }
            }
        }


        /** The main message loop server **/
        private void serverMainLoop(Connection connection, String userName) throws IOException,
                ClassNotFoundException {

            while (true) {

                Message message = connection.receive();
                // If the received message - a text (type TEXT)
                if (message.getType() == MessageType.TEXT) {

                    String s = userName + ": " + message.getData();

                    Message formattedMessage = new Message(MessageType.TEXT, s);
                    sendBroadcastMessage(formattedMessage);
                } else {
                    ConsoleHelper.writeMessage("Error");
                }
            }
        }
    }
}
