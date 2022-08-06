package com.lain.controller;

import cn.hutool.core.util.IdUtil;
import com.lain.domain.User;
import com.lain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping("/add")
    public void addUser() {
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setUsername("lain" + i);
            user.setPassword(IdUtil.randomUUID());
            user.setSex(1);
            userService.addUser(user);
        }
    }

    @RequestMapping("/find/{id}")
    public User findUser(@PathVariable("id") Integer id) {
        return userService.findUser(id);
    }

}
