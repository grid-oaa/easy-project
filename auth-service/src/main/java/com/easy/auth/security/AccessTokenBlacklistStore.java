package com.easy.auth.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 访问令牌黑名单存储，用于登录态失效控制。
 */
@Component
public class AccessTokenBlacklistStore {
    private final RedisTemplate<String, String> redisTemplate;

    public AccessTokenBlacklistStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklist(String jti, Duration ttl) {
        redisTemplate.opsForValue().set(buildKey(jti), "1", ttl);
    }

    private String buildKey(String jti) {
        return "auth:access:blacklist:" + jti;
    }
}
