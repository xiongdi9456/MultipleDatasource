package com.sheldon.multipledatasource.dao.cluster;

import com.sheldon.multipledatasource.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:
 * @Author: Sheldon
 * @Date: 2018/09/23 23:01
 */

@Mapper
public interface UserMapper {

    /**
     * 根据用户id获取用户信息
     *
     * @param userId
     * @return
     */
    public User queryByUserId(@Param("userId") int userId);
}
