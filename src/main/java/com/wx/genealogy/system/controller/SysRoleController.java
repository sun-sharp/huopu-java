package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.annotation.MyLog;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.ssjwt.SecurityUtil;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.SysRole;
import com.wx.genealogy.system.entity.SysUser;
import com.wx.genealogy.system.service.SysRoleService;
import com.wx.genealogy.system.service.SysUserService;
import com.wx.genealogy.system.vo.req.SysRoleInsertReqVo;
import com.wx.genealogy.system.vo.req.SysRoleMenuInsertReqVo;
import com.wx.genealogy.system.vo.req.SysRolePermissionInsertReqVo;
import com.wx.genealogy.system.vo.req.SysRoleUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName SysRoleController
 * @Author hangyi
 * @Data 2020/6/19 16:55
 * @Description
 * @Version 1.0
 **/
@Api(tags = "系统角色接口")
@RestController
@RequestMapping("/sysRole")
public class SysRoleController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @MyLog(value = "添加角色")
    @ApiOperation("添加角色")
    @PreAuthorize("hasAuthority('sysRole:add/sysRole')")
    @RequestMapping(value = "/addSysMenu", method = RequestMethod.POST)
    public JsonResult addSysPermission(@RequestBody SysRoleInsertReqVo sysRoleInsertReqVo) throws Exception {
        if (ObjectUtil.isNull(sysRoleInsertReqVo.getName())){
            throw new MissingServletRequestParameterException("name","String");
        }
        SysRole sysRole = new SysRole();
        ObjectUtil.copyByName(sysRoleInsertReqVo,sysRole);

        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser == null){
            throw new Exception("账号不存在");
        }

        sysRole.setCreateById(sysUser.getId());
        sysRole.setCreateTime(LocalDateTime.now());
        sysRole.setUpdateTime(LocalDateTime.now());
        sysRole.setUpdateById(sysUser.getId());

        //添加菜单
        return sysRoleService.insert(sysRole, sysUser);
    }

    @MyLog(value = "删除角色")
    @ApiOperation("删除角色")
    @PreAuthorize("hasAuthority('sysRole:delete/sysRole')")
    @RequestMapping(value = "/deleteSysRole{id}", method = RequestMethod.DELETE)
    public JsonResult deleteSysRole(@PathVariable Integer id) throws Exception {
        if (ObjectUtil.isNull(id)){
            throw new MissingServletRequestParameterException("id","number");
        }
        //删除权限
        return sysRoleService.delete(id);
    }

    @ApiOperation("查询角色")
    @RequestMapping(value = "/querySysRole", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('sysRole:get/sysRole')")
    public JsonResult querySysRole(@RequestParam(value = "limit") Integer limit,
                                   @RequestParam(value = "page") Integer page) throws Exception {
        if (ObjectUtil.isNull(limit)){
            throw new MissingServletRequestParameterException("limit","number");
        }
        if (ObjectUtil.isNull(page)){
            throw new MissingServletRequestParameterException("page","number");
        }
        HashMap<String, Object> map = sysRoleService.queryList(page,limit);
        return ResponseUtil.ok("",map);
    }

    @MyLog(value = "修改角色")
    @ApiOperation("修改角色")
    @PreAuthorize("hasAuthority('sysRole:update/sysRole')")
    @RequestMapping(value = "/updateSysRole", method = RequestMethod.PUT)
    public JsonResult updateSysRole(@RequestBody SysRoleUpdateReqVo sysRoleUpdateReqVo) throws Exception {
        if (ObjectUtil.isNull(sysRoleUpdateReqVo.getId())){
            throw new MissingServletRequestParameterException("id","number");
        }
        if (ObjectUtil.isNull(sysRoleUpdateReqVo.getName())){
            throw new MissingServletRequestParameterException("name","String");
        }
        SysRole sysRole = new SysRole();
        sysRole.setId(sysRoleUpdateReqVo.getId());
        sysRole.setName(sysRoleUpdateReqVo.getName());

        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser == null){
            throw new Exception("账号不存在");
        }
        sysRole.setUpdateById(sysUser.getId());
        sysRole.setUpdateTime(LocalDateTime.now());

        return sysRoleService.update(sysRole);
    }

    @ApiOperation("查询角色菜单")
    @PreAuthorize("hasAuthority('sysRole:add/sysRoleMenu')")
    @RequestMapping(value = "/getSysRoleMenu", method = RequestMethod.GET)
    public JsonResult getSysRoleMenu(@RequestParam(value = "roleId") Integer roleId ) throws Exception {
        List<Integer> menuIds = sysRoleService.queryRoleMenu(roleId);
        return ResponseUtil.ok("",menuIds);
    }

    @MyLog(value = "添加角色菜单")
    @ApiOperation("添加角色菜单")
    @PreAuthorize("hasAuthority('sysRole:add/sysRoleMenu')")
    @RequestMapping(value = "/addSysRoleMenu", method = RequestMethod.POST)
    public JsonResult addSysRoleMenu(@RequestBody SysRoleMenuInsertReqVo sysRoleMenuInsertReqVo) throws Exception {
        if (ObjectUtil.isNull(sysRoleMenuInsertReqVo.getRoleId())){
            throw new MissingServletRequestParameterException("roleId","number");
        }

        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser == null){
            throw new Exception("账号不存在");
        }
        return sysRoleService.insertRoleMenu(sysRoleMenuInsertReqVo.getRoleId(),sysRoleMenuInsertReqVo.getMenuIds(),sysUser);
    }

    @ApiOperation("查询角色权限")
    @PreAuthorize("hasAuthority('sysRole:add/sysRolePermission')")
    @RequestMapping(value = "/getSysRolePermission", method = RequestMethod.GET)
    public JsonResult getSysRolePermission(@RequestParam(value = "roleId") Integer roleId ) throws Exception {
        List<Integer> PermissionIds = sysRoleService.queryRolePermission(roleId);
        return ResponseUtil.ok("",PermissionIds);
    }

    @MyLog(value = "添加角色权限")
    @ApiOperation("添加角色权限")
    @PreAuthorize("hasAuthority('sysRole:add/sysRolePermission')")
    @RequestMapping(value = "/addSysRolePermission", method = RequestMethod.POST)
    public JsonResult addSysRolePermission(@RequestBody SysRolePermissionInsertReqVo sysRolePermissionInsertReqVo) throws Exception {
        if (ObjectUtil.isNull(sysRolePermissionInsertReqVo.getRoleId())){
            throw new MissingServletRequestParameterException("roleId","number");
        }
        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser == null){
            throw new Exception("账号不存在");
        }
        return sysRoleService.insertRolePermission(sysRolePermissionInsertReqVo.getRoleId(),sysRolePermissionInsertReqVo.getPermissionIds(),sysUser);
    }
}
