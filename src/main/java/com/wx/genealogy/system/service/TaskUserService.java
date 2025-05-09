package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.TaskUser;

/**
 * (TaskUser)表服务接口
 *
 * @author makejava
 * @since 2022-07-12 09:27:19
 */
public interface TaskUserService extends IService<TaskUser> {

    /**
     * 分页查询
     *
     * @param taskUser 筛选条件
     * @return 查询结果
     */
    JsonResult queryByPage(TaskUser taskUser,Integer page,Integer limit);

    /**
     * 新增数据
     *
     * @param taskUser 实例对象
     * @return 实例对象
     */
    int insert(TaskUser taskUser);

    /**
     * 修改数据
     *
     * @param taskUser 实例对象
     * @return 实例对象
     */
    int update(TaskUser taskUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    int deleteById(Integer id);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    TaskUser selectTaskUserById(Integer id);
}
