package com.easy.permission.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 角色菜单绑定请求。
 */
public class RoleMenuBindRequest {
    @NotNull(message = "角色ID不能为空")
    private Long roleId;
    @NotNull(message = "菜单列表不能为空")
    private List<Long> menuIds;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }
}