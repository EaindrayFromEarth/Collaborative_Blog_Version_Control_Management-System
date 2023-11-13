package com.we_write.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.we_write.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}