package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.SysRole;
import com.wx.genealogy.system.entity.SysUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-08
 */
@Transactional(rollbackFor = Exception.class)
public interface SysRoleService extends IService<SysRole> {

    JsonResult insert(SysRole sysRole, SysUser sysUser) throws Exception;

    JsonResult delete(Integer id);

    HashMap<String, Object> queryList(Integer page, Integer limit);

    JsonResult update(SysRole sysRole);

    JsonResult insertRoleMenu(Integer roleId, List<Integer> menuIds, SysUser sysUser) throws Exception;

    JsonResult insertRolePermission(Integer roleId, List<Integer> permissionIds, SysUser sysUser) throws Exception;

    List<Integer> queryRoleMenu(Integer roleId);

    List<Integer> queryRolePermission(Integer roleId);
}
