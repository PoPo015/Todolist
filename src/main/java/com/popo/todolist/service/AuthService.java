package com.popo.todolist.service;

import com.popo.todolist.common.RootException;
import com.popo.todolist.common.constants.ResultCodeType;
import com.popo.todolist.component.JwtUtil;
import com.popo.todolist.entity.JwtRefreshEntity;
import com.popo.todolist.entity.UserEntity;
import com.popo.todolist.model.request.LoginRequestDto;
import com.popo.todolist.model.request.SignupRequestDto;
import com.popo.todolist.model.response.TokenResponseDto;
import com.popo.todolist.repository.JwtRefreshRepository;
import com.popo.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtRefreshRepository jwtRefreshRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(SignupRequestDto signupRequest) {
        // TODO KST
        // dto validation 로직 추가
        // email/nickname 중복 로직 추가
        // password 평문 암호화 저장 추가
        UserEntity userEntity = new UserEntity(signupRequest.getEmail(), signupRequest.getPassword(), signupRequest.getNickName());
        userRepository.save(userEntity);
    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto loginRequest) {
        // TODO KST dto validation 로직 추가
        String requestEmail = loginRequest.getEmail();
        UserEntity userEntity = userRepository.findByEmail(requestEmail)
                .orElseThrow(() -> new RootException(ResultCodeType.SERVER_ERROR_4S000000));

        // TODO KST request들어온 평문 Password와 DB에 암호화 되어있는 패스워드와 비교 검증 필요
        TokenResponseDto token = jwtUtil.createToken(userEntity);
        jwtRefreshRepository.save(new JwtRefreshEntity(userEntity, token.getRefreshToken()));
        return token;
    }


    @Transactional
    public void withdraw(Long userId, String password) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RootException(ResultCodeType.SERVER_ERROR_4S000000));
        // TODO KST
        // 패스워드 검증 로직 추가 필요
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public TokenResponseDto refreshToken(String refreshToken) {

        // 제일 마지막으로 등록된것과, refresh로 요청온걸 비교
        JwtRefreshEntity jwtRefreshEntity = jwtRefreshRepository.findByTokenOrderByCreateDtDesc(refreshToken)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RootException(ResultCodeType.SERVER_ERROR_4S000000));

        if(!jwtRefreshEntity.getToken().equals(refreshToken)){
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }


        // TODO KST Batch 혹은 Redis TTL을 통해 refresh token을 주기적으로 지워주는 로직 필요

        // 로그인후 ~ 그냥 자연스럽게 끝나면 DB에 남아있는데.. ## 배치나 Redis TTL을 통해 삭제 필요
        // 탈퇴, 로그아웃 하면 전부삭제
        // 탈퇴, 로그아웃후 다시 들어오면 ?
        // 일단 액세스키는 털리는건 어느정도 허용한다..  (ㅈ짧게가져간다)
        // 액세스키가 만료되면 다시 리프레시에 호출할것이고, 이때 없으니 에러 터짐!
        return jwtUtil.refreshAccessToken(jwtRefreshEntity.getUser());
    }


}
