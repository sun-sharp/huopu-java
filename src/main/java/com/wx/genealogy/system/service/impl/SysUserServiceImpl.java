package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.mapper.*;
import com.wx.genealogy.system.service.SysUserService;
import com.wx.genealogy.system.vo.req.SysUserQueryListReqVo;
import com.wx.genealogy.system.vo.res.SysUserInfoResVo;
import com.wx.genealogy.system.vo.res.SysUserMenuInfoResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-08
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysRoleUserDao sysRoleUserDao;

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    @Autowired
    private SysMenuDao sysMenuDao;

    @Autowired
    private SysRoleDao sysRoleDao;


    @Override
    public SysUser selectUserByUserName(String userName) {
        return sysUserDao.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, userName));
    }

    @Override
    public JsonResult insertSysUser(SysUser sysUser, Integer roleId) throws Exception {
        insertSysUser1(sysUser, roleId);
        return ResponseUtil.ok("添加成功");
    }

    @Override
    public int insertSysUser1(SysUser sysUser, Integer roleId) throws Exception {
        int ret = sysUserDao.insert(sysUser);
        if (ret != 0) {
            //添加系统角色
            SysRoleUser sysRoleUser = new SysRoleUser();
            sysRoleUser.setUserId(sysUser.getId());
            sysRoleUser.setCreateById(sysUser.getCreatedById());
            sysRoleUser.setCreateTime(sysUser.getCreateTime());
            sysRoleUser.setRoleId(roleId);
            ret = sysRoleUserDao.insert(sysRoleUser);
        }
        if (ret == 0) {
            throw new Exception("添加失败");
        }
        return ret;
    }

    @Override
    public SysUserMenuInfoResVo getInfo(SysUser sysUser) {
        //查询用户角色
        SysRoleUser sysRoleUser = new SysRoleUser();
        sysRoleUser.setUserId(sysUser.getId());
        SysRoleUser sysRoleUser1 = sysRoleUserDao.selectOne(new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, sysUser.getId()));

        //查询角色菜单
        SysRoleMenu sysRoleMenu = new SysRoleMenu();
        sysRoleMenu.setRoleId(sysRoleUser1.getRoleId());
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuDao.selectMenus(sysRoleMenu);
        String[] menus = new String[sysRoleMenus.size()];
        int i = 0;
        for (SysRoleMenu sysRoleMenu1 : sysRoleMenus) {
            //查询菜单标识
            SysMenu menu = sysMenuDao.selectById(sysRoleMenu1.getMenuId());
            menus[i] = menu.getCode();
            i++;
        }
        SysUserMenuInfoResVo sysUserMenuInfoResVo = new SysUserMenuInfoResVo();
        sysUserMenuInfoResVo.setNick_name(sysUser.getNickName());
        sysUserMenuInfoResVo.setPermissions(menus);
        sysUserMenuInfoResVo.setSysRole(sysRoleDao.selectById(sysUser.getId()));
        return sysUserMenuInfoResVo;
    }

    @Override
    public JsonResult update(SysUser sysUser) throws Exception {
        int ret = sysUserDao.updateById(sysUser);
        return ret == 0 ? ResponseUtil.fail("操作失败") : ResponseUtil.ok("操作成功");
    }

    @Override
    public JsonResult updateInfo(SysUser sysUser, Integer roleId) throws Exception {
        int ret = sysUserDao.updateById(sysUser);
        if (ret != 0) {
            //修改用户角色
            //判断用户是否有系统角色
            int cont = sysRoleUserDao.selectCount(new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, sysUser.getId()));
            if (cont != 0) {
                ret = sysRoleUserDao.delete(new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, sysUser.getId()));
            }
            if (ret != 0) {
                SysRoleUser sysRoleUser = new SysRoleUser();
                sysRoleUser.setUserId(sysUser.getId());
                sysRoleUser.setRoleId(roleId);
                sysRoleUser.setCreateTime(sysUser.getUpdateTime());
                sysRoleUser.setCreateById(sysUser.getUpdateById());
                ret = sysRoleUserDao.insert(sysRoleUser);

            }
        }
        if (ret == 0) {
            throw new Exception("修改失败");
        }
        return ResponseUtil.ok("修改成功");
    }


    @Override
    public JsonResult updatePassword(SysUser sysUser) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //String password = DigestUtils.md5DigestAsHex("!@#Asun135".getBytes(StandardCharsets.UTF_8));
        sysUser.setPassword(bCryptPasswordEncoder.encode("!@#Asun135"));
        int ret = sysUserDao.updateById(sysUser);
        return ret == 0 ? ResponseUtil.fail("重置失败") : ResponseUtil.ok("重置成功,重置密码为:!@#Asun135");
    }

    @Override
    public JsonResult queryAllList() {
        List<SysUser> sysUserList = sysUserDao.selectByStatus(1, 1);
        return ResponseUtil.ok("获取成功", sysUserList);
    }

    @Override
    public HashMap<String, Object> queryList(SysUserQueryListReqVo sysUserQueryListReqVo) {
        Page<SysUser> pageInfo = new Page<>(sysUserQueryListReqVo.getPage(), sysUserQueryListReqVo.getLimit());
        Page<SysUser> sysUserIPage = sysUserDao.queryList(pageInfo, sysUserQueryListReqVo);
        List<SysUserInfoResVo> sysUserInfoResVos = new ArrayList<>();
        //查询系统用户角色
        for (SysUser sysUser : sysUserIPage.getRecords()) {
            SysRoleUser sysRoleUser = sysRoleUserDao.selectOne(new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, sysUser.getId()));
            SysUserInfoResVo sysUserInfoResVo = new SysUserInfoResVo();
            ObjectUtil.copyByName(sysUser, sysUserInfoResVo);
            if (sysRoleUser != null) {
                SysRole sysRole = sysRoleDao.selectById(sysRoleUser.getRoleId());
                sysUserInfoResVo.setRoleName(sysRole.getName());
            }
            sysUserInfoResVos.add(sysUserInfoResVo);
        }
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("sysUsers", sysUserInfoResVos);
        map.put("pages", pageInfo.getPages());
        map.put("total", pageInfo.getTotal());
        return map;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //String password = DigestUtils.md5DigestAsHex("!@#Asun135".getBytes(StandardCharsets.UTF_8));
        System.out.println(bCryptPasswordEncoder.encode("!@#Asun135"));
    }
}
