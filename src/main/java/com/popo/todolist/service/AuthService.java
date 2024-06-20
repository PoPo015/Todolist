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
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtRefreshService jwtRefreshService;
    private final JwtRefreshRepository jwtRefreshRepository;
    private final JwtUtil jwtUtil;
    private final PasswordService passwordService;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Transactional
    public void signup(SignupRequestDto signupRequest) {
        requestValidate(signupRequest);
        String encodePassword = passwordService.encodePassword(signupRequest.getPassword());
        UserEntity userEntity = new UserEntity(signupRequest.getEmail(), encodePassword, signupRequest.getNickName());
        userRepository.save(userEntity);
    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto loginRequest) {
        String requestEmail = loginRequest.getEmail();
        String requestPassword = loginRequest.getPassword();

        validateEmail(requestEmail);
        validatePassWord(requestPassword);

        UserEntity userEntity = userRepository.findByEmail(requestEmail)
                .orElseThrow(() -> new RootException(ResultCodeType.SERVER_ERROR_4S000000));

        passwordService.validatePasswordMatch(requestPassword, userEntity.getPassword());

        TokenResponseDto token = jwtUtil.createToken(userEntity);
        jwtRefreshRepository.save(new JwtRefreshEntity(userEntity, token.getRefreshToken()));
        return token;
    }

    @Transactional
    public void withdraw(Long userId, String password) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RootException(ResultCodeType.SERVER_ERROR_4S000000));
        passwordService.validatePasswordMatch(password, userEntity.getPassword());
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public TokenResponseDto refreshToken(String refreshToken) {
        JwtRefreshEntity jwtRefreshEntity = jwtRefreshRepository.findByTokenOrderByCreateDtDesc(refreshToken)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RootException(ResultCodeType.JWT_ERROR_40100000));

        if (!jwtRefreshEntity.getToken().equals(refreshToken)) {
            throw new RootException(ResultCodeType.JWT_ERROR_40300000);
        }
        return jwtUtil.refreshAccessToken(jwtRefreshEntity.getUser());
    }

    @Transactional(readOnly = true)
    public void removeRefreshToken() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(12);
        List<JwtRefreshEntity> expiredTokenList = jwtRefreshRepository.findByCreateDtBefore(cutoffTime);
        try {
            jwtRefreshService.deleteAll(expiredTokenList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void requestValidate(SignupRequestDto signupRequest) {
        String requestEmail = signupRequest.getEmail();
        String requestPassword = signupRequest.getPassword();
        String requestNickName = signupRequest.getNickName();

        validateEmail(requestEmail);
        validatePassWord(requestPassword);
        validateNickName(requestNickName);

        userRepository.findByEmail(requestEmail).ifPresent(user -> {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        });
        userRepository.findByNickName(requestNickName).ifPresent(user -> {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        });
    }

    private void validateNickName(String requestNickName) {
        if (StringUtils.isEmpty(requestNickName)) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }
    }

    private void validatePassWord(String requestPassword) {
        if (StringUtils.isEmpty(requestPassword)) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }
    }

    private void validateEmail(String requestEmail) {
        if (StringUtils.isEmpty(requestEmail)) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }

        if (requestEmail.length() > 100) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }

        if (!EMAIL_PATTERN.matcher(requestEmail).matches()) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }
    }

}
