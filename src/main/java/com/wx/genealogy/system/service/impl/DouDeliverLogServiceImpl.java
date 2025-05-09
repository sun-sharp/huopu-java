package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.DouDeliverLog;
import com.wx.genealogy.system.mapper.DouDeliverLogDao;
import com.wx.genealogy.system.service.DouDeliverLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 斗投递日志Service业务层处理
 *
 * @author leo
 * @date 2024-07-05
 */
@Service
public class DouDeliverLogServiceImpl extends ServiceImpl<DouDeliverLogDao, DouDeliverLog> implements DouDeliverLogService {

    @Resource
    private DouDeliverLogDao douDeliverLogDao;

    @Override
    public JsonResult page(Integer page, Integer limit, DouDeliverLog log) {
        IPage<DouDeliverLog> pageData = new Page<>(page, limit);
        QueryWrapper<DouDeliverLog> queryWrapper = new QueryWrapper<DouDeliverLog>();
        if (log.getUserName() != null && log.getUserName().equals("") == false) {
            queryWrapper.like("user_name", log.getUserName());
        }
        queryWrapper.orderByDesc("id");

        pageData = douDeliverLogDao.selectPage(pageData, queryWrapper);
        List<DouDeliverLog> logList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("logList", logList);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        return ResponseUtil.ok("获取成功", map);
    }

}
