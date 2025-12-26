package com.easy.permission.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 批量权限创建请求。
 */
public class PermissionBatchCreateRequest {
    @Valid
    @NotEmpty(message = "权限列表不能为空")
    private List<PermissionCreateRequest> permissions;

    public List<PermissionCreateRequest> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionCreateRequest> permissions) {
        this.permissions = permissions;
    }
}
