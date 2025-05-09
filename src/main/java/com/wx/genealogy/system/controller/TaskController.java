package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.ssjwt.SecurityUtil;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.SysUser;
import com.wx.genealogy.system.entity.Task;
import com.wx.genealogy.system.service.SysUserService;
import com.wx.genealogy.system.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 任务(Task)表控制层
 *
 * @author makejava
 * @since 2022-07-11 18:00:39
 */
@RestController
@RequestMapping("/task")
@Api(tags = "任务相关接口")
public class TaskController {
    /**
     * 服务对象
     */
    @Resource
    private TaskService taskService;

    @Resource
    private SysUserService sysUserService;

    /**
     * 分页查询
     *
     * @param task 筛选条件
     * @return 查询结果
     */
    @GetMapping("/queryByPage")
    @ApiOperation(value = "查询任务列表分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "page", value = "当前页", dataType = "java.lang.Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "limit", value = "查询页数", dataType = "java.lang.Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "status", value = "是否可领取(0否1是 默认传1)", dataType = "java.lang.Integer", paramType = "query", required = true),
    })
    public JsonResult queryByPage(Integer page, Integer limit, Task task) {
        return this.taskService.queryByPage(task, page, limit);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/queryById")
    public JsonResult queryById(Integer id) {
        return ResponseUtil.ok("操作成功", this.taskService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param task 实体
     * @return 新增结果
     */
    @PostMapping("/add")
    public JsonResult add(@RequestBody Task task) throws Exception {
        if (StringUtils.isBlank(task.getTitile())) {
            throw new Exception("标题不能为空");
        }

        if (StringUtils.isBlank(task.getContent())) {
            throw new Exception("任务规则不能为空");
        }

        if (StringUtils.isBlank(task.getDescription())) {
            throw new Exception("任务描述不能为空");
        }

        if (ObjectUtil.isNull(task.getDeadlineTime())) {
            throw new Exception("领取截至时间不能为空");
        }


/*        if (ObjectUtil.isNull(task.getRewardMi())) {
            throw new Exception("米不能为空");
        }

        if (ObjectUtil.isNull(task.getRewardDou())) {
            throw new Exception("斗不能为空");
        }*/

        SysUser sysUser2 = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser2 == null) {
            throw new Exception("账号不存在");
        }
        task.setUserId(sysUser2.getId());

        //领取人数
        task.setGetNumber(0);
        // status 是否可领取（0否1是，默认1）
        task.setStatus(1);
        //创建时间
        task.setCreateTime(new Date());

        if (task.getDeadlineTime().compareTo(task.getCreateTime()) < 1) {
            return ResponseUtil.fail("任务领取时间不能小于当前时间");
        }


        return ResponseUtil.ok(this.taskService.insert(task));
    }

    /**
     * 编辑数据
     *
     * @param task 实体
     * @return 编辑结果
     */
    @PostMapping("/edit")
    public JsonResult edit(@RequestBody Task task) {
        return ResponseUtil.ok(this.taskService.update(task));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping("/deleteById/{id}")
    public JsonResult deleteById(@PathVariable("id") Integer id) {
        return ResponseUtil.ok(this.taskService.deleteById(id));
    }

    /**
     * 查询当前用户没有领取的任务
     *
     * @return
     */
    @GetMapping("/selectNotReceiveTaskByUserId")
    @ApiOperation(value = "查询当前用户没有领取的任务")
    public JsonResult selectNotReceiveTaskByUserId(Integer page, Integer limit, Integer userId) {
        return this.taskService.selectNotReceiveTaskByUserId(page, limit, userId);
    }
}

