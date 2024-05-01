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
    public Message broadcastMessage(@Payload SendMessageToAllRequest request) {
        return messageService.sendMessageToAll(request.getSender(), request.getContent());
    }

    @MessageMapping("/sendToUser")
    public void privateMessage(@Payload SendMessageToSingleRequest request) {
        Message message = messageService.sendMessageToSingle(request.getSender(), request.getRecipient(), request.getContent());
        User user = userRepository.findByUsername(request.getRecipient());
        if (user != null) {
            messagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/messages", message); // Adjusted to use username
        }
    }
}
