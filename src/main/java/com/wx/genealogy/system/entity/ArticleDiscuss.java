package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.models.auth.In;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author ${author}
 * @since 2021-10-18
 */
public class ArticleDiscuss extends Model<ArticleDiscuss> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 文章评论id(二级评论需要)
     */
    private Integer articleDiscussId;

    /**
     * 等级：1一级评论2二级评论
     */
    private Integer level;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 发布时间
     */
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public ArticleDiscuss setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public ArticleDiscuss setArticleId(Integer articleId) {
        this.articleId = articleId;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public ArticleDiscuss setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getArticleDiscussId() {
        return articleDiscussId;
    }

    public ArticleDiscuss setArticleDiscussId(Integer articleDiscussId) {
        this.articleDiscussId = articleDiscussId;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public ArticleDiscuss setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ArticleDiscuss setContent(String content) {
        this.content = content;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public ArticleDiscuss setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ArticleDiscuss{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", userId=" + userId +
                ", articleDiscussId=" + articleDiscussId +
                ", level=" + level +
                ", content=" + content +
                ", createTime=" + createTime +
                "}";
    }
}
