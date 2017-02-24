package com.yahs.king.robot.bean;

import java.util.Date;


/**
 * Created by king on 2017/2/19.
 */

public class ChatMessage {
    private String name;

    public ChatMessage(){

    }
    public ChatMessage(String msg, Type type) {
        super();
        this.msg= msg;
        this.type= type;
        //this.date =date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Type getType() {

        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMsg() {

        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String msg;
    private Type type;
    private Date date;
    public enum Type{
        INCOMING ,OUTCOMING
    }
}
