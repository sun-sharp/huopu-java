package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.FamilyMailbox;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FamilyMailboxDao extends BaseMapper<FamilyMailbox> {


    public int updateFamilyMailboxById(FamilyMailbox familyMailbox);
}
