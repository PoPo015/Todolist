package com.popo.todolist.common.constants;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ResultCodeType {

    SUCCESS("2S000000", "성공"),
    SERVER_ERROR_5S000000("5S000000", "서버 내부 오류"),
    SERVER_ERROR_4S000000("4S000000", "비정상적인 요청"),
    ;

    private final String code;
    private final String msg;

    ResultCodeType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
