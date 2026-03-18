package com.example.hospitalmanagementsystem.service;

import com.example.hospitalmanagementsystem.DTO.RefreshTokenRequest;
import com.example.hospitalmanagementsystem.DTO.TokenResponse;
import com.example.hospitalmanagementsystem.JWT.JwtService;
import com.example.hospitalmanagementsystem.entity.RefreshToken;
import com.example.hospitalmanagementsystem.entity.User;
import com.example.hospitalmanagementsystem.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final CookieService cookieService;

    private final JwtService jwtService;

    private final RefreshTokenRepository refreshTokenRepository;


    public TokenResponse readRefreshTokenFromRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            RefreshTokenRequest body
    ) {

        // 1 Read Refresh Token form cookies
        String refreshToken = cookieService.extractRefreshToken(request)
                .orElseThrow(() -> new BadCredentialsException("Refresh token missing"));

        // 2 Validate Refresh Token
        if(!jwtService.isRefreshToken(refreshToken)){
            throw new BadCredentialsException("Invalid refresh token");
        }

        // 3 Extract Data form JWT
        String jti = jwtService.getJti(refreshToken);
        Long userId = jwtService.getUserId(refreshToken);

        // 4 Load token from the DB
        RefreshToken storedToken = refreshTokenRepository.findByJti(jti)
                .orElseThrow(() -> new BadCredentialsException("Refresh token not found"));

        // 5 Validate Token
        validateStoredToken(storedToken, userId);

        // 6 Revoke old Token (Rotation)
        storedToken.setRevoked(true);
        String newJti = UUID.randomUUID().toString();
        storedToken.setReplacedByToken(newJti);
        refreshTokenRepository.save(storedToken);

        // 7. Create new refresh token
        RefreshToken newToken = RefreshToken.builder()
                .jti(newJti)
                .user(storedToken.getUser())
                .createdAt(Instant.now())
                .expiredAt(Instant.now().plusSeconds(7 * 24 * 60 * 60))
                .revoked(false)
                .build();

        refreshTokenRepository.save(newToken);

        // 8. Generate JWTs
        String newAccessToken =
                jwtService.generateToken(storedToken.getUser().getUsername());

        String newRefreshToken =
                jwtService.generateRefreshToken(storedToken.getUser(), newJti);

        cookieService.attachRefreshTokenCookie(response, newRefreshToken, 7 * 24 * 60 * 60);

        return new TokenResponse(newRefreshToken, newAccessToken);

    }

    private void validateStoredToken(RefreshToken token, Long userId) {
        if (token.isRevoked()) {
            throw new BadCredentialsException("Refresh token revoked");
        }
        if (token.getExpiredAt().isBefore(Instant.now())) {
            throw new BadCredentialsException("Refresh token expired");
        }
        if (!token.getUser().getId().equals(userId)) {
            throw new BadCredentialsException("Token user mismatch");
        }
    }

}
