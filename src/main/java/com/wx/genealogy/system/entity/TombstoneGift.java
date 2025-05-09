package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.awt.*;
import java.util.Date;

public class TombstoneGift extends Model<TombstoneGift> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private String name;
    private String message;
    private String picture;

    private Date CreateTime;
    private Integer tombstoneId;



    public Integer getId() {
        return id;
    }

    public TombstoneGift setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getTombstoneId() {
        return tombstoneId;
    }

    public TombstoneGift setTombstoneId(Integer tombstoneId) {
        this.tombstoneId = tombstoneId;
        return this;
    }


    public String getMessage() {
        return message;
    }

    public TombstoneGift setMessage(String message) {
        this.message = message;
        return this;
    }


    public String getPicture() {
        return picture;
    }

    public TombstoneGift setPicture(String picture) {
        this.picture = picture;
        return this;
    }


    public String getname() {
        return name;
    }

    public TombstoneGift setName(String name) {
        this.name = name;
        return this;
    }

    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date CreateTime) {
        this.CreateTime = CreateTime;
    }

}
