package com.example.chatapprabbit.controller;
import com.example.chatapprabbit.entities.Message;
import com.example.chatapprabbit.entities.SendMessageToAllRequest;
import com.example.chatapprabbit.entities.SendMessageToSingleRequest;
import com.example.chatapprabbit.entities.User;
import com.example.chatapprabbit.repositories.UserRepository;
import com.example.chatapprabbit.servicies.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Controller
@CrossOrigin
public class WebSocketMessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketMessageController(MessageService messageService, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendToAll")
    @SendTo("/topic/messages")
    public SendMessageToAllRequest broadcastMessage(@Payload SendMessageToAllRequest request) {
        return request;
    }

    @MessageMapping("/sendToUser")
    public void privateMessage(@Payload SendMessageToSingleRequest request) {
        Message message = messageService.sendMessageToSingleWithoutRabbit(request.getSender(), request.getRecipient(), request.getContent());
        User user = userRepository.findByUsername(request.getRecipient());
        if (user != null) {
            // Ensure the username used here is exactly the same as the one used to establish the WebSocket session
            messagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/private", message);
        } else {
            System.err.println("No user found with username: " + request.getRecipient());
        }
    }
}
