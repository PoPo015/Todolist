package com.popo.todolist.model.request;

import lombok.Getter;

@Getter
public class TodoUpdateContentRequestDto {

    private Long id;

    private String title;

    private String description;
}
