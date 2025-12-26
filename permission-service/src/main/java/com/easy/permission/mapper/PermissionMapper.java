package com.easy.permission.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easy.permission.model.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("""
        SELECT COUNT(1)
        FROM role_permission rp
        JOIN permissions p ON rp.permission_id = p.id
        JOIN user_role ur ON rp.role_id = ur.role_id
        WHERE ur.user_id = #{userId}
          AND p.service = #{service}
          AND p.http_method = #{method}
          AND p.path = #{path}
        """)
    Integer countPermission(@Param("userId") Long userId,
                             @Param("service") String service,
                             @Param("method") String method,
                             @Param("path") String path);
}
