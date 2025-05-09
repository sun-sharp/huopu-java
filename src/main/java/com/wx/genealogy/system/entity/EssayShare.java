package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

public class EssayShare  extends Model<EssayShare> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    private Integer familyId;
    private Integer essayId;

    public Integer getId() {
        return id;
    }

    public EssayShare setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public EssayShare setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getEssayId() {
        return essayId;
    }
    public void setEssayId(Integer essayId) {
        this.essayId = essayId;
    }


    public Integer getFamilyId() {
        return familyId;
    }
    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

}
