package com.blogapp.blogApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//Bu olmadan şifre hashing yapılamaz.
@Configuration
public class SecurityBeansConfig {

    @Bean
    public PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder(); //BCrypt : Spring Security standardı . Güç: 10 (default)

    }
}
