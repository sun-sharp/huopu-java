package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.annotation.MyLog;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.ssjwt.JwtTokenUtil;
import com.wx.genealogy.common.ssjwt.SecurityUtil;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.SysUser;
import com.wx.genealogy.system.service.SysUserService;
import com.wx.genealogy.system.vo.req.*;
import com.wx.genealogy.system.vo.res.SysUserMenuInfoResVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-08
 */
@RestController
@RequestMapping("/system/sysUser")
public class SysUserController {

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "登陆")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public JsonResult login(@RequestParam(value = "password") String password,
                            @RequestParam(value = "userName") String userName) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        if (userDetails == null) {
            return ResponseUtil.fail("用户名不存在或账户被冻结");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (bCryptPasswordEncoder.matches(password, userDetails.getPassword())) {
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseUtil.buildResult(200, "登陆成功", "Bearer " + token);
        } else {
            return ResponseUtil.fail("密码错误");
        }
    }

    @ApiOperation(value = "获取系统用户自己信息")
    @RequestMapping(value = "/getMyInfo", method = RequestMethod.GET)
    public JsonResult getMyInfo() {
        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        SysUserMenuInfoResVo sysUserMenuInfoResVo = sysUserService.getInfo(sysUser);
        return ResponseUtil.ok("", sysUserMenuInfoResVo);
    }

    @MyLog(value = "修改系统用户自己信息")
    @ApiOperation(value = "修改系统用户自己信息")
    @RequestMapping(value = "/updateMyInfo", method = RequestMethod.PUT)
    public JsonResult updateMyInfo(@RequestBody SysUserUpdateMyReqVo sysUserUpdateMyReqVo) throws Exception {

        SysUser sysUser = new SysUser();

        ObjectUtil.copyByName(sysUserUpdateMyReqVo, sysUser);
        SysUser sysUser1 = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser1 == null) {
            throw new Exception("账号不存在");
        }
        sysUser.setUpdateById(sysUser1.getId());
        sysUser.setUpdateTime(LocalDateTime.now());
        sysUser.setId(sysUser1.getId());
        return sysUserService.update(sysUser);
    }


    @MyLog(value = "修改系统用户自己密码")
    @ApiOperation(value = "修改系统用户自己密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public JsonResult updatePassword(@RequestBody UpdatePasswordReqVo updatePasswordReqVo) throws Exception {
        SysUser sysUser1 = sysUserService.selectUserByUserName(Optional.ofNullable(updatePasswordReqVo.getUserName()).orElse(SecurityUtil.getUserName()));
        if (sysUser1 == null) {
            throw new Exception("账号不存在");
        }
        if (updatePasswordReqVo.getNewPassword() == null || updatePasswordReqVo.getNewPassword() == "") {
            throw new MissingServletRequestParameterException("newPassword", "String");
        }
        if (updatePasswordReqVo.getOldPassword() == null || updatePasswordReqVo.getOldPassword() == "") {
            throw new MissingServletRequestParameterException("oldPassword", "String");
        }
        //判断旧密码是否相同
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(updatePasswordReqVo.getOldPassword(), sysUser1.getPassword())) {
            return ResponseUtil.fail("密码错误");
        }

        SysUser sysUser = new SysUser();
        sysUser.setId(sysUser1.getId());
        sysUser.setPassword(bCryptPasswordEncoder.encode(updatePasswordReqVo.getNewPassword()));
        sysUser.setUpdateById(sysUser1.getId());
        sysUser.setUpdateTime(LocalDateTime.now());
        return sysUserService.update(sysUser);
    }

    @MyLog(value = "添加系统用户")
    @ApiOperation(value = "添加系统用户")
    @PreAuthorize("hasAuthority('sysUser:add/sysUser')")
    @RequestMapping(value = "/addSysUser", method = RequestMethod.POST)
    public JsonResult addSysUser(@RequestBody SysUserInsertReqVo sysUserInsertReqVo) throws Exception {
        if (ObjectUtil.checkObjFieldIsNull(sysUserInsertReqVo)) {
            throw new MissingServletRequestParameterException("", "");
        }
        SysUser sysUser = new SysUser();
        ObjectUtil.copyByName(sysUserInsertReqVo, sysUser);
        //判断账号是否重复
        SysUser sysUser1 = sysUserService.selectUserByUserName(sysUser.getUserName());
        if (sysUser1 != null) {
            return ResponseUtil.fail("账号已经存在");
        }
        sysUser1 = new SysUser();
        sysUser1.setUserName(sysUser.getUserName());
        sysUser1.setNickName(sysUser.getNickName());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        sysUser1.setPassword(bCryptPasswordEncoder.encode(sysUserInsertReqVo.getPassword()));

        SysUser sysUser2 = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser2 == null) {
            throw new Exception("账号不存在");
        }

        sysUser1.setCreateTime(LocalDateTime.now());
        sysUser1.setCreatedById(sysUser2.getId());
        sysUser1.setUpdateTime(LocalDateTime.now());
        sysUser1.setUpdateById(sysUser2.getId());
        //增加
        return sysUserService.insertSysUser(sysUser1, sysUserInsertReqVo.getRoleId());
    }

    @MyLog(value = "删除系统用户")
    @ApiOperation(value = "删除系统用户")
    @PreAuthorize("hasAuthority('sysUser:delete/sysUser')")
    @RequestMapping(value = "/deleteSysUser/{id}", method = RequestMethod.DELETE)
    public JsonResult deleteSysUser(@PathVariable Integer id) throws Exception {
        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser == null) {
            throw new Exception("账号不存在");
        }
        SysUser sysUser1 = new SysUser();
        sysUser1.setId(id);
        sysUser1.setUpdateTime(LocalDateTime.now());
        sysUser1.setUpdateById(sysUser.getId());
        sysUser1.setDelFlag(2);
        return sysUserService.update(sysUser1);
    }

    @MyLog(value = "冻结系统用户")
    @ApiOperation(value = "冻结系统用户")
    @PreAuthorize("hasAuthority('sysUser:update/sysUser')")
    @RequestMapping(value = "/frozenSysUser/{id}", method = RequestMethod.PUT)
    public JsonResult frozenSysUser(@PathVariable Integer id) throws Exception {
        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser == null) {
            throw new Exception("账号不存在");
        }
        if (id == null) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        SysUser sysUser1 = new SysUser();
        sysUser1.setId(id);
        sysUser1.setUpdateTime(LocalDateTime.now());
        sysUser1.setUpdateById(sysUser.getId());
        sysUser1.setStatus(2);
        return sysUserService.update(sysUser1);
    }

    @MyLog(value = "解冻系统用户")
    @ApiOperation(value = "解冻系统用户")
    @PreAuthorize("hasAuthority('sysUser:update/sysUser')")
    @RequestMapping(value = "/cancelFrozenSysUser/{id}", method = RequestMethod.PUT)
    public JsonResult cancelFrozenSysUser(@PathVariable Integer id) throws Exception {
        SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser == null) {
            throw new Exception("账号不存在");
        }
        if (id == null) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        SysUser sysUser1 = new SysUser();
        sysUser1.setId(id);
        sysUser1.setUpdateTime(LocalDateTime.now());
        sysUser1.setUpdateById(sysUser.getId());
        sysUser1.setStatus(1);
        return sysUserService.update(sysUser1);
    }

    @MyLog(value = "修改系统用户")
    @ApiOperation(value = "修改系统用户")
    @PreAuthorize("hasAuthority('sysUser:update/sysUser')")
    @RequestMapping(value = "/updateInfo", method = RequestMethod.PUT)
    public JsonResult updateInfo(@RequestBody SysUserUpdateReqVo sysUserUpdateReqVo) throws Exception {
        SysUser sysUser = new SysUser();
        if (ObjectUtil.checkPartObjFieldIsNull(sysUserUpdateReqVo, "headImgUrl")) {
            return ResponseUtil.fail("参数不全");
        }
        ObjectUtil.copyByName(sysUserUpdateReqVo, sysUser);
        SysUser sysUser1 = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser1 == null) {
            throw new Exception("账号不存在");
        }
        sysUser.setUpdateById(sysUser1.getId());
        sysUser.setUpdateTime(LocalDateTime.now());
        return sysUserService.updateInfo(sysUser, sysUserUpdateReqVo.getRoleId());
    }

    @MyLog(value = "重置系统用户密码")
    @ApiOperation(value = "重置系统用户密码")
    @PreAuthorize("hasAuthority('sysUser:update/sysUser')")
    @RequestMapping(value = "/resetPassword/{id}", method = RequestMethod.PUT)
    public JsonResult resetPassword(@PathVariable Integer id) throws Exception {
        SysUser sysUser1 = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
        if (sysUser1 == null) {
            throw new Exception("账号不存在");
        }
        if (id == null) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(id);

        sysUser.setUpdateById(sysUser1.getId());
        sysUser.setUpdateTime(LocalDateTime.now());

        return sysUserService.updatePassword(sysUser);
    }

    @ApiOperation(value = "查询系统用户")
    @PreAuthorize("hasAuthority('sysUser:get/sysUser')")
    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    public JsonResult queryList(@RequestBody SysUserQueryListReqVo sysUserQueryListReqVo) throws Exception {
        if (ObjectUtil.isNull(sysUserQueryListReqVo.getPage())) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(sysUserQueryListReqVo.getLimit())) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        HashMap<String, Object> map = sysUserService.queryList(sysUserQueryListReqVo);

        return ResponseUtil.ok("成功", map);
    }

    @ApiOperation(value = "查询所有系统用户")
    @PreAuthorize("hasAnyAuthority('sysUser:get/sysUser','department:add/department')")
    @RequestMapping(value = "/queryAllList", method = RequestMethod.GET)
    public JsonResult queryAllList() throws Exception {

        return sysUserService.queryAllList();
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String a = bCryptPasswordEncoder.encode("123456");
        System.out.println("a=" + a);
        String b = bCryptPasswordEncoder.encode("123456");
        System.out.println("b=" + b);
        System.out.println(bCryptPasswordEncoder.matches(a, b));

    }
}

