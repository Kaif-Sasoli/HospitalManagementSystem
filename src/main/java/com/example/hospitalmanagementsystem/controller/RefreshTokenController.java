package com.example.hospitalmanagementsystem.controller;

import com.example.hospitalmanagementsystem.DTO.RefreshTokenRequest;
import com.example.hospitalmanagementsystem.DTO.TokenResponse;
import com.example.hospitalmanagementsystem.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("token")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @GetMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody(required = false) RefreshTokenRequest body,
            HttpServletRequest request, HttpServletResponse response
            ) {

        TokenResponse tokenResponse =  refreshTokenService.readRefreshTokenFromRequest(
                request, response, body
        );
         return new ResponseEntity<>(tokenResponse, HttpStatus.CREATED);
    }
}
