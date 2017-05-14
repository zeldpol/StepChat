package com.example.rinsv.stepchat;

import java.util.Date;

/**
 * Created by rinsv on 06.04.2017.
 */

public class Message {

    private String textMessage;
    private String autor;
    private long timeMessage;

    public boolean left;

    public Message(String textMessage, String autor, boolean left) {
        this.textMessage = textMessage;
        this.autor = autor;
        this.left = left;

        timeMessage = new Date().getTime();
    }

    public Message() {
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public long getTimeMessage() {
        return timeMessage;
    }

    public void setTimeMessage(long timeMessage) {
        this.timeMessage = timeMessage;
    }


}
