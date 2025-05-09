package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;

import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.EssayShare;

import com.wx.genealogy.system.service.EssayShareService;
import com.wx.genealogy.system.vo.req.EssayShareInsertReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-09
 */
@RestController
@RequestMapping("/essayShare")
@Api(tags = "分享文章接口")
public class EssayShareController{

    @Autowired
    private EssayShareService essayShareService;

    @ApiOperation(value = "添加分享")
    @RequestMapping(value = "/insertEssayShare", method = RequestMethod.POST)
    public JsonResult insertEssayShare(@RequestBody EssayShareInsertReqVo essayShareInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(essayShareInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(essayShareInsertReqVo.getEssayId())) {
            throw new MissingServletRequestParameterException("EssayId", "number");
        }
        if (ObjectUtil.isNull(essayShareInsertReqVo.getFamilyId())){
            throw new MissingServletRequestParameterException("familyId", "number");
        }


        EssayShare essayInsert=new EssayShare();
        essayInsert.setUserId(essayShareInsertReqVo.getUserId());
        essayInsert.setFamilyId(essayShareInsertReqVo.getFamilyId());
        essayInsert.setEssayId(essayShareInsertReqVo.getEssayId());



        return essayShareService.insertEssayShare(essayInsert);
    }

}
