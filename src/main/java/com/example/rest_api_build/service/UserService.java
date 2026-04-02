package com.example.rest_api_build.service;

import com.example.rest_api_build.exception.UserNotFoundException;
import com.example.rest_api_build.model.User;
import com.example.rest_api_build.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor Injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ADD user
    public User addUser(User user) {
        return userRepository.save(user); // DB auto-generates ID
    }

    // GET user by ID
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    // UPDATE user
    public User updateUser(int id, User updatedUser) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());

        return userRepository.save(user);
    }

    // DELETE user
    public boolean deleteUser(int id) {

        if (!userRepository.existsById(id)) {
            return false;
        }

        userRepository.deleteById(id);
        return true;
    }
}