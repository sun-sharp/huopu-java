package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author ${author}
 * @since 2021-05-29
 */
public class SysPermission extends Model<SysPermission> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 权限名
     */
    private String name;

    /**
     * 创建人ID
     */
    private Integer createById;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人ID
     */
    private Integer updateById;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


    public Integer getId() {
        return id;
    }

    public SysPermission setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getPermission() {
        return permission;
    }

    public SysPermission setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public String getName() {
        return name;
    }

    public SysPermission setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCreateById() {
        return createById;
    }

    public SysPermission setCreateById(Integer createById) {
        this.createById = createById;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public SysPermission setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateById() {
        return updateById;
    }

    public SysPermission setUpdateById(Integer updateById) {
        this.updateById = updateById;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public SysPermission setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysPermission{" +
        "id=" + id +
        ", permission=" + permission +
        ", name=" + name +
        ", createById=" + createById +
        ", createTime=" + createTime +
        ", updateById=" + updateById +
        ", updateTime=" + updateTime +
        "}";
    }
}
