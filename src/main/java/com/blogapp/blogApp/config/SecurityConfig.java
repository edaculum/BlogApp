package com.blogapp.blogApp.config;


import com.blogapp.blogApp.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Bu dosya güvenlik ayarlarını belirtir:
//hangi endpoint’ler serbest
//hangi endpoint’ler korumalı
//JWT’nin nasıl devreye gireceği
//filtre zincirine hangi filtrelerin ekleneceği
//session yönetimi nasıl olacak
//login işlemleri nerede gerçekleşecek

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // 1) AuthenticationManager bean
    //AuthenticationManager, Spring Security’nin “kullanıcı doğrulama motorudur”.
    //AuthController login yaparken bunu kullanacağız:
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    // 2) Security filter chain - Güvenlik Kurallarının Tamamı
    //
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // CSRF kapatılır: Token ile çalıştığımız için gerek yok
                .csrf(csrf -> csrf.disable())

                // SESSION OLUŞTURMA — JWT olduğu için backend session tutmaz - Session kapatılır
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Hangi endpoint serbest, hangisi güvenli?
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/blogapp/auth/**",   // register, login herkese açık
                                "/error"          // hata endpoint'i
                        ).permitAll()

                        // diğer tüm endpoint'ler → giriş gerektirir ->korumalı
                        .anyRequest().authenticated()
                )

                //  UsernamePasswordAuthenticationFilter'dan önce bizim JwtFilter çalışsın
                //Her istek önce JwtFilter’den geçer. Token kontrol edilir. Kullanıcı giriş yapmış gibi context’e eklenir
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
