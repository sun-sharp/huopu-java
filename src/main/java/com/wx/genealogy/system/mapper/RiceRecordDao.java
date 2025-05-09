package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wx.genealogy.system.entity.RiceRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 米收支明细 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-10-20
 */
@Mapper
public interface RiceRecordDao extends BaseMapper<RiceRecord> {

    int insertRiceRecordList(List<RiceRecord> riceRecordList);

    /**
     * 查询所有米记录
     * @param pageData
     * @return
     */
    IPage<RiceRecord> selectRiceRecordList(IPage<RiceRecord> pageData);
}
