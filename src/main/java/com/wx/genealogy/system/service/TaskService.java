package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.Task;

/**
 * 任务(Task)表服务接口
 *
 * @author makejava
 * @since 2022-07-11 17:38:20
 */
public interface TaskService extends IService<Task> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Task queryById(Integer id);

    /**
     * 分页查询
     *
     * @param task 筛选条件
     * @return 查询结果
     */
    JsonResult queryByPage(Task task,Integer page, Integer limit);

    /**
     * 新增数据
     *
     * @param task 实例对象
     * @return 实例对象
     */
    int insert(Task task);

    /**
     * 修改数据
     *
     * @param task 实例对象
     * @return 实例对象
     */
    int update(Task task);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    int deleteById(Integer id);

    /**
     * 查询当前用户没有领取的任务
     * @param userId
     * @return
     */
    JsonResult selectNotReceiveTaskByUserId(Integer page, Integer limit, Integer userId);

}
