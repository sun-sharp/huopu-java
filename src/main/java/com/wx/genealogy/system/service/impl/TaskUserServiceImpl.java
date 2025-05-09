package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.mapper.TaskUserDao;
import com.wx.genealogy.system.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * (TaskUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-12 09:27:19
 */
@Service("taskUserService")
@Transactional
public class TaskUserServiceImpl extends ServiceImpl<TaskUserDao, TaskUser> implements TaskUserService {
    @Resource
    private TaskUserDao taskUserDao;

    @Resource
    private UserService userService;

    @Resource
    private TaskService taskService;

    @Resource
    private RiceRecordService riceRecordService;

    @Resource
    private DouRecordService douRecordService;

    /**
     * 分页查询
     *
     * @param taskUser 筛选条件
     * @return 查询结果
     */
    @Override
    public JsonResult queryByPage(TaskUser taskUser, Integer page, Integer limit) {
        IPage<TaskUser> pageData = new Page<>(page, limit);

        QueryWrapper<TaskUser> taskQueryWrapper = new QueryWrapper<TaskUser>();

        if (ObjectUtil.isNotNull(taskUser.getUserId())) {
            taskQueryWrapper.eq("a.user_id", taskUser.getUserId());
        }

        if (ObjectUtil.isNotNull(taskUser.getStatus())) {
            taskQueryWrapper.eq("a.status", taskUser.getStatus());
        }

        pageData = taskUserDao.queryAllByLimit(pageData, taskQueryWrapper);
        List<TaskUser> records = pageData.getRecords();
        records.forEach(r -> {
            if (StringUtils.isNotBlank(r.getResultsImgUrl())) {
                r.setImgList(Arrays.asList(r.getResultsImgUrl().split(",")));
            }
        });
        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("taskUserList", records);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        return ResponseUtil.ok("获取成功", map);
    }

    /**
     * 新增数据
     *
     * @param taskUser 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(TaskUser taskUser) {
        int insert = this.taskUserDao.insert(taskUser);

        //领取成功后任务+1
        if (insert > 0) {
            Task byId = taskService.getById(taskUser.getTaskId());
            if (byId != null) {
                byId.setGetNumber(byId.getGetNumber() + 1);
                taskService.update(byId);
            }

        }

        return insert;
    }

    /**
     * 修改数据
     *
     * @param taskUser 实例对象
     * @return 实例对象
     */
    @Override
    public int update(TaskUser taskUser) {
        //是否完成（0执行中/1待审核/2完成3拒绝 默认0
        //等于2通过，需要去把用户的米加上，斗也加上
        if (taskUser.getStatus() == 2) {
            //领取任务的用户
            User user = userService.getById(taskUser.getUserId());
            //领取的任务
            Task task = taskService.getById(taskUser.getTaskId());
            if (user != null && task != null) {
                //米
                user.setRice(user.getRice() + taskUser.getRewardMi());
                //斗
                user.setDou(user.getDou() + taskUser.getRewardDou());
                userService.updateById(user);

                //记录米明细
                RiceRecord record = new RiceRecord();
                //用户id
                record.setUserId(taskUser.getUserId());
                //多少米
                record.setRice(taskUser.getRewardMi());
                record.setContent("任务奖励:" + task.getTitile());
                record.setCreateTime(new Date());
                riceRecordService.save(record);

                //记录斗明细
                DouRecord douRecord = new DouRecord();
                douRecord.setUserId(taskUser.getUserId());
                douRecord.setDouAmount(taskUser.getRewardDou());
                douRecord.setContent("任务奖励:" + task.getTitile());
                douRecord.setCreateTime(new Date());

                douRecordService.save(douRecord);
            }

        }

        return this.taskUserDao.updateById(taskUser);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public int deleteById(Integer id) {
        return this.taskUserDao.deleteById(id);
    }

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    @Override
    public TaskUser selectTaskUserById(Integer id) {
        TaskUser taskUser = taskUserDao.selectTaskUserById(id);
        if (StringUtils.isNotBlank(taskUser.getResultsImgUrl())) {
            taskUser.setImgList(Arrays.asList(taskUser.getResultsImgUrl().split(",")));
        }
        return taskUser;
    }
}
