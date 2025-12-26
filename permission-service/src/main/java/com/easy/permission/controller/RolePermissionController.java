package com.easy.permission.controller;

import com.easy.common.ApiResponse;
import com.easy.permission.dto.PermissionBatchCreateRequest;
import com.easy.permission.dto.PermissionCreateRequest;
import com.easy.permission.dto.RoleCreateRequest;
import com.easy.permission.dto.RolePermissionBindRequest;
import com.easy.permission.dto.UserRoleBindRequest;
import com.easy.permission.model.Permission;
import com.easy.permission.model.Role;
import com.easy.permission.service.RolePermissionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色与权限管理接口（基础版）。
 */
@RestController
@RequestMapping("/api/permissions")
public class RolePermissionController {
    private final RolePermissionService rolePermissionService;

    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @PostMapping("/roles")
    public ApiResponse<Long> createRole(@Valid @RequestBody RoleCreateRequest request) {
        return ApiResponse.success(rolePermissionService.createRole(request.getName(), request.getCode()));
    }

    @GetMapping("/roles")
    public ApiResponse<List<Role>> listRoles() {
        return ApiResponse.success(rolePermissionService.listRoles());
    }

    @PostMapping("/permissions")
    public ApiResponse<Long> createPermission(@Valid @RequestBody PermissionCreateRequest request) {
        return ApiResponse.success(rolePermissionService.createPermission(
            request.getName(),
            request.getService(),
            request.getMethod(),
            request.getPath()
        ));
    }

    @PostMapping("/batch")
    public ApiResponse<List<Long>> createPermissionsBatch(@Valid @RequestBody PermissionBatchCreateRequest request) {
        List<Permission> permissions = request.getPermissions().stream()
            .map(item -> {
                Permission permission = new Permission();
                permission.setName(item.getName());
                permission.setService(item.getService());
                permission.setHttpMethod(item.getMethod());
                permission.setPath(item.getPath());
                return permission;
            })
            .toList();
        return ApiResponse.success(rolePermissionService.createPermissionsBatch(permissions));
    }

    @GetMapping("/permissions")
    public ApiResponse<List<Permission>> listPermissions() {
        return ApiResponse.success(rolePermissionService.listPermissions());
    }

    @PostMapping("/bind/user-role")
    public ApiResponse<Void> bindUserRole(@Valid @RequestBody UserRoleBindRequest request) {
        rolePermissionService.bindUserRole(request.getUserId(), request.getRoleId());
        return ApiResponse.success(null);
    }

    @PostMapping("/bind/role-permission")
    public ApiResponse<Void> bindRolePermission(@Valid @RequestBody RolePermissionBindRequest request) {
        rolePermissionService.bindRolePermission(request.getRoleId(), request.getPermissionId());
        return ApiResponse.success(null);
    }
}
