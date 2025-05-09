package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 角色权限表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-05-08
 */
@Mapper
public interface SysRolePermissionDao extends BaseMapper<SysRolePermission> {
    List<Integer> queryRolePermission(Integer roleId);
}
