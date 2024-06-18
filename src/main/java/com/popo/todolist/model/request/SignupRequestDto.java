package com.popo.todolist.model.request;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String email;
    private String password;
    private String nickName;
}
