package com.popo.todolist.entity;

import com.popo.todolist.entity.constatns.TodoStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "tb_todo")
public class TodoEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private UserEntity userEntity;

    @Column(name = "title", length = 2000)
    private String title;

    @Column(name = "content", length = 10)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    private TodoStatus todoStatus;

    @LastModifiedBy
    @Column(name = "update_dt", nullable = true)
    private LocalDateTime updateDt;

}
