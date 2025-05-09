package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 米收支明细
 * </p>
 *
 * @author ${author}
 * @since 2021-10-20
 */
public class RiceRecord extends Model<RiceRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 米
     */
    private Integer rice;

    /**
     * 明细内容
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

    public RiceRecord setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public RiceRecord setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getRice() {
        return rice;
    }

    public RiceRecord setRice(Integer rice) {
        this.rice = rice;
        return this;
    }

    public String getContent() {
        return content;
    }

    public RiceRecord setContent(String content) {
        this.content = content;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public RiceRecord setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "RiceRecord{" +
        "id=" + id +
        ", userId=" + userId +
        ", rice=" + rice +
        ", content=" + content +
        ", createTime=" + createTime +
        "}";
    }
}
