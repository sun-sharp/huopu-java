package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.EssayShare;
import org.apache.ibatis.annotations.Mapper;



@Mapper

public interface EssayShareDao extends BaseMapper<EssayShare> {

    int setInc(EssayShare essayShare);

    int setDec(EssayShare essayShare);
}
