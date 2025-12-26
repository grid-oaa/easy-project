package com.easy.common;

/**
 * 基础错误码约定，后续可扩展为模块化错误码体系。
 */
public enum ErrorCode {
    OK(0, "成功"),
    INVALID_PARAM(400, "参数错误"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限"),
    USER_EXISTS(1001, "用户已存在"),
    USER_NOT_FOUND(1002, "用户不存在"),
    PASSWORD_INVALID(1003, "密码错误"),
    TOKEN_INVALID(1004, "令牌无效"),
    TOKEN_EXPIRED(1005, "令牌已过期"),
    INTERNAL_ERROR(500, "服务器内部错误");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
