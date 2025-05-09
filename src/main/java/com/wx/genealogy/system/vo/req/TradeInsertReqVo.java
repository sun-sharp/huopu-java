package com.wx.genealogy.system.vo.req;
import lombok.Data;

@Data
public class TradeInsertReqVo {

    /**
     * 订单总金额，单位为分
     */
    private Integer amountTotal;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 米
     */
    private Integer rice;

    private Integer type;

}
