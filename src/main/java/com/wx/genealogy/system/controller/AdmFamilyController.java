package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.annotation.MyLog;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.Family;
import com.wx.genealogy.system.service.FamilyService;
import com.wx.genealogy.system.vo.req.FamilyUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName AdmFamilyController
 * @Author weixin
 * @Data 2021/10/26 10:12
 * @Description
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admFamily")
@Api(tags = "家族接口（管理员）")
public class AdmFamilyController {

    @Autowired
    private FamilyService familyService;

    @MyLog(value = "管理员修改家族")
    @ApiOperation(value = "根据id修改家族")
    @PreAuthorize("hasAuthority('family:update/family')")
    @RequestMapping(value = "/updateFamilyById", method = RequestMethod.PUT)
    public JsonResult updateFamilyById(@RequestBody Family family) throws Exception {
        if (ObjectUtil.isNull(family.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        return familyService.updateFamilyById(family);
    }


    @ApiOperation(value = "家族分页查询")
    @PreAuthorize("hasAuthority('family:get/family')")
    @RequestMapping(value = "/selectFamily", method = RequestMethod.GET)
    public JsonResult selectFamily(@RequestParam(value = "limit") Integer limit,
                                         @RequestParam(value = "page") Integer page,
                                         @RequestParam(value = "name",required = false) String name) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        Family family = new Family();
        family.setName(name);
        return familyService.selectFamily(page ,limit,family);
    }


}
