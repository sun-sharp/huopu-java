package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.FamilyPedigreeApply;
import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.FamilyPedigreeApplyService;
import com.wx.genealogy.system.service.FamilyUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/familyPedigreeApply")
@Api(tags = "族谱")
public class FamilyPedigreeApplyController {


    @Autowired
    public FamilyPedigreeApplyService familyPedigreeApplyService;

    @Autowired
    public UserDao userDao;

    @ApiOperation(value = "获取申请列表")
    @GetMapping("/getList")
    public JsonResult getGeneration(FamilyPedigreeApply familyPedigreeApply)  {
        return  ResponseUtil.ok("获取成功",familyPedigreeApplyService.selectFamilyPedigreeApplyList(familyPedigreeApply));
    }


    @ApiOperation(value = "新增申请")
    @GetMapping("/add")
    public JsonResult add(FamilyPedigreeApply familyPedigreeApply)  {
        Date date = new Date();
        familyPedigreeApply.setTime(date);

        return  ResponseUtil.ok("已申请",familyPedigreeApplyService.selectFamilyPedigreeApplyList(familyPedigreeApply));
    }

    @ApiOperation(value = "修改申请")
    @PutMapping("/update")
    public JsonResult update(@RequestBody FamilyPedigreeApply familyPedigreeApply)  {
        return  ResponseUtil.ok("已修改",familyPedigreeApplyService.updateFamilyPedigreeApply(familyPedigreeApply));
    }


}
