package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.Family;
import com.wx.genealogy.system.entity.FamilyMessage;
import com.wx.genealogy.system.service.FamilyMessageService;
import com.wx.genealogy.system.vo.req.FamilyMessageUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/familyMessage")
@Api(tags = "家族信息")
public class FamilyMessageController {
    @Autowired
    private FamilyMessageService familyMessageService;

    @ApiOperation(value = "修改家族信息")
    @RequestMapping(value = "/updateFamilyMessage", method = RequestMethod.POST)
    public JsonResult updateFamilyMessage(@RequestBody FamilyMessageUpdateReqVo familyMessageUpdateReqVo) throws Exception {
        FamilyMessage familyUpdate=new FamilyMessage();
        ObjectUtil.copyByName(familyMessageUpdateReqVo,familyUpdate);
        return familyMessageService.updateFamilyMessage(familyUpdate);
    }


    @ApiOperation(value = "修改家族信息")
    @RequestMapping(value = "/updateFamilyMessageEmpty", method = RequestMethod.POST)
    public JsonResult updateFamilyMessageEmpty(@RequestBody FamilyMessageUpdateReqVo familyMessageUpdateReqVo) throws Exception {
        FamilyMessage familyUpdate=new FamilyMessage();
        ObjectUtil.copyByName(familyMessageUpdateReqVo,familyUpdate);
        return familyMessageService.updateFamilyMessage(familyUpdate);
    }

}
