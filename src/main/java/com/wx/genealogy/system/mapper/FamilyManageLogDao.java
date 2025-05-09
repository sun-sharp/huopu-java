package com.wx.genealogy.system.mapper;

import com.wx.genealogy.system.entity.FamilyManageLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 家族管理日志 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-10-12
 */
@Mapper
public interface FamilyManageLogDao extends BaseMapper<FamilyManageLog> {

    int insertFamilyManageLogList(List<FamilyManageLog> familyManageLogList);
}
