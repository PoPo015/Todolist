package com.popo.todolist.batch;

import com.popo.todolist.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRefreshTokenTask {

    private final AuthService authService;

    @Scheduled(cron = "0 */5 * * * *")
    public void runRemoveRefreshToken(){
        log.info("> 리프레시 토큰 삭제 배치 시작");
        authService.removeRefreshToken();
        log.info("> 리프레시 토큰 삭제 배치 종료");
    }

}
