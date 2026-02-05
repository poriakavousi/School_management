package com.school.app.dto.mapper;

import com.school.app.dto.UserRequest;
import com.school.app.dto.UserResponse;
import com.school.app.model.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        if (user == null) return null;

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName()
        );
    }

    public static User toEntity(UserRequest request) {
        if (request == null) return null;

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // در حالت واقعی باید hash شود
        user.setFullName(request.getFullName());
        return user;
    }

    public static UserResponse toSafeResponse(User user) {
        if (user == null) return null;

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName()
        );
    }
}