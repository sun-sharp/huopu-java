package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.TombstoneMessage;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TombstoneMessageDao extends BaseMapper<TombstoneMessage> {
    int setInc(TombstoneMessage tombstoneMessage);
    int setDec(TombstoneMessage tombstoneMessage);
}