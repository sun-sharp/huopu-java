package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.mapper.*;
import com.wx.genealogy.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-08
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    @Autowired
    private SysRolePermissionDao sysRolePermissionDao;

    @Autowired
    private SysRoleUserDao sysRoleUserDao;

    @Autowired
    private SysPermissionDao sysPermissionDao;

    @Override
    public JsonResult insert(SysRole sysRole, SysUser sysUser) throws Exception {
        int ret = sysRoleDao.insert(sysRole);
        //查询所有权限
        List<SysPermission> sysPermissionList = sysPermissionDao.selectList(null);
        if (!sysPermissionList.isEmpty()) {
            for (SysPermission permission : sysPermissionList) {
                SysRolePermission sysRolePermission1 = new SysRolePermission();
                sysRolePermission1.setRoleId(sysRole.getId());
                sysRolePermission1.setPermissionId(permission.getId());
                sysRolePermission1.setCreateById(sysUser.getId());
                sysRolePermission1.setCreateTime(sysUser.getCreateTime());
                ret = sysRolePermissionDao.insert(sysRolePermission1);
                if (ret == 0) {
                    throw new Exception("添加失败");
                }
            }
        }
        return ret == 0 ? ResponseUtil.fail("添加失败") : ResponseUtil.ok("添加成功");
    }

    @Override
    public JsonResult delete(Integer id) {
        sysRolePermissionDao.delete(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, id));

        sysRoleMenuDao.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));

        sysRoleUserDao.delete(new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getRoleId, id));

        int ret = sysRoleDao.deleteById(id);
        return ret == 0 ? ResponseUtil.fail("删除失败") : ResponseUtil.ok("删除成功");
    }

    @Override
    public HashMap<String, Object> queryList(Integer page, Integer limit) {
        Page<SysRole> pageInfo = new Page<>(page, limit);
        pageInfo.addOrder(OrderItem.desc("create_time"));
        Page<SysRole> sysMenuIPage = sysRoleDao.selectPage(pageInfo,null);
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("sysMenus", sysMenuIPage.getRecords());
        map.put("pages", pageInfo.getPages());
        map.put("total", pageInfo.getTotal());
        return map;
    }

    @Override
    public JsonResult update(SysRole sysRole) {
        int ret = sysRoleDao.updateById(sysRole);
        return ret == 0 ? ResponseUtil.fail("修改失败") : ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult insertRoleMenu(Integer roleId, List<Integer> menuIds, SysUser sysUser) throws Exception {
        //删除角色的菜单
        sysRoleMenuDao.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        if (ObjectUtil.isNotNull(menuIds)) {
            for (Integer menuId : menuIds) {
                SysRoleMenu sysRoleMenu1 = new SysRoleMenu();
                sysRoleMenu1.setRoleId(roleId);
                sysRoleMenu1.setMenuId(menuId);
                sysRoleMenu1.setCreateById(sysUser.getId());
                sysRoleMenu1.setCreateTime(sysUser.getCreateTime());
                int ret = sysRoleMenuDao.insert(sysRoleMenu1);
                if (ret == 0) {
                    throw new Exception("添加失败");
                }
            }
        }
        return ResponseUtil.ok("添加成功");
    }

    @Override
    public JsonResult insertRolePermission(Integer roleId, List<Integer> permissionIds, SysUser sysUser) throws Exception {
        //删除角色的权限
        sysRolePermissionDao.delete(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, roleId));
        if (ObjectUtil.isNotNull(permissionIds)) {
            for (Integer permissionId : permissionIds) {
                SysRolePermission sysRolePermission1 = new SysRolePermission();
                sysRolePermission1.setRoleId(roleId);
                sysRolePermission1.setPermissionId(permissionId);
                sysRolePermission1.setCreateById(sysUser.getId());
                sysRolePermission1.setCreateTime(sysUser.getCreateTime());
                int ret = sysRolePermissionDao.insert(sysRolePermission1);
                if (ret == 0) {
                    throw new Exception("添加失败");
                }
            }
        }

        return ResponseUtil.ok("添加成功");
    }

    @Override
    public List<Integer> queryRoleMenu(Integer roleId) {
        return sysRoleMenuDao.queryRoleMenu(roleId);
    }

    @Override
    public List<Integer> queryRolePermission(Integer roleId) {
        return sysRolePermissionDao.queryRolePermission(roleId);
    }
}
