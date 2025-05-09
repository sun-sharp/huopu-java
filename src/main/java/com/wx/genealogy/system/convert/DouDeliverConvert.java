package com.wx.genealogy.system.convert;

import com.wx.genealogy.system.entity.DouDeliver;
import com.wx.genealogy.system.vo.req.DouDeliverReqVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DouDeliverConvert {

    DouDeliverConvert INSTANCE = Mappers.getMapper(DouDeliverConvert.class);

    DouDeliver convert(DouDeliverReqVo vo);
}
