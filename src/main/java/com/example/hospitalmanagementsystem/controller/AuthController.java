package com.example.hospitalmanagementsystem.controller;

import com.example.hospitalmanagementsystem.DTO.LoginRequestDto;
import com.example.hospitalmanagementsystem.DTO.LoginResponseDto;
import com.example.hospitalmanagementsystem.DTO.SignUpRequestDto;
import com.example.hospitalmanagementsystem.DTO.SignUpResponseDto;
import com.example.hospitalmanagementsystem.entity.User;
import com.example.hospitalmanagementsystem.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

// Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response
    ) {
        return new ResponseEntity<>(
                authService.loginUser(loginRequestDto, authenticationManager, response),
                HttpStatus.OK
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return new ResponseEntity<>(authService.register(signUpRequestDto), HttpStatus.CREATED);
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(
            @AuthenticationPrincipal User user,
            HttpServletResponse response
    ){
          return new ResponseEntity<>(authService.logout(user, response), HttpStatus.OK);
    }


}
