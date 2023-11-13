package com.we_write.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.we_write.entity.Blog;
import com.we_write.entity.BlogVersion;
import com.we_write.entity.User;
import com.we_write.repository.BlogVersionRepository;
@Service
public class BlogVersionService {

    @Autowired
    private BlogVersionRepository blogVersionRepository;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    public BlogVersion createVersion(Long blogId, String content, User createdBy) {
        // Step 1: Fetch the blog and contributor
        Blog blog = blogService.getBlogById(blogId);
        User contributor = userService.getUserByUsername(createdBy);

        // Step 2: Create a new BlogVersion
        BlogVersion version = new BlogVersion();
        version.setBlog(blog);
        version.setCreatedBy(createdBy);
        version.setContent(content);

        // Step 3: Save the new BlogVersion
        return blogVersionRepository.save(version);
    }

    public List<BlogVersion> getVersionsForBlog(Long blogId) {
        // Step 1: Fetch the blog
        Blog blog = blogService.getBlogById(blogId);

        // Step 2: Get versions for the blog
        return blogVersionRepository.findByBlogOrderByCreatedAtDesc(blog);
    }
}

