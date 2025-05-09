package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

public class TombstoneEssay  extends Model<TombstoneEssay>{
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer tombstone_id;
    private Integer essay_id;


    private Date AddTime;
    private Integer user_id;


    public Integer getId() {
        return id;
    }

    public TombstoneEssay setId(Integer id) {
        this.id = id;
        return this;
    }
    public Integer getUserId() {
        return user_id;
    }

    public TombstoneEssay setUserId(Integer UserId) {
        this.user_id = UserId;
        return this;
    }
    public Integer getEssayId() {
        return essay_id;
    }

    public TombstoneEssay setEssayId(Integer essayId) {
        this.essay_id = essayId;
        return this;
    }


    public Date getAddTime() {
        return AddTime;
    }

    public void setAddTime(Date AddTime) {
        this.AddTime = AddTime;
    }

    public Integer getTombstoneId() {
        return tombstone_id;
    }
    public void setTombstoneId(Integer tombstoneId) {
        this.tombstone_id = tombstoneId;
    }
}
