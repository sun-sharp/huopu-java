package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.Tombstone;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface TombstoneDao extends BaseMapper<Tombstone> {
    int setInc(Tombstone tombstone);

    int setDec(Tombstone tombstone);

}
