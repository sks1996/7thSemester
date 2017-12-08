package com.example.a1405264.jarvis;
/**
 * Created by JohnConnor on 28-Apr-17.
 */

public class ChatModel {

    private String message;
    public boolean status;
    private int action;
    private int from;
    private String extraMessage;

    public String getExtraMessage() {
        return extraMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

    public ChatModel(String message, boolean status, int action, int from, String extraMessage) {
        this.message = message;
        this.status = status;
        this.action = action;
        this.from = from;
        this.extraMessage = extraMessage;
    }
    public ChatModel(String message, boolean status, int action, int from) {
        this.message = message;
        this.status = status;
        this.action = action;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
