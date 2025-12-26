package com.easy.auth.dto;

import java.time.LocalDateTime;

/**
 * 用户展示对象。
 */
public class UserView {
    private Long id;
    private String username;
    private LocalDateTime createdAt;

    public UserView(Long id, String username, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
