package com.example.rest_api_build.repository;

import com.example.rest_api_build.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}