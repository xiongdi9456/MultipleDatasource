package com.sheldon.multipledatasource.service;

import com.sheldon.multipledatasource.entity.User;

/**
 * @Description:
 * @Author: Sheldon
 * @Date: 2018/09/23 23:02
 */


public interface UserService {
    /**
     * 根据用户id获取用户信息，包括从库的地址信息
     * @param userId
     * @return
     */
    public User queryById(int userId);
}
