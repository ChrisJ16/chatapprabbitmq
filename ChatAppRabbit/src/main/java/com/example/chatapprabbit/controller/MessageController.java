package com.example.chatapprabbit.controller;

import com.example.chatapprabbit.entities.Message;
import com.example.chatapprabbit.entities.SendMessageToAllRequest;
import com.example.chatapprabbit.entities.SendMessageToSingleRequest;
import com.example.chatapprabbit.entities.User;
import com.example.chatapprabbit.servicies.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/all")
    public ResponseEntity<Message> sendMessageToAll(@RequestBody SendMessageToAllRequest request) {
        // Retrieve sender and message content from request
        String sender = request.getSender();
        String content = request.getContent();

        // Send message to all users
        Message message = messageService.sendMessageToAll(sender, content);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PostMapping("/single")
    public ResponseEntity<Message> sendMessageToSingle(@RequestBody SendMessageToSingleRequest request) {
        String sender = request.getSender();
        String recipient = request.getRecipient();
        String content = request.getContent();

        // Send message to the specified recipient
        Message message = messageService.sendMessageToSingle(sender, recipient, content);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        // Retrieve all messages
        List<Message> messages = messageService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
