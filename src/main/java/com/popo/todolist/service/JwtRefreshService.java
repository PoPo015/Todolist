package com.popo.todolist.service;

import com.popo.todolist.entity.JwtRefreshEntity;
import com.popo.todolist.repository.JwtRefreshRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtRefreshService {

    private final JwtRefreshRepository jwtRefreshRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAll(List<JwtRefreshEntity> expiredTokenList){
        for (JwtRefreshEntity jwtRefreshEntity : expiredTokenList) {
            log.info("> 만료된 jwtRefreshToken [token] : {} [id]: {}", jwtRefreshEntity.getToken(), jwtRefreshEntity.getId());
            jwtRefreshRepository.delete(jwtRefreshEntity);
        }
    }
}
