package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.BizUtil;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.common.util.wechat.pay.HttpRequestUtil;
import com.wx.genealogy.common.util.wechat.pay.WXPayUtil;
import com.wx.genealogy.common.util.wechat.prop.WeChatProp;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.mapper.*;
import com.wx.genealogy.system.service.TradeService;
import com.wx.genealogy.system.service.UserService;
import com.wx.genealogy.system.vo.req.TradeInsertReqVo;
import com.wx.genealogy.system.vo.res.TradeResVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-11-02
 */
@Slf4j
@Service
public class TradeServiceImpl extends ServiceImpl<TradeDao, Trade> implements TradeService {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private UserDao userDao;
    private FireDao fireDao;
    @Autowired
    private RiceRecordDao riceRecordDao;

    @Autowired
    private DouRecordDao douRecordDao;
    @Autowired
    private UserService userService;
    @Autowired
    private DouDeliverDao douDeliverDao;





    @Override
    public JsonResult addTrade(TradeInsertReqVo tradeInsertReqVo, String ip) throws Exception {
        //判断用户是否存在
        User user = userDao.selectById(tradeInsertReqVo.getUserId());
        if (ObjectUtil.isNull(user)) {
            return ResponseUtil.fail("用户不存在");
        }

        //添加订单
        Trade trade = new Trade();
        trade.setOutTradeNo(BizUtil.getOrderIdByTime());
        trade.setAmountTotal(tradeInsertReqVo.getAmountTotal());
        trade.setUserId(user.getId());
        trade.setCreateTime(DateUtils.getDateTime());
        trade.setRice(tradeInsertReqVo.getRice());
        System.out.println(trade);
        Boolean result = tradeService.save(trade);

        if (!result) {
            throw new Exception("下单失败");
        }
        //获取支付凭证
        return ResponseUtil.ok("下单成功请完成支付", this.wechatPay(trade, ip, user.getOpenid()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult updateByOutTradeNo(Trade trade) throws Exception {
        //先查看订单
        QueryWrapper<Trade> tradeFind = new QueryWrapper<Trade>();
        tradeFind.eq("out_trade_no", trade.getOutTradeNo());
        Trade tradeData = tradeService.getOne(tradeFind);

        log.info("【2"+tradeData.getDou()+'a'+tradeData.getUserId());

        User userData = userService.selectUserById(tradeData.getUserId());


        if(tradeData==null){
            return ResponseUtil.fail("修改失败");
        }

        //再修改订单
        trade.setId(tradeData.getId());
        boolean isSuccess = tradeService.updateById(trade);
        if(!isSuccess){
            return ResponseUtil.fail("修改失败");
        }
        log.info("【{}】用户火把充值【{}】的结果type：{}"+tradeData.getDou()+'a'+tradeData.getUserId());
        if(tradeData.getType() == 2){
            log.info("【{}】用户火把充值【{}】的结果：{}");
            //下发米并且记录
            User user = new User();
            user.setId(tradeData.getUserId());
            user.setDou(tradeData.getDou()+userData.getDou());
            int res = userDao.updateById(user);
            log.info("【{}】用户火把充值【{}】的结果：{}", tradeData.getUserId(), tradeData.getDou(), res);

            DouRecord douRecord = new DouRecord();
            douRecord.setUserId(tradeData.getUserId());
            douRecord.setDouAmount(tradeData.getDou());
            douRecord.setContent("充值火把获得");
            douRecord.setCreateTime(DateUtils.getDateTime());
            int rrRes = douRecordDao.insert(douRecord);

            Date curDate = new Date();
            DouDeliver douDeliver = new DouDeliver();
            douDeliver.setValidYear(20);
            douDeliver.setAmount(tradeData.getDou());
            douDeliver.setReason("点燃火把赠送");
            douDeliver.setCreateById(tradeData.getUserId());
            douDeliver.setCreateTime(curDate);

            douDeliver.setEndTime(DateUtils.getEndDate(curDate, 20));
            douDeliver.setUserName(userData.getNickName());
            douDeliver.setCreateByName(userData.getNickName());
            douDeliver.setUserId(tradeData.getUserId());

            log.info("【", douDeliver);


            douDeliverDao.insert(douDeliver);



            log.info("【{}】用户米充值记录的结果：{}", tradeData.getUserId(), rrRes);
        }else{
            //下发米并且记录
            User user = new User();
            user.setId(tradeData.getUserId());
            user.setRice(tradeData.getRice());
            int res = userDao.setInc(user);
            log.info("【{}】用户米充值【{}】的结果：{}", tradeData.getUserId(), tradeData.getRice(), res);

            RiceRecord riceRecord = new RiceRecord();
            riceRecord.setUserId(tradeData.getUserId());
            riceRecord.setRice(tradeData.getRice());
            riceRecord.setContent("充值获得");
            riceRecord.setCreateTime(DateUtils.getDateTime());
            int rrRes = riceRecordDao.insert(riceRecord);
            log.info("【{}】用户米充值记录的结果：{}", tradeData.getUserId(), rrRes);
        }




        return ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult getTradeDetails(Integer tradeId) {
        Trade trade = tradeService.getById(tradeId);
        return ResponseUtil.ok("获取成功", trade);
    }

    @Override
    public JsonResult getUserTrade(Integer page, Integer limit, Integer userId) {
        List<TradeResVo> dataList = new ArrayList<>();

        IPage<Trade> pageData = new Page<>(page, limit);
        pageData = tradeService.page(pageData, new LambdaQueryWrapper<Trade>()
                .eq(Trade::getStatus, 1)
                .eq(Trade::getUserId, userId)
                .orderByDesc(Trade::getId));

        for (Trade trade : pageData.getRecords()) {
            TradeResVo tradeResVo = new TradeResVo();
            tradeResVo.setTrade(trade);
            tradeResVo.setUser(userDao.selectById(trade.getUserId()));
            dataList.add(tradeResVo);
        }
        return ResponseUtil.ok("结果", dataList);
    }

    private static Map<String, String> wechatPay(Trade trade, String ip, String openid) throws Exception {
        String nowTime = String.valueOf(System.currentTimeMillis() / 1000);
        Map<String, String> resultData = new HashMap<String, String>();
        String nonceStr = WXPayUtil.generateNonceStr();

        Map<String, String> wechatOrderData = new HashMap<String, String>();
        wechatOrderData.put("appid", WeChatProp.APP_ID);
        wechatOrderData.put("body", "平台商品购买");
        wechatOrderData.put("mch_id", WeChatProp.MCH_ID);
        wechatOrderData.put("nonce_str", nonceStr);
        wechatOrderData.put("openid", openid);
        wechatOrderData.put("out_trade_no", String.valueOf(trade.getOutTradeNo()));
        wechatOrderData.put("spbill_create_ip", ip);
        wechatOrderData.put("total_fee", String.valueOf(trade.getAmountTotal()));
        wechatOrderData.put("trade_type", "JSAPI");
        wechatOrderData.put("notify_url", WeChatProp.MY_URL + "/trade/payBack");
        String sign = WXPayUtil.generateSignature(wechatOrderData, WeChatProp.MCH_KEY);
        wechatOrderData.put("sign", sign);
        String params = WXPayUtil.mapToXml(wechatOrderData);//将所有参数(map)转xml格式
        //发送请求
        String getData = HttpRequestUtil.sendPost(WeChatProp.WEIXIN_MCH_URL + "/pay/unifiedorder", params);
        System.out.println(getData);
        //以下内容是返回前端页面的json数据
        String prepayId = "";//预支付id
        if (getData.indexOf("SUCCESS") != -1) {
            Map<String, String> map = WXPayUtil.xmlToMap(getData);
            if (map.get("return_code").equals("SUCCESS") == false) {
                resultData.put("package", "");
                return resultData;
            }
            if (map.get("result_code").equals("SUCCESS") == false) {
                resultData.put("package", "");
                return resultData;
            }
            prepayId = map.get("prepay_id");
        }
        resultData.put("appId", WeChatProp.APP_ID);
        resultData.put("timeStamp", nowTime);
        resultData.put("nonceStr", nonceStr);
        resultData.put("signType", "MD5");
        resultData.put("package", "prepay_id=" + prepayId);
        String paySign = WXPayUtil.generateSignature(resultData, WeChatProp.MCH_KEY);
        resultData.put("paySign", paySign);
        resultData.put("tradeId", String.valueOf(trade.getId()));
        return resultData;
    }
}
