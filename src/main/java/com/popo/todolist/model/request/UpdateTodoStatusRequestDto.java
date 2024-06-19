package com.popo.todolist.model.request;

import com.popo.todolist.entity.constatns.TodoStatus;
import lombok.Getter;

@Getter
public class UpdateTodoStatusRequestDto {

    private Long id;

    private TodoStatus status;
}
