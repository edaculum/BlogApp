package com.blogapp.blogApp.dto;

//Auth yanıtı
public record AuthResponse(
        String message,
        String token,
        UserResponseDto user
) {}
