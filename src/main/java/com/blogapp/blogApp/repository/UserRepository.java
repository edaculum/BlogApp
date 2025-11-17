package com.blogapp.blogApp.repository;

import com.blogapp.blogApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//Bu interface’ler(repository) Spring Data JPA sayesinde otomatik SQL sorguları üretir.Yani kod yazmadan veritabanına ekleme, silme, güncelleme, sorgulama yapabiliyorsun.
//Hibernate bu metodları otomatik implement eder. Spring Data JPA metod adını okuyarak otomatik SQL üretir.
//Kayıt, giriş
public interface UserRepository extends JpaRepository<User,Long> {

    //Optional<User> → Kullanıcı varsa döner, yoksa boş döner (NullPointerException yok!)
    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.roles ur " +
            "LEFT JOIN FETCH ur.role r " +   // ← BU SATIR KRİTİK!
            "WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(@Param("email") String email);
    boolean existsByEmail(String email); //Register olurken email daha önce kullanılmış mı? kontrolü için

    boolean existsByUserName(String userName);
    Optional<User> findByUserName(String userName);
}
