package com.we_write.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.we_write.entity.Blog;
import com.we_write.entity.Category;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByBlogIdOrderByCreatedAt(Long blogId);
    List<Blog> findByCategory_Id(Category categoryId);
}
