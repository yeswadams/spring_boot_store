package com.masoko.store.features.auth.dto;

import com.masoko.store.features.user.dto.UserResponseDto;
import com.masoko.store.features.user.entity.User;

public class AuthResponseDto {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private UserResponseDto user;

    public AuthResponseDto() {}

    public AuthResponseDto(
            String accessToken,
            String tokenType,
            long expiresIn,
            UserResponseDto user
    ) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public UserResponseDto getUser() {
        return user;
    }
}
