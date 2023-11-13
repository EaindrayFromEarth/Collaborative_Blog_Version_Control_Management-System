package com.we_write.payload;


import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;


import lombok.Data;

@Data
//Equivalent to @Getter @Setter 
//@RequiredArgsConstructor
//@ToString @EqualsAndHashCode. 

public class BlogVersionRequest {

	@NotBlank
    private Long id;
	@NotBlank
    private String title;
    private String content;
    private LocalDateTime createdAt;

    private Long blogId;
    private String blogTitle;

    private Long createdByUserId;
    private String createdByUsername;

}