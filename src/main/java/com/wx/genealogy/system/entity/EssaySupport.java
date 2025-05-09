package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2021-10-15
 */
public class EssaySupport extends Model<EssaySupport> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章id
     */
    private Integer essayId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 点赞状态0取消1确实
     */
    @TableField("is_status")
    private Integer status;

    /**
     * 点赞时间
     */
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public EssaySupport setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getEssayId() {
        return essayId;
    }

    public EssaySupport setEssayId(Integer essayId) {
        this.essayId = essayId;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public EssaySupport setUserId(Integer userId) {
        this.userId = userId;
        return this;
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "EssaySupport{" +
        "id=" + id +
        ", essayId=" + essayId +
        ", userId=" + userId +
        ", createTime=" + createTime +
        "}";
    }
}
