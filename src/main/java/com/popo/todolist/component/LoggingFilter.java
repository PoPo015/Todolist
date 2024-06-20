package com.popo.todolist.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popo.todolist.config.SecurityConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Value("${filter.logging.isLogDisable}")
    private boolean isLogDisable;
    private final ObjectMapper objectMapper;

    public LoggingFilter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        String uuid = UUID.randomUUID().toString();
        byte[] requestContent = requestWrapper.getContentAsByteArray();
        logRequest(uuid, requestWrapper, requestContent);
        filterChain.doFilter(requestWrapper, responseWrapper);
        logResponse(uuid, requestWrapper, responseWrapper);
        responseWrapper.copyBodyToResponse();
    }


    private void logRequest(String uuid, ContentCachingRequestWrapper requestWrapper, byte[] requestContent) {
        try {
            if(isStaticHtmlRequest(requestWrapper.getRequestURI())){
                log.info("> GET HTML FILE [URI] : {}", requestWrapper.getRequestURI());
                return;
            }
            if (requestWrapper.getContentType() != null && requestWrapper.getContentType().contains("application/json")) {
                JsonNode requestJson = this.objectMapper.readTree(requestContent);
                log.info("> [UUID] : {} [URI] : {} [RequestJson] : {}", uuid, requestWrapper.getRequestURI(), requestJson);
            } else {
                log.info("> [UUID] : {} [URI] : {}", uuid, requestWrapper.getRequestURI());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logResponse(String uuid, ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) {
        if (isLogDisable) {
            log.info("> 로그 비활성화 [RequestURL] : {} [logDisableStatus] : {}", requestWrapper.getRequestURL(), isLogDisable);
            return;
        }
        try {
            if(isStaticHtmlRequest(requestWrapper.getRequestURI())){
                log.info("> GET HTML FILE [URI] : {}", requestWrapper.getRequestURI());
                return;
            }
            if (requestWrapper.getContentType() != null && requestWrapper.getContentType().contains("application/json")) {
                JsonNode requestJson = this.objectMapper.readTree(requestWrapper.getContentAsByteArray());
                log.info("> [UUID] : {} [URI] : {} [RequestJson] : {}", uuid, requestWrapper.getRequestURI(), requestJson);
            } else {
                log.info("> [UUID] : {} [URI] : {}", uuid, requestWrapper.getRequestURI());
            }
            JsonNode responseJson = this.objectMapper.readTree(responseWrapper.getContentAsByteArray());
            log.info("> [UUID] : {} [URI] : {} [ResponseJson] : {}", uuid, requestWrapper.getRequestURI(), responseJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isStaticHtmlRequest(String requestURI) {
        return requestURI.startsWith("/html/");
    }


}
