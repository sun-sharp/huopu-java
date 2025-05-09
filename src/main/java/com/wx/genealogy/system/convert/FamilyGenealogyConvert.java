package com.wx.genealogy.system.convert;

import com.wx.genealogy.system.entity.FamilyGenealogy;
import com.wx.genealogy.system.entity.FamilyGenealogyImg;
import com.wx.genealogy.system.vo.res.FamilyGenealogyImgResVo;
import com.wx.genealogy.system.vo.res.FamilyGenealogyTreeResVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FamilyGenealogyConvert {

    FamilyGenealogyConvert INSTANCE = Mappers.getMapper(FamilyGenealogyConvert.class);

    List<FamilyGenealogyTreeResVo> convertToTree(List<FamilyGenealogy> vo);

    @Mappings({
            @Mapping(target = "name", source = "genealogyName"),
            @Mapping(target = "ranking", source = "ranking")
    })
    FamilyGenealogyTreeResVo convertToTree(FamilyGenealogy vo);

    List<FamilyGenealogyImgResVo> convertToResVo(List<FamilyGenealogyImg> vo);

    @Mappings({
            @Mapping(target = "url", source = "vo.img"),
            @Mapping(target = "name", expression = "java(vo.getId().toString())")
    })
    FamilyGenealogyImgResVo convertToResVo(FamilyGenealogyImg vo);
}
