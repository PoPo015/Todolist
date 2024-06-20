package com.popo.todolist.common.constants;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ResultCodeType {

    SUCCESS("2S000000", "성공"),
    SERVER_ERROR_5S000000("5S000000", "서버 내부 오류"),
    SERVER_ERROR_4S000000("4S000000", "비정상적인 요청"),


    //JWT
    JWT_ERROR_40000000("40000000", "비정상 토큰"),
    JWT_ERROR_40100000("40100000", "비인증(로그인필요)"),
    JWT_ERROR_40300000("40300000", "권한 없음"),
    JWT_ERROR_40500000("40500000", "만료된 토큰")


    ;

    private final String code;
    private final String msg;

    ResultCodeType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
