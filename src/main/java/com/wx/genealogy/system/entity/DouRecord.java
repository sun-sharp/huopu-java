package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

/**
 * <p>
 * 斗收入明细
 * </p>
 *
 * @author ${author}
 * @since 2022-07-12
 */
public class DouRecord extends Model<DouRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 领取的斗数量
     */
    private Integer douAmount;

    /**
     * 说明
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private User user;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDouAmount() {
        return douAmount;
    }

    public void setDouAmount(Integer douAmount) {
        this.douAmount = douAmount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "DouRecord{" +
        "id=" + id +
        ", userId=" + userId +
        ", douAmount=" + douAmount +
        ", content=" + content +
        ", createTime=" + createTime +
        "}";
    }
}
