package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author ${author}
 * @since 2021-05-29
 */
public class SysRoleUser extends Model<SysRoleUser> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 创建者ID
     */
    private Integer createById;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    public Integer getId() {
        return id;
    }

    public SysRoleUser setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public SysRoleUser setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public SysRoleUser setRoleId(Integer roleId) {
        this.roleId = roleId;
        return this;
    }

    public Integer getCreateById() {
        return createById;
    }

    public SysRoleUser setCreateById(Integer createById) {
        this.createById = createById;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public SysRoleUser setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysRoleUser{" +
        "id=" + id +
        ", userId=" + userId +
        ", roleId=" + roleId +
        ", createById=" + createById +
        ", createTime=" + createTime +
        "}";
    }
}
