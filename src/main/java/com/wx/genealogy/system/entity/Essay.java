package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2021-09-09
 */
public class Essay extends Model<Essay> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    private Integer familyId;

    /**
     * 浏览量
     */
    private Integer browseNumber;

    /**
     * 点赞数
     */
    private Integer praiseNumber;

    private Integer discussNumber;

    /**
     * 内容
     */
    private String content;

    private String address;

    /**
     * 开放1保密2家族内公开3完全公开
     */
    private Integer open;

    /**
     * 发布时间
     */
    private Date createTime;


    @TableField("is_knit")
    private Integer knit;

    private Integer knitCycle;

    private Long knitStartTime;

    private Long knitEndTime;

    private Long autoRemoveTime;

    public Integer getId() {
        return id;
    }

    public Essay setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Essay setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getBrowseNumber() {
        return browseNumber;
    }

    public Essay setBrowseNumber(Integer browseNumber) {
        this.browseNumber = browseNumber;
        return this;
    }

    public Integer getPraiseNumber() {
        return praiseNumber;
    }

    public Essay setPraiseNumber(Integer praiseNumber) {
        this.praiseNumber = praiseNumber;
        return this;
    }

    public Integer getDiscussNumber() {
        return discussNumber;
    }

    public void setDiscussNumber(Integer discussNumber) {
        this.discussNumber = discussNumber;
    }

    public String getContent() {
        return content;
    }

    public Essay setContent(String content) {
        this.content = content;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getKnit() {
        return knit;
    }

    public void setKnit(Integer knit) {
        this.knit = knit;
    }

    public Integer getKnitCycle() {
        return knitCycle;
    }

    public void setKnitCycle(Integer knitCycle) {
        this.knitCycle = knitCycle;
    }

    public Long getKnitStartTime() {
        return knitStartTime;
    }

    public void setKnitStartTime(Long knitStartTime) {
        this.knitStartTime = knitStartTime;
    }

    public Long getKnitEndTime() {
        return knitEndTime;
    }

    public void setKnitEndTime(Long knitEndTime) {
        this.knitEndTime = knitEndTime;
    }

    public Long getAutoRemoveTime() {
        return autoRemoveTime;
    }

    public void setAutoRemoveTime(Long autoRemoveTime) {
        this.autoRemoveTime = autoRemoveTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Essay{" +
        "id=" + id +
        ", userId=" + userId +
        ", browseNumber=" + browseNumber +
        ", praiseNumber=" + praiseNumber +
        ", content=" + content +
        ", open=" + open +
        ", createTime=" + createTime +
        "}";
    }
}
