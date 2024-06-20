package com.popo.todolist.entity;

import com.popo.todolist.common.RootException;
import com.popo.todolist.common.constants.ResultCodeType;
import com.popo.todolist.entity.constatns.TodoStatus;
import com.popo.todolist.model.request.TodoCreateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.popo.todolist.entity.constatns.TodoStatus.*;

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

    public void validateStateTransition(TodoStatus nextStatus){
        switch (this.todoStatus) {
            case TODO:
                validateTransitionFromTodo(nextStatus);
                break;
            case IN_PROGRESS:
                validateTransitionFromInProgress(nextStatus);
                break;
            case PENDING:
                // PENDING 상태 일경우 어떤 상태로든 변경 가능.
                break;
            case DONE:
                // DONE 상태 일경우 변경 불가.
                throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }
    }

    private void validateTransitionFromTodo(TodoStatus nextStatus) {
        //  상태에서 가능한 다음 상태는 IN_PROGRESS, DONE, PENDING
        if (nextStatus == TODO) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }
    }

    private void validateTransitionFromInProgress(TodoStatus nextStatus) {
        if (nextStatus == IN_PROGRESS) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }

        // IN_PROGRESS 상태에서 변경 가능한 다음 상태는 PENDING, DONE
        if (!(nextStatus == PENDING || nextStatus == DONE)) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }
    }

}
