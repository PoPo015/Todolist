package com.popo.todolist.common.handler;

import com.popo.todolist.common.Common;
import com.popo.todolist.common.CommonResponseDto;
import com.popo.todolist.common.RootException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler extends CommonExceptionHandler {

    @ExceptionHandler(value = RootException.class)
    public ResponseEntity<CommonResponseDto<Object>> rootException(HttpServletRequest request, RootException rootException) {
        log.error("> [Request] : {}, [rootException] : {}", request.getRequestURI(), rootException);
        Common common = new Common(rootException.getResultCodeType());
        return ResponseEntity.ok(new CommonResponseDto<>(common));
    }


}
