package com.example.chatapprabbit.Consumer;

import com.example.chatapprabbit.entities.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @RabbitListener(queues = "queue_all")
    public void receiveMessageForAll(String message) {
        // Process the received message for group chat
        System.out.println("Received message for group chat: " + message);
    }

    @RabbitListener(queues = "queue_single")
    public void receiveMessageForSingle(String message) {
        // Process the received message for private chat
        System.out.println("Received message for private chat: " + message);
    }
}
