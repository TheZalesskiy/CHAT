package com.javarush.test.level30.lesson15.big01.client;

import com.javarush.test.level30.lesson15.big01.Connection;
import com.javarush.test.level30.lesson15.big01.ConsoleHelper;
import com.javarush.test.level30.lesson15.big01.Message;
import com.javarush.test.level30.lesson15.big01.MessageType;


import java.io.IOException;
import java.net.Socket;


import static com.javarush.test.level30.lesson15.big01.ConsoleHelper.readString;

/**
 * Client class
 * date: 21.05.2016
 *
 * @autor TheZalesskie
 */
public class Client {

    protected Connection connection;
    private volatile boolean clientConnected = false;


    /** PSVM Client **/
    public static void main(String[] args) {

        Client client = new Client();
        client.run();
    }


    /** Methods **/
    /** run **/
    public void run() {

        // To create a new stream of the socket using the method getSocketThread
        SocketThread socketThread = getSocketThread();
        // Display an established flow as a daemon , it is necessary to exit
        // From the program helper thread is automatically stopped.
        socketThread.setDaemon(true);
        // Run the helper thread
        socketThread.start();

        // Making the current thread to wait until it receives notification from another thread
        try {
            synchronized (this) {
                this.wait();
            }
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage("Eror");
            return;
        }

        //After a stream of wait notification , check the value of clientConnected
        if (clientConnected) {
            ConsoleHelper.writeMessage("The connection is established . To exit, type the command 'exit'.");

            //Reads the message from the console while the client is connected . If the 'exit' command is entered , then come out of the loop
            while (clientConnected) {
                String message;
                if (!(message = ConsoleHelper.readString()).equals("exit")) {
                    if (shouldSentTextFromConsole()) {
                        sendTextMessage(message);
                    }
                } else {
                    return;
                }
            }
        }
        else {
            ConsoleHelper.writeMessage("An error occurred during client work.");
        }
    }


    /** It should seek to enter the server address and return the entered value**/
    protected String getServerAddress() {

        ConsoleHelper.writeMessage("Enter the server address : ");
        return ConsoleHelper.readString();
    }


    /** Must ask for the server port entry and return it **/
    protected int getServerPort() {

        ConsoleHelper.writeMessage("Enter the server port: ");
        return ConsoleHelper.readInt();
    }


    /** Must request and return the user name **/
    protected String getUserName() {

        ConsoleHelper.writeMessage("Enter your username : ");
        return ConsoleHelper.readString();
    }


    protected boolean shouldSentTextFromConsole() {

        return true;
    }


    /** should create and return a new object of class SocketThread **/
    protected SocketThread getSocketThread() {

        return new SocketThread();
    }


    /**  It creates a new text message using the transmitted text and sends it to the server through the connection **/
    protected void sendTextMessage(String text) {

        try {
            connection.send(new Message(MessageType.TEXT, text));

        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error sending");
            clientConnected = false;
        }
    }


    /** SocketThread **/
    public class SocketThread extends Thread {

        /** Methods **/
        public void run() {

            try {
                // Create a new class object java.net.Socket c server request and port
                Socket socket = new Socket(getServerAddress(), getServerPort());

                // Make a Connection object class using a socket
                Client.this.connection = new Connection(socket);


                clientHandshake();
                clientMainLoop();


            } catch (IOException e) {
                notifyConnectionStatusChanged(false);
            } catch (ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            }

        }


        /** This method will implement the main loop processing server message **/
        protected void clientMainLoop() throws IOException, ClassNotFoundException {

            while (true) {

                // In the cycle of receiving messages using the Connect connection
                Message message = connection.receive();

                switch (message.getType()) {

                    // If it is a text message (type TEXT), process it using the method processIncomingMessage()
                    case TEXT:
                        processIncomingMessage(message.getData());
                        break;

                    // If the message with the type of USER_ADDED, process it using the method informAboutAddingNewUser()
                    case USER_ADDED:
                        informAboutAddingNewUser(message.getData());
                        break;

                    // If the message with the type of USER_REMOVED, process it using the method informAboutDeletingNewUser()
                    case USER_REMOVED:
                        informAboutDeletingNewUser(message.getData());
                        break;

                    default:
                        throw new IOException("Unexpected MessageType");
                }
            }
        }


        /** clientHandshake **/
        protected void clientHandshake() throws IOException, ClassNotFoundException {

            while (true) {

                // In a series of receive messages using the compound connection
                Message message = connection.receive();

                switch (message.getType()) {

                    // 	If the type of the received message NAME_REQUEST ( requested server name)
                    case NAME_REQUEST: {

                        // Request a user name using getUserName () method
                        // Create a new message with the type USER_NAME and entered the name , send a message to the server .
                        String userName = getUserName();
                        connection.send(new Message(MessageType.USER_NAME, userName));
                        break;
                    }

                    // If the type of the received message NAME_ACCEPTED ( server took the name )
                    case NAME_ACCEPTED: {

                        // Means the server accepted the client's name , it is necessary to inform about this the main thread , that he is waiting for .
                        // Do this by using the method notifyConnectionStatusChanged (), passing it true. After that come out of the method.
                        notifyConnectionStatusChanged(true);
                        return;
                    }

                    default: {
                        throw new IOException("Unexpected MessageType");
                    }
                }
            }
        }


        /** must display the text message to the console **/
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }


        /** must display information in the console that the party named userName joined the chat **/
        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage("participant " + userName + " joined the chat");
        }


        /**  should output to the console that the participant named userName left the chat **/
        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage("participant " + userName + " left the chat");
        }


        /** Уrehydrating field value clientConnected Client Class in accordance with the
         pass parameters . Notify ( waiting to awaken ) the main stream class Client **/
        protected void notifyConnectionStatusChanged(boolean clientConnected) {


            Client.this.clientConnected = clientConnected;

            synchronized (Client.this) {
                Client.this.notify();
            }
        }
    }
}
