package org.example.backend.chat;

import jakarta.servlet.http.HttpSession;
import org.example.backend.login.CustomUserDetails;
import org.example.backend.login.User;
import org.example.backend.login.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatController(SimpMessageSendingOperations messagingTemplate, MessageRepository messageRepository, UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message, Principal principal) {

        String loginId = principal.getName();
        User user = userRepository.findByLoginId(loginId).orElse(null);
        if (user != null) {
            message.setUser(user);
            messageRepository.save(message);
            messagingTemplate.convertAndSend("/topic/public", message);
        }
    }

    @GetMapping("/chat")
    public String getChatPage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                Long pkId = ((CustomUserDetails) principal).getId();
                // 사용자 이름을 가져오거나 필요한 경우 세션 정보를 사용할 수 있습니다.
                System.out.println(pkId);
            }
        }

//        if (user == null) {
//            return "redirect:/login";
//        }
//        model.addAttribute("username", user.getLoginId());
        return "chatroom";
    }

}