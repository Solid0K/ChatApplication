package com.krishu.chatapplication.WebSocket;

import com.krishu.chatapplication.DTO.ChatMessage;
import com.krishu.chatapplication.Service.JWTService;
import com.krishu.chatapplication.Service.MessageService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectmapper;
    private MessageService messageservice;
    private JWTService jwtService;

    public ChatWebSocketHandler(ObjectMapper objectmapper,MessageService messageservice,JWTService jwtService){
        this.objectmapper=objectmapper;
        this.messageservice=messageservice;
        this.jwtService=jwtService;
    }

    private Set<WebSocketSession> sessions= ConcurrentHashMap.newKeySet();
    private Map<String,String> sessionUser=new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        String query=session.getUri().getQuery();
        if(query!=null && query.startsWith("token=")){
            String token=query.substring(6);
            String username=jwtService.extractUsername(token);
            if(jwtService.validateToken(token,username)){
                sessionUser.put(session.getId(), username);
            }
        }
        System.out.println("Connected: "+session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session,WebSocketMessage<?> message) throws Exception {
        String sender=sessionUser.get(session.getId());
        ChatMessage chatmessage=objectmapper.readValue(message.getPayload().toString(),ChatMessage.class);
        ChatMessage response=new ChatMessage();
        response.setUsername(sender);
        response.setMessage(chatmessage.getMessage());
        System.out.println("Receice message: "+response.getMessage());
        messageservice.addMessage(sender,response.getMessage());
        String messageTransmit=objectmapper.writeValueAsString(response);
        for(WebSocketSession s:sessions){
            if(s.isOpen()){
                s.sendMessage(new TextMessage(messageTransmit));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        sessionUser.remove(session.getId());
        System.out.println("Connection closed: "+session.getId());
    }

}
