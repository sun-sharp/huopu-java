package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.VideoSupport;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-10-15
 */
@Transactional(rollbackFor = Exception.class)
public interface VideoSupportService extends IService<VideoSupport> {

    JsonResult insertVideoSupport(VideoSupport videoSupport) throws  Exception;

    JsonResult updateVideoSupportById(VideoSupport videoSupport) throws Exception;

    JsonResult selectVideoSupportAndUser(Integer page,Integer limit,VideoSupport videoSupport);

    JsonResult findVideoSupport(VideoSupport videoSupport);
}
