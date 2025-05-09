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
 * @since 2021-10-15
 */
public class ArticleSupport extends Model<ArticleSupport> {

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
     * 点赞状态0取消1确实
     */
    @TableField("is_status")
    private Integer status;

    /**
     * 点赞时间
     */
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public ArticleSupport setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public ArticleSupport setArticleId(Integer articleId) {
        this.articleId = articleId;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public ArticleSupport setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        return "ArticleSupport{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", userId=" + userId +
                ", createTime=" + createTime +
                "}";
    }
}
