package com.blogapp.blogApp.service;

import com.blogapp.blogApp.dto.UserCreateDto;
import com.blogapp.blogApp.dto.UserLoginDto;
import com.blogapp.blogApp.dto.UserResponseDto;

//UserService sadece kullanıcıyla ilgilenir

public interface UserService {

    //UserCreateDto tipi bir request alır ve UserResponseDto döndürür.

    UserResponseDto register(UserCreateDto request); //register → yeni kullanıcı oluşturur
    UserResponseDto login(UserLoginDto request); //login → kullanıcıyı doğrular (JWT’den önce)

}
