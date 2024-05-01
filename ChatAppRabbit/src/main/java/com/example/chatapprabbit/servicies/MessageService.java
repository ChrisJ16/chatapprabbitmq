package com.example.chatapprabbit.servicies;

import com.example.chatapprabbit.entities.Message;

import com.example.chatapprabbit.entities.User;
import com.example.chatapprabbit.repositories.MessageRepository;
import com.example.chatapprabbit.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public Message sendMessageToAll(String sendername, String content) {
        User sender = userRepository.findByUsername(sendername);
        Message message = Message.builder()
                .sender(sender)
                .content(content)
                .sentAt(LocalDateTime.now())
                .build();

        // Save message to the database
        message = messageRepository.save(message);

        // Send message to RabbitMQ for group chat
        String json = "{ \"sender\": \"" + message.getSender().getUsername() + "\", \"content\": \"" + message.getContent() + "\" }";
        rabbitTemplate.convertAndSend("exchange_all", "routing_all", json);

        return message;
    }

    public Message sendMessageToSingle(String sendername, String recipientname, String content) {
        User recipient = userRepository.findByUsername(recipientname);
        User sender = userRepository.findByUsername(sendername);
        Message message = Message.builder()
                .sender(sender)
                .recipient(recipient)
                .content(content)
                .sentAt(LocalDateTime.now())
                .build();

        // Save message to the database
        message = messageRepository.save(message);

        // Send message to RabbitMQ for private chat
        //rabbitTemplate.convertAndSend("exchange_all", "routing_single", message);

        String json = "{ \"sender\": \"" + message.getSender().getUsername() + "\", \"recipient\": \"" + message.getRecipient().getUsername()  + "\", \"content\": \"" + message.getContent() + "\" }";
        rabbitTemplate.convertAndSend("exchange_all", "routing_single", json);

        return message;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}