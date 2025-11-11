package com.blogapp.blogApp.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//Kayıt İçin
//Validation (Doğrulama) da kullanılıyor
public record UserCreateDto(

        @NotBlank (message = "Kullanıcı adı boş olamaz")
        @Size (min = 3, max = 50, message = "Kullanıcı adı 3-50 karakter olmalı")
        String username,

        @NotBlank(message = "Email boş olamaz")
        @Email (message = "Geçerli bir email giriniz")
        String email,

        @NotBlank(message = "Şifre boş olamaz")
        @Size(min =6, message = "Şifre en az 6 karakter olmalı") //Uzunluk kontrolü
        String password

) {}

