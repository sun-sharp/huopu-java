package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.FamilyPedigree;
import com.wx.genealogy.system.vo.req.FamilyPedigreeRecursionVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FamilyPedigreeDao extends BaseMapper<FamilyPedigree> {

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public FamilyPedigree selectFamilyPedigreeById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param familyPedigree 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<FamilyPedigree> selectFamilyPedigreeList(FamilyPedigree familyPedigree);

    public List<FamilyPedigreeRecursionVo> selectFamilyPedigreeRecursionVoList(FamilyPedigree familyPedigree);

    /**
     * 新增【请填写功能名称】
     *
     * @param familyPedigree 【请填写功能名称】
     * @return 结果
     */
    public int insertFamilyPedigree(FamilyPedigree familyPedigree);

    /**
     * 修改【请填写功能名称】
     *
     * @param familyPedigree 【请填写功能名称】
     * @return 结果
     */
    public int updateFamilyPedigree(FamilyPedigree familyPedigree);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteFamilyPedigreeById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFamilyPedigreeByIds(Long[] ids);


    public List<Map<String, Integer>> selectGenerationCountsById(Long id);

}
