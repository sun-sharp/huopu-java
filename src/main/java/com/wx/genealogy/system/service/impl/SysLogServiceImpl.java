package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.SysLog;
import com.wx.genealogy.system.mapper.SysLogDao;
import com.wx.genealogy.system.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-12
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLog> implements SysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    @Override
    public void insertLog(SysLog sysLog) {
        sysLogDao.insert(sysLog);
    }
    @Override
    public JsonResult sysUserGetLog(Integer page, Integer limit, Date startTime, Date endTime) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        Page<SysLog> pageInfo = new Page<>(page, limit);
        pageInfo.addOrder(OrderItem.desc("create_time"));
        Page<SysLog> sysLogIPage = sysLogDao.sysUserGetLog(pageInfo,map);
        HashMap<String, Object> map1 = new HashMap<>(8);
        map1.put("sysLogs",sysLogIPage.getRecords());
        map1.put("pages", sysLogIPage.getPages());
        map1.put("total", sysLogIPage.getTotal());
        return ResponseUtil.ok("",map1);
    }

    @Override
    public JsonResult deleteLog(Integer id) {
        int ret = sysLogDao.deleteById(id);
        return ret == 0 ? ResponseUtil.fail("删除失败！") : ResponseUtil.ok("删除成功");
    }
}
