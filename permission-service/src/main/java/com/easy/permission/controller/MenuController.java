package com.easy.permission.controller;

import com.easy.common.ApiResponse;
import com.easy.permission.dto.MenuCreateRequest;
import com.easy.permission.dto.MenuIdRequest;
import com.easy.permission.dto.MenuUpdateRequest;
import com.easy.permission.dto.RoleMenuBindRequest;
import com.easy.permission.model.Menu;
import com.easy.permission.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单管理接口。
 */
@RestController
@RequestMapping("/api/menus")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody MenuCreateRequest request) {
        return ApiResponse.success(menuService.create(request));
    }

    @PostMapping("/update")
    public ApiResponse<Void> update(@Valid @RequestBody MenuUpdateRequest request) {
        menuService.update(request.getId(), request);
        return ApiResponse.success(null);
    }

    @PostMapping("/delete")
    public ApiResponse<Void> delete(@Valid @RequestBody MenuIdRequest request) {
        menuService.delete(request.getId());
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<List<Menu>> list() {
        return ApiResponse.success(menuService.list());
    }

    @GetMapping("/tree")
    public ApiResponse<List<Menu>> tree() {
        return ApiResponse.success(menuService.listTree());
    }

    @PostMapping("/roles/bind")
    public ApiResponse<Void> bindRoleMenus(@Valid @RequestBody RoleMenuBindRequest request) {
        menuService.bindRoleMenus(request.getRoleId(), request.getMenuIds());
        return ApiResponse.success(null);
    }

    @GetMapping("/roles")
    public ApiResponse<List<Long>> listRoleMenus(@RequestParam("roleId") Long roleId) {
        return ApiResponse.success(menuService.listRoleMenuIds(roleId));
    }
}
