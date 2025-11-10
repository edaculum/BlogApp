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

    @OneToMany(mappedBy = "role")
    private Set<UserRole> users = new HashSet<>();



}
