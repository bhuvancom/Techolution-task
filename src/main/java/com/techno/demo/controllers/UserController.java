package com.techno.demo.controllers;

import com.techno.demo.exception.ResourceNotFoundException;
import com.techno.demo.model.User;
import com.techno.demo.model.request.UserCreateDto;
import com.techno.demo.services.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
    public ResponseEntity<User> findById(@PathVariable("id") Integer id) {
        User user = userService.getUserById(id);
        if (user == null) throw new ResourceNotFoundException("User with id -> " + id + " was not found");
        return ResponseEntity.ok(user);
    }

    @GetMapping("findByUsername/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username).map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("User with username -> " + username + " was not found"));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateDto user) {
        User u = user.toUser();
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        u = userService.saveUser(u, user.getRoles());
        return ResponseEntity.created(URI.create("/api/user")).body(u);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody UserCreateDto user, @PathVariable("id") Integer id) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Deleted");
    }

}
