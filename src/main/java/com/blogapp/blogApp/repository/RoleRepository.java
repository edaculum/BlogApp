package com.blogapp.blogApp.repository;

import com.blogapp.blogApp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Varsayılan rol atama
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByRoleName(String roleName);
}
