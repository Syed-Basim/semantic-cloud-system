package com.example.rest_api_build.controller;

import org.springframework.http.ResponseEntity;
import com.example.rest_api_build.model.User;
import com.example.rest_api_build.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users") // ✅ Cleaner base mapping
public class UserController {

    private final UserService userService;

    // Constructor Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ GET all users
    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    // ✅ POST user
    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    // ✅ GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build(); // 404
        }

        return ResponseEntity.ok(user); // 200
    }

    // ✅ PUT update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);

        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedUser);
    }

    // ✅ DELETE user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {

        boolean isDeleted = userService.deleteUser(id);

        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok("User deleted successfully");
    }
}