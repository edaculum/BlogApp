package com.blogapp.blogApp.repository;

import com.blogapp.blogApp.entity.User;
import com.blogapp.blogApp.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//UserRole ara tablo olduğu için, özel sorgulara ihtiyacın var.

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {

    List<UserRole> findByUserAndActiveTrue(User user); //kullanıcıya atanmış ve hâlâ aktif olan tüm rolleri getir
    Optional<UserRole> findByUserAndRoleRoleName(User user, String roleName); //kullanıcıda bu rol var mı? Varsa hangi UserRole kaydı?
}
