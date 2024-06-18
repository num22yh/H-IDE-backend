package org.example.backend.chat;

import org.example.backend.login.CustomUserDetails;
import org.example.backend.login.User;
import org.example.backend.login.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatService {

    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatService(SimpMessageSendingOperations messagingTemplate, MessageRepository messageRepository, UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public void sendLoginMessage(String loginId) {
        Optional<User> userOptional = userRepository.findByLoginId(loginId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setLastLoginTime(LocalDateTime.now());
            userRepository.save(user);

            Map<String, String> messageContent = new HashMap<>();
            messageContent.put("content", "User " + loginId + " has entered the chat");
            messagingTemplate.convertAndSend("/sub/chat", messageContent);
        }
    }

    public boolean sendMessage(Message message, Principal principal) {
        String loginId = principal.getName();
        Optional<User> userOptional = userRepository.findByLoginId(loginId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            message.setUser(user);
            messageRepository.save(message);
            messagingTemplate.convertAndSend("/sub/chat", message);
            return true;
        }
        return false;
    }

    public Long getAuthenticatedUserId(Principal principal) {
        String loginId = principal.getName();
        Optional<User> userOptional = userRepository.findByLoginId(loginId);
        return userOptional.map(User::getId).orElse(null);
    }

    public List<Message> searchMessages(String content, Principal principal) {
        String loginId = principal.getName();
        Optional<User> userOptional = userRepository.findByLoginId(loginId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            LocalDateTime lastLoginTime = user.getLastLoginTime();
            return messageRepository.findByContentIgnoreCaseContainingAndTimestampAfter(content, lastLoginTime);
        }
        return List.of(); // 빈 리스트 반환
    }
}