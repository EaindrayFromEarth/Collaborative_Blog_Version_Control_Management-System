package com.we_write.payload;


import javax.validation.constraints.NotBlank;


import lombok.Data;

@Data
public class BlogVersionRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}