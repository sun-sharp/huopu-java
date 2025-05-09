package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 角色菜单表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-05-08
 */
@Mapper
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenu> {
    List<SysRoleMenu> selectMenus(SysRoleMenu sysRoleMenu);

    List<Integer> queryRoleMenu(Integer roleId);
}
