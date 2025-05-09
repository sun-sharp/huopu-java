package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

public class TombstoneUsergift extends Model<TombstoneUsergift> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer giftId;
    private Integer userId;
    private Integer tombstoneId;
    private Date CreateTime;

    public Integer getId() {
        return id;
    }

    public TombstoneUsergift setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getGiftid() {
        return giftId;
    }

    public TombstoneUsergift setGiftid(Integer giftId) {
        this.giftId = giftId;
        return this;
    }
    public Integer getUserid() {
        return userId;
    }

    public TombstoneUsergift setUserid(Integer userId) {
        this.userId = userId;
        return this;
    }
    public Integer getTombstoneId() {
        return tombstoneId;
    }

    public TombstoneUsergift setTombstone(Integer tombstoneId) {
        this.tombstoneId = tombstoneId;
        return this;
    }

    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date CreateTime) {
        this.CreateTime = CreateTime;
    }
}
