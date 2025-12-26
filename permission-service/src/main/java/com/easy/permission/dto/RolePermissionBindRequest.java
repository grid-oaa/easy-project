package com.easy.permission.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 角色与权限绑定请求。
 */
public class RolePermissionBindRequest {
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @NotNull(message = "权限ID不能为空")
    private Long permissionId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }
}
