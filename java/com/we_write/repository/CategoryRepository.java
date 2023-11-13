package com.we_write.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.we_write.entity.Category;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}