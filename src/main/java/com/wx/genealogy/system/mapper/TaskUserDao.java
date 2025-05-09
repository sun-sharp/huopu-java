package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wx.genealogy.system.entity.TaskUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * (TaskUser)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-12 09:27:14
 */
@Mapper
public interface TaskUserDao extends BaseMapper<TaskUser> {

    IPage<TaskUser> queryAllByLimit(IPage<TaskUser> pageData,@Param(Constants.WRAPPER) QueryWrapper<TaskUser> taskQueryWrapper);


    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    TaskUser selectTaskUserById(Integer id);
}

