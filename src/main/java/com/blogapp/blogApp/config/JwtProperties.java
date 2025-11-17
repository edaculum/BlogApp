package com.blogapp.blogApp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//Bu sınıf application.yml’de jwt: ile başlayan ayarları bu sınıfa doldurur

@Configuration
@ConfigurationProperties(prefix = "jwt") //jwt: ile başlayan ayarları oku
public class JwtProperties {

    private String secret;
    private Long expiration;

    //getter ve setter zorunlu (Spring okuma yapabilsin diye)Lombok ile bunları @Getter / @Setter anotasyonlarıyla kısaltabiliriz
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }


}
