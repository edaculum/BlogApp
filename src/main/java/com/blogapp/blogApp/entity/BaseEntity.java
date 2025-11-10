package com.blogapp.blogApp.entity;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

//Bu sınıf bir tablo değildir. Ama miras alan diğer entity’lere kolon ekler.
@MappedSuperclass
@Getter
public abstract class BaseEntity {

    //yeni bir kayıt eklendiğinde otomatik olarak tarih/saat değerini atar. Kayıt ilk oluşturulurken
    @CreationTimestamp
    @Column(updatable = false) //Bu kolon asla güncellenemez
    private LocalDateTime createdAt;

    //güncellendiğinde otomatik olarak tarih/saat değerini günceller. Kayıt her güncellendiğinde
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //@CreationTimestamp, @UpdateTimestamp anotasyonları Hibernate’e özgüdür;
    // Spring Data JPA’nın kendisinde direkt yoktur ama Hibernate JPA implementasyonu ile çalışır.
}
