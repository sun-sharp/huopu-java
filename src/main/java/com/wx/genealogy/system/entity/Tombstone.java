package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

public class Tombstone extends Model<Tombstone> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private String userName;

    private Integer familyId;
    private Date CreateTime;

    private String picture;
    private String content;

    public Integer getId() {
        return id;
    }

    public Tombstone setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public Tombstone setUserName(String userName) {
        this.userName = userName;
        return this;
    }
    public String getPicture() {
        return picture;
    }

    public Tombstone setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Tombstone setContent(String content) {
        this.content = content;
        return this;
    }


    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date CreateTime) {
        this.CreateTime = CreateTime;
    }

    public Integer getFamilyId() {
        return familyId;
    }
    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }
}
