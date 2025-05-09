package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wx.genealogy.system.entity.DouRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 斗收入明细 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2022-07-12
 */
@Mapper
public interface DouRecordDao extends BaseMapper<DouRecord> {

    /**
     * 查询所有斗明细记录
     * @return
     */
    IPage<DouRecord> selectRiceRecordList(IPage<DouRecord> pageData, @Param(Constants.WRAPPER)QueryWrapper<DouRecord> wrapper);
}
