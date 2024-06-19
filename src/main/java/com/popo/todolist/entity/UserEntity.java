package com.popo.todolist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tb_user")
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "nick_name", nullable = false, length = 30)
    private String nickName;


    @BatchSize(size = 50)
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JwtRefreshEntity> jwtRefreshEntityList = new ArrayList<>();

    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "userEntity",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoEntity> todoEntityList = new ArrayList<>();


    public UserEntity() {
    }

    public UserEntity(String email, String password, String nickName) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }
}
