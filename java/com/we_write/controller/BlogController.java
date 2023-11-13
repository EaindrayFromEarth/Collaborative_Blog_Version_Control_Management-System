
package com.we_write.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.we_write.entity.Blog;
import com.we_write.entity.BlogVersion;
import com.we_write.entity.Review;
import com.we_write.entity.ReviewStatus;
import com.we_write.entity.User;
import com.we_write.payload.BlogDto;
import com.we_write.payload.BlogVersionRequest;
import com.we_write.payload.ReviewRequest;
import com.we_write.repository.UserRepository;
import com.we_write.service.BlogService;
import com.we_write.service.ReviewService;
import com.we_write.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/blogs")
@Tag(
        name = "CRUD REST APIs for Blogs"
)
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/{blogId}/versions")
    public ResponseEntity<?> createBlogVersion(
        @PathVariable Long blogId,
        @RequestBody BlogVersionRequest request,
        Principal principal
    ) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Blog blog = blogService.createNewVersion(blogId, request, user);

        if (blog != null) {
            return ResponseEntity.ok(blog);
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping("/{blogId}/versions")
    public ResponseEntity<List<BlogVersion>> getBlogVersions(@PathVariable Long blogId) {
        List<BlogVersion> versions = blogService.getVersionsForBlog(blogId);
        if (versions != null) {
            return ResponseEntity.ok(versions);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Build Get Posts by Category REST API
    // http://localhost:8080/api/posts/category/3
    @GetMapping("/category/{id}")
    public ResponseEntity<List<BlogDto>> getBlogsByCategory(@PathVariable("id") Long categoryId) {
        List<BlogDto> postDtos = blogService.getBlogsByCategory(categoryId);
        return ResponseEntity.ok(postDtos);
    }
    

    
    @PostMapping
    public ResponseEntity<Blog> createBlog(@RequestBody BlogDto request, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        if (user != null) {
            Blog blog = blogService.createBlog(request.getTitle(), request.getContent(), user);
            return ResponseEntity.ok(blog);
        } else {
            Blog errorBlog = new Blog();
            errorBlog.setTitle("User not found");
            errorBlog.setContent("Access Denied");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBlog);
        }
    }
    
    // get Blog by id
    @GetMapping("/{id}")
    public ResponseEntity<BlogDto> getBlogById(@PathVariable(name = "blog_id") long id) {
        Blog blog = BlogService.getBlogById(id);

        if (blog != null) {
            BlogDto blogDto = convertBlogToDto(blog);
            return ResponseEntity.ok(blogDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{blogId}")
    public ResponseEntity<Blog> updateBlog(
            @PathVariable Long blogId,
            @RequestBody BlogDto request,
            Principal principal
    ) {
        User user = userService.getUserByUsername(principal.getName());
        Blog blog = blogService.getBlogById(blogId);

        if (user != null && blog != null) {
            if (user.getId().equals(blog.getCreatedBy().getId())) {
                blog = blogService.updateBlog(blog, request.getTitle(), request.getContent(), user);
                return ResponseEntity.ok(blog);
            }
            // Permission denied, return an error message
            Blog errorBlog = new Blog();
            errorBlog.setTitle("Permission denied");
            errorBlog.setContent("You don't have permission to update this blog.");
            return ResponseEntity.badRequest().body(errorBlog);
        }
        // User or blog not found, return an error message
        Blog errorBlog = new Blog();
        errorBlog.setTitle("User or blog not found");
        errorBlog.setContent("User or blog with the given ID not found.");
        return ResponseEntity.badRequest().body(errorBlog);
    }

	
    // delete blog rest API
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable(name = "id") long id) {
        blogService.deleteBlogById(id);

        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }

    // Step 2: Endpoint to get pending reviews
    @GetMapping("/reviews/pending")
    public List<Review> getPendingReviews() {
        return reviewService.getPendingReviews();
    }

    // Step 3: Endpoint to create a review for a blog version
    @PostMapping("/reviews")
    public Review createReview(@RequestBody ReviewRequest reviewRequest, @RequestAttribute("user") User user) {
        return reviewService.createReview(user, reviewRequest.getBlogVersionId());
    }

    // Step 4: Endpoint to process a review
    @PutMapping("/reviews/{reviewId}")
    public void processReview(@PathVariable Long reviewId, @RequestParam ReviewStatus status) {
        reviewService.processReview(reviewId, status);
    }

    private BlogDto convertBlogToDto(Blog blog) {
        BlogDto blogDto = new BlogDto();
        blogDto.setId(blog.getBlogid());
        blogDto.setTitle(blog.getTitle());
        blogDto.setContent(blog.getContent());
        // Set other properties as needed
        return blogDto;
    }
}


