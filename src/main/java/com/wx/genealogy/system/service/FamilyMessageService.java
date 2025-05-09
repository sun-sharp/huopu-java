package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;

import com.wx.genealogy.system.entity.FamilyMessage;
import org.springframework.transaction.annotation.Transactional;


@Transactional(rollbackFor = Exception.class)
public interface FamilyMessageService extends IService<FamilyMessage> {
    JsonResult updateFamilyMessage(FamilyMessage familyMessage)throws  Exception;
}