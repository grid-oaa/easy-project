package com.easy.auth.controller;

import com.easy.auth.dto.UserView;
import com.easy.auth.repository.UserRepository;
import com.easy.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理接口（基础版）。
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ApiResponse<List<UserView>> listUsers() {
        List<UserView> users = userRepository.findAll().stream()
            .map(user -> new UserView(user.getId(), user.getUsername(), user.getCreatedAt()))
            .toList();
        return ApiResponse.success(users);
    }
}
