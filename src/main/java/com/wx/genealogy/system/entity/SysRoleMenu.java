package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色菜单表
 * </p>
 *
 * @author ${author}
 * @since 2021-05-29
 */
public class SysRoleMenu extends Model<SysRoleMenu> {

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
     * 菜单ID
     */
    private Integer menuId;

    /**
     * 创建人ID
     */
    private Integer createById;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    public Integer getId() {
        return id;
    }

    public SysRoleMenu setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public SysRoleMenu setRoleId(Integer roleId) {
        this.roleId = roleId;
        return this;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public SysRoleMenu setMenuId(Integer menuId) {
        this.menuId = menuId;
        return this;
    }

    public Integer getCreateById() {
        return createById;
    }

    public SysRoleMenu setCreateById(Integer createById) {
        this.createById = createById;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public SysRoleMenu setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysRoleMenu{" +
        "id=" + id +
        ", roleId=" + roleId +
        ", menuId=" + menuId +
        ", createById=" + createById +
        ", createTime=" + createTime +
        "}";
    }
}
