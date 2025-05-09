package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

public class FamilyClean   extends Model<FamilyClean>{
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    private Integer familyId;
    private Date cleanTime;

    public Integer getId() {
        return id;
    }

    public FamilyClean setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public FamilyClean setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Date getCleanTime() {
        return cleanTime;
    }

    public void setCleanTime(Date cleanTime) {
        this.cleanTime = cleanTime;
    }

    public Integer getFamilyId() {
        return familyId;
    }
    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }
}
