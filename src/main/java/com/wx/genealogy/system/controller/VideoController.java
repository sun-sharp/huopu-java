package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.Video;
import com.wx.genealogy.system.entity.VideoImg;
import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.service.VideoService;
import com.wx.genealogy.system.service.FamilyUserService;
import com.wx.genealogy.system.vo.req.VideoInsertReqVo;
import com.wx.genealogy.system.vo.req.VideoUpdateReqVo;
import com.wx.genealogy.system.vo.req.UnlockVideoUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-09
 */
@RestController
@RequestMapping("/video")
@Api(tags = "家族文章接口")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Resource
    private FamilyUserService familyUserService;

    @ApiOperation(value = "发布文章")
    @RequestMapping(value = "/insertVideo", method = RequestMethod.POST)
    public JsonResult insertVideo(@RequestBody VideoInsertReqVo videoInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(videoInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }

        if (ObjectUtil.isNull(videoInsertReqVo.getFamilyId())) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }

        long nowTime = System.currentTimeMillis() / 1000;

        Video videoInsert = new Video();
        videoInsert.setUserId(videoInsertReqVo.getUserId());
        videoInsert.setFamilyId(videoInsertReqVo.getFamilyId());

        videoInsert.setTitle(videoInsertReqVo.getTitle());
        videoInsert.setCreateTime(DateUtils.getDateTime());
        videoInsert.setKnitCycle(2592000);//30天（测试时建议用一天）
        videoInsert.setKnitStartTime(nowTime);
        videoInsert.setKnitEndTime(nowTime + videoInsert.getKnitCycle());//加上周期就是被锁触发时间

        List<VideoImg> videoImgListInsert = new ArrayList<VideoImg>();


        for (int i = 0; i < videoInsertReqVo.getVideoImgInsertReqVoList().size(); i++) {
            VideoImg videoImg = new VideoImg();

            videoImg.setImg(videoInsertReqVo.getVideoImgInsertReqVoList().get(i).getImg());
            videoImgListInsert.add(videoImg);
        }

        return videoService.insertVideo(videoInsert, videoImgListInsert);
    }

    @ApiOperation(value = "分享文章")
    @RequestMapping(value = "/shareVideo", method = RequestMethod.POST)
    public JsonResult shareVideo(@RequestBody VideoInsertReqVo videoInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(videoInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(videoInsertReqVo.getOpen())) {
            throw new MissingServletRequestParameterException("open", "number");
        }
        if (ObjectUtil.isNull(videoInsertReqVo.getFamilyId())) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }

        long nowTime = System.currentTimeMillis() / 1000;

        Video videoInsert = new Video();
        videoInsert.setUserId(videoInsertReqVo.getUserId());
        videoInsert.setFamilyId(videoInsertReqVo.getFamilyId());
        videoInsert.setContent(videoInsertReqVo.getContent());
       // videoInsert.setAddress(videoInsertReqVo.getAddress());
       // videoInsert.setOpen(videoInsertReqVo.getOpen());
        videoInsert.setCreateTime(DateUtils.getDateTime());
        videoInsert.setKnitCycle(2592000);//30天（测试时建议用一天）
        videoInsert.setKnitStartTime(nowTime);
        videoInsert.setKnitEndTime(nowTime + videoInsert.getKnitCycle());//加上周期就是被锁触发时间

        List<VideoImg> videoImgListInsert = new ArrayList<VideoImg>();
        for (int i = 0; i < videoInsertReqVo.getVideoImgInsertReqVoList().size(); i++) {
            VideoImg videoImg = new VideoImg();
            videoImg.setImg(videoInsertReqVo.getVideoImgInsertReqVoList().get(i).getImg());
            videoImgListInsert.add(videoImg);
        }

        return videoService.insertVideo(videoInsert, videoImgListInsert);
    }


    @ApiOperation(value = "根据用户id分页查询")
    @RequestMapping(value = "/selectVideoByUserId", method = RequestMethod.GET)
    public JsonResult selectVideoByUserId(@RequestParam(value = "limit") Integer limit,
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
        Video video = new Video();
        video.setUserId(userId);
        return videoService.selectVideoByUserId(page, limit, video);
    }

    @ApiOperation(value = "根据家族id和浏览者id分页查询")
    @RequestMapping(value = "/selectVideoByFamilyId", method = RequestMethod.GET)
    public JsonResult selectVideoByFamilyId(@RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "page") Integer page,
                                            @RequestParam(value = "familyId") Integer familyId,
                                            @RequestParam(value = "userId") Integer userId,
                                            @RequestParam(value = "authorId", required = false) Integer authorId,

                                            @RequestParam(value = "searchData", required = false) String searchData,

                                            @RequestParam(value = "sort", required = false) Integer sort) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        Video video = new Video();
        video.setUserId(authorId);
        video.setFamilyId(familyId);

        FamilyUser familyUser = new FamilyUser();
        familyUser.setUserId(userId);

        return videoService.selectVideoByFamilyId(page, limit, video, familyUser, sort, searchData);
    }

    @ApiOperation(value = "根据文章id和浏览者id查询单个")
    @RequestMapping(value = "/findVideoById", method = RequestMethod.GET)
    public JsonResult findVideoById(@RequestParam(value = "id") Integer id,
                                    @RequestParam(value = "userId") Integer userId) throws Exception {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        Video video = new Video();
        video.setId(id);
        video.setUserId(userId);//当前id为浏览者id
        return videoService.findVideoById(video);
    }

    @ApiOperation(value = "根据用户id分页查询加入和关注的(首页专供)")
    @RequestMapping(value = "/selectVideo", method = RequestMethod.GET)
    public JsonResult selectVideo(@RequestParam(value = "limit") Integer limit,
                                  @RequestParam(value = "page") Integer page,
                                  @RequestParam(value = "userId") Integer userId,
                                  @RequestParam(value = "sort", required = false) Integer sort) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        Video video = new Video();
        video.setUserId(userId);
        return videoService.selectVideoByOpen(page, limit, video, sort);
    }

    @ApiOperation(value = "根据id修改文章")
    @RequestMapping(value = "/updateVideoById", method = RequestMethod.PUT)
    public JsonResult updateVideoById(@RequestBody VideoUpdateReqVo videoUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(videoUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        Video videoUpdate = new Video();
        ObjectUtil.copyByName(videoUpdateReqVo, videoUpdate);
        return videoService.updateVideoById(videoUpdate);
    }

    @ApiOperation(value = "根据id修改文章访问量")
    @RequestMapping(value = "/updateVideoBrowseNumberById", method = RequestMethod.PUT)
    public JsonResult updateVideoBrowseNumberById(@RequestBody VideoUpdateReqVo videoUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(videoUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        if (ObjectUtil.isNull(videoUpdateReqVo.getBrowseNumber())) {
            throw new MissingServletRequestParameterException("browseNumber", "number");
        }

        long nowTime = System.currentTimeMillis() / 1000;

        Video videoUpdate = new Video();
        videoUpdate.setId(videoUpdateReqVo.getId());
        videoUpdate.setUserId(videoUpdateReqVo.getUserId());//这里的id是浏览者id
        videoUpdate.setBrowseNumber(videoUpdateReqVo.getBrowseNumber());
        videoUpdate.setKnitStartTime(nowTime);
        return videoService.updateVideoBrowseNumberById(videoUpdate);
    }

    @ApiOperation(value = "根据id解锁文章")
    @RequestMapping(value = "/unlockVideoById", method = RequestMethod.PUT)
    public JsonResult unlockVideoById(@RequestBody UnlockVideoUpdateReqVo unlockVideoUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(unlockVideoUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }

        long nowTime = System.currentTimeMillis() / 1000;

        Video video = new Video();
        video.setId(unlockVideoUpdateReqVo.getId());
        video.setUserId(unlockVideoUpdateReqVo.getUserId());
        video.setKnit(0);
        video.setKnitCycle(unlockVideoUpdateReqVo.getMultiple() * 2592000);//测试是1天为基本单位
        video.setKnitStartTime(nowTime);
        video.setKnitEndTime(nowTime + video.getKnitCycle());
        return videoService.updateVideoUnlockById(video);
    }

    @RequestMapping(value = "/deleteVideoById", method = RequestMethod.DELETE)
    @ApiOperation("根据id删除单个功能")
    public JsonResult deleteVideoById(@RequestParam("id") Integer id) throws Exception {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        return videoService.deleteVideoById(id);

    }

    @RequestMapping(value = "/selectotherVideo", method = RequestMethod.GET)
    @ApiOperation("查找别人的video")
    public JsonResult selectotherVideo(@RequestParam(value = "limit") Integer limit,
                                       @RequestParam(value = "page") Integer page, @RequestParam("sheuserId") Integer sheuserId, @RequestParam("userId") Integer userId) throws Exception {
        if (ObjectUtil.isNull(sheuserId)) {
            throw new MissingServletRequestParameterException("sheuserId", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        return videoService.selectotherVideo(limit, page, sheuserId, userId);

    }

    /**
     * 修改文章开放/保密权限
     * @param id
     * @param userId
     * @param open
     * @return
     * @throws Exception
     */
    @GetMapping("/upVideoOpenById")
    @ApiOperation(value = "修改文章开放/保密权限")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "当前数据id", dataType = "java.lang.Integer", paramType = "path", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "java.lang.Integer", paramType = "path", required = true),
            @ApiImplicitParam(name = "open", value = "是否开放(开放1保密2家族内公开3完全公开)", dataType = "java.lang.Integer", paramType = "path", required = true),
    })
    public JsonResult upVideoOpenById(Integer id, Integer userId,Integer open) throws Exception {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id,", "为空");
        }

        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "为空");
        }

        Video videoById = videoService.selectVideoById(id);

        if (ObjectUtil.isNull(videoById)) {
            throw new MissingServletRequestParameterException("videoById", "数据不存在");
        }

        //判断是创建者
        if (videoById.getUserId().equals(userId)) {
          //  videoById.setOpen(open);
            if (videoService.updateById(videoById)) {
                return ResponseUtil.ok("操作成功");
            }
        }

        //判断是否是家族管理员
        FamilyUser familyUserSelect = new FamilyUser();
        familyUserSelect.setUserId(videoById.getUserId());
        familyUserSelect.setStatus(2);

        //文章的创建者家族
        List<FamilyUser> familyUserList = familyUserService.selectFamilyByUserId(familyUserSelect);

        //修改者家族
        familyUserSelect = new FamilyUser();
        familyUserSelect.setUserId(userId);
        familyUserSelect.setStatus(2);
        List<FamilyUser> userList = familyUserService.selectFamilyByUserId(familyUserSelect);

        if (ObjectUtil.isNotNull(familyUserList) && ObjectUtil.isNotNull(userList)) {
            for (FamilyUser familyUser : familyUserList) {
                for (FamilyUser user : userList) {
                    //相同
                    if (familyUser.getFamilyId().equals(user.getFamilyId())) {
                        if (user.getLevel() == 1 || user.getLevel() == 2) {
                           // videoById.setOpen(open);
                            if (videoService.updateById(videoById)) {
                                return ResponseUtil.ok("操作成功");
                            }
                        }

                    }
                }
            }
        }

        return ResponseUtil.fail("没有权限");
    }
}

