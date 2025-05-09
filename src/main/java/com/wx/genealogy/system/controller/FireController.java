package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.annotation.MyLog;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.CusAccessObjectUtil;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.common.util.wechat.pay.WXPayUtil;
import com.wx.genealogy.system.entity.Essay;
import com.wx.genealogy.system.entity.Fire;
import com.wx.genealogy.system.entity.Trade;
import com.wx.genealogy.system.service.FireService;
import com.wx.genealogy.system.service.SysLogService;
import com.wx.genealogy.system.service.TradeService;
import com.wx.genealogy.system.vo.req.FireInsertReqVo;
import com.wx.genealogy.system.vo.req.GetLogReqVo;
import com.wx.genealogy.system.vo.req.TradeInsertReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

/**
 * @ClassName LogController
 * @Author hangyi
 * @Data 2020/7/21 12:02
 * @Description
 * @Version 1.0
 **/
@Api(tags = "点燃火把")
@RestController
@RequestMapping("/fire")
public class FireController {

    @Autowired
    private FireService fireService;
    private TradeService tradeService;

    @ApiOperation(value = "根据用户id分页查询")
    @RequestMapping(value = "/selectFireByUserId", method = RequestMethod.GET)
    public JsonResult selectFireByUserId(@RequestParam(value = "limit") Integer limit,
                                          @RequestParam(value = "page") Integer page,
                                          @RequestParam(value = "userId") Integer userId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        Fire fire = new Fire();
        fire.setUserId(userId);
        return fireService.selectFireByUserId(page, limit, fire);
    }



    @ApiOperation(value = "分页查询所有")
    @RequestMapping(value = "/selectFire", method = RequestMethod.GET)
    public JsonResult selectFire(@RequestParam(value = "limit") Integer limit,
                                          @RequestParam(value = "page") Integer page,
                                          @RequestParam(value = "userId") Integer userId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        Fire fire = new Fire();
        fire.setUserId(userId);
        return fireService.selectFire(page, limit, fire);
    }



    @ApiOperation(value = "下单")
    @RequestMapping(value = "/insertFire", method = RequestMethod.POST)
    public JsonResult addTrade(HttpServletRequest request, @RequestBody FireInsertReqVo fireInsertReqVo) throws Exception {

        if (ObjectUtil.checkPartObjFieldIsNull(fireInsertReqVo)) {
            throw new MissingServletRequestParameterException("", "");
        }

       return fireService.insertFire(fireInsertReqVo, CusAccessObjectUtil.getIpAddress(request));
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



}
