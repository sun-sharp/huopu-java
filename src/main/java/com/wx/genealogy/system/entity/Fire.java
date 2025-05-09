package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/**

 *
 * @author ${author}
 * @since 2021-09-09
 */
public class Fire extends Model<Fire> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    private Integer tradeId;

    /**
     * 浏览量
     */
    private Integer firenumber;

    /**
     * 点赞数
     */
    private Integer dounumber;

    private Integer amount;

    private Integer year;

    /**
     * 内容
     */
    private String nickname;



    private String familyname;

    /**
     * 发布时间
     */
    private Date createTime;
    private Date updateTime;




    public Integer getId() {
        return id;
    }

    public Fire setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Fire setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }



    public Integer getFirenumber() {
        return firenumber;
    }

    public Fire setFirenumber(Integer firenumber) {
        this.firenumber = firenumber;
        return this;
    }


    public Integer getTradeId() {
        return tradeId;
    }

    public Fire setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
        return this;
    }

    public Integer getDounumber() {
        return dounumber;
    }

    public Fire setDounumber(Integer dounumber) {
        this.dounumber = dounumber;
        return this;
    }


    public Integer getAmount() {
        return amount;
    }

    public Fire setAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getNickname() {
        return nickname;
    }

    public Fire setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }


    public String getFamilyname() {
        return familyname;
    }

    public Fire setFamilyname(String familyname) {
        this.familyname = familyname;
        return this;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Integer pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Fire{" +
                "id=" + id +
                ", userId=" + userId +
                ", firenumber=" + firenumber +
                ", dounumber=" + dounumber +
                ", year=" + year +
                ", nickname=" + nickname +
                ", familyname=" + familyname +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", tradeId=" + tradeId +
                "}";
    }
}
