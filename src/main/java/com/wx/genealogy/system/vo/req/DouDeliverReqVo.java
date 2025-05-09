package com.wx.genealogy.system.vo.req;

import java.util.List;

/**
 * 斗投递对象 dou_deliver
 *
 * @author leo
 * @date 2024-07-05
 */
public class DouDeliverReqVo {

    /** 用户id */
    private List<Integer> ids;

    /** 投递数量 */
    private Integer amount;

    /** 投递理由 */
    private String reason;

    /** 有效期（单位年） */
    private Integer validYear;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
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

    public Integer getValidYear() {
        return validYear;
    }

    public void setValidYear(Integer validYear) {
        this.validYear = validYear;
    }

}
