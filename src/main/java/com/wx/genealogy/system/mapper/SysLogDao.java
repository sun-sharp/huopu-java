package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.genealogy.system.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-05-12
 */
@Transactional(rollbackFor = Exception.class)
@Mapper
public interface SysLogDao extends BaseMapper<SysLog> {

    Page<SysLog> sysUserGetLog(Page<SysLog> pageInfo, @Param("map") HashMap<String, Object> map);
}
