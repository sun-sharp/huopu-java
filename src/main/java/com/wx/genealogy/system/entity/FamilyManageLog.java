package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 家族管理日志
 * </p>
 *
 * @author ${author}
 * @since 2021-10-12
 */
public class FamilyManageLog extends Model<FamilyManageLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer familyId;

    /**
     * 家族成员id
     */
    private Integer familyUserId;

    private Integer userId;

    /**
     * 行为
     */
    private String action;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public FamilyManageLog setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getFamilyUserId() {
        return familyUserId;
    }

    public FamilyManageLog setFamilyUserId(Integer familyUserId) {
        this.familyUserId = familyUserId;
        return this;
    }

    public String getAction() {
        return action;
    }

    public FamilyManageLog setAction(String action) {
        this.action = action;
        return this;
    }

    public String getContent() {
        return content;
    }

    public FamilyManageLog setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "FamilyManageLog{" +
        "id=" + id +
        ", familyUserId=" + familyUserId +
        ", action=" + action +
        ", content=" + content +
        ", createTime=" + createTime +
        "}";
    }
}
