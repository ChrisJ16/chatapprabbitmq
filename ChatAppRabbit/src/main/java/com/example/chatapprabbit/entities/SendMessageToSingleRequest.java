package com.example.chatapprabbit.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SendMessageToSingleRequest {
    private String sender;
    private String recipient;
    private String content;
}