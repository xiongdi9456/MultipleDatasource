package com.sheldon.multipledatasource.service.impl;

import com.sheldon.multipledatasource.dao.cluster.UserMapper;
import com.sheldon.multipledatasource.dao.master.CityMapper;
import com.sheldon.multipledatasource.entity.City;
import com.sheldon.multipledatasource.entity.User;
import com.sheldon.multipledatasource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Sheldon
 * @Date: 2018/09/23 23:03
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CityMapper cityMapper;


    @Override
    public User queryById(int userId) {
        User user = userMapper.queryByUserId(userId);

        City city = cityMapper.queryById(1);
        if (city != null && user != null){
            user.setCity(city);
        }

        return user;
    }
}
