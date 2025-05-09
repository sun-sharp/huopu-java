package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.genealogy.system.entity.Family;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 家族 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@Mapper
public interface FamilyDao extends BaseMapper<Family> {

    Page<Family> selectPageList(Page<Family> page, @Param("family") Family family);
}
