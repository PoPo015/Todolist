package com.popo.todolist.common.handler;

import com.popo.todolist.common.Common;
import com.popo.todolist.common.CommonResponseDto;
import com.popo.todolist.common.constants.ResultCodeType;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

public class SuccessResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return new CommonResponseDto<>(new Common(ResultCodeType.SUCCESS), body);
    }

}

