package com.easy.auth.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.easy.auth.mapper.UserMapper;
import com.easy.auth.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final UserMapper userMapper;

    public UserRepository(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Optional<User> findByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return Optional.ofNullable(userMapper.selectOne(wrapper));
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMapper.selectById(id));
    }

    public Long insert(String username, String passwordHash) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);
        user.setCreatedAt(LocalDateTime.now());
        userMapper.insert(user);
        return user.getId();
    }

    public List<User> findAll() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        return userMapper.selectList(wrapper);
    }
}
