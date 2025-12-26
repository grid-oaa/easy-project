package com.easy.permission.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMenuMapper {

    @Delete("DELETE FROM role_menu WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") Long roleId);

    @Delete("DELETE FROM role_menu WHERE menu_id = #{menuId}")
    void deleteByMenuId(@Param("menuId") Long menuId);

    @Insert("INSERT IGNORE INTO role_menu (role_id, menu_id) VALUES (#{roleId}, #{menuId})")
    void bindRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    @Select("SELECT menu_id FROM role_menu WHERE role_id = #{roleId}")
    List<Long> listMenuIdsByRoleId(@Param("roleId") Long roleId);
}