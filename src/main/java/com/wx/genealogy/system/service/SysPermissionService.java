package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.SysPermission;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-08
 */
@Transactional(rollbackFor = Exception.class)
public interface SysPermissionService extends IService<SysPermission> {
    JsonResult insert(SysPermission sysPermission);

    JsonResult delete(Integer id) throws Exception;

    HashMap<String, Object> queryList(Integer page, Integer limit);

    JsonResult update(SysPermission sysPermission);
}
