package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.FamilyGenealogyImg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 个人简介图片表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2024-10-8
 */
@Mapper
public interface FamilyGenealogyImgDao extends BaseMapper<FamilyGenealogyImg> {

    int insertUserImgList(List<FamilyGenealogyImg> userImgList);

    int deleteByFamilyGenealogyId(Integer familyGenealogyId);
}
