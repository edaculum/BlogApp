package com.blogapp.blogApp.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blogapp/user")
public class UserController {

    @GetMapping("/profile")
    public String profile(){
        return "Bu endpoint USER ve ADMIN tarafından görülebilir.";
    }
}
