package com.blogapp.blogApp.security;

import com.blogapp.blogApp.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

//Spring Security’nin UserDetails interface’ini implement eder.Spring Security bu sınıfı JWT filtresinde kullanır.
// User Entity'i → Spring Security formatına dönüştürme sınıfıdır.

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user; // Bütün bilgiler User entity'mizden okunur.

    //Spring Security String roller ile çalışır → "ROLE_ADMIN", "ROLE_USER"
    //Senin Role entity’in nesne şeklinde → Role(id=1, name="ROLE_ADMIN")
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // User entity'sindeki rol nesnelerinin Spring Security formatına çevrilmesi
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getRoleName()))
                .collect(Collectors.toSet());
    }

    //Spring Security login işleminde buradan hash’li password okur
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //JWT token’ın subject kısmı email
    // Sistem JWT içinde subject olarak buradaki userName’i(email olarak) taşır.
    //Spring Security artık email üzerinden doğrulama yapacak.
    @Override
    public String getUsername() {
        return user.getEmail(); // Spring Security userName olarak email'i kullanacak
    }

    // Hesap durumu kontrolleri //
    //Bu 4 metod kullanıcı hesaplarının durumunu gösterir//
    @Override
    public boolean isAccountNonExpired() {
        return true; // Basit örnek için hep true
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Basit örnek için hep true
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Basit örnek için hep true
    }

    @Override
    public boolean isEnabled() {
        return true; // Kullanıcı aktif mi?
    }
}

//CustomUserDetails:
//JWT Token doğrulanınca kullanıcı bilgisi buradan okunur: JWT token içindeki username → DB’de User bulunur → CustomUserDetails yapılır.
//SecurityContext içine eklenir. Artık spring biliyor:
//- bu istek hangi kullanıcıdan geliyor
//- rolleri neler
//- şifreli mi
//- hesabı aktif mi
//User entity → Spring Security kullanıcı formatı
//Role entity → GrantedAuthority formatına (Rolleri dönüştürür)