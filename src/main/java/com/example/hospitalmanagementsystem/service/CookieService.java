package com.example.hospitalmanagementsystem.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@Data
public class CookieService {

    private final String refreshTokenCookieName;
    private final boolean cookieHttpOnly;
    private final boolean cookieSecure;
    private final String cookieDomain;
    private final String cookieSameSite;


    public CookieService(
            @Value("${jwt.refresh-token.cookie-name}") String refreshTokenCookieName,
            @Value("${jwt.refresh-token.cookie-http-only}") boolean cookieHttpOnly,
            @Value("${jwt.refresh-token.cookie-secure}") boolean cookieSecure,
            @Value("${jwt.refresh-token.cookie-domain:}") String cookieDomain,
            @Value("${jwt.refresh-token.cookie-same-site}") String cookieSameSite
    ) {
        this.refreshTokenCookieName = refreshTokenCookieName;
        this.cookieHttpOnly = cookieHttpOnly;
        this.cookieSecure = cookieSecure;
        this.cookieDomain = cookieDomain;
        this.cookieSameSite = cookieSameSite;
    }


    public void attachRefreshTokenCookie(HttpServletResponse response, String value, int maxAge) {

            ResponseCookie.ResponseCookieBuilder builder =
                    ResponseCookie.from(refreshTokenCookieName, value)
                    .httpOnly(cookieHttpOnly)
                    .secure(cookieSecure)
                    .path("/")
                    .maxAge(maxAge)
                    .sameSite(cookieSameSite);

            if(cookieDomain !=null && !cookieDomain.isBlank()) {
                builder.domain(cookieDomain);
            }

            response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }

    public void removeRefreshTokenCookie(HttpServletResponse response) {

        ResponseCookie.ResponseCookieBuilder builder =
                ResponseCookie.from(refreshTokenCookieName, null)
                        .httpOnly(cookieHttpOnly)
                        .secure(cookieSecure)
                        .path("/")
                        .maxAge(0)
                        .sameSite(cookieSameSite);

        if (cookieDomain != null && !cookieDomain.isBlank()) {
            builder.domain(cookieDomain);
        }

        response.addHeader(HttpHeaders.SET_COOKIE, builder.build().toString());
    }


    // Extract Refresh Token from the Cookies
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        if(request.getCookies() == null) return Optional.empty();

        return Arrays.stream(request.getCookies())
                .filter(c -> refreshTokenCookieName.equals(c.getName()))
                .map(c -> c.getValue())
                .filter(v -> !v.isBlank())
                .findFirst();
    }
}
