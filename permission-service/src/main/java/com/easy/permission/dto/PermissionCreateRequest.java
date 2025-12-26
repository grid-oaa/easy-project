package com.easy.permission.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 权限创建请求。
 */
public class PermissionCreateRequest {
    @NotBlank(message = "权限名称不能为空")
    private String name;

    @NotBlank(message = "服务名不能为空")
    private String service;

    @NotBlank(message = "HTTP方法不能为空")
    private String method;

    @NotBlank(message = "路径不能为空")
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
