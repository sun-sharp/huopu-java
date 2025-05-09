package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

public class TombstoneMessage extends Model<TombstoneMessage> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer tombstoneId;

    private Integer userId;
    private String message;
    private Date CreateTime;

    public Integer getId() {
        return id;
    }

    public TombstoneMessage setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public TombstoneMessage setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public TombstoneMessage setMessage(String message) {
        this.message = message;
        return this;
    }


    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date CreateTime) {
        this.CreateTime = CreateTime;
    }

    public Integer getTombstoneId() {
        return tombstoneId;
    }
    public void setTombstoneId(Integer tombstoneId) {
        this.tombstoneId = tombstoneId;
    }
}
