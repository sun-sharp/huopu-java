package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.SysMenu;
import com.wx.genealogy.system.entity.SysRoleMenu;
import com.wx.genealogy.system.mapper.SysMenuDao;
import com.wx.genealogy.system.mapper.SysRoleMenuDao;
import com.wx.genealogy.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-05-08
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenu> implements SysMenuService {
    @Autowired
    private SysMenuDao sysMenuDao;

    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;

    @Override
    public JsonResult insert(SysMenu sysMenu) {
        int ret =  sysMenuDao.insert(sysMenu);
        return ret == 0 ? ResponseUtil.fail("添加失败") : ResponseUtil.ok("添加成功");
    }

    @Override
    public JsonResult delete(Integer id) throws Exception {
        sysRoleMenuDao.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, id));
        int ret = sysMenuDao.deleteById(id);
        if (ret == 0){
            throw new Exception("删除失败");
        }
        return ResponseUtil.ok("删除成功");
    }

    @Override
    public HashMap<String, Object> queryList(Integer page, Integer limit) {
        Page<SysMenu> pageInfo = new Page<>(page, limit);
        pageInfo.addOrder(OrderItem.desc("create_time"));
        Page<SysMenu> sysMenuIPage = sysMenuDao.selectPage(pageInfo,null);
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("sysMenus",sysMenuIPage.getRecords());
        map.put("pages", sysMenuIPage.getPages());
        map.put("total", sysMenuIPage.getTotal());
        return map;
    }

    @Override
    public JsonResult update(SysMenu sysMenu) {
        int ret =  sysMenuDao.updateById(sysMenu);
        return ret == 0 ? ResponseUtil.fail("修改失败") : ResponseUtil.ok("修改成功");
    }
}
