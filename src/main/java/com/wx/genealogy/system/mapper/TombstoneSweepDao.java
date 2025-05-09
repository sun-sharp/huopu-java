package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.TombstoneFlower;
import com.wx.genealogy.system.entity.TombstoneSweep;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TombstoneSweepDao extends BaseMapper<TombstoneSweep> {
    int setInc(TombstoneSweep tombstoneSweep);
    int setDec(TombstoneSweep tombstoneSweep);

}