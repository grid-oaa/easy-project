package com.easy.permission.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 权限服务配置。
 */
@ConfigurationProperties(prefix = "app.permission")
public class PermissionProperties {
    private long cacheTtlSeconds = 300;

    public long getCacheTtlSeconds() {
        return cacheTtlSeconds;
    }

    public void setCacheTtlSeconds(long cacheTtlSeconds) {
        this.cacheTtlSeconds = cacheTtlSeconds;
    }
}
