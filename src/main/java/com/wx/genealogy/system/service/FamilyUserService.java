package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.FamilyManageLog;
import com.wx.genealogy.system.entity.FamilyUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 家族用户关联 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@Transactional(rollbackFor = Exception.class)
public interface FamilyUserService extends IService<FamilyUser> {

    JsonResult insertFamilyUser(FamilyUser familyUser) throws Exception;

    JsonResult insertFamilyUserNoApply(FamilyUser familyUser) throws Exception;

    JsonResult updateFamilyUserById(FamilyUser familyUser, FamilyManageLog familyManageLog) throws Exception;

    JsonResult updateStatusById(FamilyUser familyUser,FamilyManageLog familyManageLog) throws Exception;

    JsonResult selectFamilyUserAndFamily(Integer page, Integer limit, FamilyUser familyUser);

    JsonResult selectFamilyUserByFamilyId(Integer page, Integer limit, Integer sort, FamilyUser familyUser);

    JsonResult updateFamilydai(Integer generation, Integer familyId, Integer userId,String puming,String hunyin,Integer relation);

    JsonResult selectManagerByFamilyId(Integer page, Integer limit, FamilyUser familyUser);
    JsonResult updateFamilyupdates(Integer id, Integer status, Integer userId ,Integer familyId,Integer generation,String genealogyName,Integer level,Integer joins);

    JsonResult updateFamilyUserByIdList(List<Integer> idList,FamilyUser familyUser,List<FamilyManageLog> familyManageLogList) throws Exception;
    JsonResult insertFamilyUserBygenertion(Integer family_userid,Integer number,Integer familyId,String genealogy ,String genealogy_two) throws Exception;
    JsonResult selectFamilyUserBygenertion(Integer genealogy, Integer familyId,Integer familyUserId)throws Exception;

    JsonResult selectFamilyUserByuserid(Integer familyId,Integer userId)throws Exception;

    List<FamilyUser> selectFamilyByUserId(FamilyUser familyUserSelect);

    JsonResult selectManagerByFamilyIdNo(Integer familyId);


    JsonResult selectStatusByFamilyId(Integer familyId);

    public JsonResult updateFamilyUp(Integer userId, Integer familyIdUserId,Integer generation,String genealogyName,Integer relation,Integer familyId);

    JsonResult cepstrumFamily(Integer userId, Integer familyId);

    JsonResult selectCepstrumFamily(Integer familyId);

    JsonResult getFamilyUserListPaging(Integer familyId,String genealogyName,Integer limit,Integer page);

    JsonResult deleteFamilyUser(FamilyUser familyUser);


}
