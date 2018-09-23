package com.sheldon.multipledatasource.dao.master;

import com.sheldon.multipledatasource.entity.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:
 * @Author: Sheldon
 * @Date: 2018/09/23 22:59
 */

@Mapper
public interface CityMapper {

    /**
     * 根据城市名称，查询城市信息
     *
     * @param cityId 城市名
     */
    public City queryById(@Param("cityId") int cityId);
}
