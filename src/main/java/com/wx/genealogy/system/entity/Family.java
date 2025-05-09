package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 家族
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
public class Family extends Model<Family> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 创建者id(用户id)
     */
    private Integer userId;

    /**
     * logo
     */
    private String logo;

    /**
     * 家族名称
     */
    private String name;
    private String mark;
    private Integer peopleNumber;

    private Integer examineNumber;
    @TableField(exist = false)
    private Integer userCount;
    @TableField(exist = false)
    private Integer genealogyCount;

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getGenealogyCount() {
        return genealogyCount;
    }

    public void setGenealogyCount(Integer genealogyCount) {
        this.genealogyCount = genealogyCount;
    }

    private String puname;
    private String hunname;

    private String audio;


    public String getPuname() {
        return puname;
    }

    public Family setPuname(String puname) {
        this.puname = puname;
        return this;
    }


    public String getHunname() {
        return hunname;
    }

    public Family setHunname(String hunname) {
        this.hunname = hunname;
        return this;
    }


    public Integer getId() {
        return id;
    }

    public Family setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Family setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getLogo() {
        return logo;
    }

    public Family setLogo(String logo) {
        this.logo = logo;
        return this;
    }


    public String getMark() {
        return mark;
    }

    public Family setMark(String mark) {
        this.mark = mark;
        return this;
    }


    public String getName() {
        return name;
    }

    public Family setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(Integer peopleNumber) {
        this.peopleNumber = peopleNumber;
    }


    public Integer getExamineNumber() {
        return examineNumber;
    }

    @Override
    public String toString() {
        return "Family{" +
                "id=" + id +
                ", userId=" + userId +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                ", mark='" + mark + '\'' +
                ", peopleNumber=" + peopleNumber +
                ", examineNumber=" + examineNumber +
                ", puname='" + puname + '\'' +
                ", hunname='" + hunname + '\'' +
                ", audio='" + audio + '\'' +
                '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public void setExamineNumber(Integer examineNumber) {
        this.examineNumber = examineNumber;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
