package com.easy.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 认证相关配置，便于后续替换认证策略。
 */
@ConfigurationProperties(prefix = "app.auth")
public class AuthProperties {
    private long accessTokenExpireSeconds = 7200;
    private long refreshTokenExpireSeconds = 604800;
    private String issuer = "easy-auth";
    private String jwtSecret = "easy-auth-secret-change-me-please-please-please";

    public long getAccessTokenExpireSeconds() {
        return accessTokenExpireSeconds;
    }

    public void setAccessTokenExpireSeconds(long accessTokenExpireSeconds) {
        this.accessTokenExpireSeconds = accessTokenExpireSeconds;
    }

    public long getRefreshTokenExpireSeconds() {
        return refreshTokenExpireSeconds;
    }

    public void setRefreshTokenExpireSeconds(long refreshTokenExpireSeconds) {
        this.refreshTokenExpireSeconds = refreshTokenExpireSeconds;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }
}
