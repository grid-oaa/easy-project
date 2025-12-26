package com.easy.permission.controller;

import com.easy.common.ApiResponse;
import com.easy.permission.dto.PermissionCheckRequest;
import com.easy.permission.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限校验接口。
 */
@RestController
@RequestMapping("/internal/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/check")
    public ApiResponse<Boolean> check(@Valid @RequestBody PermissionCheckRequest request) {
        boolean allowed = permissionService.checkPermission(
            request.getUserId(),
            request.getService(),
            request.getMethod(),
            request.getPath()
        );
        return ApiResponse.success(allowed);
    }
}
