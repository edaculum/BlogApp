package com.blogapp.blogApp.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity {
    @Id //Primary Key alanı
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK değerini veritabanı üretir
    private Long roleId;

    //USER, ADMIN gibi rol isimleri
    //ue = true diyerek aynı rol isminin iki kez eklenmesini engelliyoruz.
    @Column(nullable = false, unique = true)
    private String roleName;

    //Bir Role nesnesinin birden çok UserRole kaydı olabilir.
    //	mappedBy = "role" demek: UserRole entity’sindeki role alanı bu ilişkiyi yönetiyor.
    //Bu ilişki Role → UserRole yönünü temsil eder

    @OneToMany(mappedBy = "role") //Bir role ait tüm kullanıcı-rol bağlantılarını temsil eder
    private Set<UserRole> users = new HashSet<>();



}
