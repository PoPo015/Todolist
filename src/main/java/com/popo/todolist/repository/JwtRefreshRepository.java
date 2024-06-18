package com.popo.todolist.repository;

import com.popo.todolist.entity.JwtRefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JwtRefreshRepository extends JpaRepository<JwtRefreshEntity, Long> {
    List<JwtRefreshEntity> findByTokenOrderByCreateDtDesc(String refreshToken);
}
