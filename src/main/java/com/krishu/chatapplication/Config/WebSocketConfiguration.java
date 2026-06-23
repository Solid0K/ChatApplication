package com.krishu.chatapplication.Config;

import com.krishu.chatapplication.Service.MessageService;
import com.krishu.chatapplication.WebSocket.ChatWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    private final ChatWebSocketHandler handler;

    public WebSocketConfiguration(ChatWebSocketHandler handler){
        this.handler=handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler,"/chat").setAllowedOrigins("*");
    }
}
