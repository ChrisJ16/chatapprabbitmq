package com.example.chatapprabbit.dto.builder;

import com.example.chatapprabbit.entities.User;
import com.example.chatapprabbit.repositories.UserRepository;
import com.example.chatapprabbit.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBuilder.class);

    private final UserRepository userRepository;

    @Autowired
    public UserBuilder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(),
                user.getUsername(),
                user.getPassword());
    }

    public static User toEntity(UserDTO userDTO) {
        return new User(userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword());
    }

    public static User toEntityWithoutID(UserDTO userDTO) {
        return new User(userDTO.getUsername(), userDTO.getPassword());
    }
}