package com.popo.todolist.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenResponseDto {

    private String accessToken;

    private String refreshToken;


    public TokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
