package com.we_write.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.we_write.entity.Blog;
import com.we_write.entity.BlogVersion;
import com.we_write.entity.Category;
import com.we_write.entity.User;
import com.we_write.exception.ResourceNotFoundException;
import com.we_write.payload.BlogDto;
import com.we_write.payload.BlogVersionRequest;
import com.we_write.repository.BlogRepository;
import com.we_write.repository.BlogVersionRepository;
import com.we_write.repository.CategoryRepository;

@Service
public class BlogService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private static BlogRepository blogRepository;

    @Autowired
    private BlogVersionRepository blogVersionRepository;

 // Update createNewVersion method to accept BlogVersionRequest
    public Blog createNewVersion(Long blogId, BlogVersionRequest request, User user) {
        Blog blog = getBlogById(blogId);

        if (blog != null) {
            BlogVersion blogVersion = new BlogVersion();
            blogVersion.setBlog(blog);
            blogVersion.setTitle(request.getTitle());
            blogVersion.setContent(request.getContent());
            blogVersion.setCreatedBy(user);

            blogVersionRepository.save(blogVersion);

            blog.setTitle(request.getTitle());
            blog.setContent(request.getContent());
            blog.setLastModifiedBy(user);
            blog.setLastModifiedAt(LocalDateTime.now());

            blogRepository.save(blog);

            return blog;
        }

        return null;
    }


    // Delete a blog by its ID
    public void deleteBlogById(long id) {
        blogRepository.deleteById(id);
    }

    // Get a list of blogs by category ID
    public List<BlogDto> getBlogsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (category != null) {
            List<Blog> blogs = blogRepository.findByCategory_Id(category);

            return blogs.stream()
                    .map(this::convertBlogToDto)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    // Convert a Blog entity to a BlogDto
    private BlogDto convertBlogToDto(Blog blog) {
        BlogDto blogDto = new BlogDto();
        // Set the DTO properties based on the Blog entity
        blogDto.setId(blog.getBlogid());
        blogDto.setTitle(blog.getTitle());
        // Set other properties as needed

        return blogDto;
    }

    // Create a new blog with the provided title, content, and user
    public Blog createBlog(String title, String content, User user) {
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setCreatedBy(user);
        blog.setCreatedAt(LocalDateTime.now());

        return blogRepository.save(blog);
    }

    // Get a blog by its ID or throw a ResourceNotFoundException if not found
    public static  Blog getBlogById(Long id) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog == null) {
            throw new ResourceNotFoundException("Blog with ID " + id + " not found");
        }
        return blog;
    }

    // Get a list of versions for a blog by its ID
    public List<BlogVersion> getVersionsForBlog(Long blogId) {
        return blogVersionRepository.findByBlogIdOrderByCreatedAt(blogId);
    }

    // Update a blog with the provided title, content, and user
    public Blog updateBlog(Blog blog, String title, String content, User user) {
        blog.setTitle(title);
        blog.setContent(content);
        blog.setLastModifiedBy(user);
        blog.setLastModifiedAt(LocalDateTime.now());

        return blogRepository.save(blog);
    }

    public BlogVersion getBlogVersionById(Long blogVersionId) {
        return blogVersionRepository.findById(blogVersionId)
                .orElseThrow(() -> new ResourceNotFoundException("BlogVersion with ID " + blogVersionId + " not found"));
    }

    public static void updateBlogContent(Long blogid, Blog blog, String title, String content, User user) {
        Blog blog1 = blogRepository.findById(blogid)
                .orElseThrow(() -> new ResourceNotFoundException("Blog with ID " + blogid + " not found"));
        blog1.setTitle(title);
        blog1.setContent(content);
        blog1.setLastModifiedBy(user);
        blog1.setLastModifiedAt(LocalDateTime.now());

        blogRepository.save(blog1);
    }

    public static void updateBlogContent(Long blogid, String title, String content, User createdBy) {
        Blog blog = blogRepository.findById(blogid)
                .orElseThrow(() -> new ResourceNotFoundException("Blog with ID " + blogid + " not found"));

        blog.setTitle(title);
        blog.setContent(content);
        blog.setLastModifiedBy(createdBy);
        blog.setLastModifiedAt(LocalDateTime.now());

        blogRepository.save(blog);
    }

    public void deleteBlogVersion(BlogVersion blogVersion) {
        // Step 2: Delete the blog version
        blogVersionRepository.delete(blogVersion);
    }



}
