package com.javarush.test.level30.lesson15.big01;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Connection class
 * класс соединения между клиентом и сервером
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

    //read message - должен записывать(сериализовать) сообщение message в ObjectOutputStream
    public void send(Message message) throws IOException {
        synchronized (out) {
            out.writeObject(message);
            out.flush();
        }
    }

    //write data - должен читать (десериализовать) данные из ObjectInputStream
    public Message receive() throws IOException,
            ClassNotFoundException{
        Message message;
        synchronized (in){
            message = (Message) in.readObject();
            return message;
        }
    }

    //return remote address - возвращающий удаленный адрес сокетного соединения
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
