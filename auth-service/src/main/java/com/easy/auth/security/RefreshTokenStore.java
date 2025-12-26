package com.easy.auth.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 刷新令牌存储，便于后续替换为数据库或其他方案。
 */
@Component
public class RefreshTokenStore {
    private final RedisTemplate<String, String> redisTemplate;

    public RefreshTokenStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void store(Long userId, String jti, String token, Duration ttl) {
        String key = buildKey(userId, jti);
        redisTemplate.opsForValue().set(key, token, ttl);
    }

    public String get(Long userId, String jti) {
        return redisTemplate.opsForValue().get(buildKey(userId, jti));
    }

    public void delete(Long userId, String jti) {
        redisTemplate.delete(buildKey(userId, jti));
    }

    private String buildKey(Long userId, String jti) {
        return "auth:refresh:" + userId + ":" + jti;
    }
}
