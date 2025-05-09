package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.Essay;
import com.wx.genealogy.system.entity.EssaySupport;
import com.wx.genealogy.system.service.EssaySupportService;
import com.wx.genealogy.system.vo.req.EssaySupportInsertReqVo;
import com.wx.genealogy.system.vo.req.EssaySupportUpdateReqVo;
import com.wx.genealogy.system.vo.req.UserFamilyFollowUpdateReqVo;
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
 * @since 2021-10-15
 */
@RestController
@RequestMapping("/essaySupport")
@Api(tags = "文章点赞接口")
public class EssaySupportController {

    @Autowired
    private EssaySupportService essaySupportService;

    @ApiOperation(value = "新增点赞")
    @RequestMapping(value = "/insertEssaySupport", method = RequestMethod.POST)
    public JsonResult insertEssaySupport(@RequestBody EssaySupportInsertReqVo essaySupportInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(essaySupportInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(essaySupportInsertReqVo.getEssayId())) {
            throw new MissingServletRequestParameterException("essayId", "number");
        }

        EssaySupport essaySupportInsert=new EssaySupport();
        ObjectUtil.copyByName(essaySupportInsertReqVo,essaySupportInsert);
        essaySupportInsert.setStatus(1);
        essaySupportInsert.setCreateTime(DateUtils.getDateTime());
        return essaySupportService.insertEssaySupport(essaySupportInsert);
    }

    @ApiOperation(value = "根据id修改点赞")
    @RequestMapping(value = "/updateEssaySupportById", method = RequestMethod.PUT)
    public JsonResult updateEssaySupportById(@RequestBody EssaySupportUpdateReqVo essaySupportUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(essaySupportUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        EssaySupport essaySupportUpdate=new EssaySupport();
        ObjectUtil.copyByName(essaySupportUpdateReqVo,essaySupportUpdate);
        return essaySupportService.updateEssaySupportById(essaySupportUpdate);
    }

    @ApiOperation(value = "根据文章id分页查询点赞记录")
    @RequestMapping(value = "/selectEssaySupportAndUser", method = RequestMethod.GET)
    public JsonResult selectEssaySupportAndUser(@RequestParam(value = "limit") Integer limit,
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
        EssaySupport essaySupportSelect=new EssaySupport();
        essaySupportSelect.setEssayId(essayId);
        essaySupportSelect.setStatus(1);
        return essaySupportService.selectEssaySupportAndUser(page ,limit,essaySupportSelect);
    }

    @ApiOperation(value = "根据文章id和浏览者id查询单个点赞情况")
    @RequestMapping(value = "/findEssaySupport", method = RequestMethod.GET)
    public JsonResult findEssaySupport(@RequestParam(value = "userId") Integer userId,
                                                @RequestParam(value = "essayId") Integer essayId) throws Exception {
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(essayId)) {
            throw new MissingServletRequestParameterException("essayId", "number");
        }
        EssaySupport essaySupport=new EssaySupport();
        essaySupport.setEssayId(essayId);
        essaySupport.setUserId(userId);
        return essaySupportService.findEssaySupport(essaySupport);
    }

}

