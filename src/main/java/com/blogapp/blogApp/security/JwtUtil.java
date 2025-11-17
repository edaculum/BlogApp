package com.blogapp.blogApp.security;

import com.blogapp.blogApp.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component; //Spring bean yapar

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


//JWT token oluşturur, Token doğrular ve geçerliliğini kontrol etme işlerini üstlenir.
//Spring bu sınıfı otomatik oluşturur ve JwtProperties’i inject eder
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    //Token imzasını atmak için bir gizli anahtar (secret key) gerekir.
    //jwt.secret string’ini güvenli bir anahtar (Key) haline getirir.
    public Key getJwtKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()); //Secret en az 32 karakter olmalı!
    }

    //Token oluşturur. Kullanıcı giriş yaptığında ona bir JWT token üretir.
    //Token’ın içine username, oluşturulma zamanı ve bitiş zamanı yazar.
    public String generateToken(String userName) {
        Date now = new Date(); //şu anki zamanı alır.
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration()); //şu anki zamana token’ın geçerlilik süresini ekler. token’ın ne zaman biteceğini hesaplar.

        return Jwts.builder()
                .setSubject(userName) //Token içine “kimin olduğunu” yazıyor.
                .setIssuedAt(now) //Token ne zaman oluşturuldu.
                .setExpiration(expiryDate) //Token ne zaman geçersiz olacak?
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) //Token’ı bir gizli anahtar ile imzalıyor.
                .compact(); //Token’ı string haline getirir (HEADER . PAYLOAD . SIGNATURE)

        //Bu imza sayesinde: Kimse token’ı değiştiremez /Token sahte yazılamaz /Backend bu imzaya bakarak token’ın gerçek olup olmadığını anlar
    }

    // KULLANICI + ROLLER ile TOKEN ÜRET- overload
    public String generateToken(String userName, Object roles) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getExpiration());

        return Jwts.builder()
                .setSubject(userName)          // token'ın sahibi
                .claim("roles", roles)         // ROLLERİ CLAIM OLARAK EKLİYORUZ
                .setIssuedAt(now)              // oluşturulma zamanı
                .setExpiration(expiryDate)     // bitiş zamanı
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Token’dan bilgi okur
    // Token içinden kullanıcı adını (username) (subject) çıkarma
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Token’dan bilgi okur
    // Belirli bir claim’i çıkarma
    //Token'ın içindeki claim’leri alır ve içinden istediğin bilgiyi seçip geri verir
    //Claim: Token’ın içinde taşınan bilgi parçalarıdır. Her biri (ad,soyad,..)birer claimdir
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Token’ın geçerli olup olmadığını kontrol etme
    //2 şeyi kontrol eder:
    // Token'ın içindeki username doğru mu?
    //Token süresi dolmuş mu?
    //İkisi de doğruysa = GEÇERLİ
    public boolean isTokenValid(String token, String userName) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(userName) && !isTokenExpired(token)); //Token’daki kullanıcı = giriş yapan mı? //Süre doldu mu?
    }

    // Token süresi dolmuş mu kontrolü
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //oluşturduğun token’ın içindeki expiration bilgisini okur
    //Token’ın içindeki “exp” claim'ini alır . Exp = token’ın bitiş zamanı
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); //bitiş zamanını döndürür.
    }

    // Tüm claim’leri çözümleme
    //Token’ı açar:
    // İmzası doğru mu kontrol et
    //Token geçerliyse içindeki bilgileri al
    // Geçerliyse exception fırlat
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Güvenlik Kontrolü
    //JWT token 3 parçadan oluşur (HEADER . PAYLOAD . SIGNATURE). SIGNATURE (imza) kısmı, token’ın sahte olmadığını garantiler.
    private Key getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(); //application.properties içinde senin bir secret key’i alıp bu metni byte dizisine dönüştürür

        //Gizli anahtar 32 byte’tan kısa mı kontrol ediyor.32 byte’tan kısa ise hata fırlatır (güvenlik için)
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters!");
        }
        return Keys.hmacShaKeyFor(keyBytes); //İmza Anahtarını Oluşturur
    }
}

