package com.krishu.chatapplication.WebSocket;

import com.krishu.chatapplication.DTO.ChatMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.ObjectMapper;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectmapper;

    public ChatWebSocketHandler(ObjectMapper objectmapper){
        this.objectmapper=objectmapper;
    }

    private Set<WebSocketSession> sessions= ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Connected: "+session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session,WebSocketMessage<?> message) throws Exception {
        ChatMessage chatmessage=objectmapper.readValue(message.getPayload().toString(),ChatMessage.class);
        System.out.println("Sender: "+chatmessage.getSender());
        System.out.println("Receice message: "+chatmessage.getMessage());
        String messageTransmit=objectmapper.writeValueAsString(chatmessage);
        for(WebSocketSession s:sessions){
            if(s.isOpen()){
                s.sendMessage(new TextMessage(messageTransmit));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Connection closed: "+session.getId());
    }

}
