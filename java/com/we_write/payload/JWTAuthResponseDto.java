package com.we_write.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
    
    public JWTAuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}