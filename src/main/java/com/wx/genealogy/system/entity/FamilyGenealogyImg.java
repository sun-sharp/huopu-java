package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * <p>
 * 用户个人介绍图片表
 * </p>
 *
 * @author ${author}
 * @since 2024-10-8
 */
public class FamilyGenealogyImg extends Model<FamilyGenealogyImg> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 家族成员id
     */
    private Integer familyGenealogyId;

    /**
     * 图片地址
     */
    private String img;


    public Integer getId() {
        return id;
    }

    public FamilyGenealogyImg setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getFamilyGenealogyId() {
        return familyGenealogyId;
    }

    public void setFamilyGenealogyId(Integer familyGenealogyId) {
        this.familyGenealogyId = familyGenealogyId;
    }

    public String getImg() {
        return img;
    }

    public FamilyGenealogyImg setImg(String img) {
        this.img = img;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserImg{" +
        "id=" + id +
        ", familyGenealogyId=" + familyGenealogyId +
        ", img=" + img +
        "}";
    }
}
