package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.SysUser;
import com.wx.genealogy.system.vo.req.SysUserQueryListReqVo;
import com.wx.genealogy.system.vo.res.SysUserMenuInfoResVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-08
 */
@Transactional(rollbackFor = Exception.class)
public interface SysUserService extends IService<SysUser> {

    SysUser selectUserByUserName(String userName);

    JsonResult insertSysUser(SysUser sysUser, Integer roleId) throws Exception;

    SysUserMenuInfoResVo getInfo(SysUser sysUser);

    JsonResult update(SysUser sysUser) throws Exception;

    HashMap<String, Object> queryList(SysUserQueryListReqVo sysUserQueryListReqVo);

    JsonResult updateInfo(SysUser sysUser, Integer roleId)throws Exception;

    JsonResult updatePassword(SysUser sysUser);

    JsonResult queryAllList();

    int insertSysUser1(SysUser sysUser, Integer roleId) throws Exception;
}
