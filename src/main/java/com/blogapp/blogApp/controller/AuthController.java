package com.blogapp.blogApp.controller;


import com.blogapp.blogApp.dto.AuthResponse;
import com.blogapp.blogApp.dto.UserCreateDto;
import com.blogapp.blogApp.dto.UserLoginDto;
import com.blogapp.blogApp.dto.UserResponseDto;
import com.blogapp.blogApp.security.JwtUtil;
import com.blogapp.blogApp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blogapp/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;


    //REGISTER → Kullanıcı oluşturur + Token döner
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreateDto request) {

        UserResponseDto user = userService.register(request);

        //Token oluştur
        String token= jwtUtil.generateToken(user.email());

        return ResponseEntity.ok(
                new AuthResponse(
                        "Kayıt Başarılı!",
                        token,
                        user
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto request) {

        UserResponseDto user = userService.login(request);

        // Token oluştur
        String token = jwtUtil.generateToken(user.email());

        return ResponseEntity.ok(
                new AuthResponse(
                        "Giriş başarılı!",
                        token,
                        user
                )
        );
    }

}
