package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.DouRecord;
import com.wx.genealogy.system.entity.RiceRecord;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.DouRecordDao;
import com.wx.genealogy.system.service.DouRecordService;
import com.wx.genealogy.system.service.RiceRecordService;
import com.wx.genealogy.system.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 斗收入明细 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2022-07-12
 */
@Service
@Transactional
public class DouRecordServiceImpl extends ServiceImpl<DouRecordDao, DouRecord> implements DouRecordService {

    @Resource
    private DouRecordDao douRecordDao;

    @Resource
    private UserService userService;

    @Resource
    private RiceRecordService riceRecordService;


    /**
     * 查询所有斗明细记录
     *
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    @Override
    public JsonResult selectDouRecordList(Integer page, Integer limit, Integer userId) {
        IPage<DouRecord> pageData = new Page<>(page, limit);

        QueryWrapper<DouRecord> wrapper = new QueryWrapper<DouRecord>();
        if (ObjectUtil.isNotNull(userId)) {
            wrapper.eq("a.user_id", userId);
        }

        pageData = douRecordDao.selectRiceRecordList(pageData, wrapper);
        List<DouRecord> records = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("douRecordList", records);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        return ResponseUtil.ok("获取成功", map);
    }


    /**
     * 兑换斗
     *
     * @return
     */
    @Override
    public JsonResult exchangeDouRecord(DouRecord douRecord) {
        if (ObjectUtil.isNull(douRecord.getUserId())) {
            return ResponseUtil.fail("用户id不能为空");
        }

        if (ObjectUtil.isNull(douRecord.getDouAmount())) {
            return ResponseUtil.fail("兑换的斗数量不能为空");
        }

        //判断米够不够  5万米才可以换1斗
        //需要多少米才能换输入的斗数量
        Integer douAmount = 50000 * douRecord.getDouAmount();

        //查询登录人的米数量
        User byId = userService.getById(douRecord.getUserId());
        if (byId == null) {
            return ResponseUtil.fail("用户不存在");
        }

        if (byId.getRice() < douAmount) {
            return ResponseUtil.fail("您的米数量不足");
        }
        //修改米数量
        byId.setRice(byId.getRice() - douAmount);
        //换的斗数量
        byId.setDou(byId.getDou() + douRecord.getDouAmount());
        userService.updateById(byId);

        //记录米明细
        RiceRecord record = new RiceRecord();
        //用户id
        record.setUserId(douRecord.getUserId());
        //多少米
        record.setRice(-douAmount);
        record.setContent("兑换斗扣除米");
        record.setCreateTime(new Date());
        riceRecordService.save(record);

        //记录斗明细
        douRecord.setContent("兑换斗");
        douRecord.setCreateTime(new Date());
        douRecordDao.insert(douRecord);


        return ResponseUtil.ok();
    }


}
