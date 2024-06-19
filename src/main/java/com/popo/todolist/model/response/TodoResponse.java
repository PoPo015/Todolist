package com.popo.todolist.model.response;

import com.popo.todolist.entity.TodoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TodoResponse {
    private Long id;
    private String title;
    private String description;
    private String status;


    public TodoResponse(TodoEntity todoEntity) {
        if (todoEntity == null) {
            return;
        }
        this.id = todoEntity.getId();
        this.title = todoEntity.getTitle();
        this.description = todoEntity.getDescription();
        this.status = todoEntity.getTodoStatus().toString();
    }

    public TodoResponse(Long id, String title, String description, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
