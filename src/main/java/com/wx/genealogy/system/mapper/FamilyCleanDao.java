package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.FamilyClean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FamilyCleanDao extends BaseMapper<FamilyClean> {
    int setInc(FamilyClean familyClean);

    int setDec(FamilyClean familyClean);

}
