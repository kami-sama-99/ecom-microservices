package com.ecommerce.user.controller;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @PostMapping("/api/users")
    public ResponseEntity<String> createUser(@RequestBody UserRequest user) {
        userService.addUser(user);
        return ResponseEntity.ok("User created successfully");
    }

    @GetMapping("/api/users/{id}")
    public Optional<UserResponse> getUser(@PathVariable Long id) {
        return userService.fetchUser(id);
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest updatedUser
    ) {
        if (userService.updateUser(id, updatedUser)) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
