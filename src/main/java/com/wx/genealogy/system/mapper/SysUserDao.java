package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.genealogy.system.entity.SysUser;
import com.wx.genealogy.system.vo.req.SysUserQueryListReqVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-05-08
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUser> {

    Page<SysUser> queryList(Page<SysUser> page, @Param("sysUserQueryListReqVo")SysUserQueryListReqVo sysUserQueryListReqVo);

    List<SysUser> selectByStatus(@Param("status")int status, @Param("delFlag")Integer delFlag);

    SysUser selectByPrimaryKey1(@Param("userId")Integer userId);
}
