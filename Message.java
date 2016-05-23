package com.javarush.test.level30.lesson15.big01;

import java.io.Serializable;

/**
 * Message class
 * класс, отвечающий за пересылаемые сообщения
 * 17.05.2016
 * Autor by TheZalesskie
 */
public class Message implements Serializable {
    //тип сообщения
    private final MessageType type;
    //данные сообщения
    private final String data;

    public Message(MessageType type) {
        this.type = type;
        this.data = null;
    }

    public Message(MessageType type, String data) {
        this.type = type;
        this.data = data;
    }

    public MessageType getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
