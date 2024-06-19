package com.popo.todolist.controller;

import com.popo.todolist.component.security.CustomUserDetails;
import com.popo.todolist.model.request.TodoCreateRequestDto;
import com.popo.todolist.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public void createTodo(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                           @RequestBody TodoCreateRequestDto todoCreateRequestDto) {
        todoService.createTodo(customUserDetails.getId(), todoCreateRequestDto);
    }

}
