package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.FamilyManageLog;
import com.wx.genealogy.system.service.FamilyManageLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 家族管理日志 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-10-12
 */
@RestController
@Api(tags = "家族管理日志接口")
@RequestMapping("/familyManageLog")
public class FamilyManageLogController {

    @Autowired
    private FamilyManageLogService familyManageLogService;

    @ApiOperation(value = "根据条件分页查询")
    @RequestMapping(value = "/selectFamilyManageLog", method = RequestMethod.GET)
    public JsonResult selectFamilyManageLog(@RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "page") Integer page,
                                            @RequestParam(value = "familyId") Integer familyId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }
        FamilyManageLog familyManageLog = new FamilyManageLog();
        familyManageLog.setFamilyId(familyId);
        return familyManageLogService.selectFamilyManageLog(page ,limit,familyManageLog);
    }

    @ApiOperation(value = "根据条件分页查询")
    @RequestMapping(value = "/selectFamilyManageLogs", method = RequestMethod.GET)
    public JsonResult selectFamilyManageLogs(@RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "page") Integer page,
                                            @RequestParam(value = "familyId") Integer familyId,
                                             @RequestParam(value = "userId") Integer userId
    ) throws Exception {

        FamilyManageLog familyManageLog = new FamilyManageLog();
        familyManageLog.setFamilyId(familyId);
        familyManageLog.setUserId(userId);
        return familyManageLogService.selectFamilyManageLogs(page ,limit,familyManageLog);
    }

}

