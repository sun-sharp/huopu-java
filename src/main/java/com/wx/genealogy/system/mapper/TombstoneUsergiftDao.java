package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.TombstoneMessage;
import com.wx.genealogy.system.entity.TombstoneUsergift;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TombstoneUsergiftDao extends BaseMapper<TombstoneUsergift> {
    int setInc(TombstoneUsergift tombstoneUsergift);
    int setDec(TombstoneUsergift tombstoneUsergift);
}
