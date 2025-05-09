package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wx.genealogy.common.domin.JsonResult;

import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.EssayShare;
import com.wx.genealogy.system.entity.FamilyMessage;
import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.mapper.EssayShareDao;
import com.wx.genealogy.system.mapper.FamilyMessageDao;
import com.wx.genealogy.system.mapper.FamilyUserDao;
import com.wx.genealogy.system.service.EssayShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EssayShareServicelmpl extends ServiceImpl<EssayShareDao, EssayShare> implements EssayShareService {

    @Autowired
    private EssayShareDao essayShareDao;

    @Autowired
    private FamilyUserDao familyUserDao;

    @Autowired
    private FamilyMessageDao familyMessageDao;


    @Override
    public JsonResult insertEssayShare(EssayShare essayShare) throws Exception {

        QueryWrapper<EssayShare> ess = new QueryWrapper<EssayShare>();

        ess.eq("family_id",essayShare.getFamilyId());
        ess.eq("user_id",essayShare.getUserId());
        ess.eq("essay_id",essayShare.getEssayId());
        EssayShare ess_list = essayShareDao.selectOne(ess);
        if(ess_list!=null){
            return ResponseUtil.fail("您已经分享过了");

        }


        int result=essayShareDao.insert(essayShare);


        QueryWrapper<FamilyUser> familyuser_find = new QueryWrapper<FamilyUser>();
        familyuser_find.eq("family_id",essayShare.getFamilyId());
        familyuser_find.gt("user_id",0);
        List<FamilyUser> family_find = familyUserDao.selectList(familyuser_find);
        for(int i=0;i<family_find.size();i++){
            QueryWrapper<FamilyMessage> familymessage = new QueryWrapper<FamilyMessage>();
            familymessage.eq("family_id",essayShare.getFamilyId());
            familymessage.eq("user_id",family_find.get(i).getUserId());
            FamilyMessage familydata=  familyMessageDao.selectOne(familymessage);
            if(familydata == null){
                FamilyMessage familyMessage = new FamilyMessage();
                familyMessage.setEssayMessage(1);
                familyMessage.setFamilyId(essayShare.getFamilyId());
                familyMessage.setUserId(family_find.get(i).getUserId());

                familyMessageDao.insert(familyMessage);
            }else{
                FamilyMessage familyMessage = new FamilyMessage();
                familyMessage.setId(familydata.getId());
                familyMessage.setEssayMessage(familydata.getEssayMessage()+1);
                familyMessage.setFamilyId(essayShare.getFamilyId());
                familyMessage.setUserId(family_find.get(i).getUserId());

                familyMessageDao.updateById(familyMessage);
            }
            FamilyUser fami = new FamilyUser();
            fami.setId(family_find.get(i).getId());
            fami.setUpdateTime(DateUtils.getDateTime());
            familyUserDao.updateById(fami);
        }



        if(result==0){
            throw new Exception("发布失败");
        }
        return ResponseUtil.ok("发布成功");
    }
}
