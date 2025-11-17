package com.blogapp.blogApp.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProtectedController {

    // Artık sadece giriş yapılmış olması yeterli, rol kontrolü yok
    @GetMapping("/api/protected")
    public String getProtectedData() {
        return "Bu endpoint giriş yapmış kullanıcılar tarafından görülebilir!";
    }
}
