package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 认领族谱图申请
 * </p>
 *
 * @author ${author}
 * @since 2023-03-03
 */
public class FamilyGenealogyReceive extends Model<FamilyGenealogyReceive> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 家族id
     */
    private Integer familyId;

    /**
     * 认领的族谱图id
     */
    private Integer familyGenealogyId;

    /**
     * 认领人id
     */
    private Integer userId;

    /**
     * 用户头像
     */
    private String userImg;

    private Integer generation;

    /**
     * 用户名字
     */
    private String userName;

    /**
     * 审核状态（0待审核 1审核通过 2拒绝）
     */
    private Integer status;

    /**
     * 申请备注
     */
    private String applyRemark;

    /**
     * 拒绝原因
     */
    private String refuse;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    //关系
    private  Integer relation;
//    private String phone;


    @Override
    public String toString() {
        return "FamilyGenealogyReceive{" +
                "id=" + id +
                ", familyId=" + familyId +
                ", familyGenealogyId=" + familyGenealogyId +
                ", userId=" + userId +
                ", userImg='" + userImg + '\'' +
                ", generation=" + generation +
                ", userName='" + userName + '\'' +
                ", status=" + status +
                ", applyRemark='" + applyRemark + '\'' +
                ", refuse='" + refuse + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +

                '}';
    }



    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getGeneration() {
        return generation;
    }

    public void setGeneration(Integer generation) {
        this.generation = generation;
    }

    public Integer getId() {
        return id;
    }

    public FamilyGenealogyReceive setId(Integer id) {
        this.id = id;
        return this;
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

    public FamilyGenealogyReceive setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getUserImg() {
        return userImg;
    }

    public FamilyGenealogyReceive setUserImg(String userImg) {
        this.userImg = userImg;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public FamilyGenealogyReceive setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public Integer getFamilyGenealogyId() {
        return familyGenealogyId;
    }

    public void setFamilyGenealogyId(Integer familyGenealogyId) {
        this.familyGenealogyId = familyGenealogyId;
    }

    public Integer getStatus() {
        return status;
    }

    public FamilyGenealogyReceive setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public FamilyGenealogyReceive setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
        return this;
    }

    public String getRefuse() {
        return refuse;
    }

    public FamilyGenealogyReceive setRefuse(String refuse) {
        this.refuse = refuse;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public FamilyGenealogyReceive setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public FamilyGenealogyReceive setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
