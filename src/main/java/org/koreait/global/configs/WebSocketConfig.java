package org.koreait.global.configs;

import org.koreait.message.websockets.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private MessageHandler messageHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // ws://localhost:3000/message
        String profile = System.getenv("spring.profiles.active");
        registry.addHandler(messageHandler, "msg")
        .setAllowedOrigins(profile.contains("prod") ? "" : "http://localhost:3000");
        // 나중에 ""에 내가 원하는 도메인 넣기. 그쪽에서만 메시지 받을 수 있게 만들어주는거.
        // registry.addHandler(messageHandler, "msg").setAllowedOrigins("*");
    }
}
