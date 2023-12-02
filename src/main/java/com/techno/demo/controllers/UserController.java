package com.techno.demo.controllers;

import com.techno.demo.exception.ResourceNotFoundException;
import com.techno.demo.model.User;
import com.techno.demo.model.request.UserCreateDto;
import com.techno.demo.services.UserService;
import jakarta.websocket.server.PathParam;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@Log
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("findById/{id}")
    public ResponseEntity<User> findById(@PathParam("id") Integer id) {
        return userService.getUserById(id).map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("User with id -> " + id + " was not found"));
    }

    @GetMapping("findByUsername/{username}")
    public ResponseEntity<User> findByUsername(@PathParam("username") String username) {
        return userService.getUserByUsername(username).map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("User with username -> " + username + " was not found"));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateDto user) {
        User u = user.toUser();
        u.setUsername(passwordEncoder.encode(u.getPassword()));
        return ResponseEntity.ok(userService.saveUser(u, user.getRoles()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody UserCreateDto user, @PathParam("id") Integer id) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }
}
