package com.example.chatapprabbit.repositories;

import com.example.chatapprabbit.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    // Add custom query methods if needed
}