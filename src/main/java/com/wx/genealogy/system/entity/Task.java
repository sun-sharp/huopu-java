package com.wx.genealogy.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

/**
 * 任务(Task)实体类
 *
 * @author makejava
 * @since 2022-07-11 17:38:07
 */
public class Task extends Model<Task> {
    private static final long serialVersionUID = -33540746465615329L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 标题
     */
    private String titile;
    /**
     * 任务规则
     */
    private String content;
    /**
     * 任务描述
     */
    @TableField("description")
    private String description;

    /**
     * 图片多个逗号隔开
     */
    private String imgUrls;
    /**
     * 奖励米
     */
    private Integer rewardMi;
    /**
     * 奖励斗
     */
    private Integer rewardDou;
    /**
     * 发布人
     */
    private Integer userId;
    /**
     * 领取人数
     */
    private Integer getNumber;
    /**
     * 是否可领取（0否1是，默认1）
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 领取截止时间
     */
    @JSONField(format = "YYYY-MM-DD HH:mm:ss")
    private Date deadlineTime;
    /**
     * 创建人
     */
    @TableField(exist=false)
    private User user;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGetNumber() {
        return getNumber;
    }

    public void setGetNumber(Integer getNumber) {
        this.getNumber = getNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(Date deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", titile='" + titile + '\'' +
                ", content='" + content + '\'' +
                ", imgUrls='" + imgUrls + '\'' +
                ", rewardMi=" + rewardMi +
                ", rewardDou=" + rewardDou +
                ", userId=" + userId +
                ", getNumber=" + getNumber +
                ", status=" + status +
                ", createTime=" + createTime +
                ", deadlineTime=" + deadlineTime +
                '}';
    }
}

