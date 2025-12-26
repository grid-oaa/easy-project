package com.easy.permission.service;

import com.easy.permission.config.PermissionProperties;
import com.easy.permission.mapper.PermissionMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class PermissionService {
    private final PermissionMapper permissionMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final PermissionProperties permissionProperties;

    public PermissionService(PermissionMapper permissionMapper,
                             RedisTemplate<String, String> redisTemplate,
                             PermissionProperties permissionProperties) {
        this.permissionMapper = permissionMapper;
        this.redisTemplate = redisTemplate;
        this.permissionProperties = permissionProperties;
    }

    public boolean checkPermission(Long userId, String service, String method, String path) {
        String key = cacheKey(userId, service, method, path);
        String cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return "1".equals(cached);
        }
        Integer count = permissionMapper.countPermission(userId, service, method, path);
        boolean allowed = count != null && count > 0;
        redisTemplate.opsForValue().set(key, allowed ? "1" : "0",
            Duration.ofSeconds(permissionProperties.getCacheTtlSeconds()));
        return allowed;
    }

    private String cacheKey(Long userId, String service, String method, String path) {
        return "perm:" + userId + ":" + service + ":" + method + ":" + path;
    }
}
