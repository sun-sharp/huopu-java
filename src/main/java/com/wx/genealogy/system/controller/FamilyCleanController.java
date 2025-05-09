package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.Essay;
import com.wx.genealogy.system.entity.FamilyClean;

import com.wx.genealogy.system.service.FamilyCleanService;
import com.wx.genealogy.system.vo.req.FamilyCleanInsertReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-09
 */


@RestController
@RequestMapping("/familyClean")
@Api(tags = "打扫家族")
public class FamilyCleanController {

    @Autowired
    private FamilyCleanService familyCleanService;

    @ApiOperation(value = "打扫家族")
    @RequestMapping(value = "/insertFamilyClean", method = RequestMethod.POST)
    public JsonResult insertFamilyClean(@RequestBody FamilyCleanInsertReqVo familyCleanInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(familyCleanInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }

        if (ObjectUtil.isNull(familyCleanInsertReqVo.getFamilyId())){
            throw new MissingServletRequestParameterException("familyId", "number");
        }
        FamilyClean familyClean=new FamilyClean();
        familyClean.setUserId(familyCleanInsertReqVo.getUserId());
        familyClean.setFamilyId(familyCleanInsertReqVo.getFamilyId());
        return familyCleanService.insertFamilyClean(familyClean);
    }

    @ApiOperation(value = "打扫家族排行榜")
    @RequestMapping(value = "/selectFamilyClean", method = RequestMethod.GET)
    public JsonResult selectFamilyClean(@RequestParam(value = "limit") Integer limit,
                                  @RequestParam(value = "page") Integer page,@RequestParam(value = "familyId") Integer familyId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }

        return familyCleanService.selectFamilyClean(page ,limit,familyId);
    }

}
