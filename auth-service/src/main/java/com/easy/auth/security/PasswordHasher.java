package com.easy.auth.security;

/**
 * 密码哈希接口，便于后续替换加密方案。
 */
public interface PasswordHasher {
    String hash(String rawPassword);

    boolean matches(String rawPassword, String hashedPassword);
}
