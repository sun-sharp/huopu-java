package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.VideoDiscuss;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-10-18
 */
@Transactional(rollbackFor = Exception.class)
public interface VideoDiscussService extends IService<VideoDiscuss> {

    JsonResult insertVideoDiscuss(VideoDiscuss videoDiscuss) throws  Exception;

    JsonResult selectVideoDiscussAndUser(Integer page,Integer limit,VideoDiscuss videoDiscuss);
}
