package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wx.genealogy.system.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 任务(Task)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-11 17:46:26
 */
@Mapper
public interface TaskDao extends BaseMapper<Task> {

    /**
     * 查询所有任务
     * @param pageData
     * @param taskQueryWrapper
     * @return
     */
    IPage<Task> queryAllTask(IPage<Task> pageData, @Param(Constants.WRAPPER)QueryWrapper<Task> taskQueryWrapper);
}

