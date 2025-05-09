package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.RiceRecord;
import com.wx.genealogy.system.mapper.RiceRecordDao;
import com.wx.genealogy.system.service.RiceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 米收支明细 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-10-20
 */
@Service
public class RiceRecordServiceImpl extends ServiceImpl<RiceRecordDao, RiceRecord> implements RiceRecordService {

    @Autowired
    private RiceRecordDao riceRecordDao;

    @Override
    public JsonResult selectRiceRecord(Integer page, Integer limit,Integer type, RiceRecord riceRecord) {
        IPage<RiceRecord> pageData =new Page<RiceRecord>(page, limit);
        QueryWrapper<RiceRecord> riceRecordSelect = new QueryWrapper<RiceRecord>();
        if(type==0){
            riceRecordSelect.eq("content","充值获得");
        }
        if(type==1){
            riceRecordSelect.lt("rice",0);
        }

        if(type==2){
            riceRecordSelect.gt("rice",0);
            riceRecordSelect.ne("content","充值获得");
        }
        riceRecordSelect.eq("user_id",riceRecord.getUserId());
        riceRecordSelect.orderByDesc("id");
        pageData=riceRecordDao.selectPage(pageData, riceRecordSelect);
        List<RiceRecord> riceRecordList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        map.put("riceRecordList",riceRecordList);
        return ResponseUtil.ok("获取成功",map);
    }

    /**
     * 查询所有明细记录
     * @param page
     * @param limit
     * @return
     */
    @Override
    public JsonResult selectRiceRecordList(Integer page, Integer limit) {
        IPage<RiceRecord> pageData = new Page<>(page, limit);

        pageData = riceRecordDao.selectRiceRecordList(pageData);
        List<RiceRecord> records = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("riceRecordList", records);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        return ResponseUtil.ok("获取成功", map);
    }
}
