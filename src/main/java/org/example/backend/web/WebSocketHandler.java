package org.example.backend.web;

import org.example.backend.chat.ChatService;
import org.example.backend.chat.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketHandler {

    private final ChatService chatService;

    @Autowired
    public WebSocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.login")
    public void sendLoginMessage(String loginId) {
        chatService.sendLoginMessage(loginId);
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message, Principal principal) {
        chatService.sendMessage(message, principal);
    }
}