package com.easy.auth.service;

import com.easy.auth.config.AuthProperties;
import com.easy.auth.dto.TokenResponse;
import com.easy.auth.model.User;
import com.easy.auth.repository.UserRepository;
import com.easy.auth.security.AccessTokenBlacklistStore;
import com.easy.auth.security.JwtTokenService;
import com.easy.auth.security.PasswordHasher;
import com.easy.auth.security.RefreshTokenStore;
import com.easy.common.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

/**
 * 认证服务，当前仅支持账号密码登录，后续可扩展短信/验证码。
 */
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenStore refreshTokenStore;
    private final AccessTokenBlacklistStore accessTokenBlacklistStore;
    private final AuthProperties authProperties;

    public AuthService(UserRepository userRepository,
                       PasswordHasher passwordHasher,
                       JwtTokenService jwtTokenService,
                       RefreshTokenStore refreshTokenStore,
                       AccessTokenBlacklistStore accessTokenBlacklistStore,
                       AuthProperties authProperties) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenStore = refreshTokenStore;
        this.accessTokenBlacklistStore = accessTokenBlacklistStore;
        this.authProperties = authProperties;
    }

    public Long register(String username, String password) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new AuthServiceException(ErrorCode.USER_EXISTS);
        });
        String hashed = passwordHasher.hash(password);
        return userRepository.insert(username, hashed);
    }

    public TokenResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthServiceException(ErrorCode.USER_NOT_FOUND));
        if (!passwordHasher.matches(password, user.getPasswordHash())) {
            throw new AuthServiceException(ErrorCode.PASSWORD_INVALID);
        }
        JwtTokenService.TokenPair pair = jwtTokenService.createTokenPair(user.getId(), user.getUsername());
        refreshTokenStore.store(
            user.getId(),
            jwtTokenService.parse(pair.refreshToken()).getId(),
            pair.refreshToken(),
            Duration.ofSeconds(authProperties.getRefreshTokenExpireSeconds())
        );
        return new TokenResponse(pair.accessToken(), pair.refreshToken(), pair.accessExpireAt(), pair.refreshExpireAt());
    }

    public TokenResponse refresh(String refreshToken) {
        Claims claims = parseRefreshClaims(refreshToken);
        Long userId = Long.parseLong(claims.getSubject());
        String jti = claims.getId();
        String cached = refreshTokenStore.get(userId, jti);
        if (cached == null || !cached.equals(refreshToken)) {
            throw new AuthServiceException(ErrorCode.TOKEN_INVALID);
        }
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AuthServiceException(ErrorCode.USER_NOT_FOUND));
        JwtTokenService.TokenPair pair = jwtTokenService.createTokenPair(user.getId(), user.getUsername());
        refreshTokenStore.store(
            user.getId(),
            jwtTokenService.parse(pair.refreshToken()).getId(),
            pair.refreshToken(),
            Duration.ofSeconds(authProperties.getRefreshTokenExpireSeconds())
        );
        refreshTokenStore.delete(userId, jti);
        return new TokenResponse(pair.accessToken(), pair.refreshToken(), pair.accessExpireAt(), pair.refreshExpireAt());
    }

    public void logout(String accessToken, String refreshToken) {
        Claims refreshClaims = parseRefreshClaims(refreshToken);
        Long userId = Long.parseLong(refreshClaims.getSubject());
        String refreshJti = refreshClaims.getId();
        String cached = refreshTokenStore.get(userId, refreshJti);
        if (cached == null || !cached.equals(refreshToken)) {
            throw new AuthServiceException(ErrorCode.TOKEN_INVALID);
        }
        refreshTokenStore.delete(userId, refreshJti);

        Claims accessClaims = parseAccessClaims(accessToken);
        long now = Instant.now().getEpochSecond();
        long exp = accessClaims.getExpiration().toInstant().getEpochSecond();
        long ttlSeconds = Math.max(exp - now, 1);
        accessTokenBlacklistStore.blacklist(accessClaims.getId(), Duration.ofSeconds(ttlSeconds));
    }

    private Claims parseRefreshClaims(String refreshToken) {
        try {
            Claims claims = jwtTokenService.parse(refreshToken);
            if (!"refresh".equals(claims.get("type"))) {
                throw new AuthServiceException(ErrorCode.TOKEN_INVALID);
            }
            return claims;
        } catch (ExpiredJwtException ex) {
            throw new AuthServiceException(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException ex) {
            throw new AuthServiceException(ErrorCode.TOKEN_INVALID);
        }
    }

    private Claims parseAccessClaims(String accessToken) {
        try {
            Claims claims = jwtTokenService.parse(accessToken);
            if (!"access".equals(claims.get("type"))) {
                throw new AuthServiceException(ErrorCode.TOKEN_INVALID);
            }
            return claims;
        } catch (ExpiredJwtException ex) {
            throw new AuthServiceException(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException ex) {
            throw new AuthServiceException(ErrorCode.TOKEN_INVALID);
        }
    }

    public static class AuthServiceException extends RuntimeException {
        private final ErrorCode errorCode;

        public AuthServiceException(ErrorCode errorCode) {
            super(errorCode.message());
            this.errorCode = errorCode;
        }

        public ErrorCode getErrorCode() {
            return errorCode;
        }
    }
}
