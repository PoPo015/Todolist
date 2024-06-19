package com.popo.todolist.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoListResponse {

    private TodoResponse latest;
    private List<TodoResponse> todoList;
    private List<TodoResponse> inProgressList;
    private List<TodoResponse> pendingList;
    private List<TodoResponse> doneList;
}
