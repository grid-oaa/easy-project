package com.easy.auth.security;

import com.easy.auth.config.AuthProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * JWT 生成与解析服务。
 */
@Component
public class JwtTokenService {
    private final AuthProperties authProperties;
    private final SecretKey secretKey;

    public JwtTokenService(AuthProperties authProperties) {
        this.authProperties = authProperties;
        this.secretKey = Keys.hmacShaKeyFor(authProperties.getJwtSecret().getBytes(StandardCharsets.UTF_8));
    }

    public TokenPair createTokenPair(Long userId, String username) {
        String accessJti = UUID.randomUUID().toString();
        String refreshJti = UUID.randomUUID().toString();
        Instant now = Instant.now();
        Instant accessExp = now.plusSeconds(authProperties.getAccessTokenExpireSeconds());
        Instant refreshExp = now.plusSeconds(authProperties.getRefreshTokenExpireSeconds());

        String accessToken = Jwts.builder()
            .setSubject(String.valueOf(userId))
            .setIssuer(authProperties.getIssuer())
            .setId(accessJti)
            .claim("username", username)
            .claim("type", "access")
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(accessExp))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();

        String refreshToken = Jwts.builder()
            .setSubject(String.valueOf(userId))
            .setIssuer(authProperties.getIssuer())
            .setId(refreshJti)
            .claim("username", username)
            .claim("type", "refresh")
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(refreshExp))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();

        return new TokenPair(accessToken, refreshToken, accessExp.getEpochSecond(), refreshExp.getEpochSecond());
    }

    public Claims parse(String token) {
        return Jwts.parser()
            // 使用对称密钥验签并解析 Claims
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public record TokenPair(String accessToken, String refreshToken, long accessExpireAt, long refreshExpireAt) {}
}
