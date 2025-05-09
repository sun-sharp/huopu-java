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
public class VideoImg extends Model<VideoImg> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联文章id
     */
    private Integer videoId;

    /**
     * 图片地址
     */
    private String img;


    public Integer getId() {
        return id;
    }

    public VideoImg setId(Integer id) {
        this.id = id;
        return this;
    }


    public VideoImg setVideoId(Integer videoId) {
        this.videoId = videoId;
        return this;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public String getImg() {
        return img;
    }

    public VideoImg setImg(String img) {
        this.img = img;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "VideoImg{" +
                "id=" + id +
                ", videoId=" + videoId +
                ", img=" + img +
                "}";
    }
}
