package com.popo.todolist.common.handler;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class SuccessBodyAdvice extends SuccessResponseAdvice {
}
