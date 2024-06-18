package com.popo.todolist.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "tb_refresh_token")
public class JwtRefreshEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private UserEntity user;

    @Column(name = "token", nullable = false)
    private String token;


    public JwtRefreshEntity() {
    }

    public JwtRefreshEntity(UserEntity userEntity, String token) {
        this.user = userEntity;
        this.token = token;
    }
}
