package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.UserFamilyFollow;
import com.wx.genealogy.system.service.UserFamilyFollowService;
import com.wx.genealogy.system.vo.req.UserFamilyFollowInsertReqVo;
import com.wx.genealogy.system.vo.req.UserFamilyFollowUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 用户关注家族 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/userFamilyFollow")
@Api(tags = "用户关注家族接口")
public class UserFamilyFollowController {

    @Autowired
    private UserFamilyFollowService userFamilyFollowService;

    @ApiOperation(value = "关注家族")
    @RequestMapping(value = "/insertUserFamilyFollow", method = RequestMethod.POST)
    public JsonResult insertUserFamilyFollow(@RequestBody UserFamilyFollowInsertReqVo userFamilyFollowInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(userFamilyFollowInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(userFamilyFollowInsertReqVo.getFamilyId())) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }

        UserFamilyFollow userFamilyFollowInsert=new UserFamilyFollow();
        ObjectUtil.copyByName(userFamilyFollowInsertReqVo,userFamilyFollowInsert);
        return userFamilyFollowService.insertUserFamilyFollow(userFamilyFollowInsert);
    }

    @ApiOperation(value = "取消关注")
    @RequestMapping(value = "/cancelAttention", method = RequestMethod.PUT)
    public JsonResult cancelAttention(@RequestBody UserFamilyFollowUpdateReqVo userFamilyFollowUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(userFamilyFollowUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        UserFamilyFollow userFamilyFollowUpdate=new UserFamilyFollow();
        userFamilyFollowUpdate.setId(userFamilyFollowUpdateReqVo.getId());
        userFamilyFollowUpdate.setStatus(0);
        return userFamilyFollowService.updateUserFamilyFollowById(userFamilyFollowUpdate);
    }

    @ApiOperation(value = "根据id修改用户关注关联信息")
    @RequestMapping(value = "/updateUserFamilyFollowById", method = RequestMethod.PUT)
    public JsonResult updateUserFamilyFollowById(@RequestBody UserFamilyFollowUpdateReqVo userFamilyFollowUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(userFamilyFollowUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        UserFamilyFollow userFamilyFollowUpdate=new UserFamilyFollow();
        ObjectUtil.copyByName(userFamilyFollowUpdateReqVo,userFamilyFollowUpdate);
        return userFamilyFollowService.updateUserFamilyFollowById(userFamilyFollowUpdate);
    }

    @ApiOperation(value = "根据用户id分页查询已关注的家族")
    @RequestMapping(value = "/selectUserFamilyFollowAndFamily", method = RequestMethod.GET)
    public JsonResult selectUserFamilyFollowAndFamily(@RequestParam(value = "limit") Integer limit,
                                           @RequestParam(value = "page") Integer page,
                                           @RequestParam(value = "userId") Integer userId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        UserFamilyFollow userFamilyFollowSelect=new UserFamilyFollow();
        userFamilyFollowSelect.setUserId(userId);
        userFamilyFollowSelect.setStatus(1);
        return userFamilyFollowService.selectUserFamilyFollowAndFamily(page ,limit,userFamilyFollowSelect);
    }
}

