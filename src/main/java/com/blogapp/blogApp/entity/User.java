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
    //Bir User’ın birden çok rolü varsa, onlar UserRole tablosunda tutulur.
    //mappedBy = "user" demek: UserRole entity’sindeki user alanı bu ilişkiyi yönetiyor.
    //cascade = CascadeType.ALL :User üzerinde yapılan tüm işlemleri UserRole tablosuna yansıtır. User oluştur → UserRole otomatik kaydedilir. User sil → tüm UserRole kayıtları otomatik silinir

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //Bir kullanıcıya bağlı tüm rol kayıtlarını temsil eder
    private Set<UserRole> roles = new HashSet<>(); //User entity'si içinde bulunan, kullanıcının rollerini temsil eden koleksiyon alanıdır

}
