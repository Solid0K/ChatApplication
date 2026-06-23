package com.krishu.chatapplication.DTO;

import jakarta.validation.constraints.NotBlank;

public class ChatMessage {
    @NotBlank
    private String sender;
    @NotBlank
    private String message;

    public ChatMessage(){}

    public ChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
