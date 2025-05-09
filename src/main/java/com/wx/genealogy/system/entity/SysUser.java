package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author ${author}
 * @since 2021-05-29
 */
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像链接
     */
    private String headImgUrl;

    /**
     * 姓名
     */
    private String nickName;

    /**
     * 创建人ID
     */
    private Integer createdById;

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

    public SysUser setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public SysUser setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SysUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public SysUser setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public SysUser setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public SysUser setCreatedById(Integer createdById) {
        this.createdById = createdById;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public SysUser setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getUpdateById() {
        return updateById;
    }

    public SysUser setUpdateById(Integer updateById) {
        this.updateById = updateById;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public SysUser setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public SysUser setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public SysUser setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "SysUser{" +
        "id=" + id +
        ", userName=" + userName +
        ", password=" + password +
        ", headImgUrl=" + headImgUrl +
        ", nickName=" + nickName +
        ", createdById=" + createdById +
        ", createTime=" + createTime +
        ", updateById=" + updateById +
        ", updateTime=" + updateTime +
        ", status=" + status +
        ", delFlag=" + delFlag +
        "}";
    }
}
