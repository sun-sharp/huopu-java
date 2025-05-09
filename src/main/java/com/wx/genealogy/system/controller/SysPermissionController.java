package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.annotation.MyLog;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.ssjwt.SecurityUtil;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.SysPermission;
import com.wx.genealogy.system.entity.SysUser;
import com.wx.genealogy.system.service.SysPermissionService;
import com.wx.genealogy.system.service.SysUserService;
import com.wx.genealogy.system.vo.req.SysPermissionInsertReqVo;
import com.wx.genealogy.system.vo.req.SysPermissionUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @ClassName SysPermission
 * @Author hangyi
 * @Data 2020/6/17 16:12
 * @Description
 * @Version 1.0
 **/
@Api(tags = "系统权限接口")
@RestController
@RequestMapping("/sysPermission")
public class SysPermissionController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @MyLog(value = "添加权限")
    @ApiOperation("添加权限")
    @PreAuthorize("hasAuthority('sysPermission:add/sysPermission')")
    @RequestMapping(value = "/addSysPermission", method = RequestMethod.POST)
    public JsonResult addSysPermission(@RequestBody SysPermissionInsertReqVo sysPermissionInsertReqVo) throws Exception {
        if (ObjectUtil.isNull(sysPermissionInsertReqVo.getPermission())){
            throw new MissingServletRequestParameterException("permission","String");
        }
        if (ObjectUtil.isNull(sysPermissionInsertReqVo.getName())){
            throw new MissingServletRequestParameterException("name","String");
        }
        SysPermission sysPermission = new SysPermission();
        ObjectUtil.copyByName(sysPermissionInsertReqVo,sysPermission);

        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser == null){
            throw new Exception("账号不存在");
        }

        sysPermission.setCreateById(sysUser.getId());
        sysPermission.setCreateTime(LocalDateTime.now());
        sysPermission.setUpdateTime(LocalDateTime.now());
        sysPermission.setUpdateById(sysUser.getId());

        //添加权限
        return sysPermissionService.insert(sysPermission);
    }

    @MyLog(value = "删除权限")
    @ApiOperation("删除权限")
    @PreAuthorize("hasAuthority('sysPermission:delete/sysPermission')")
    @RequestMapping(value = "/deleteSysPermission{id}", method = RequestMethod.DELETE)
    public JsonResult deleteSysPermission(@PathVariable Integer id) throws Exception {
        if (ObjectUtil.isNull(id)){
            throw new MissingServletRequestParameterException("id","number");
        }
        //删除权限
        return sysPermissionService.delete(id);
    }

    @ApiOperation("查询权限")
    @PreAuthorize("hasAuthority('sysPermission:get/sysPermission')")
    @RequestMapping(value = "/querySysPermission", method = RequestMethod.GET)
    public JsonResult querySysPermission(@RequestParam(value = "limit") Integer limit,
                                         @RequestParam(value = "page") Integer page) throws Exception {
        if (ObjectUtil.isNull(limit)){
            throw new MissingServletRequestParameterException("limit","number");
        }
        if (ObjectUtil.isNull(page)){
            throw new MissingServletRequestParameterException("page","number");
        }
        HashMap<String, Object> map = sysPermissionService.queryList(page,limit);
        return ResponseUtil.ok("",map);
    }

    @MyLog(value = "修改权限")
    @ApiOperation("修改权限")
    @PreAuthorize("hasAuthority('sysPermission:update/sysPermission')")
    @RequestMapping(value = "/updateSysPermission", method = RequestMethod.PUT)
    public JsonResult updateSysPermission(@RequestBody SysPermissionUpdateReqVo sysPermissionUpdateReqVo) throws Exception {
        if (ObjectUtil.isNull(sysPermissionUpdateReqVo.getId())){
            throw new MissingServletRequestParameterException("id","number");
        }
        if (ObjectUtil.isNull(sysPermissionUpdateReqVo.getName())){
            throw new MissingServletRequestParameterException("name","String");
        }
        SysPermission sysPermission = new SysPermission();
        sysPermission.setId(sysPermissionUpdateReqVo.getId());
        sysPermission.setName(sysPermissionUpdateReqVo.getName());

        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser == null){
            throw new Exception("账号不存在");
        }
        sysPermission.setUpdateById(sysUser.getId());
        sysPermission.setUpdateTime(LocalDateTime.now());
        return sysPermissionService.update(sysPermission);
    }
}
