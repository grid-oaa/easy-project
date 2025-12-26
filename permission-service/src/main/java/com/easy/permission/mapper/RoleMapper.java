package com.easy.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easy.permission.model.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Insert("INSERT IGNORE INTO user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    void bindUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Insert("INSERT IGNORE INTO role_permission (role_id, permission_id) VALUES (#{roleId}, #{permissionId})")
    void bindRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
}
