package com.we_write.repository;


	import org.springframework.data.jpa.repository.JpaRepository;

import com.we_write.entity.Comment;

import java.util.List;

	public interface CommentRepository extends JpaRepository<Comment, Long> {
	    List<Comment> findByPostId(long postId);
	}