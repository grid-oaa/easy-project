package com.easy.auth.dto;

/**
 * 令牌响应。
 */
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private long accessExpireAt;
    private long refreshExpireAt;

    public TokenResponse(String accessToken, String refreshToken, long accessExpireAt, long refreshExpireAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessExpireAt = accessExpireAt;
        this.refreshExpireAt = refreshExpireAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getAccessExpireAt() {
        return accessExpireAt;
    }

    public long getRefreshExpireAt() {
        return refreshExpireAt;
    }
}
