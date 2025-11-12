package com.blogapp.blogApp.service;

import com.blogapp.blogApp.entity.Role;

//"USER" rolü yoksa oluştur, varsa DB’den getirir"
// Register aşaması için gereklidir.
//Yeniden kullanılabilirlik → ADMIN rolü eklemek istersen aynı metot
//sadece rolla ilgilenir

public interface RoleService {

    Role getOrCreate(String roleName);

}
