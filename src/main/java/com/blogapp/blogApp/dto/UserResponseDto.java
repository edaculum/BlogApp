package com.blogapp.blogApp.dto;

//JWT Sonrası Dönüş
//Bu DTO dışarıya kullanıcı dönerken şifreyi gizlemek için kullanılır.
//login-register işlemi sonrası frontende cevap dönen gerçek bir Auth Response DTO’

import java.util.Set;

public record UserResponseDto(
        Long userId,
        String userName,
        String email,
        Set<String> roles

) {}
