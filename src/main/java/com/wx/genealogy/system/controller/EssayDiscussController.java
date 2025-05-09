package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.EssayDiscuss;
import com.wx.genealogy.system.entity.EssaySupport;
import com.wx.genealogy.system.service.EssayDiscussService;
import com.wx.genealogy.system.vo.req.EssayDiscussInsertReqVo;
import com.wx.genealogy.system.vo.req.EssaySupportInsertReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-10-18
 */
@RestController
@RequestMapping("/essayDiscuss")
@Api(tags = "文章评论接口")
public class EssayDiscussController {

    @Autowired
    private EssayDiscussService essayDiscussService;

    @ApiOperation(value = "新增评论")
    @RequestMapping(value = "/insertEssayDiscuss", method = RequestMethod.POST)
    public JsonResult insertEssayDiscuss(@RequestBody EssayDiscussInsertReqVo essayDiscussInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(essayDiscussInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(essayDiscussInsertReqVo.getEssayId())) {
            throw new MissingServletRequestParameterException("essayId", "number");
        }
        if (ObjectUtil.isNull(essayDiscussInsertReqVo.getContent())) {
            throw new MissingServletRequestParameterException("content", "String");
        }

        EssayDiscuss essayDiscussInsert=new EssayDiscuss();
        ObjectUtil.copyByName(essayDiscussInsertReqVo,essayDiscussInsert);
        essayDiscussInsert.setCreateTime(DateUtils.getDateTime());
        return essayDiscussService.insertEssayDiscuss(essayDiscussInsert);
    }

    @ApiOperation(value = "根据文章id分页查询评论记录")
    @RequestMapping(value = "/selectEssayDiscussAndUser", method = RequestMethod.GET)
    public JsonResult selectEssayDiscussAndUser(@RequestParam(value = "limit") Integer limit,
                                                @RequestParam(value = "page") Integer page,
                                                @RequestParam(value = "essayId") Integer essayId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(essayId)) {
            throw new MissingServletRequestParameterException("essayId", "number");
        }
        EssayDiscuss essayDiscussSelect=new EssayDiscuss();
        essayDiscussSelect.setEssayId(essayId);
        return essayDiscussService.selectEssayDiscussAndUser(page ,limit,essayDiscussSelect);
    }
}

