package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.annotation.MyLog;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.ssjwt.SecurityUtil;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.SysMenu;
import com.wx.genealogy.system.entity.SysUser;
import com.wx.genealogy.system.service.SysMenuService;
import com.wx.genealogy.system.service.SysUserService;
import com.wx.genealogy.system.vo.req.SysMenuInsertReqVo;
import com.wx.genealogy.system.vo.req.SysMenuUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * @ClassName SysMenuController
 * @Author hangyi
 * @Data 2020/6/19 14:42
 * @Description
 * @Version 1.0
 **/
@Api(tags = "系统菜单接口")
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @MyLog(value = "添加菜单")
    @ApiOperation("添加菜单")
    @PreAuthorize("hasAuthority('sysMenu:add/sysMenu')")
    @RequestMapping(value = "/addSysMenu", method = RequestMethod.POST)
    public JsonResult addSysPermission(@RequestBody SysMenuInsertReqVo sysMenuInsertReqVo) throws Exception {
        if (ObjectUtil.isNull(sysMenuInsertReqVo.getName())){
            throw new MissingServletRequestParameterException("name","String");
        }
        if (ObjectUtil.isNull(sysMenuInsertReqVo.getCode())){
            throw new MissingServletRequestParameterException("code","String");
        }
        SysMenu sysMenu = new SysMenu();
        ObjectUtil.copyByName(sysMenuInsertReqVo,sysMenu);

        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser == null){
            throw new Exception("账号不存在");
        }

        sysMenu.setCreateById(sysUser.getId());
        sysMenu.setCreateTime(LocalDateTime.now());
        sysMenu.setUpdateTime(LocalDateTime.now());
        sysMenu.setUpdateById(sysUser.getId());

        //添加菜单
        return sysMenuService.insert(sysMenu);
    }

    @MyLog(value = "删除菜单")
    @ApiOperation("删除菜单")
    @PreAuthorize("hasAuthority('sysMenu:delete/sysMenu')")
    @RequestMapping(value = "/deleteSysMenu{id}", method = RequestMethod.DELETE)
    public JsonResult deleteSysMenu(@PathVariable Integer id) throws Exception {
        if (ObjectUtil.isNull(id)){
            throw new MissingServletRequestParameterException("id","number");
        }
        //删除权限
        return sysMenuService.delete(id);
    }

    @ApiOperation("查询菜单")
    @RequestMapping(value = "/querySysMenu", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('sysMenu:get/sysMenu')")
    public JsonResult querySysMenu(@RequestParam(value = "limit") Integer limit,
                                         @RequestParam(value = "page") Integer page) throws Exception {
        if (ObjectUtil.isNull(limit)){
            throw new MissingServletRequestParameterException("limit","number");
        }
        if (ObjectUtil.isNull(page)){
            throw new MissingServletRequestParameterException("page","number");
        }
        HashMap<String, Object> map = sysMenuService.queryList(page,limit);
        return ResponseUtil.ok("",map);
    }

    @MyLog(value = "修改菜单")
    @ApiOperation("修改菜单")
    @PreAuthorize("hasAuthority('sysMenu:update/sysMenu')")
    @RequestMapping(value = "/updateSysMenu", method = RequestMethod.PUT)
    public JsonResult updateSysMenu(@RequestBody SysMenuUpdateReqVo sysMenuUpdateReqVo) throws Exception {
        if (ObjectUtil.isNull(sysMenuUpdateReqVo.getId())){
            throw new MissingServletRequestParameterException("id","number");
        }
        if (ObjectUtil.isNull(sysMenuUpdateReqVo.getName())){
            throw new MissingServletRequestParameterException("name","String");
        }
        SysMenu sysMenu = new SysMenu();
        sysMenu.setId(sysMenuUpdateReqVo.getId());
        sysMenu.setName(sysMenuUpdateReqVo.getName());
        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser == null){
            throw new Exception("账号不存在");
        }
        sysMenu.setUpdateById(sysUser.getId());
        sysMenu.setUpdateTime(LocalDateTime.now());
        return sysMenuService.update(sysMenu);
    }
}
