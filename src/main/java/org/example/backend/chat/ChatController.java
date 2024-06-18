package org.example.backend.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chatroom")
    public ResponseEntity<Long> getChatPage(Principal principal) {
        Long userId = chatService.getAuthenticatedUserId(principal);
        if (userId != null) {
            System.out.println("Authenticated User ID: " + userId);
            return ResponseEntity.ok(userId);
        } else {
            // 사용자가 인증되지 않은 경우 처리할 로직 추가
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/chat/search")
    public ResponseEntity<List<Message>> searchMessages(@RequestParam("query") String query, Principal principal) {
        List<Message> messages = chatService.searchMessages(query, principal);
        return ResponseEntity.ok(messages);
    }
}
