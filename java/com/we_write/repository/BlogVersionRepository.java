package com.we_write.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.we_write.entity.Blog;
import com.we_write.entity.BlogVersion;


public interface BlogVersionRepository extends JpaRepository<BlogVersion, Long> {
    List<BlogVersion> findByBlogIdOrderByCreatedAt(Long blogId);
    List<BlogVersion> findByBlogOrderByTimestampDesc(Blog blog);
    
	List<BlogVersion> findByBlogOrderByCreatedAtDesc(Blog blog);
}
