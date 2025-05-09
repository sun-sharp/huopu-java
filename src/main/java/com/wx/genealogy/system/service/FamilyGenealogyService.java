package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.FamilyGenealogy;
import com.wx.genealogy.system.vo.req.FamilyGenealogyDeleteReqVo;
import com.wx.genealogy.system.vo.req.FamilyGenealogyInsertReqVo;
import com.wx.genealogy.system.vo.req.FamilyGenealogyIntroduceReqVo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 家谱图 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-10-20
 */
@Transactional(rollbackFor = Exception.class)
public interface FamilyGenealogyService extends IService<FamilyGenealogy> {

    JsonResult insertFamilyGenealogy(FamilyGenealogy familyGenealogy) throws Exception;

    JsonResult updateFamilyGenealogyById(FamilyGenealogy familyGenealogy) throws Exception;

    JsonResult deleteFamilyGenealogyById(FamilyGenealogy familyGenealogy) throws Exception;

    JsonResult selectFamilyGenealogy(FamilyGenealogy familyGenealogy);

    JsonResult selectAllFamilyGenealogy(FamilyGenealogy familyGenealogy);

    JsonResult deleteFamilyGenealogyBylevel(FamilyGenealogyDeleteReqVo familyGenealogyDeleteReqVo) throws Exception;

    JsonResult getFamilyGenealogyBylevel(Integer familyId, Integer familyUserId, Integer familyId1, Integer userId) throws Exception;

    JsonResult apply(FamilyGenealogy familyGenealogy);

    JsonResult selectFamilyGenealogyList(FamilyGenealogy familyGenealogySelect);

    /**
     * 审核族谱图申请
     *
     * @param genealogy
     * @return
     */
    JsonResult auditFamilyGenealogy(FamilyGenealogy genealogy);

    /**
     * 管理员修改族谱图信息
     *
     * @param familyGenealogy
     * @return
     */
    JsonResult upFamilyGenealogyInfo(FamilyGenealogy familyGenealogy);

    Boolean isAdmin(Integer userId, Integer familyId);
    //更新族谱关系图
    JsonResult upFamilyGenealogyChart(Integer familyId);


    public JsonResult importUser(MultipartFile file, Integer familyId) throws Exception;

    JsonResult selectByName(FamilyGenealogyInsertReqVo familyGenealogyInsertReqVo);

    JsonResult getFamilyGenealogyList(Integer familyId,Integer generation,String genealogyName);//查询家族同代

    JsonResult getFamilyGenealogyListNotClaim(Integer familyId,Integer generation,String genealogyName,Integer limit,Integer page);//查询家族同代


    JsonResult getFamilyGenealogyListPaging(Integer familyId,Integer generation,String genealogyName,Integer limit,Integer page);//分页查询家族信息



    JsonResult upFamilyGenealogyPid(FamilyGenealogy familyGenealogy);


    JsonResult insertData(FamilyGenealogy familyGenealogy);

    public JsonResult deriveExcle(Integer familyId,String mailbox,Integer userId);

    public JsonResult createConnect(Integer familyId ,Integer userId);

    JsonResult viewFamilyTreePay(Integer familyId ,Integer userId);

    public JsonResult selectFamilyGenealogyByGenerationId(FamilyGenealogy familyGenealogy);

    public JsonResult selectFamilyGenealogyParentList(FamilyGenealogy familyGenealogy);


    JsonResult getClaimShow(Integer userId,Integer familyId);

    JsonResult deleteFamilyGenealogy(Integer id,Integer familyId,Integer uid,Integer gUserId);

    JsonResult selectFamilyGenealogyByGeneration(FamilyGenealogy familyGenealogy,Integer limit, Integer page);

    public JsonResult cancelClaim(FamilyGenealogy familyGenealogy);

    JsonResult getFamilyGenealogyListPagingGap(Integer familyId,Integer generation,String genealogyName,Integer userId,Integer limit,Integer page,Integer generationNum);

    JsonResult getFamilyGenealogyTreeByFamilyId(Integer familyId, Integer parentId, Integer level);

    JsonResult updateResumeById(FamilyGenealogyIntroduceReqVo userIntroduceReqVo) throws Exception;
}
