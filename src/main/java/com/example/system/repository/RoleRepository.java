package com.example.system.repository;

import com.example.system.Enum.RoleName;
import com.example.system.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(RoleName roleName) ;
}
