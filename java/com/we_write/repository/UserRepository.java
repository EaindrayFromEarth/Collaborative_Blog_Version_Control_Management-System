package com.we_write.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.we_write.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(User createdBy);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

	Optional<User> findByUsername(String createdBy);
}