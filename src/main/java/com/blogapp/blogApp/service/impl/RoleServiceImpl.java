package com.blogapp.blogApp.service.impl;


import com.blogapp.blogApp.entity.Role;
import com.blogapp.blogApp.repository.RoleRepository;
import com.blogapp.blogApp.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //final alanlar için constructor oluşturur.
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository; ////Repository Bağlantısı

    //sistemde bir rol yoksa oluştur, varsa var olan rolü getir.
    //.orElseGet(() -> ...)  Eğer rol bulunursa → mevcut rol döner. Eğer rol yoksa → lambda çalışır .Yeni Role entity oluşturulur ve DB’ye kaydedilir
    @Override
    public Role getOrCreate(String roleName) {

        return roleRepository.findByRoleName(roleName) //Veritabanında roleName ile kayıtlı bir rol var mı diye bakar
                .orElseGet(() -> roleRepository.save(
                        Role.builder().roleName(roleName).build()
                ));
    }


}
