package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.SysLog;

import java.util.Date;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-12
 */
public interface SysLogService extends IService<SysLog> {

    void insertLog(SysLog sysLog);

    JsonResult sysUserGetLog(Integer page, Integer limit, Date startTime, Date endTime);

    JsonResult deleteLog(Integer id);
}
