package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.SysPermission;
import com.wx.genealogy.system.entity.SysRolePermission;
import com.wx.genealogy.system.mapper.SysPermissionDao;
import com.wx.genealogy.system.mapper.SysRolePermissionDao;
import com.wx.genealogy.system.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-08
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionDao, SysPermission> implements SysPermissionService {
    @Autowired
    private SysPermissionDao sysPermissionDao;

    @Autowired
    private SysRolePermissionDao sysRolePermissionDao;

    @Override
    public JsonResult insert(SysPermission sysPermission) {
        int ret =  sysPermissionDao.insert(sysPermission);
        return ret == 0 ? ResponseUtil.fail("添加失败") : ResponseUtil.ok("添加成功");
    }

    @Override
    public JsonResult delete(Integer id) throws Exception {
        sysRolePermissionDao.delete(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getPermissionId, id));
        int ret = sysPermissionDao.deleteById(id);
        return ret == 0 ? ResponseUtil.fail("删除失败") : ResponseUtil.ok("删除成功");
    }

    @Override
    public HashMap<String, Object> queryList(Integer page, Integer limit) {
        Page<SysPermission> pageInfo = new Page<>(page, limit);
        pageInfo.addOrder(OrderItem.desc("create_time"));
        Page<SysPermission> sysPermissionIPage = sysPermissionDao.selectPage(pageInfo, null);
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("sysPermissions",sysPermissionIPage.getRecords());
        map.put("pages", pageInfo.getPages());
        map.put("total", pageInfo.getTotal());
        return map;
    }

    @Override
    public JsonResult update(SysPermission sysPermission) {
        int ret = sysPermissionDao.updateById(sysPermission);
        return ret == 0 ? ResponseUtil.fail("修改失败") : ResponseUtil.ok("修改成功");
    }
}
