package com.popo.todolist.common;

import com.popo.todolist.common.constants.ResultCodeType;
import lombok.Getter;

public class RootException extends RuntimeException{
    @Getter
   	private final ResultCodeType resultCodeType;

    public RootException(ResultCodeType resultCodeType) {
        this.resultCodeType = resultCodeType;
    }
}
