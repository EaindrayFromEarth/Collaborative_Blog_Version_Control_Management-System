package com.we_write.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.we_write.entity.Blog;
import com.we_write.entity.BlogVersion;
import com.we_write.entity.User;
import com.we_write.repository.BlogRepository;
import com.we_write.repository.BlogVersionRepository;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BlogVersionRepository blogVersionRepository;
    

    public Blog createNewVersion(Long blogId, String title, String content, User user) {
        Blog blog = blogRepository.findById(blogId).orElse(null);

        if (blog != null) {
            BlogVersion blogVersion = new BlogVersion();
            blogVersion.setBlog(blog);
            blogVersion.setTitle(title);
            blogVersion.setContent(content);
            blogVersion.setCreatedBy(user);

            blogVersionRepository.save(blogVersion);

            // Update the current blog content
            blog.setTitle(title);
            blog.setContent(content);
            blog.setLastModifiedBy(user);
            blog.setLastModifiedAt(LocalDateTime.now());

            blogRepository.save(blog);

            return blog;
        }

        return null;
    }

    public List<BlogVersion> getVersionsForBlog(Long blogId) {
        return blogVersionRepository.findByBlogIdOrderByCreatedAt(blogId);
    }
}
