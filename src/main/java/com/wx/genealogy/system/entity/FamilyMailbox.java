package com.wx.genealogy.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.models.auth.In;

import java.util.Date;

public class FamilyMailbox {
    private static final long serialVersionUID = 1L;

    private Integer id;//id

   private  Integer userId;//用户id

    private String mailbox;

    private String dispose;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date disposeTime;


    @Override
    public String toString() {
        return "FamilyMailbox{" +
                "id=" + id +
                ", userId=" + userId +
                ", mailbox='" + mailbox + '\'' +
                ", dispose='" + dispose + '\'' +
                ", applyTime=" + applyTime +
                ", disposeTime=" + disposeTime +
                '}';
    }

    public String getDispose() {
        return dispose;
    }

    public void setDispose(String dispose) {
        this.dispose = dispose;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getDisposeTime() {
        return disposeTime;
    }

    public void setDisposeTime(Date disposeTime) {
        this.disposeTime = disposeTime;
    }
}
