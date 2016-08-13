package com.javarush.test.level30.lesson15.big01;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Connection class
 * the connection between the client and the server class
 * 17.05.2016
 * Autor by TheZalesskie
 */
public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    //Constructor
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    //read message - I must write ( serialize ) message in the message ObjectOutputStream
    public void send(Message message) throws IOException {
        synchronized (out) {
            out.writeObject(message);
            out.flush();
        }
    }

    //write data - should read ( deserialized) data from ObjectInputStream
    public Message receive() throws IOException,
            ClassNotFoundException{
        Message message;
        synchronized (in){
            message = (Message) in.readObject();
            return message;
        }
    }

    //return remote address - returns the address of a remote socket connection
    public SocketAddress getRemoteSocketAddress(){
        return socket.getRemoteSocketAddress();
    }

    //close
    public void close() throws IOException{
        in.close();
        out.close();
        socket.close();

    }


}
