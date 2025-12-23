package com.easy.common;

/**
 * 基础错误码约定，后续可扩展为模块化错误码体系。
 */
public enum ErrorCode {
    OK(0, "成功"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限"),
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
