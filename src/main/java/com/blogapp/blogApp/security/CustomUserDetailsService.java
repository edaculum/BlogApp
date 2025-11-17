package com.blogapp.blogApp.security;

import com.blogapp.blogApp.entity.User;
import com.blogapp.blogApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Spring Security, kullanıcı giriş yaptığında (login olduğunda), username’e göre kullanıcıyı bulmak için bu servisi çağırır.
// bu sınıf= Kullanıcıyı veritabanından bulup getir

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; //DB’den kullanıcıyı çekmek için UserRepository’ye bağlıdır.

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + email));
        return new CustomUserDetails(user);
    }

}

//loadUserByUsername():
// Spring Security giriş işleminde otomatik olarak bu metodu çağırır.
// Login olurken kullanıcı adı → buraya gelir
//Bu metod DB’den kullanıcıyı bulur (UserRepository ile veritabanına bağlanır)
//Kullanıcı yoksa hata fırlatır
//Kullanıcı varsa → CustomUserDetails’e sarıp geri döner

//Bu sınıf olmadan: Spring Security kullanıcıyı bulamaz /Login çalışmaz /JWT üretilemez

//Spring Security içinde giriş aşaması şöyle işler:
//Login → AuthenticationManager → UserDetailsService → JwtUtil → SecurityContext
//AuthenticationManager : kullanıcıyı doğrulamak için UserDetailsService kullanır ve loadUserByUsername(username) metodu çağırılır
