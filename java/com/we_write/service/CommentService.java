package com.we_write.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.we_write.entity.Blog;
import com.we_write.entity.Comment;
import com.we_write.exception.ResourceNotFoundException;
import com.we_write.payload.CommentDto;
import com.we_write.repository.CommentRepository;
import com.we_write.utils.CommentMapper;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentMapper commentMapper;

    
    public List<CommentDto> getCommentsByBlogId(Long blogId) {
        List<Comment> comments = commentRepository.findByBlogId(blogId);

        // Convert Comment entities to CommentDto objects
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public CommentDto createComment(CommentDto commentDto, Long blogId) {
        Comment comment = commentMapper.toComment(commentDto);

        // Set the blog ID for the comment
        Blog blog = new Blog();
        blog.setBlogid(blogId);
        comment.setBlog(blog);

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toCommentDto(savedComment);
    }
    
    public CommentDto getCommentById(long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + commentId + " not found"));
        return commentMapper.toCommentDto(comment);
    }
    
    public CommentDto updateComment(Long commentId, CommentDto updatedCommentDto) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + commentId + " not found"));

        // Update fields based on the updatedCommentDto
        existingComment.setName(updatedCommentDto.getName());
        existingComment.setEmail(updatedCommentDto.getEmail());
        existingComment.setBody(updatedCommentDto.getBody());

        Comment updatedComment = commentRepository.save(existingComment);
        return commentMapper.toCommentDto(updatedComment);
    }
    
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + commentId + " not found"));
        commentRepository.delete(comment);
    }
    private CommentDto convertToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        // Add other mappings as needed
        return commentDto;
    }

}
