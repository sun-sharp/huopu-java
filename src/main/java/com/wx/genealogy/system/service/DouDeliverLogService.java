package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.DouDeliverLog;

/**
 * 斗投递日志Service接口
 *
 * @author leo
 * @date 2024-07-05
 */
public interface DouDeliverLogService extends IService<DouDeliverLog> {

    JsonResult page(Integer page, Integer limit, DouDeliverLog log);
}
