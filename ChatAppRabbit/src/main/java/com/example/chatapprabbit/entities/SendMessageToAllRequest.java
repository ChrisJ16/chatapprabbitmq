package com.example.chatapprabbit.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SendMessageToAllRequest {
    private String sender;
    private String content;
}
