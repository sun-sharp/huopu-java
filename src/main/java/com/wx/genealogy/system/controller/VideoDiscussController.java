package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.VideoDiscuss;
import com.wx.genealogy.system.entity.VideoSupport;
import com.wx.genealogy.system.service.VideoDiscussService;
import com.wx.genealogy.system.vo.req.VideoDiscussInsertReqVo;
import com.wx.genealogy.system.vo.req.VideoSupportInsertReqVo;
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
@RequestMapping("/videoDiscuss")
@Api(tags = "文章评论接口")
public class VideoDiscussController {

    @Autowired
    private VideoDiscussService videoDiscussService;

    @ApiOperation(value = "新增评论")
    @RequestMapping(value = "/insertVideoDiscuss", method = RequestMethod.POST)
    public JsonResult insertVideoDiscuss(@RequestBody VideoDiscussInsertReqVo videoDiscussInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(videoDiscussInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(videoDiscussInsertReqVo.getVideoId())) {
            throw new MissingServletRequestParameterException("videoId", "number");
        }
        if (ObjectUtil.isNull(videoDiscussInsertReqVo.getContent())) {
            throw new MissingServletRequestParameterException("content", "String");
        }

        VideoDiscuss videoDiscussInsert=new VideoDiscuss();
        ObjectUtil.copyByName(videoDiscussInsertReqVo,videoDiscussInsert);
        videoDiscussInsert.setCreateTime(DateUtils.getDateTime());
        return videoDiscussService.insertVideoDiscuss(videoDiscussInsert);
    }

    @ApiOperation(value = "根据文章id分页查询评论记录")
    @RequestMapping(value = "/selectVideoDiscussAndUser", method = RequestMethod.GET)
    public JsonResult selectVideoDiscussAndUser(@RequestParam(value = "limit") Integer limit,
                                                @RequestParam(value = "page") Integer page,
                                                @RequestParam(value = "videoId") Integer videoId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(videoId)) {
            throw new MissingServletRequestParameterException("videoId", "number");
        }
        VideoDiscuss videoDiscussSelect=new VideoDiscuss();
        videoDiscussSelect.setVideoId(videoId);
        return videoDiscussService.selectVideoDiscussAndUser(page ,limit,videoDiscussSelect);
    }
}

