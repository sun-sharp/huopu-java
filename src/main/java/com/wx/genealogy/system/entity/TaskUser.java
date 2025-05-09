package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * (TaskUser)实体类
 *
 * @author makejava
 * @since 2022-07-12 09:27:19
 */
@ApiModel(description = "领取的任务")
public class TaskUser extends Model<TaskUser> {
    private static final long serialVersionUID = -96563089266056126L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;
    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id",required = true)
    private Integer taskId;
    /**
     * 领取人id
     */
    @ApiModelProperty(value = "领取人id",required = true)
    private Integer userId;
    /**
     * 是否完成（0执行中/1待审核/2完成3拒绝  默认0）
     */
    @ApiModelProperty("是否完成（0执行中/1待审核/2完成3拒绝  默认0）")
    private Integer status;
    /**
     * 完成截图（多张逗号隔开）
     */
    @ApiModelProperty("完成截图（多张逗号隔开）")
    private String resultsImgUrl;
    /**
     * 拒绝原因
     */
    @ApiModelProperty("拒绝原因")
    private String refuseWhy;
    /**
     * 领取时间
     */
    @ApiModelProperty("领取时间")
    private Date createTime;

    /**
     * 用户对象
     */
    @TableField(exist = false)
    private User user;

    //表字段外的属性

    /**
     * 任务对象
     */
    @TableField(exist = false)
    private Task task;

    @TableField(exist = false)
    private List<String> imgList;

    /**
     * 奖励的米
     */
    @TableField(exist = false)
    private Integer rewardMi;

    /**
     * 奖励的斗
     */
    @TableField(exist = false)
    private Integer rewardDou;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResultsImgUrl() {
        return resultsImgUrl;
    }

    public void setResultsImgUrl(String resultsImgUrl) {
        this.resultsImgUrl = resultsImgUrl;
    }

    public String getRefuseWhy() {
        return refuseWhy;
    }

    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public Integer getRewardMi() {
        return rewardMi;
    }

    public void setRewardMi(Integer rewardMi) {
        this.rewardMi = rewardMi;
    }

    public Integer getRewardDou() {
        return rewardDou;
    }

    public void setRewardDou(Integer rewardDou) {
        this.rewardDou = rewardDou;
    }
}

