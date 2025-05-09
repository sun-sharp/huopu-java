package com.wx.genealogy.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wx.genealogy.system.entity.FamilyGenealogy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 家谱图 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-10-20
 */
@Mapper
public interface FamilyGenealogyDao extends BaseMapper<FamilyGenealogy> {
    int setInc(FamilyGenealogy familyGenealogy);


    List<FamilyGenealogy> selectFamilyGenealogyList(FamilyGenealogy familyGenealogySelect);

    List<FamilyGenealogy> selectFGList(FamilyGenealogy familyGenealogySelect);

    List<FamilyGenealogy> selectFG(FamilyGenealogy familyGenealogySelect);

    List<FamilyGenealogy> selectLt(FamilyGenealogy familyGenealogySelect);

    List<FamilyGenealogy> list(FamilyGenealogy familyGenealogySelect);

    List<Map<Object, Object>> countPeopleByGeneration(int familyId);

    int updateChart(FamilyGenealogy familyGenealogy);

    int updatePid(FamilyGenealogy familyGenealogy);

    Integer  selectUidMax(Integer familyId);//查询最大uid-用户后台新增

    Integer selectNameGenerationList(FamilyGenealogy familyGenealogy);//查询同一代是否重名，大于0重名

    List<FamilyGenealogy> selectNameGenerationListOne();

    int updatePidFamilyId(FamilyGenealogy familyGenealogy);

    int updatePidIsNull(FamilyGenealogy familyGenealogy);

    int updateUserIdIsNull(FamilyGenealogy familyGenealogy);

    int upFamilyGenealogyPid(FamilyGenealogy familyGenealogy);
}
