package com.easy.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关鉴权配置。
 */
@ConfigurationProperties(prefix = "app.gateway.auth")
public class GatewayAuthProperties {
    private String jwtSecret = "easy-auth-secret-change-me-please-please-please";
    private String permissionServiceUrl = "http://permission-service";
    private List<String> whitelistPaths = new ArrayList<>();

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public String getPermissionServiceUrl() {
        return permissionServiceUrl;
    }

    public void setPermissionServiceUrl(String permissionServiceUrl) {
        this.permissionServiceUrl = permissionServiceUrl;
    }

    public List<String> getWhitelistPaths() {
        return whitelistPaths;
    }

    public void setWhitelistPaths(List<String> whitelistPaths) {
        this.whitelistPaths = whitelistPaths;
    }
}
