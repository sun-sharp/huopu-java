package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.EssayDiscuss;
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
public interface EssayDiscussService extends IService<EssayDiscuss> {

    JsonResult insertEssayDiscuss(EssayDiscuss essayDiscuss) throws  Exception;

    JsonResult selectEssayDiscussAndUser(Integer page,Integer limit,EssayDiscuss essayDiscuss);
}
