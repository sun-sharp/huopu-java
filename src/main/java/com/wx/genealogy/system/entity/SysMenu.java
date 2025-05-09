package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author ${author}
 * @since 2021-05-29
 */
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单标识
     */
    private String code;

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

    /**
     * 状态(1：正常  2：冻结 ）
     */
    private Integer status;

    /**
     * 删除状态(1：正常  2：删除 ）
     */
    private Integer delFlag;


    public Integer getId() {
        return id;
    }

    public SysMenu setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SysMenu setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public SysMenu setCode(String code) {
        this.code = code;
        return this;
    }

    public Integer getCreateById() {
        return createById;
    }

    public SysMenu setCreateById(Integer createById) {
        this.createById = createById;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public SysMenu setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateById() {
        return updateById;
    }

    public SysMenu setUpdateById(Integer updateById) {
        this.updateById = updateById;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public SysMenu setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public SysMenu setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public SysMenu setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysMenu{" +
        "id=" + id +
        ", name=" + name +
        ", code=" + code +
        ", createById=" + createById +
        ", createTime=" + createTime +
        ", updateById=" + updateById +
        ", updateTime=" + updateTime +
        ", status=" + status +
        ", delFlag=" + delFlag +
        "}";
    }
}
