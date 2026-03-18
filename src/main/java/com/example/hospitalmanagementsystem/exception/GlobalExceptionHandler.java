package com.example.hospitalmanagementsystem.exception;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ErrorResponse> userAlreadyExists(UserAlreadyExists userAlreadyExists) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                         HttpStatus.CONFLICT.value(),
                         "USER_ALREADY_EXISTS",
                         userAlreadyExists.getMessage(),
                         LocalDateTime.now()
                ));
    }

//
//    @ExceptionHandler(JwtException.class)
//    public ResponseEntity<ErrorResponse> handleJwtException(Exception exception) {
//        log.error("Jwt: {}", exception);
//        return new ResponseEntity<>(new ErrorResponse(
//                HttpStatus.FORBIDDEN.value(),
//                "Invalid JWT Token",
//                exception.getMessage(),
//                LocalDateTime.now()
//        ), HttpStatus.FORBIDDEN);
//    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(Exception exception) {
        log.error("AccessDeniedException: {} ", exception);
        return new ResponseEntity<>(new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Access denied",
                exception.getMessage(),
                LocalDateTime.now()
        ), HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.error("Authentication Error: {}", ex);
        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        "UNAUTHORIZED",
                        ex.getMessage(),
                        LocalDateTime.now()
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception exceptions) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                exceptions.getMessage(),
                LocalDateTime.now()
        );
        log.error("Exception: {}", exceptions.getMessage());
        log.error("Exception Detail: {}", exceptions);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
