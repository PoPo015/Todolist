package com.popo.todolist.service;

import com.popo.todolist.common.RootException;
import com.popo.todolist.common.constants.ResultCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final BCryptPasswordEncoder passwordEncoder;

    public String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public void validatePasswordMatch(String plainPassword, String encodedPassword) {
        boolean matches = passwordEncoder.matches(plainPassword, encodedPassword);
        if (!matches){
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }
    }
}
