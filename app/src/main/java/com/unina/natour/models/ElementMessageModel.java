package com.unina.natour.models;

import java.util.Calendar;

public class ElementMessageModel extends NaTourModel{

    public final static int CODE_MESSAGE_SENT = 0;
    public final static int CODE_MESSAGE_RECEIVED = 1;

    private String message;
    private int type;
    private Calendar time;


    public ElementMessageModel(){
        super();

        this.message = "";
        this.type = -1;
        this.time = Calendar.getInstance();
    }

    public ElementMessageModel(String message, boolean didISendIt){
        super();

        this.message = message;
        if(didISendIt) this.type = CODE_MESSAGE_SENT;
        else this.type = CODE_MESSAGE_RECEIVED;
        this.time = Calendar.getInstance();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public boolean didISendIt(){
        if (type == CODE_MESSAGE_SENT) return true;
        return false;
    }

    public void clear(){
        this.message = null;
        this.time = null;
        this.type = -1;
    }
}
