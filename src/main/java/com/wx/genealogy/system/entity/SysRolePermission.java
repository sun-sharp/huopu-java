package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色权限表
 * </p>
 *
 * @author ${author}
 * @since 2021-05-29
 */
public class SysRolePermission extends Model<SysRolePermission> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 权限ID
     */
    private Integer permissionId;

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

    public SysRolePermission setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public SysRolePermission setRoleId(Integer roleId) {
        this.roleId = roleId;
        return this;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public SysRolePermission setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public Integer getCreateById() {
        return createById;
    }

    public SysRolePermission setCreateById(Integer createById) {
        this.createById = createById;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public SysRolePermission setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysRolePermission{" +
        "id=" + id +
        ", roleId=" + roleId +
        ", permissionId=" + permissionId +
        ", createById=" + createById +
        ", createTime=" + createTime +
        "}";
    }
}
