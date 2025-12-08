package com.blogapp.blogApp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor //final alanlar için otomatik constructor (dependency injection).
@RequestMapping("/blogapp/admin")
public class AdminController {

    @GetMapping("/panel")
    public String adminPanel(){
        return "Burası sadece ADMIN kullanıcıların görebileceği panel!";
    }
}
