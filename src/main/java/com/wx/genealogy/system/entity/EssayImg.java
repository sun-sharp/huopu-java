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
public class EssayImg extends Model<EssayImg> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联文章id
     */
    private Integer essayId;

    /**
     * 图片地址
     */
    private String img;


    public Integer getId() {
        return id;
    }

    public EssayImg setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getEssayId() {
        return essayId;
    }

    public EssayImg setEssayId(Integer essayId) {
        this.essayId = essayId;
        return this;
    }

    public String getImg() {
        return img;
    }

    public EssayImg setImg(String img) {
        this.img = img;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "EssayImg{" +
        "id=" + id +
        ", essayId=" + essayId +
        ", img=" + img +
        "}";
    }
}
