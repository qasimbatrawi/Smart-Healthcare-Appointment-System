package com.example.system.repository;

import com.example.system.Enum.RoleName;
import com.example.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username) ;
    List<User> findByRole_RoleName(RoleName roleName);
    Optional<User> findByUsernameAndRole_RoleName(String username, RoleName roleName);
}
