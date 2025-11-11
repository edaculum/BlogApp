package com.blogapp.blogApp.repository;

import com.blogapp.blogApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Bu interface’ler(repository) Spring Data JPA sayesinde otomatik SQL sorguları üretir.Yani kod yazmadan veritabanına ekleme, silme, güncelleme, sorgulama yapabiliyorsun.
//Hibernate bu metodları otomatik implement eder. Spring Data JPA metod adını okuyarak otomatik SQL üretir.
//Kayıt, giriş
public interface UserRepository extends JpaRepository<User,Long> {

    //Optional<User> → Kullanıcı varsa döner, yoksa boş döner (NullPointerException yok!)
    Optional<User> findByEmail(String email); //Login olurken kullanıcıyı email ile bulmak için

    boolean existsByEmail(String email); //Register olurken email daha önce kullanılmış mı? kontrolü için
}
