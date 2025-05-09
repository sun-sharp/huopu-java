package com.wx.genealogy.system.service;

import com.wx.genealogy.system.entity.FamilyPedigreeApply;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
public interface FamilyPedigreeApplyService {

    /**
     * 查询
     *
     */
    public FamilyPedigreeApply selectFamilyPedigreeApplyByApplyId(Long applyId);

    /**
     * 查询
     *
     */
    public List<FamilyPedigreeApply> selectFamilyPedigreeApplyList(FamilyPedigreeApply familyPedigreeApply);

    /**
     * 新增
     *

     */
    public int insertFamilyPedigreeApply(FamilyPedigreeApply familyPedigreeApply);

    /**
     * 修改【请填写功能名称】
     *
     */
    public int updateFamilyPedigreeApply(FamilyPedigreeApply familyPedigreeApply);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param applyIds 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteFamilyPedigreeApplyByApplyIds(Long[] applyIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param applyId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteFamilyPedigreeApplyByApplyId(Long applyId);
}
