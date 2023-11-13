package com.we_write.repository;


	import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.we_write.entity.Comment;

import java.util.List;

@Repository
	public interface CommentRepository extends JpaRepository<Comment, Long> {
	    List<Comment> findByBlogId(long blogId);

		
	}