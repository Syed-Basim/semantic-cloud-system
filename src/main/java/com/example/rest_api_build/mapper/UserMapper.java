package com.example.rest_api_build.mapper;

import com.example.rest_api_build.dto.UserRequestDTO;
import com.example.rest_api_build.dto.UserResponseDTO;
import com.example.rest_api_build.model.User;

public class UserMapper {

    // Convert Request DTO → Entity
    public static User toEntity(UserRequestDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        return user;
    }

    // Convert Entity → Response DTO
    public static UserResponseDTO toDTO(User user) {
        if (user == null) return null;

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        return dto;
    }
}