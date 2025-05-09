package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.Task;
import com.wx.genealogy.system.entity.TaskUser;
import com.wx.genealogy.system.mapper.TaskDao;
import com.wx.genealogy.system.service.TaskService;
import com.wx.genealogy.system.service.TaskUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 任务(Task)表服务实现类
 *
 * @author makejava
 * @since 2022-07-11 17:38:20
 */
@Service("taskService")
public class TaskServiceImpl extends ServiceImpl<TaskDao, Task> implements TaskService {
    @Resource
    private TaskDao taskDao;

    @Resource
    private TaskUserService taskUserService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Task queryById(Integer id) {
        return this.taskDao.selectById(id);
    }

    /**
     * 分页查询
     *
     * @param task 筛选条件
     * @return 查询结果
     */
    @Override
    public JsonResult queryByPage(Task task, Integer page, Integer limit) {
        IPage<Task> pageData = new Page<>(page, limit);
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<Task>();

        if (StringUtils.isNotBlank(task.getTitile())) {
            taskQueryWrapper.like("a.titile", task.getTitile());
        }

        if (ObjectUtil.isNotNull(task.getStatus())) {
            taskQueryWrapper.eq("a.status", task.getStatus());
        }

        pageData = taskDao.queryAllTask(pageData, taskQueryWrapper);
        List<Task> records = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("taskList", records);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        return ResponseUtil.ok("获取成功", map);
    }

    /**
     * 新增数据
     *
     * @param task 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(Task task) {
        return this.taskDao.insert(task);
    }

    /**
     * 修改数据
     *
     * @param task 实例对象
     * @return 实例对象
     */
    @Override
    public int update(Task task) {

        return this.taskDao.updateById(task);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    @Transactional
    public int deleteById(Integer id) {
        int i = this.taskDao.deleteById(id);
        QueryWrapper<TaskUser> wrapper = new QueryWrapper<TaskUser>();
        wrapper.eq("task_id",id);

        //删除领取了任务的人
        taskUserService.remove(wrapper);
        return i;
    }

    /**
     * 查询当前用户没有领取的任务
     *
     * @param userId
     * @return
     */
    @Override
    public JsonResult selectNotReceiveTaskByUserId(Integer page, Integer limit, Integer userId) {

        if (userId == null) {
            return ResponseUtil.fail("当前用户id不能为空");
        }

        //查询当前登录人领取了哪些任务
        IPage<Task> pageData = new Page<>(page, limit);
        QueryWrapper<Task> taskQueryWrapper = new QueryWrapper<Task>();

        taskQueryWrapper.notInSql("id", "select task_id from task_user where user_id=" + userId);

        pageData = taskDao.selectPage(pageData, taskQueryWrapper);
        List<Task> records = pageData.getRecords();
        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("taskList", records);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        return ResponseUtil.ok("获取成功", map);

    }
}
