package com.example.chatapprabbit.servicies;

import com.example.chatapprabbit.dto.UserDTO;
import com.example.chatapprabbit.dto.builder.UserBuilder;
import com.example.chatapprabbit.repositories.UserRepository;
import com.example.chatapprabbit.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        LOGGER.info("UserService: findAll");
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        return UserBuilder.toUserDTO(userOptional.get());
    }


    public int insert(UserDTO userDTO) {
        User user = UserBuilder.toEntityWithoutID(userDTO);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        return user.getId();
    }

    public int update(Integer id, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        User user = userOptional.get();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        LOGGER.debug("User with id {} was updated in db", user.getId());
        return userRepository.save(user).getId();
    }

    public int delete(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        userRepository.deleteById(id);
        LOGGER.debug("User with id {} was deleted from db", id);
        return id;
    }

    public UserDTO findUserByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user == null) {
            LOGGER.error("User with username {} and password {} was not found in db", username, password);
            throw new ResourceNotFoundException("User with username: " + username + " and password: " + password);
        }
        return UserBuilder.toUserDTO(user);
    }
}
