package com.easy.permission.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.easy.permission.dto.MenuCreateRequest;
import com.easy.permission.dto.MenuUpdateRequest;
import com.easy.permission.mapper.MenuMapper;
import com.easy.permission.mapper.RoleMenuMapper;
import com.easy.permission.model.Menu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuService {
    private final MenuMapper menuMapper;
    private final RoleMenuMapper roleMenuMapper;

    public MenuService(MenuMapper menuMapper, RoleMenuMapper roleMenuMapper) {
        this.menuMapper = menuMapper;
        this.roleMenuMapper = roleMenuMapper;
    }

    public Long create(MenuCreateRequest request) {
        Menu menu = new Menu();
        menu.setName(request.getName());
        menu.setParentId(defaultParentId(request.getParentId()));
        menu.setPath(request.getPath());
        menu.setComponent(request.getComponent());
        menu.setType(request.getType());
        menu.setIcon(request.getIcon());
        menu.setSortOrder(defaultSort(request.getSortOrder()));
        menuMapper.insert(menu);
        return menu.getId();
    }

    public void update(Long id, MenuUpdateRequest request) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(request.getName());
        menu.setParentId(defaultParentId(request.getParentId()));
        menu.setPath(request.getPath());
        menu.setComponent(request.getComponent());
        menu.setType(request.getType());
        menu.setIcon(request.getIcon());
        menu.setSortOrder(defaultSort(request.getSortOrder()));
        menuMapper.updateById(menu);
    }

    @Transactional
    public void delete(Long id) {
        menuMapper.deleteById(id);
        roleMenuMapper.deleteByMenuId(id);
    }

    public List<Menu> list() {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("sort_order").orderByAsc("id");
        return menuMapper.selectList(wrapper);
    }

    public List<Menu> listTree() {
        List<Menu> menus = list();
        // 根据 parentId 构建菜单树，便于前端直接渲染层级结构
        Map<Long, Menu> menuMap = new LinkedHashMap<>();
        for (Menu menu : menus) {
            menu.setChildren(new ArrayList<>());
            menuMap.put(menu.getId(), menu);
        }
        List<Menu> roots = new ArrayList<>();
        for (Menu menu : menus) {
            Long parentId = menu.getParentId();
            if (parentId == null || parentId == 0) {
                roots.add(menu);
                continue;
            }
            Menu parent = menuMap.get(parentId);
            if (parent == null) {
                roots.add(menu);
            } else {
                parent.getChildren().add(menu);
            }
        }
        return roots;
    }

    @Transactional
    public void bindRoleMenus(Long roleId, List<Long> menuIds) {
        roleMenuMapper.deleteByRoleId(roleId);
        if (menuIds == null || menuIds.isEmpty()) {
            return;
        }
        for (Long menuId : menuIds) {
            roleMenuMapper.bindRoleMenu(roleId, menuId);
        }
    }

    public List<Long> listRoleMenuIds(Long roleId) {
        return roleMenuMapper.listMenuIdsByRoleId(roleId);
    }

    private Long defaultParentId(Long parentId) {
        return parentId == null ? 0L : parentId;
    }

    private Integer defaultSort(Integer sortOrder) {
        return sortOrder == null ? 0 : sortOrder;
    }
}