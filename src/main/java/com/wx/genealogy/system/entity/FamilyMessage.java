package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
public class FamilyMessage extends Model<FamilyMessage> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer familyId;
    private Integer essayMessage;
    private Integer articleMessage;
    private Integer videoMessage;
    private Integer familyuserMessage;
    private Integer familyshenMessage;
    private Integer familyshenMessage2;
    private Integer familyshenMessage3;
    private Integer familymanageMessage;
    private Integer familylogMessage;
    private Integer editapplyMessage;
    private Integer puapplyMessage;

    public Integer getPuapplyMessage() {
        return puapplyMessage;
    }

    public void setPuapplyMessage(Integer puapplyMessage) {
        this.puapplyMessage = puapplyMessage;
    }

    public Integer getEditapplyMessage() {
        return editapplyMessage;
    }

    public void setEditapplyMessage(Integer editapplyMessage) {
        this.editapplyMessage = editapplyMessage;
    }

    public Integer getId() {
        return id;
    }

    public FamilyMessage setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public FamilyMessage setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getFamilyshenMessage2() {
        return familyshenMessage2;
    }

    @Override
    public String toString() {
        return "FamilyMessage{" +
                "id=" + id +
                ", userId=" + userId +
                ", familyId=" + familyId +
                ", essayMessage=" + essayMessage +
                ", familyuserMessage=" + familyuserMessage +
                ", familyshenMessage=" + familyshenMessage +
                ", familyshenMessage2=" + familyshenMessage2 +
                ", familyshenMessage3=" + familyshenMessage3 +
                ", familymanageMessage=" + familymanageMessage +
                ", familylogMessage=" + familylogMessage +
                ", articleMessage=" + articleMessage +
                ", videoMessage=" + videoMessage +
                ", editapplyMessage=" + editapplyMessage +
                ", puapplyMessage=" + puapplyMessage +
                '}';
    }

    public void setFamilyshenMessage2(Integer familyshenMessage2) {
        this.familyshenMessage2 = familyshenMessage2;
    }

    public Integer getEssayMessage() {
        return essayMessage;
    }
    public void setEssayMessage(Integer essayMessage) {
        this.essayMessage = essayMessage;
    }



    public Integer getArticleMessage() {
        return articleMessage;
    }
    public void setArticleMessage(Integer articleMessage) {
        this.articleMessage = articleMessage;
    }



    public Integer getVideoMessage() {
        return videoMessage;
    }
    public void setVideoMessage(Integer videoMessage) {
        this.videoMessage = videoMessage;
    }



    public Integer getFamilyuserMessage() {
        return familyuserMessage;
    }
    public void setFamilyuserMessage(Integer familyuserMessage) {
        this.familyuserMessage = familyuserMessage;
    }

    public Integer getFamilyshenMessage() {
        return familyshenMessage;
    }
    public void setFamilyshenMessage(Integer familyshenMessage) {
        this.familyshenMessage = familyshenMessage;
    }


    public Integer getFamilymanageMessage() {
        return familymanageMessage;
    }
    public void setFamilymanageMessage(Integer familymanageMessage) {
        this.familymanageMessage = familymanageMessage;
    }


    public Integer getFamilylogMessage() {
        return familylogMessage;
    }
    public void setFamilylogMessage(Integer familylogMessage) {
        this.familylogMessage = familylogMessage;
    }



    public Integer getFamilyId() {
        return familyId;
    }
    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public Integer getFamilyshenMessage3() {
        return familyshenMessage3;
    }

    public void setFamilyshenMessage3(Integer familyshenMessage3) {
        this.familyshenMessage3 = familyshenMessage3;
    }
}
