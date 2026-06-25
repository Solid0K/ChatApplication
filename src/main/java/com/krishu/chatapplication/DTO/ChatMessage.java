package com.krishu.chatapplication.DTO;

import jakarta.validation.constraints.NotBlank;

public class ChatMessage {
    private String username;
    @NotBlank
    private String message;
    private String receiver;
    private String messageType;

    public ChatMessage(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return messageType;
    }

    public void setType(String type) {
        this.messageType = type;
    }
}
