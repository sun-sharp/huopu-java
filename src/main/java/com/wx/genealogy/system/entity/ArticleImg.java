package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 帖子图片表
 * </p>
 *
 * @author ${author}
 * @since 2021-10-12
 */
public class ArticleImg extends Model<ArticleImg> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联文章id
     */
    private Integer articleId;

    /**
     * 图片地址
     */
    private String img;


    public Integer getId() {
        return id;
    }

    public ArticleImg setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public ArticleImg setArticleId(Integer articleId) {
        this.articleId = articleId;
        return this;
    }

    public String getImg() {
        return img;
    }

    public ArticleImg setImg(String img) {
        this.img = img;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ArticleImg{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", img=" + img +
                "}";
    }
}
