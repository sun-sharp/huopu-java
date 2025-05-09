package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.common.util.wechat.pay.HttpRequestUtil;
import com.wx.genealogy.common.util.wechat.pay.WXPayUtil;
import com.wx.genealogy.common.util.wechat.prop.WeChatProp;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.common.util.BizUtil;
import com.wx.genealogy.system.mapper.FireDao;

import com.wx.genealogy.system.mapper.TradeDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.FireService;

import com.wx.genealogy.system.service.TradeService;
import com.wx.genealogy.system.vo.req.FireInsertReqVo;
import com.wx.genealogy.system.vo.req.TradeInsertReqVo;
import com.wx.genealogy.system.vo.res.EssayAndImgSelectResVo;
import com.wx.genealogy.system.vo.res.UserFindResVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**

 *
 * @author makejava
 * @since 2022-07-11 17:38:20
 */
@Service("fireService")
public class FireServiceImpl extends ServiceImpl<FireDao, Fire> implements FireService {
    @Resource
    private FireDao fireDao;
    @Resource
    private TradeService tradeService;
    @Resource
    private UserDao userDao;
    @Resource
    private TradeDao tradeDao;
    @Resource
    private FireService fireService;



    @Override
    public JsonResult selectFireByUserId(Integer page, Integer limit, Fire fire) {
        IPage<Fire> pageData = new Page<>(page, limit);
        QueryWrapper<Fire> essaySelect = new QueryWrapper<Fire>();
        essaySelect.inSql("trade_id", "SELECT id FROM trade WHERE status = 1");
        essaySelect.eq("user_id", fire.getUserId());
        essaySelect.orderByDesc("create_time");

        pageData = fireDao.selectPage(pageData, essaySelect);
        List<Fire> essayList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());


        if (essayList == null || essayList.size() == 0) {
            map.put("Firelist", null);
            return ResponseUtil.ok("获取成功", map);
        }


        // 循环 essayList，匹配用户信息
        List<Map<String, Object>> fireListWithUser = new ArrayList<>();
        for (Fire essay : essayList) {
            Map<String, Object> fireMap = new HashMap<>();
            fireMap.put("fire", essay);

            User user = userDao.selectById(essay.getUserId());
            if (user != null) {
                fireMap.put("user", user);
            } else {
                fireMap.put("user", null);
            }

            fireListWithUser.add(fireMap);
        }
        map.put("data",fireListWithUser);
        List<Object> result = fireDao.selectObjs(new QueryWrapper<Fire>()
                .select("SUM(firenumber)")
                .inSql("trade_id", "SELECT id FROM trade WHERE status = 1")
                .eq("user_id", fire.getUserId())); // 增加 user_id = 3 的条件

        Double totalAmount = 0.0; // 初始化总和

        if (result != null && !result.isEmpty() && result.get(0) != null) {
            BigDecimal amount = (BigDecimal) result.get(0);  // 获取结果
            totalAmount = amount.doubleValue();  // 将 BigDecimal 转换为 Double
        }

// 将总和添加到返回结果中
        map.put("totalAmount", totalAmount);


        return ResponseUtil.ok("获取成功", map);
    }


    @Override
    public JsonResult selectFire(Integer page, Integer limit, Fire fire) {
        IPage<Fire> pageData = new Page<>(page, limit);
        QueryWrapper<Fire> essaySelect = new QueryWrapper<Fire>();
        essaySelect.inSql("trade_id", "SELECT id FROM trade WHERE status = 1");
        essaySelect.orderByDesc("create_time");

        pageData = fireDao.selectPage(pageData, essaySelect);
        List<Fire> essayList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());


        if (essayList == null || essayList.size() == 0) {
            map.put("Firelist", null);
            return ResponseUtil.ok("获取成功", map);
        }


        // 循环 essayList，匹配用户信息
        List<Map<String, Object>> fireListWithUser = new ArrayList<>();
        for (Fire essay : essayList) {
            Map<String, Object> fireMap = new HashMap<>();
            fireMap.put("fire", essay);

            User user = userDao.selectById(essay.getUserId());
            if (user != null) {
                fireMap.put("user", user);
            } else {
                fireMap.put("user", null);
            }

            fireListWithUser.add(fireMap);
        }
        map.put("data",fireListWithUser);



        List<Object> result = fireDao.selectObjs(new QueryWrapper<Fire>()
                .select("SUM(firenumber)")
                .inSql("trade_id", "SELECT id FROM trade WHERE status = 1"));

        Double totalAmount = 0.0; // 初始化总和

        if (result != null && !result.isEmpty() && result.get(0) != null) {
            BigDecimal amount = (BigDecimal) result.get(0);  // 获取结果
            totalAmount = amount.doubleValue();  // 将 BigDecimal 转换为 Double
        }

        // 将总和添加到返回结果中
        map.put("totalAmount", totalAmount);


        return ResponseUtil.ok("获取成功", map);
    }

    /**
     * 分页查询
     *
     * @param fire 筛选条件
     * @return 查询结果
     */

    public JsonResult queryByPage(Fire fire, Integer page, Integer limit) {
        IPage<Fire> pageData = new Page<>(page, limit);
        QueryWrapper<Fire> fireQueryWrapper = new QueryWrapper<>();

        // 关联 Trade 表，并筛选 status = 1 的记录
        fireQueryWrapper.inSql("trade_id", "SELECT id FROM trade WHERE status = 1");

        if (fire.getUserId() != null) {
            fireQueryWrapper.eq("user_id", fire.getUserId());
        }

        // 查询 Fire 表
        pageData = fireDao.queryAllFire(pageData, fireQueryWrapper);
        List<Fire> records = pageData.getRecords();

        // 准备返回数据
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("taskList", records);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        return ResponseUtil.ok("获取成功", map);
    }
