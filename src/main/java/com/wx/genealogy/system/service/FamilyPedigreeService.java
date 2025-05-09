package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.FamilyPedigree;
import com.wx.genealogy.system.vo.req.FamilyPedigreeRecursionVo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
public interface FamilyPedigreeService extends IService<FamilyPedigreeService> {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public FamilyPedigree selectFamilyPedigreeById(Long id);


    public FamilyPedigreeRecursionVo getFamilyPedigreeRecursion(Long id);


    public List<FamilyPedigreeRecursionVo> treeFamilyPedigreeRecursionVo();
    /**
     * 查询【请填写功能名称】列表
     *
     * @param familyPedigree 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<FamilyPedigree> selectFamilyPedigreeList(FamilyPedigree familyPedigree);

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
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteFamilyPedigreeByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteFamilyPedigreeById(Long id);

    public JsonResult importFamilyPedigreeByUser(MultipartFile file, String jsonString)throws Exception;

    public JsonResult getGeneration(Long id);
}
