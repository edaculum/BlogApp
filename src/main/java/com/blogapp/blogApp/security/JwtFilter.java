package com.blogapp.blogApp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//JwtFilter Spring Security ile entegre çalışan bir filtre
//Amacı:
//Her HTTP isteğini yakalamak
//Eğer istek bir JWT token içeriyorsa bu token’ı doğrulamak
//Kullanıcının kimliğini (Authentication) Spring Security context’ine eklemek

//OncePerRequestFilter: Her HTTP isteği için bir kez çalışacak filtre.
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, //@NonNull → Parametre, alan veya dönüş değeri null olmamalı.
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization"); //HTTP header’ından Authorization alanını alıyor.

        String userName = null;
        String jwt= null;

        // Header "Bearer token" formatında mı kontrol et. Normalde JWT token bu header’da "Bearer <token>" formatında gelir.
        //Eğer header var ve "Bearer " ile başlıyorsa:
        //Token’ı "Bearer " kısmından sonra alıyor (substring(7))
        //JWT içindeki username bilgisini JwtUtil ile çıkarıyor.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            userName = jwtUtil.extractUsername(jwt);
        }

        //Kullanıcı henüz authenticate edilmemişse devam ediyor.
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //Token’daki kullanıcı adını veritabanından çekiyor ve Spring Security UserDetails objesine dönüştürüyor.
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);

            //Token geçerliyse:
            //Bir Authentication objesi oluşturuyor
            //Bu objeyi Security context’e ekliyor
            //Artık Spring Security bu kullanıcıyı giriş yapmış gibi kabul ediyor.
            if (jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities() // DB’den alıyor
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Security context’e kullanıcıyı koy
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }
        //Filtre zincirindeki bir sonraki filtreye devam ediyor.
        filterChain.doFilter(request, response);
    }
}

//Özet Mantık:
//Her HTTP isteği geldiğinde filtre çalışır (OncePerRequestFilter)
//Header’da JWT var mı kontrol edilir
//JWT geçerliyse kullanıcı bilgisi alınır ve Security context’e eklenir
//Sonra request normal şekilde devam eder


//Bağlantılar:
//JWT token → JwtUtil
//Kullanıcı bilgisi → CustomUserDetailsService
//Spring Security → UsernamePasswordAuthenticationToken + SecurityContextHolder
