package com.we_write.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.we_write.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}