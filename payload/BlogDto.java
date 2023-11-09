package com.we_write.payload;


import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.security.core.userdetails.User;

import io.swagger.v3.oas.annotations.media.Schema;


    // title should not be null  or empty
    // title should have at least 2 characters
//    private Long blogid;
//    
//    private String title;
//    private String content;
//    
//    @ManyToOne
//    @CreatedBy
//    private User createdBy;
//    
//    @CreatedDate
//    private LocalDateTime createdAt;
//    
//    @ManyToOne
//    @LastModifiedBy
//    private User lastModifiedBy;
//    
//    @LastModifiedDate
//    private LocalDateTime lastModifiedAt;

    
@Data
@Schema(
        description = "PostDto Model Information"
)
public class BlogDto {
    private long id;

    @Schema(
            description = "Blog Post Title"
    )
    @NotEmpty
    @Size(min = 2, message = "Blog title should have at least 2 characters")
    private String title;

    @Schema(
            description = "Blog Post Description"
    )
    // post description should be not null or empty
    // post description should have at least 10 characters
    @NotEmpty
    @Size(min = 10, message = "Blog description should have at least 10 characters")
    private String description;

    @Schema(
            description = "Blog Post Content"
    )
    // post content should not be null or empty
    @NotEmpty
    private String content;
    private Set<CommentDto> comments;

    @Schema(
            description = "Blog Post Category"
    )
    
    private User lastModifiedBy;
    private LocalDateTime lastModifiedAt;
    
    private Long categoryId;
}