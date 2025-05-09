package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.Tombstone;
import com.wx.genealogy.system.entity.TombstoneFlower;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TombstoneFlowerDao extends BaseMapper<TombstoneFlower> {
    int setInc(TombstoneFlower tombstoneFlower);
    int setDec(TombstoneFlower tombstoneFlower);

}