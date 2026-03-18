package com.example.hospitalmanagementsystem.JWT;

import com.example.hospitalmanagementsystem.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@Data
public class JwtService {

    private final String SECRET;
    private final Long EXPIRATION;
    private final String ISSUER;


    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration}") Long expiration,
                      @Value("${jwt.issuer}") String issuer
    ) {
        SECRET = secret;
        EXPIRATION = expiration;
        ISSUER = issuer;
    }


    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Generate AccessToken
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey())
                .compact();
    }

    // Generate RefreshToken
    public String generateRefreshToken(User user, String jti) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(jti)
                .subject(user.getId().toString())
                .issuer(ISSUER)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(EXPIRATION)))
                .claim("type", "refresh")
                .signWith(getKey())
                .compact();
    }

    // Is Access Token Valid
    public boolean isValidToken(String token, UserDetails userDetails) {
            return extractUsername(token).equals(userDetails.getUsername())
                    && !isExpired(token);
    }

    // Extract Username from accessToken
    public String extractUsername (String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Is AccessToken Expired
    private boolean isExpired(String token) {
        Date expiry = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiry.before(new Date());
    }

    // Check is RefreshToken
    public boolean isRefreshToken(String token) {
        String type = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("type", String.class);

        return "refresh".equals(type);
    }

    public String getJti(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();
    }

    public Long getUserId(String token) {
                  return Long.parseLong(
                          Jwts.parser()
                                  .verifyWith(getKey())
                                  .build()
                                  .parseSignedClaims(token)
                                  .getPayload()
                                  .getSubject()
                  );
    }

}
