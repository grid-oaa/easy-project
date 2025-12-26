package com.easy.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 注销请求。
 */
public class LogoutRequest {
    @NotBlank(message = "访问令牌不能为空")
    private String accessToken;

    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
