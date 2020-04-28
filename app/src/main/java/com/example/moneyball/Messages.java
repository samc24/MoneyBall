package com.example.moneyball;

public class Messages {    ///created Messages object. This is used by the Message Adapter when collecting a messages children
    private String name,message,date,time;

    public Messages(){

    }

    public Messages(String name,String message,String date,String time){
        this.name = name;
        this.message =message;
        this.date =date;
        this.time= time;

    }

    public String getName() {
        return name;
    }   //returns user's name. Used in MessageAdapter

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    } //returns user's message. Used in MessageAdapter

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    } //returns date of message sent. Used in MessageAdapter

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    } //returns time of message sent. Used in MessageAdapter

    public void setTime(String time) {
        this.time = time;
    }
}
