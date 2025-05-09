package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author ${author}
 * @since 2021-05-29
 */
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名
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

    public SysRole setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SysRole setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getCreateById() {
        return createById;
    }

    public SysRole setCreateById(Integer createById) {
        this.createById = createById;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public SysRole setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateById() {
        return updateById;
    }

    public SysRole setUpdateById(Integer updateById) {
        this.updateById = updateById;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public SysRole setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysRole{" +
        "id=" + id +
        ", name=" + name +
        ", createById=" + createById +
        ", createTime=" + createTime +
        ", updateById=" + updateById +
        ", updateTime=" + updateTime +
        "}";
    }
}
