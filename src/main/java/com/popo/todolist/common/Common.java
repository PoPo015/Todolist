package com.popo.todolist.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.popo.todolist.common.constants.ResultCodeType;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class Common {
    private final String code;
    private final String msg;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime timestamp;

    public Common(ResultCodeType resultCodeType) {
        this.code = resultCodeType.getCode();
        this.msg = resultCodeType.getMsg();
        this.timestamp = LocalDateTime.now();
    }
}
