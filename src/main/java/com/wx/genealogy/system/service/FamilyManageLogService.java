package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.FamilyManageLog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 家族管理日志 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-10-12
 */
@Transactional(rollbackFor = Exception.class)
public interface FamilyManageLogService extends IService<FamilyManageLog> {

    JsonResult selectFamilyManageLog(Integer page ,Integer limit,FamilyManageLog familyManageLog);

    JsonResult selectFamilyManageLogs(Integer page ,Integer limit,FamilyManageLog familyManageLog);
}
