package com.example.system.repository;

import com.example.system.Enum.RoleName;
import com.example.system.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName) ;
}
