package com.example.hospitalmanagementsystem.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.example.hospitalmanagementsystem.entity.type.RoleType.*;


@Component
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final HandlerExceptionResolver handlerExceptionResolver;

    private final OAuthSuccessHandler oAuth2SuccessHandler;

    private final JWTAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return  http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/oauth2/**","/login/oauth2/**","/auth/**", "/token/refresh").permitAll()
                        .requestMatchers("/admin/**").hasRole(ADMIN.name())
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth -> oauth
                        .failureHandler((request, response, exception) -> {
                            log.error("OAuth2 error: {}", exception.getMessage());
                            handlerExceptionResolver.resolveException(request, response, null, exception);
                        })
                        .successHandler(oAuth2SuccessHandler)
                )
//                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
//                                .accessDeniedHandler((request, response, accessDeniedException) -> {
//                                    handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
//                                })
//                                .authenticationEntryPoint((request, response, authException) -> {
//                                    handlerExceptionResolver.resolveException(request, response, null, authException);
//                                })
//                )
                .build();

    }
//@Bean
//@Order(1)
//public SecurityFilterChain apiFilterChain(HttpSecurity http) {
//    return http
//            .securityMatcher("/admin/**") // only API endpoints
//            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
//            .exceptionHandling(e -> e
//                    .authenticationEntryPoint((req, res, ex) -> {
//                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    }))
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//            .csrf(AbstractHttpConfigurer::disable)
//            .build();
//}
//
//    @Bean
//    @Order(2)
//    public SecurityFilterChain webFilterChain(HttpSecurity http) {
//        return http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers( "/login", "/oauth2/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth -> oauth.successHandler(oAuth2SuccessHandler))
//                .build();
//    }
}
