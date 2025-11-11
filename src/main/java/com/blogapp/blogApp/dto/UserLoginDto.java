package com.blogapp.blogApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//Giriş İçin
public record UserLoginDto(

        @NotBlank(message = "Email boş olamaz")
        @Email(message = "Geçerli email giriniz")
        String email,

        @NotBlank(message = "Şifre boş olamaz")
        String password

) {}
