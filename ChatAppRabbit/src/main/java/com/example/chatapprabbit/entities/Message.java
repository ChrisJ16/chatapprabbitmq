package com.example.chatapprabbit.entities;

import jakarta.persistence.*;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime sentAt;
}