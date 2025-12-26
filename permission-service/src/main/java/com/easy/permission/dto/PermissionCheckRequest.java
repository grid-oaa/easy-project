package com.easy.permission.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 权限校验请求。
 */
public class PermissionCheckRequest {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "服务名不能为空")
    private String service;

    @NotBlank(message = "HTTP方法不能为空")
    private String method;

    @NotBlank(message = "路径不能为空")
    private String path;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
