package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户编辑申请表
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
public class FamilyGenealogyEditApply extends Model<FamilyGenealogyEditApply> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer familyGenealogyId;

    @TableField(exist = false)
    private String familyGenealogyName;

    /** 申请理由*/
    private String reason;

    /** 审核理由*/
    private String auditReason;

    /**
     * 申请类型：0关闭申请；1打开申请 2家谱关闭编辑；3家谱打开编辑
     */
    private Integer type;

    /**
     * 审核状态：0待审核；1审核通过；2审核不通过；
     */
    private Integer status;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 审核时间
     */
    private Date auditTime;

    public String getAuditReason() {
        return auditReason;
    }

    public void setAuditReason(String auditReason) {
        this.auditReason = auditReason;
    }

    public String getFamilyGenealogyName() {
        return familyGenealogyName;
    }

    public void setFamilyGenealogyName(String familyGenealogyName) {
        this.familyGenealogyName = familyGenealogyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFamilyGenealogyId() {
        return familyGenealogyId;
    }

    public void setFamilyGenealogyId(Integer familyGenealogyId) {
        this.familyGenealogyId = familyGenealogyId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    @Override
    public String toString() {
        return "User{" +
        "id=" + id +
        ", familyGenealogyId=" + familyGenealogyId +
        ", reason=" + reason +
        ", type=" + type +
        ", auditTime=" + auditTime +
        ", applyTime=" + applyTime +
        ", status=" + status +
        "}";
    }
}
