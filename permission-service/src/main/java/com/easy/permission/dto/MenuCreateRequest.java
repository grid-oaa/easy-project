package com.easy.permission.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 菜单创建请求。
 */
public class MenuCreateRequest {
    @NotBlank(message = "菜单名称不能为空")
    private String name;
    private Long parentId;
    private String path;
    private String component;
    @NotBlank(message = "菜单类型不能为空")
    private String type;
    private String icon;
    private Integer sortOrder;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}