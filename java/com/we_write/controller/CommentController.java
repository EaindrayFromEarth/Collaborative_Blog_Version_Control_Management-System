package com.we_write.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.we_write.payload.CommentDto;
import com.we_write.service.CommentService;

@RestController
@RequestMapping("/api/blogs/{blogId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getCommentsByBlogId(@PathVariable Long blogId) {
        List<CommentDto> comments = commentService.getCommentsByBlogId(blogId);
        return ResponseEntity.ok(comments);
    }
    
    @PostMapping
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long blogId,
            @RequestBody CommentDto commentDto) {
        CommentDto createdComment = commentService.createComment(commentDto, blogId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }
    
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long blogId,
            @PathVariable Long commentId,
            @RequestBody CommentDto updatedCommentDto) {
        CommentDto updatedComment = commentService.updateComment(commentId, updatedCommentDto);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long blogId,
            @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
    // Other methods...

}
