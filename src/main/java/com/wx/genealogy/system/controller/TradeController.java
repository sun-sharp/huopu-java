package com.wx.genealogy.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.CusAccessObjectUtil;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.wechat.pay.WXPayUtil;
import com.wx.genealogy.system.entity.Trade;
import com.wx.genealogy.system.service.TradeService;
import com.wx.genealogy.system.vo.req.TradeInsertReqVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-11-02
 */
@RestController
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @ApiOperation(value = "下单")
    @RequestMapping(value = "/addTrade", method = RequestMethod.POST)
    public JsonResult addTrade(HttpServletRequest request, @RequestBody TradeInsertReqVo tradeInsertReqVo) throws Exception {
        if (ObjectUtil.checkPartObjFieldIsNull(tradeInsertReqVo)) {
            throw new MissingServletRequestParameterException("", "");
        }
        return tradeService.addTrade(tradeInsertReqVo, CusAccessObjectUtil.getIpAddress(request));
    }

    @ApiOperation(value = "支付回调")
    @RequestMapping(value = "/payBack", method = RequestMethod.POST)
    public String payBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();//获取请求的流信息(这里是微信发的xml格式所有只能使用流来读)
            String xml = WXPayUtil.inputStream2String(inputStream, "UTF-8");
            System.out.println(xml);
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);//将微信发的xml转map

            if (notifyMap.get("return_code").equals("SUCCESS")) {
                if (notifyMap.get("result_code").equals("SUCCESS")) {
                    //String totalFee = notifyMap.get("total_fee");//实际支付的订单金额:单位 分
                    //接下来自定义操作
                    Trade tradeUpdate = new Trade();
                    tradeUpdate.setOutTradeNo(notifyMap.get("out_trade_no"));
                    tradeUpdate.setTransactionId(notifyMap.get("transaction_id"));
                    tradeUpdate.setStatus(1);
                    tradeService.updateByOutTradeNo(tradeUpdate);
                }
            }

            //告诉微信服务器收到信息了，不要在调用回调action了========这里很重要回复微信服务器信息用流发送一个xml即可
            response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "用户查询订单")
    @RequestMapping(value = "/getTrade", method = RequestMethod.GET)
    public JsonResult getTrade(@RequestParam(value = "page") Integer page,
                               @RequestParam(value = "limit") Integer limit,
                               @RequestParam("userId") Integer userId) throws Exception {
        return tradeService.getUserTrade(page, limit, userId);
    }

    @ApiOperation(value = "用户查询订单详情")
    @RequestMapping(value = "/getTradeDetails", method = RequestMethod.GET)
    public JsonResult getTradeDetails(@RequestParam("tradeId") Integer tradeId) throws Exception {
        return tradeService.getTradeDetails(tradeId);
    }
}

