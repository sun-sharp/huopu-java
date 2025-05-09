package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 家族用户关联
 * </p>
 *
 * @author ${author}
 * @since 2021-10-09
 */
public class FamilyUser extends Model<FamilyUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 家族id
     */
    private Integer familyId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 身份等级：1是创建者2是管理员3是会员
     */
    private Integer level;

    private Integer generation;

    /**
     * 家谱名
     */
    private String genealogyName;

    /**
     * 和家族关系1直亲2婚姻3表亲
     */
    private Integer relation;

    /**
     * 性别1男2女
     */
    private Integer sex;

    /**
     * 申请状态：1申请中2已通过3已拒绝
     */
    private Integer status;

    private Integer joins;//入谱状态
    /**
     * 申请说明
     */
    private String introduce;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 日期
     */
    private Date createTime;

    /**
     * 新消息数
     */
    private Integer messageNumber;

    /**
     * 成为管理员日期
     */
    private Date manageTime;
    private Date updateTime;

    private Integer updatestatus;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public FamilyUser setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUpdatestatus() {
        return updatestatus;
    }

    public FamilyUser setUpdatestatus(Integer updatestatus) {
        this.updatestatus = updatestatus;
        return this;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public FamilyUser setFamilyId(Integer familyId) {
        this.familyId = familyId;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public FamilyUser setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public FamilyUser setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public Integer getGeneration() {
        return generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
    }

    public String getGenealogyName() {
        return genealogyName;
    }

    public FamilyUser setGenealogyName(String genealogyName) {
        this.genealogyName = genealogyName;
        return this;
    }

    public Integer getRelation() {
        return relation;
    }

    public FamilyUser setRelation(Integer relation) {
        this.relation = relation;
        return this;
    }

    public Integer getSex() {
        return sex;
    }

    public FamilyUser setSex(Integer sex) {
        this.sex = sex;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public FamilyUser setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getIntroduce() {
        return introduce;
    }

    public FamilyUser setIntroduce(String introduce) {
        this.introduce = introduce;
        return this;
    }

    public String getRemarks() {
        return remarks;
    }

    @Override
    public String toString() {
        return "FamilyUser{" +
                "id=" + id +
                ", familyId=" + familyId +
                ", userId=" + userId +
                ", level=" + level +
                ", generation=" + generation +
                ", genealogyName='" + genealogyName + '\'' +
                ", relation=" + relation +
                ", sex=" + sex +
                ", status=" + status +
                ", joins=" + joins +
                ", introduce='" + introduce + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createTime=" + createTime +
                ", messageNumber=" + messageNumber +
                ", manageTime=" + manageTime +
                ", updateTime=" + updateTime +
                ", updatestatus=" + updatestatus +
                '}';
    }

    public Integer getJoins() {
        return joins;
    }

    public void setJoins(Integer joins) {
        this.joins = joins;
    }

    public FamilyUser setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public FamilyUser setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getMessageNumber() {
        return messageNumber;
    }

    public FamilyUser setMessageNumber(Integer messageNumber) {
        this.messageNumber = messageNumber;
        return this;
    }

    public Date getManageTime() {
        return manageTime;
    }

    public FamilyUser setManageTime(Date manageTime) {
        this.manageTime = manageTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
