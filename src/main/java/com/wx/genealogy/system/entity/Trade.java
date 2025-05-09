package com.wx.genealogy.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author ${author}
 * @since 2021-11-02
 */
public class Trade extends Model<Trade> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户订单号,长度6-31
     */
    private String outTradeNo;

    /**
     * 订单总金额，单位为分
     */
    private Integer amountTotal;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 订单状态0待支付1已支付
     */
    private Integer status;

    private Integer type;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 米
     */
    private Integer rice;

    private Integer dou;


    public Integer getId() {
        return id;
    }

    public Trade setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public Trade setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
        return this;
    }

    public Integer getAmountTotal() {
        return amountTotal;
    }

    public Trade setAmountTotal(Integer amountTotal) {
        this.amountTotal = amountTotal;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Trade setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Trade setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public Trade setStatus(Integer status) {
        this.status = status;
        return this;
    }



    public Integer getType() {
        return type;
    }

    public Trade setType(Integer type) {
        this.type = type;
        return this;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Trade setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public Integer getRice() {
        return rice;
    }

    public Trade setRice(Integer rice) {
        this.rice = rice;
        return this;
    }


    public Integer getDou() {
        return dou;
    }

    public Trade setDou(Integer dou) {
        this.dou = dou;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Trade{" +
        "id=" + id +
        ", outTradeNo=" + outTradeNo +
        ", amountTotal=" + amountTotal +
        ", userId=" + userId +
        ", createTime=" + createTime +
        ", status=" + status +
        ", type=" + type +
        ", transactionId=" + transactionId +
        ", rice=" + rice +
        "}";
    }
}
