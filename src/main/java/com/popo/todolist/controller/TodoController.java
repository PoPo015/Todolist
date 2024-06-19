package com.popo.todolist.controller;

import com.popo.todolist.component.security.CustomUserDetails;
import com.popo.todolist.model.request.UpdateTodoStatusRequestDto;
import com.popo.todolist.model.request.TodoCreateRequestDto;
import com.popo.todolist.model.request.TodoUpdateContentRequestDto;
import com.popo.todolist.model.response.TodoListResponse;
import com.popo.todolist.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/status")
    public void updateTodoStatus(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                 @RequestBody UpdateTodoStatusRequestDto updateTodoStatusRequestDto) {
        todoService.updateTodoStatus(customUserDetails.getId(), updateTodoStatusRequestDto);
    }

    @PatchMapping
    public void updateTodoContent(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  @RequestBody TodoUpdateContentRequestDto updateTodoContentRequestDto) {
        todoService.updateTodoContent(customUserDetails.getId(), updateTodoContentRequestDto);
    }

    @GetMapping
    public ResponseEntity<TodoListResponse> getAllTodos(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(todoService.getAllTodos(customUserDetails.getId()));
    }

}
