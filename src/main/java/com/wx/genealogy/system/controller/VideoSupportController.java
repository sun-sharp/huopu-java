package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.Video;
import com.wx.genealogy.system.entity.VideoSupport;
import com.wx.genealogy.system.service.VideoSupportService;
import com.wx.genealogy.system.vo.req.VideoSupportInsertReqVo;
import com.wx.genealogy.system.vo.req.VideoSupportUpdateReqVo;
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
@RequestMapping("/videoSupport")
@Api(tags = "文章点赞接口")
public class VideoSupportController {

    @Autowired
    private VideoSupportService videoSupportService;

    @ApiOperation(value = "新增点赞")
    @RequestMapping(value = "/insertVideoSupport", method = RequestMethod.POST)
    public JsonResult insertVideoSupport(@RequestBody VideoSupportInsertReqVo videoSupportInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(videoSupportInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(videoSupportInsertReqVo.getVideoId())) {
            throw new MissingServletRequestParameterException("videoId", "number");
        }

        VideoSupport videoSupportInsert=new VideoSupport();
        ObjectUtil.copyByName(videoSupportInsertReqVo,videoSupportInsert);
        videoSupportInsert.setStatus(1);
        videoSupportInsert.setCreateTime(DateUtils.getDateTime());
        return videoSupportService.insertVideoSupport(videoSupportInsert);
    }

    @ApiOperation(value = "根据id修改点赞")
    @RequestMapping(value = "/updateVideoSupportById", method = RequestMethod.PUT)
    public JsonResult updateVideoSupportById(@RequestBody VideoSupportUpdateReqVo videoSupportUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(videoSupportUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        VideoSupport videoSupportUpdate=new VideoSupport();
        ObjectUtil.copyByName(videoSupportUpdateReqVo,videoSupportUpdate);
        return videoSupportService.updateVideoSupportById(videoSupportUpdate);
    }

    @ApiOperation(value = "根据文章id分页查询点赞记录")
    @RequestMapping(value = "/selectVideoSupportAndUser", method = RequestMethod.GET)
    public JsonResult selectVideoSupportAndUser(@RequestParam(value = "limit") Integer limit,
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
        VideoSupport videoSupportSelect=new VideoSupport();
        videoSupportSelect.setVideoId(videoId);
        videoSupportSelect.setStatus(1);
        return videoSupportService.selectVideoSupportAndUser(page ,limit,videoSupportSelect);
    }

    @ApiOperation(value = "根据文章id和浏览者id查询单个点赞情况")
    @RequestMapping(value = "/findVideoSupport", method = RequestMethod.GET)
    public JsonResult findVideoSupport(@RequestParam(value = "userId") Integer userId,
                                       @RequestParam(value = "videoId") Integer videoId) throws Exception {
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(videoId)) {
            throw new MissingServletRequestParameterException("videoId", "number");
        }
        VideoSupport videoSupport=new VideoSupport();
        videoSupport.setVideoId(videoId);
        videoSupport.setUserId(userId);
        return videoSupportService.findVideoSupport(videoSupport);
    }

}

