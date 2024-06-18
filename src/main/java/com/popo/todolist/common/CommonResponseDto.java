package com.popo.todolist.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@ToString
@Getter
@Slf4j
@RequiredArgsConstructor
public class CommonResponseDto<T> implements Serializable {
    private static final long serialVersionUID = -4498168998405018053L;

    private final Common common;
    private T result;

    public CommonResponseDto(Common common, T result) {
        this.common = common;
        this.result = result;
    }

}
