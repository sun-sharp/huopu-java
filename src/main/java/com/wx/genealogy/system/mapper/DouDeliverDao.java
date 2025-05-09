package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.DouDeliver;
import org.apache.ibatis.annotations.Mapper;

/**
 * 斗投递Mapper接口
 *
 * @author leo
 * @date 2024-07-05
 */
@Mapper
public interface DouDeliverDao extends BaseMapper<DouDeliver> {

    Integer getAmountByUserId(Integer userId);
}
