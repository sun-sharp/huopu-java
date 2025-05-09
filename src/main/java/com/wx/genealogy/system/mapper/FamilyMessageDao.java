package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.FamilyMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FamilyMessageDao extends BaseMapper<FamilyMessage> {
    int setInc(FamilyMessage familyMessage);
    int setDec(FamilyMessage familyMessage);

    int updateFamilyMessage(FamilyMessage familyMessagedc);

    int updateFamilyMessageEmpty(FamilyMessage familyMessagedc);

    int updateFamilyMessageEmpty2(FamilyMessage familyMessagedc);

}