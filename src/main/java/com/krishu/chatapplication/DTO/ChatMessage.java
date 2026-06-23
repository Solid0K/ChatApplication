package com.krishu.chatapplication.DTO;

import jakarta.validation.constraints.NotBlank;

public class ChatMessage {
    private String username;
    @NotBlank
    private String message;

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
}
