

package com.we_write.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.we_write.entity.Blog;
import com.we_write.entity.BlogVersion;
import com.we_write.entity.Review;
import com.we_write.entity.ReviewStatus;
import com.we_write.entity.User;
import com.we_write.exception.ResourceNotFoundException;
import com.we_write.repository.BlogRepository;
import com.we_write.repository.BlogVersionRepository;
import com.we_write.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private BlogService blogService;
    
    @Autowired
    private BlogVersionRepository blogVersionRepository;

    @Autowired
    private static BlogRepository blogRepository;

    public Review createReview(User reviewer, Long blogVersionId) {
        BlogVersion blogVersion = blogService.getBlogVersionById(blogVersionId);

        if (blogVersion != null) {
            Review review = new Review();
            review.setUsername(reviewer);
            review.setBlogVersion(blogVersion);
            review.setStatus(ReviewStatus.PENDING);
            return reviewRepository.save(review);
        } else {
            throw new ResourceNotFoundException("BlogVersion with ID " + blogVersionId + " not found");
        }
    }


    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> getPendingReviews() {
        return reviewRepository.findByStatus(ReviewStatus.PENDING);
    }


    public void processReview(Long reviewId, ReviewStatus status) {
        // Step 1: Retrieve the review by ID
        Review review = reviewRepository.findById(reviewId).orElse(null);

        // Step 2: Check if the review exists and is pending
        if (review != null && review.getStatus() == ReviewStatus.PENDING) {
            // Step 3: Update the review status
            review.setStatus(status);

            // Step 4: Process the review based on the status
            if (status == ReviewStatus.APPROVED) {
                applyChanges(review.getBlogVersion());
            } else if (status == ReviewStatus.REJECTED) {
                // Step 5: Handle rejected status
                handleRejectedReview(review);
            }

            // Step 6: Save the updated review
            reviewRepository.save(review);
        }
    }

    // ... other methods

    private void handleRejectedReview(Review review) {
        // Step 1: Get the ID of the blog version to be deleted
        Long blogVersionId = review.getBlogVersion().getId();

        // Step 2: Delete the blog version
        deleteBlogVersion(blogVersionId);

        // Step 3: Notify the user who edited the blog about the rejection
       // notifyUserAboutRejection(review.getBlogVersion().getCreatedBy(), "Your blog edit has been rejected.");
        
        // Additional logic for handling rejected review
    }

    private void deleteBlogVersion(Long blogVersionId) {
        // Step 1: Get the blog version by ID
        BlogVersion blogVersion = blogService.getBlogVersionById(blogVersionId);

        // Step 2: Check if the blog version exists
        if (blogVersion != null) {
            // Step 3: Delete the blog version
            blogService.deleteBlogVersion(blogVersion);
        }
    }



	public void applyChanges(BlogVersion blogVersion) {
        if (blogVersion != null && blogVersion.getBlog() != null) {
            Blog blog = blogVersion.getBlog();
            BlogService.updateBlogContent(blog.getBlogid(), blogVersion.getTitle(), blogVersion.getContent(), blogVersion.getCreatedBy());
        } else {
            throw new ResourceNotFoundException("Invalid BlogVersion or Blog");
        }
    }

    public void applyBlogVersionChanges(BlogVersion blogVersion) {
        Blog blog = blogRepository.findById(blogVersion.getBlog().getBlogid())
                .orElseThrow(() -> new ResourceNotFoundException("Blog with ID " + blogVersion.getBlog().getBlogid() + " not found"));

        blog.setTitle(blogVersion.getTitle());
        blog.setContent(blogVersion.getContent());
        blog.setLastModifiedBy(blogVersion.getCreatedBy());
        blog.setLastModifiedAt(LocalDateTime.now());

        blogRepository.save(blog);
    }

    // Other review-related methods
}
