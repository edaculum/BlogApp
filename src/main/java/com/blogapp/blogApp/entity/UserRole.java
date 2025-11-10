package com.blogapp.blogApp.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRole extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //User ve Role arasında çoktan çoğa (Many-to-Many) bir ilişki vardır.
    //User ve Role arasında doğrudan @ManyToMany kullanılmak yerine, ilişkiler UserRole adlı entity üzerinden yönetilir.

    // Kullanıcı -> birden çok role sahip olabilir
    //nullable = false → User olmadan UserRole oluşturamazsın.
    @ManyToOne(fetch = FetchType.LAZY) // Her UserRole kaydı bir kullanıcıya bağlıdır. Bir kullanıcı ise birden çok UserRole kaydına sahip olabilir.
    @JoinColumn(name = "user_id", nullable = false) //Foreign key kolon adları
    private User user;

    // Rol -> birden çok kullanıcıya atanabilir
    @ManyToOne(fetch = FetchType.LAZY) // her UserRole kaydı bir role bağlıdır.
    @JoinColumn(name = "role_id", nullable = false)//Foreign key kolon adları
    private Role role;

    // Ek kolonlar
    @CreationTimestamp //Veritabanına kayıt ilk yazıldığında doldurur
    @Column(nullable = false, updatable = false) //updatable = false → Bir kez verilir, sonra asla değişmez.
    private LocalDateTime assignedAt;

    @Column(nullable = false)
    private Boolean active = true;



}
