package com.popo.todolist.controller;

import com.popo.todolist.component.security.CustomUserDetails;
import com.popo.todolist.model.request.LoginRequestDto;
import com.popo.todolist.model.request.RefreshRequestDto;
import com.popo.todolist.model.request.SignupRequestDto;
import com.popo.todolist.model.response.TokenResponseDto;
import com.popo.todolist.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequestDto signupRequest) {
        authService.signup(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        TokenResponseDto tokens = authService.login(loginRequest);
        return ResponseEntity.ok(tokens);
    }

    @DeleteMapping("/withdraw")
    public void withdraw(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                         @RequestParam("password") String password) {
        authService.withdraw(customUserDetails.getId(), password);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody RefreshRequestDto refreshRequestDto) {
        TokenResponseDto tokens = authService.refreshToken(refreshRequestDto.getRefreshToken());
        return ResponseEntity.ok(tokens);
    }

//    회원가입 중복체크. API 필요.
//    로그아웃 API 필요

}
