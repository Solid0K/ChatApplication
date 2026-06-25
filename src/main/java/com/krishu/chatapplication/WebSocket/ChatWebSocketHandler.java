package com.krishu.chatapplication.WebSocket;

import com.krishu.chatapplication.DTO.ChatMessage;
import com.krishu.chatapplication.Service.JWTService;
import com.krishu.chatapplication.Service.MessageService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectmapper;
    private final MessageService messageservice;
    private final JWTService jwtService;

    public ChatWebSocketHandler(ObjectMapper objectmapper,MessageService messageservice,JWTService jwtService){
        this.objectmapper=objectmapper;
        this.messageservice=messageservice;
        this.jwtService=jwtService;
    }

    private final Set<WebSocketSession> sessions= ConcurrentHashMap.newKeySet();
    private final Map<String,String> sessionUser=new ConcurrentHashMap<>();
    private final Map<String,WebSocketSession> UserSession=new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query=session.getUri().getQuery();
        if(query!=null && query.startsWith("token=")){
            String token=query.substring(6);
            String username=jwtService.extractUsername(token);
            if(jwtService.validateToken(token,username)){
                sessionUser.put(session.getId(), username);
                UserSession.put(username,session);
                sessions.add(session);
                for(WebSocketSession s:sessions){
                    if(s.isOpen() && !s.equals(session)){
                        s.sendMessage(new TextMessage(username+" has joined the chat"));
                    }
                }
            }else{
                session.close();
                return;
            }
        }
        System.out.println("Connected: "+session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,TextMessage message) throws Exception {
        String sender=sessionUser.get(session.getId());
        ChatMessage chatmessage=objectmapper.readValue(message.getPayload(),ChatMessage.class);
        ChatMessage response=new ChatMessage();
        response.setUsername(sender);
        response.setMessage(chatmessage.getMessage());
        response.setReceiver(chatmessage.getReceiver());
        response.setType(chatmessage.getType());
        if("private".equalsIgnoreCase(response.getType())){
            System.out.println("Private receive message: "+response.getMessage());
            String receiver= chatmessage.getReceiver();
            WebSocketSession receiverSession=UserSession.get(receiver);
            if(receiverSession!=null && receiverSession.isOpen()){
                String privateMessage=objectmapper.writeValueAsString(response);
                receiverSession.sendMessage(new TextMessage(privateMessage));
                session.sendMessage(new TextMessage(privateMessage));
            }else{
                ChatMessage errorMessage = new ChatMessage();
                errorMessage.setType("ERROR");
                errorMessage.setMessage("User " +response.getReceiver() + " is offline");
                String error = objectmapper.writeValueAsString(errorMessage);
                session.sendMessage(new TextMessage(error));
            }
            return;
        }
        System.out.println("Receive message: "+response.getMessage());
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
        for(WebSocketSession s:sessions){
            if(s.isOpen() && !s.equals(session)){
                s.sendMessage(new TextMessage(sessionUser.get(session.getId())+" has left the chat"));
            }
        }
        UserSession.remove(sessionUser.get(session.getId()));
        sessions.remove(session);
        sessionUser.remove(session.getId());
        System.out.println("Connection closed: "+session.getId());
    }

    public Set<String> onlineUser(){
        return new HashSet<>(sessionUser.values());
    }
}
