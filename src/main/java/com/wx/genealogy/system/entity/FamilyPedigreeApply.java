package com.wx.genealogy.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FamilyPedigreeApply {

    private static final long serialVersionUID = 1L;

    /** apply_id */
    private Long applyId;

    /** 家族id */
    private Long familyId;

    /** 关系 */
    private String relation;

    /** 申请说明 */
    private String text;

    /** 申请userId */
    private Long userId;

    /** 申请者 */
    private String name;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /** 0-未申请，1-申请中，2-通过，3-拒绝 */
    private Long type;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date disposeTime;

    /** 申请回执 */
    private String receipt;

    /** 处理人员 */
    private String processingUser;

    /** 处理人员姓名 */
    private String processingName;

    @Override
    public String toString() {
        return "FamilyPedigreeApply{" +
                "applyId=" + applyId +
                ", familyId=" + familyId +
                ", relation='" + relation + '\'' +
                ", text='" + text + '\'' +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", type=" + type +
                ", disposeTime=" + disposeTime +
                ", receipt='" + receipt + '\'' +
                ", processingUser='" + processingUser + '\'' +
                ", processingName='" + processingName + '\'' +
                '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Date getDisposeTime() {
        return disposeTime;
    }

    public void setDisposeTime(Date disposeTime) {
        this.disposeTime = disposeTime;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getProcessingUser() {
        return processingUser;
    }

    public void setProcessingUser(String processingUser) {
        this.processingUser = processingUser;
    }

    public String getProcessingName() {
        return processingName;
    }

    public void setProcessingName(String processingName) {
        this.processingName = processingName;
    }
}
