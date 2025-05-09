package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wx.genealogy.system.entity.Fire;
import com.wx.genealogy.system.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-09-09
 */

@Mapper

public interface FireDao extends BaseMapper<Fire> {

    int setInc(Fire fire);

    int setDec(Fire fire);
    IPage<Fire> queryAllFire(IPage<Fire> pageData, @Param(Constants.WRAPPER) QueryWrapper<Fire> fireQueryWrapper);
}
