package com.popo.todolist.common.handler;

import com.popo.todolist.common.Common;
import com.popo.todolist.common.CommonResponseDto;
import com.popo.todolist.common.constants.ResultCodeType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CommonExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<CommonResponseDto<Object>> exception(HttpServletRequest request, Exception ex) {
        logRequestInfo(request, ex);
        Common common = new Common(ResultCodeType.SERVER_ERROR_5S000000);
        return ResponseEntity.ok(new CommonResponseDto<>(common));
    }

    private void logRequestInfo(HttpServletRequest request, Exception ex) {
        LocalDateTime now = LocalDateTime.now();
        log.error("> ======== [Exception Occurrence Time] : {}", now);
        getParameters(request);
        getCookies(request);
        getHeaders(request);
        getRemoteAddr(request);
        getURLAndException(request, ex);
        log.error("> ======== [Exception Occurrence Time] : {}", now);
    }
    private void getRemoteAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        log.warn("> [Remote Ip] : {}", ipAddress);
    }

    private void getHeaders(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            String headerValue = request.getHeader(headerName);
            log.warn("> [headerName] : {}, [headerValue] : {}", headerName, headerValue);
        }
    }

    private void getURLAndException(HttpServletRequest request, Exception ex) {
        log.warn("> [URL] : {} [Exception] : ", request.getRequestURL(), ex);
    }

    private void getParameters(HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String parameterName = params.nextElement();
            String parameterValue = request.getParameter(parameterName);
            log.warn("> [parameterName] : {}, [parameterValue] : {}", parameterName, parameterValue);
        }
    }

    private void getCookies(HttpServletRequest request) {
        Optional.ofNullable(request.getCookies())
                .ifPresent(cookies -> {
                    for (Cookie cookie : cookies) {
                        String cookieName = cookie.getName();
                        String cookieValue = cookie.getValue();
                        log.warn("> [cookieName] : {}, [cookieValue] : {}", cookieName, cookieValue);
                    }
                });
    }

}
