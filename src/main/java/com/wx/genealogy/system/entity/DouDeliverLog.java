package com.wx.genealogy.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 斗投递日志对象 dou_deliver_log
 *
 * @author leo
 * @date 2024-07-05
 */
public class DouDeliverLog extends Model<DouDeliverLog> {

    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 用户id */
    private Long userId;

    /** 投递数量 */
    private Long amount;

    /** 投递理由 */
    private String reason;

    /** 有效期（单位年） */
    private Long validYear;

    /** 操作类型 */
    private String type;

    /**
     * 创建人ID
     */
    private Integer createById;
    private String createByName;
    private String userName;

    /**
     * 创建时间
     */
    @JSONField(format = "YYYY-MM-DD HH:mm:ss")
    private Date createTime;

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setAmount(Long amount)
    {
        this.amount = amount;
    }

    public Long getAmount()
    {
        return amount;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public Integer getCreateById() {
        return createById;
    }

    public void setCreateById(Integer createById) {
        this.createById = createById;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getValidYear() {
        return validYear;
    }

    public void setValidYear(Long validYear) {
        this.validYear = validYear;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("amount", getAmount())
            .append("reason", getReason())
            .append("type", getType())
            .append("createById", getCreateById())
            .append("createTime", getCreateTime())
            .toString();
    }
}
