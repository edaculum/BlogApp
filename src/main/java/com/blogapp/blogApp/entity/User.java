package com.blogapp.blogApp.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity{
    @Id //Primary Key alanı
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK değerini veritabanı üretir
    private Long userId;

    //@Column: JPA’ya bu alanın veritabanında nasıl bir sütun olacağını söyler.
    @Column(nullable = false)
    private String userName;

    //nullable = false: Bu sütun boş bırakılamaz demektir. Veritabanı düzeyinde NOT NULL kısıtlaması eklenir.
    //unique = true: Bu sütun için benzersizlik (unique) kısıtlaması oluşturur. Aynı email adresi birden fazla kayıtta bulunamaz.
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Kullanıcının rollerini tutar
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> roles = new HashSet<>();

}
