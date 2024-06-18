package org.example.backend.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //stomp의 접속 주소
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트에서 보내는 요청을 -> pub
        registry.setApplicationDestinationPrefixes("/pub");

        // 클라이언트에게 보내는 응답을 -> sub
        registry.enableSimpleBroker("/sub");
    }


}