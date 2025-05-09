package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.Family;
import com.wx.genealogy.system.entity.FamilyClean;
import com.wx.genealogy.system.entity.FamilyMailbox;
import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.service.FamilyCleanService;
import com.wx.genealogy.system.service.FamilyService;
import com.wx.genealogy.system.service.impl.FamilyMailboxServiceImpl;
import com.wx.genealogy.system.vo.req.FamilyInsertReqVo;
import com.wx.genealogy.system.vo.req.FamilyUpdateReqVo;
import com.wx.genealogy.system.vo.req.UnlockFamilyUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 家族 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/familyMailbox")
@Api(tags = "邮箱接口")
public class FamilyMailboxController {

    @Autowired
    private FamilyMailboxServiceImpl familyMailboxService;

    @ApiOperation(value = "创建家族")
    @GetMapping("/get")
    public JsonResult select(){
        return  familyMailboxService.select();
    }

    @ApiOperation(value = "创建家族")
    @RequestMapping(value = "/updateDispose", method = RequestMethod.PUT)
    public JsonResult updateFamilyMailbox(@RequestParam(value = "id") Integer id){
        FamilyMailbox familyMailbox = new FamilyMailbox();
        familyMailbox.setId(id);
        return  familyMailboxService.updateFamilyMailbox(familyMailbox);
    }


}

