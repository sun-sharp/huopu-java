package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.system.entity.VideoImg;
import com.wx.genealogy.system.entity.FamilyUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-09
 */
@Transactional(rollbackFor = Exception.class)
public interface VideoService extends IService<Video> {

    JsonResult insertVideo(Video video, List<VideoImg> videoImgList) throws Exception;

    JsonResult selectVideoByUserId(Integer page,Integer limit,Video video);

    JsonResult selectVideoByOpen(Integer page,Integer limit,Video video,Integer sort);

    JsonResult selectVideo(Integer page,Integer limit,Video video,Integer sort);

    JsonResult selectVideoByFamilyId(Integer page, Integer limit,Video video, FamilyUser familyUser,Integer sort,String searchData);

    JsonResult findVideoById(Video video);

    JsonResult updateVideoById(Video video) throws Exception;

    JsonResult updateVideoBrowseNumberById(Video video) throws Exception;

    JsonResult updateVideoUnlockById(Video video) throws Exception;

    JsonResult deleteVideoById(Integer id) throws Exception;
    JsonResult selectotherVideo(Integer limit,Integer page,Integer id,Integer userId) throws Exception;

    /**
     * 根据id查询帖子
     * @param id 帖子id
     * @return
     */
    Video selectVideoById(Integer id);
}
