package com.easy.permission.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.easy.permission.mapper.PermissionMapper;
import com.easy.permission.mapper.RoleMapper;
import com.easy.permission.model.Permission;
import com.easy.permission.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolePermissionService {
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    public RolePermissionService(RoleMapper roleMapper, PermissionMapper permissionMapper) {
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    public Long createRole(String name, String code) {
        Role role = new Role();
        role.setName(name);
        role.setCode(code);
        roleMapper.insert(role);
        return role.getId();
    }

    public List<Role> listRoles() {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return roleMapper.selectList(wrapper);
    }

    public Long createPermission(String name, String service, String method, String path) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setService(service);
        permission.setHttpMethod(method);
        permission.setPath(path);
        permissionMapper.insert(permission);
        return permission.getId();
    }

    @Transactional
    public List<Long> createPermissionsBatch(List<Permission> permissions) {
        for (Permission permission : permissions) {
            permissionMapper.insert(permission);
        }
        return permissions.stream().map(Permission::getId).toList();
    }

    public List<Permission> listPermissions() {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return permissionMapper.selectList(wrapper);
    }

    public void bindUserRole(Long userId, Long roleId) {
        roleMapper.bindUserRole(userId, roleId);
    }

    public void bindRolePermission(Long roleId, Long permissionId) {
        roleMapper.bindRolePermission(roleId, permissionId);
    }
}
