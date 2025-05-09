package com.wx.genealogy.system.mapper;

import com.wx.genealogy.system.entity.FamilyPedigreeApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FamilyPedigreeApplyDao {



    //根据id查询
    public FamilyPedigreeApply selectFamilyPedigreeApplyByApplyId(Long applyId);

    //查询
    public List<FamilyPedigreeApply> selectFamilyPedigreeApplyList(FamilyPedigreeApply familyPedigreeApply);

    //新增
    public int insertFamilyPedigreeApply(FamilyPedigreeApply familyPedigreeApply);

    //修改
    public int updateFamilyPedigreeApply(FamilyPedigreeApply familyPedigreeApply);

    //删除
    public int deleteFamilyPedigreeApplyByApplyId(Long applyId);

    //批量删除
    public int deleteFamilyPedigreeApplyByApplyIds(Long[] applyIds);
}
