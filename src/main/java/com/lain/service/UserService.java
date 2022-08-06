package com.lain.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lain.domain.User;
import com.lain.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String CACHE_KEY_USER = "user:";

    private final UserMapper userMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public void addUser(User user) {
        int insert = userMapper.insert(user);
        if (insert > 0) {
            String key = CACHE_KEY_USER + user.getId();
            redisTemplate.opsForValue().set(key, JSON.toJSONString(user));
        }
    }

    public User findUser(Integer id) {
        User user;
        String key = CACHE_KEY_USER + id;
        String userJson = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(userJson)) {
            user = userMapper.selectById(id);
            if (user != null) {
                redisTemplate.opsForValue().set(key, JSON.toJSONString(user));
            }
        } else {
            user = JSON.parseObject(userJson, User.class);
        }
        return user;
    }

}
