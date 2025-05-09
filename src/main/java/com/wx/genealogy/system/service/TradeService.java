package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.Trade;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.system.vo.req.TradeInsertReqVo;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-11-02
 */
@Transactional(rollbackFor = Exception.class)
public interface TradeService extends IService<Trade> {

    JsonResult addTrade(TradeInsertReqVo tradeInsertReqVo, String ip) throws Exception;

    JsonResult updateByOutTradeNo(Trade trade) throws  Exception;

    /**
     * 查询订单详情
     * @param tradeId
     * @return
     */
    JsonResult getTradeDetails(Integer tradeId);

    /**
     * 根据用户订单
     * @param userId
     * @return
     */
    JsonResult getUserTrade(Integer page, Integer limit, Integer userId);
}
