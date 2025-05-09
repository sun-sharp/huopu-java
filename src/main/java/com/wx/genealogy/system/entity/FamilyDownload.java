package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

public class FamilyDownload {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String uid;
    private Integer familyId;
    private Integer userId;
    private Date time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "FamilyDownload{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", familyId=" + familyId +
                ", userId=" + userId +
                ", time=" + time +
                '}';
    }
}
