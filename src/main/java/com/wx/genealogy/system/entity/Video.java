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
public class Video extends Model<Video> {

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



    private String title;

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

    public Video setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Video setUserId(Integer userId) {
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

    public Video setBrowseNumber(Integer browseNumber) {
        this.browseNumber = browseNumber;
        return this;
    }

    public Integer getPraiseNumber() {
        return praiseNumber;
    }

    public Video setPraiseNumber(Integer praiseNumber) {
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

    public Video setContent(String content) {
        this.content = content;
        return this;
    }


    public String getTitle() {
        return title;
    }

    public Video setTitle(String title) {
        this.title = title;
        return this;
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
        return "Video{" +
                "id=" + id +
                ", userId=" + userId +
                ", browseNumber=" + browseNumber +
                ", praiseNumber=" + praiseNumber +
                ", title=" + title +
                ", content=" + content +

                ", createTime=" + createTime +
                "}";
    }
}
