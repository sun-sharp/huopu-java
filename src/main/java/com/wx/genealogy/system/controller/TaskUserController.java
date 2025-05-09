package com.wx.genealogy.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.entity.Task;
import com.wx.genealogy.system.entity.TaskUser;
import com.wx.genealogy.system.service.FamilyUserService;
import com.wx.genealogy.system.service.TaskService;
import com.wx.genealogy.system.service.TaskUserService;
import com.wx.genealogy.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (TaskUser)表控制层
 *
 * @author makejava
 * @since 2022-07-12 09:27:14
 */
@RestController
@RequestMapping("/taskUser")
@Api(tags = "用户任务控制器")
public class TaskUserController {
    /**
     * 用户任务服务
     */
    @Resource
    private TaskUserService taskUserService;

    /**
     * 任务服务
     */
    @Resource
    private TaskService taskService;

    /**
     * 用户服务
     */
    @Autowired
    private UserService userService;

    /**
     * 用户家族服务
     */
    @Resource
    private FamilyUserService familyUserService;

    /**
     * 分页查询
     *
     * @param taskUser 筛选条件
     * @return 查询结果
     */
    @GetMapping("/queryByPage")
    public JsonResult queryByPage(TaskUser taskUser, Integer page, Integer limit) {
        return this.taskUserService.queryByPage(taskUser, page, limit);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/queryById")
    public JsonResult queryById(Integer id) {
        return ResponseUtil.ok("", this.taskUserService.selectTaskUserById(id));
    }

    /**
     * 领取任务
     *
     * @param taskUser 实体
     * @return 新增结果
     */
    @PostMapping("/add")
    @ApiOperation(value = "领取任务")
    public JsonResult add(@RequestBody TaskUser taskUser) throws Exception {
        if (ObjectUtil.isNull(taskUser.getTaskId())) {
            throw new MissingServletRequestParameterException("task_id", "不能为空");
        }

        if (ObjectUtil.isNull(taskUser.getUserId())) {
            throw new MissingServletRequestParameterException("user_id", "不能为空");
        }
        //创建时间
        taskUser.setCreateTime(new Date());
        //是否完成（0执行中/1待审核/2完成3拒绝  默认0）
        taskUser.setStatus(0);

        //判断是否超过领取时间了
        Task task = taskService.getById(taskUser.getTaskId());
        if (taskUser.getCreateTime().compareTo(task.getDeadlineTime()) > 0) {
            return ResponseUtil.fail("不能领取该任务,已超过领取截至日期");
        }

        //判断是否已领取过了
        QueryWrapper<TaskUser> wrapper = new QueryWrapper<TaskUser>();
        wrapper.eq("user_id", taskUser.getUserId());
        wrapper.eq("task_id", taskUser.getTaskId());
        TaskUser exist = taskUserService.getOne(wrapper);
        if (exist != null) {
            return ResponseUtil.fail("当前任务已领取过了,不可重复领取");
        }

        //判断是否实名认证
/*        User user = userService.getById(taskUser.getUserId());
        if (user == null) {
            // 406是未实名认证  407是没有加入家族
            return ResponseUtil.fail(406, "未登录");
        }
        //是否实名认证（0待审核1通过 默认0）
        if (user.getIsCertification() == 0) {
            return ResponseUtil.fail(406, "未实名认证,不可领取");
        }*/

        //判断有没有加入家族
        FamilyUser familyUser = new FamilyUser();
        //用户id
        familyUser.setUserId(taskUser.getUserId());
        //申请状态：1申请中2已通过3已拒绝
        familyUser.setStatus(2);
        List<FamilyUser> familyUsers = familyUserService.selectFamilyByUserId(familyUser);
        if (familyUsers == null) {
            return ResponseUtil.fail(407, "您未加入任何家族不可领取");
        }

        return ResponseUtil.ok(this.taskUserService.insert(taskUser));
    }

    /**
     * 编辑数据
     *
     * @param taskUser 实体
     * @return 编辑结果
     */
    @PostMapping("/edit")
    @ApiOperation(value = "后台管理员审核用户任务")
    public JsonResult edit(@RequestBody TaskUser taskUser) {
        return ResponseUtil.ok(this.taskUserService.update(taskUser));
    }


    /**
     * 用户任务完成上传截图正面
     *
     * @param taskUser 实体
     * @return 编辑结果
     */
    @PostMapping("/updateTaskUser")
    @ApiOperation(value = "用户任务完成上传截图正面")
    public JsonResult updateTaskUser(@RequestBody TaskUser taskUser) {
        //是否完成（0执行中/1待审核/2完成3拒绝 默认0
        taskUser.setStatus(1);
        int i = 0;
        if (this.taskUserService.updateById(taskUser)) {
            i = 1;
        }
        return ResponseUtil.ok(i);
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/deleteById")
    public JsonResult deleteById(Integer id) {
        return ResponseUtil.ok(this.taskUserService.deleteById(id));
    }
}

