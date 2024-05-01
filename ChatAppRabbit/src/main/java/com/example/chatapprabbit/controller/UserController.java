package com.example.chatapprabbit.controller;


import com.example.chatapprabbit.dto.UserDTO;
import com.example.chatapprabbit.servicies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> dtos = userService.findAll();
        for(UserDTO dto : dtos) {
            Link userLink = linkTo(methodOn(UserController.class).getUser(dto.getId())).withRel("userDetails");
            dto.add(userLink);
        }
        return new ResponseEntity<>(dtos, org.springframework.http.HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Integer id) {
        UserDTO dto = userService.findUserById(id);
        return new ResponseEntity<>(dto, org.springframework.http.HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody UserDTO request) {
        UserDTO userDTO = userService.findUserByUsernameAndPassword(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(userDTO);
    }


    @PostMapping()
    public ResponseEntity<Integer> insertUser(@Valid @RequestBody UserDTO userDTO) {
        int userId = userService.insert(userDTO);
        return new ResponseEntity<>(userId, org.springframework.http.HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Integer> updateUser(@PathVariable("id") Integer id, @RequestBody UserDTO userDTO) {
        int userId = userService.update(id, userDTO);
        return new ResponseEntity<>(userId, org.springframework.http.HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Integer> deleteUser(@PathVariable("id") Integer id) {
        int userId = userService.delete(id);
        return new ResponseEntity<>(userId, org.springframework.http.HttpStatus.OK);
    }
}
