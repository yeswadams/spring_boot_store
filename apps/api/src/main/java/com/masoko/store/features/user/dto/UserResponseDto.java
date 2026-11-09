package com.masoko.store.features.user.dto;

import java.time.LocalDateTime;

public class UserResponseDto {

    private Long id;
    private String email;
    private String role;
    private LocalDateTime createdAt;


    public  UserResponseDto() {}

    public UserResponseDto(
            Long id,
            String email,
            String role,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
