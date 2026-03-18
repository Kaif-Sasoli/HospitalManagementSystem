package com.example.hospitalmanagementsystem.service;

import com.example.hospitalmanagementsystem.DTO.LoginRequestDto;
import com.example.hospitalmanagementsystem.DTO.LoginResponseDto;
import com.example.hospitalmanagementsystem.DTO.SignUpRequestDto;
import com.example.hospitalmanagementsystem.DTO.SignUpResponseDto;
import com.example.hospitalmanagementsystem.JWT.JwtService;
import com.example.hospitalmanagementsystem.entity.Patient;
import com.example.hospitalmanagementsystem.entity.RefreshToken;
import com.example.hospitalmanagementsystem.entity.User;
import com.example.hospitalmanagementsystem.entity.type.AuthProviderType;
import com.example.hospitalmanagementsystem.entity.type.RoleType;
import com.example.hospitalmanagementsystem.repository.PatientRepository;
import com.example.hospitalmanagementsystem.repository.RefreshTokenRepository;
import com.example.hospitalmanagementsystem.repository.UserRepository;
import com.example.hospitalmanagementsystem.util.AuthUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthUtil authUtil;

    private final UserRepository userRepository;

    private final PatientRepository patientRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final CookieService cookieService;

    // Login User
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto, AuthenticationManager authenticationManager,
                                      HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );
        if(!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid username or password");
        }

        User user = (User) authentication.getPrincipal();

        String token = jwtService.generateToken(user.getUsername());
        String jti = UUID.randomUUID().toString();

        var refreshTokenOb = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .createdAt(Instant.now())
                .expiredAt(Instant.now().plusSeconds(7 * 24 * 60 * 60))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshTokenOb);

        String refreshToken = jwtService.generateRefreshToken(user, jti);

        cookieService.attachRefreshTokenCookie(response, refreshToken, 7 * 24 * 60 * 60);

        return new LoginResponseDto(token);
    }


    public User signUpInternal(SignUpRequestDto signUpRequestDto, AuthProviderType authProviderType, String providerId) {

        if(userRepository.findByUsername(signUpRequestDto.getUsername()).isPresent()) {
            throw new UsernameNotFoundException(
                    "User with username" + signUpRequestDto.getUsername() + "already exists!"
            );
        }
        User user = User.builder()
                .username(signUpRequestDto.getUsername())
                .providerId(providerId)
                .roles(signUpRequestDto.getRoles())
                .authProviderType(authProviderType)
                .build();

        if(authProviderType == AuthProviderType.EMAIL) {
            user.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        }
        Patient patient = Patient.builder()
                .name(signUpRequestDto.getName())
                .email(signUpRequestDto.getEmail())
                .createdAt(LocalDate.now())
                .user(user)
                .build();

        userRepository.save(user);
        patientRepository.save(patient);

        return user;
    }

    @Transactional
    public SignUpResponseDto register(SignUpRequestDto signUpRequestDto) {
        User user = signUpInternal(signUpRequestDto, AuthProviderType.EMAIL, null);

        String jti = UUID.randomUUID().toString();

        var refreshToken = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .createdAt(Instant.now())
                .expiredAt(Instant.now().plusSeconds(jwtService.getEXPIRATION()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);

        return new SignUpResponseDto(
                jwtService.generateToken(user.getUsername()),
                jwtService.generateRefreshToken(user, refreshToken.getJti())
        );
    }

    @Transactional
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {

        AuthProviderType authProviderType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        User user = userRepository.findByProviderIdAndAuthProviderType(providerId, authProviderType).orElse(null);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User emailUser = userRepository.findByUsername(email).orElse(null);

        if(user == null && emailUser == null) {

            user = signUpInternal(
                    new SignUpRequestDto(name, null, name, email, Set.of(RoleType.PATIENT)),
                    authProviderType, providerId
            );
        }
        String token = jwtService.generateToken(name);

        return new ResponseEntity<>(new LoginResponseDto(token), HttpStatus.CREATED);
    }

    // Logout Logic
    public String logout(User user, HttpServletResponse response) {

        RefreshToken refreshToken = refreshTokenRepository
                .findFirstByUserAndRevokedFalseOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
        cookieService.removeRefreshTokenCookie(response);

        return "Done";
    }
}
