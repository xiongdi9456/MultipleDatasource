package com.sheldon.multipledatasource.controller;

import com.sheldon.multipledatasource.entity.User;
import com.sheldon.multipledatasource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Sheldon
 * @Date: 2018/09/23 23:08
 */

@RestController
@RequestMapping("user/")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="query/{userId}",method = RequestMethod.GET)
    public User findUserById(@PathVariable("userId") int userId){
        User user = userService.queryById(userId);
        return user;
    }

}
