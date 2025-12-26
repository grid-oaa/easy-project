package com.easy.permission.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 菜单ID请求。
 */
public class MenuIdRequest {
    @NotNull(message = "菜单ID不能为空")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}