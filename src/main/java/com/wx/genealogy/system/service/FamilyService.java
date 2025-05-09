package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.Family;
import com.wx.genealogy.system.entity.FamilyUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 家族 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@Transactional(rollbackFor = Exception.class)
public interface FamilyService extends IService<Family> {

    JsonResult insertFamily(Family family,FamilyUser familyUser) throws Exception;

    JsonResult updateFamilyById(Family family) throws Exception;

    JsonResult selectFamilyByName(Integer page,Integer limit,String name);

    JsonResult findFamilyByIdAndUserId(Integer id,Integer userId,Integer authorId, Integer familyGenealogyId);
    JsonResult deleteFamilyById(Integer id,Integer userId);
    JsonResult selectFamily(Integer page,Integer limit,Family family);



}
