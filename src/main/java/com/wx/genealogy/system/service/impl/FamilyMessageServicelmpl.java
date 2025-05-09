package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.FamilyMessage;
import com.wx.genealogy.system.mapper.FamilyMessageDao;
import com.wx.genealogy.system.service.FamilyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FamilyMessageServicelmpl extends ServiceImpl<FamilyMessageDao, FamilyMessage> implements FamilyMessageService {
    @Autowired
    private FamilyMessageDao familyMessageDao;
    @Override
    public JsonResult updateFamilyMessage(FamilyMessage familyMessage) throws Exception {
        QueryWrapper<FamilyMessage> familymessagedata = new QueryWrapper<FamilyMessage>();
        familymessagedata.eq("user_id",familyMessage.getUserId());
        familymessagedata.eq("family_id",familyMessage.getFamilyId());
        System.out.println("====================>");
        System.out.println(familyMessage);
       int result = familyMessageDao.update(familyMessage,familymessagedata);
        if (result == 0) {
            throw new Exception("修改失败");
        }
        return ResponseUtil.ok("修改成功");
    }

    }
