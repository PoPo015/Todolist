package com.popo.todolist.entity;

import com.popo.todolist.entity.constatns.TodoStatus;
import com.popo.todolist.model.request.TodoCreateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "tb_todo")
public class TodoEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private UserEntity userEntity;

    @Column(name = "title", length = 2000, nullable = false)
    private String title;

    @Column(name = "description", length = 10, nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private TodoStatus todoStatus;

    @Column(name = "update_dt", nullable = true)
    private LocalDateTime updateDt;

    public TodoEntity() {
    }

    public TodoEntity(UserEntity userEntity, TodoCreateRequestDto todoCreateRequestDto) {
        userEntity.getTodoEntityList().add(this);
        this.userEntity = userEntity;
        this.title = todoCreateRequestDto.getTitle();
        this.description = todoCreateRequestDto.getDescription();
        this.todoStatus = TodoStatus.TODO;
    }

    public void updateStatus(TodoStatus todoStatus) {
        this.todoStatus = todoStatus;
        this.updateDt = LocalDateTime.now();
    }

    public void updateContent(String title, String description) {
        this.title = title;
        this.description = description;
        this.updateDt = LocalDateTime.now();
    }
}
