package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 用户关注家族
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
public class UserFamilyFollow extends Model<UserFamilyFollow> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 家族id
     */
    private Integer familyId;

    /**
     * 状态：1已关注0未关注
     */
    @TableField("is_status")
    private Integer status;

    private Integer messageNumber;


    public Integer getId() {
        return id;
    }

    public UserFamilyFollow setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public UserFamilyFollow setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public UserFamilyFollow setFamilyId(Integer familyId) {
        this.familyId = familyId;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(Integer messageNumber) {
        this.messageNumber = messageNumber;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserFamilyFollow{" +
        "id=" + id +
        ", userId=" + userId +
        ", familyId=" + familyId +
        "}";
    }
}
