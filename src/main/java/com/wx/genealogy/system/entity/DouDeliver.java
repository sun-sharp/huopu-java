package com.wx.genealogy.system.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 斗投递对象 dou_deliver
 *
 * @author leo
 * @date 2024-07-05
 */
public class DouDeliver extends Model<DouDeliver> {

    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelIgnore
    private Integer id;

    /** 用户id */
    @ExcelIgnore
    private Integer userId;

    @ExcelProperty("用户名称")
    @ColumnWidth(15)
    private String userName;

    /** 投递数量 */
    @ExcelProperty("投递数量")
    @ColumnWidth(15)
    private Integer amount;

    /** 有效期（单位年） */
    @ExcelProperty("投递有效期（年）")
    @ColumnWidth(25)
    private Integer validYear;

    /** 投递理由 */
    @ExcelProperty("投递理由")
    @ColumnWidth(20)
    private String reason;

    /**
     * 创建人ID
     */
    @ExcelIgnore
    private Integer createById;

    /**
     * 创建时间
     */
    @ExcelProperty("开始时间")
    @ColumnWidth(20)
    private Date createTime;

    /**
     * 有效截止日期
     */
    @ExcelProperty("截止时间")
    @ColumnWidth(20)
    private Date endTime;


    @ExcelProperty("操作人")
    @ColumnWidth(10)
    private String createByName;


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

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Integer getUserId()
    {
        return userId;
    }
    public void setAmount(Integer amount)
    {
        this.amount = amount;
    }

    public Integer getAmount()
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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getValidYear() {
        return validYear;
    }

    public void setValidYear(Integer validYear) {
        this.validYear = validYear;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("amount", getAmount())
            .append("reason", getReason())
            .append("createById", getCreateById())
            .toString();
    }
}