//    public JsonResult queryByPage(Fire fire, Integer page, Integer limit) {
//        IPage<Fire> pageData = new Page<>(page, limit);
//        QueryWrapper<Fire> fireQueryWrapper = new QueryWrapper<Fire>();
//
////        if (StringUtils.isNotBlank(task.getTitile())) {
////            taskQueryWrapper.like("a.titile", task.getTitile());
////        }
////
////        if (ObjectUtil.isNotNull(task.getStatus())) {
////            taskQueryWrapper.eq("a.status", task.getStatus());
////        }
//
//        pageData = fireDao.queryAllFire(pageData, fireQueryWrapper);
//        List<Fire> records = pageData.getRecords();
//
//        //准备
//        HashMap<String, Object> map = new HashMap<>(8);
//        map.put("taskList", records);
//        map.put("pages", pageData.getPages());
//        map.put("total", pageData.getTotal());
//        return ResponseUtil.ok("获取成功", map);
//    }

    /**
     * 新增数据
     *
     * @param
     * @return 实例对象
     */

    public JsonResult  insertFire(FireInsertReqVo fireInsertReqVo, String ip) throws Exception {

       User user = userDao.selectById(fireInsertReqVo.getUserId());

        if (ObjectUtil.isNull(user)) {
            return ResponseUtil.fail("用户不存在");
        }

        Trade trade = new Trade();

        trade.setOutTradeNo(BizUtil.getOrderIdByTime());

        trade.setAmountTotal(fireInsertReqVo.getAmount()*100);
        trade.setUserId(fireInsertReqVo.getUserId());
        trade.setCreateTime(DateUtils.getDateTime());
        trade.setRice(0);
        trade.setDou(fireInsertReqVo.getDounumber());
        trade.setType(2);
        trade.setStatus(0);
        System.out.println(trade);
        System.out.println("asdasd11");
       int result1 = tradeDao.insert(trade);



        Fire fire  = new Fire();
        fire.setCreateTime(DateUtils.getDateTime());
        fire.setUpdateTime(DateUtils.getDateTime());
        fire.setNickname(fireInsertReqVo.getNickname());
        fire.setFamilyname(fireInsertReqVo.getFamilyname());
        fire.setFirenumber(fireInsertReqVo.getFirenumber());
        fire.setDounumber(fireInsertReqVo.getDounumber());
         fire.setAmount(fireInsertReqVo.getAmount()*100);
        fire.setUserId(fireInsertReqVo.getUserId());
        fire.setTradeId(trade.getId());
        fire.setYear(20);
        int tradeResult = fireDao.insert(fire);
        if (tradeResult==0) {
            throw new RuntimeException("下单失败");
        }

       return ResponseUtil.ok("下单成功请完成支付", this.wechatPay(trade, ip,user.getOpenid()));

    }


    private static Map<String, String> wechatPay(Trade trade, String ip, String openid) throws Exception {
        String nowTime = String.valueOf(System.currentTimeMillis() / 1000);
        Map<String, String> resultData = new HashMap<String, String>();
        String nonceStr = WXPayUtil.generateNonceStr();

        Map<String, String> wechatOrderData = new HashMap<String, String>();
        wechatOrderData.put("appid", WeChatProp.APP_ID);
        wechatOrderData.put("body", "点燃火把");
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

    /**
     * 修改数据
     *
     * @param
     * @return 实例对象
     */

    public int update(Fire fire) {

        return this.fireDao.updateById(fire);
    }





    @Override
    public JsonResult findFireById(Fire fire) {
        return null;
    }



}
