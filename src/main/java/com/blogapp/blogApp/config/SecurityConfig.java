package com.blogapp.blogApp.config;


import com.blogapp.blogApp.security.CustomUserDetailsService;
import com.blogapp.blogApp.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

@Configuration //bu sınıf Spring bean tanımları içeriyor.
@RequiredArgsConstructor
@EnableWebSecurity //Spring Security web desteğini aktif eder.
@EnableMethodSecurity //@PreAuthorize gibi metod seviyesinde güvenlik anotasyonlarını açar.
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService customUserDetailsService;

    // 1) AuthenticationManager bean
    //AuthenticationManager, Spring Security’nin “kullanıcı doğrulama motorudur”.
    //AuthController login yaparken bunu kullanacağız:
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    // 2) Security filter chain - ana güvenlik kuralları

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // CSRF kapatılır: Token ile çalıştığımız için gerek yok
                .csrf(csrf -> csrf.disable())


                // Hangi endpoint serbest, hangisi güvenli? endpoint bazlı erişim kuralları
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/blogapp/auth/**").permitAll()   // /api/auth/* altındaki endpoint'ler (ör. login, register) herkese açık. Token olmadan erişilebilir.
                        .requestMatchers("/blogapp/admin/**").hasRole("ADMIN") // sadece ROLE_ADMIN olanlar erişebilir.
                        .requestMatchers("/blogapp/user/**").hasAnyRole("USER", "ADMIN") //ROLE_USER veya ROLE_ADMIN gerekli.
                        .anyRequest().authenticated()  // diğerleri login gerektirir

                ).userDetailsService(customUserDetailsService) //Security’nin authentication mekanizmasında kullanılacak UserDetailsService bean’ini burada belirtiyoruz.
                //Özellikle AuthenticationManager veya bazı otomatik yapılandırmalarda Security, kullanıcıyı bulmak için bu servisi kullanır.

                // SESSION OLUŞTURMA — JWT olduğu için backend session tutmaz - Session kapatılır .
                //Her istek token ile beraber gelir ve doğrulanır. Bu satır server-side session oluşturulmasını engeller.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

                //  UsernamePasswordAuthenticationFilter'dan önce bizim JwtFilter çalışsın
                //Her istek önce kendi JWT filtrenden (JwtFilter) geçer. Token kontrol edilir. Kullanıcı giriş yapmış gibi context’e eklenir
                http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

//hasRole("ADMIN") kullanıldığında Spring otomatik olarak "ROLE_" prefix'ini ekler.
// Yani hasRole("ADMIN") JWT/Security GrantedAuthority içinde ROLE_ADMIN arar.
// rolleri DB'de ROLE_ADMIN şeklinde saklayabilirsin;
// veya DB'de ADMIN saklıyorsa getAuthorities() içinde new SimpleGrantedAuthority("ROLE_"+roleName) yapmalısın.
// Aksi halde rol eşleşmesi olmaz.
