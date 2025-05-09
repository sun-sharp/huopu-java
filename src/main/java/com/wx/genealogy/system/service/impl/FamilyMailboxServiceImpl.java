package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.FamilyMailbox;
import com.wx.genealogy.system.mapper.FamilyMailboxDao;
import com.wx.genealogy.system.service.FamilyMailboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FamilyMailboxServiceImpl extends ServiceImpl<FamilyMailboxDao, FamilyMailbox> implements FamilyMailboxService {


    @Autowired
    private FamilyMailboxDao familyMailboxDao;


    public JsonResult select() {
        QueryWrapper<FamilyMailbox> queryWrapper = new QueryWrapper<>();

        queryWrapper.orderByDesc("apply_time");

        return ResponseUtil.ok("查询成功：",familyMailboxDao.selectList(queryWrapper));
    }


    public JsonResult updateFamilyMailbox(FamilyMailbox familyMailbox) {
        familyMailboxDao.updateFamilyMailboxById(familyMailbox);
        return ResponseUtil.ok("修改成功：");
    }


}
