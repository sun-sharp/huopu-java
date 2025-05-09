package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.FamilyGenealogyReceive;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 认领族谱图申请 服务类
 * </p>
 *
 * @author ${author}
 * @since 2023-03-03
 */
public interface FamilyGenealogyReceiveService extends IService<FamilyGenealogyReceive> {

    JsonResult fetch(Integer userId, Integer familyGenealogyId ,Integer generation,Integer familyId,String phone,String text,Integer relation);

    JsonResult selectAuditApply(Integer familyId);

    /**
     * 审核族谱图认领申请
     * @param receive
     * @return
     */
    JsonResult auditFamilyGenealogyReceive(FamilyGenealogyReceive receive);

    /**
     * 查询我的认领族谱图申请记录
     * @param receive
     * @return
     */
    JsonResult selectMyApply(FamilyGenealogyReceive receive);

    public JsonResult selectApply(FamilyGenealogyReceive receive);


    //判断是否显示按钮
    public JsonResult claim(Integer userId,Integer familyId);


    public JsonResult getGenealogy(Integer userId,Integer familyId);
}
