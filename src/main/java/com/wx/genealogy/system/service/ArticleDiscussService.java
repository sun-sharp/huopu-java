package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.ArticleDiscuss;
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
public interface ArticleDiscussService extends IService<ArticleDiscuss> {

    JsonResult insertArticleDiscuss(ArticleDiscuss articleDiscuss) throws  Exception;

    JsonResult selectArticleDiscussAndUser(Integer page,Integer limit,ArticleDiscuss articleDiscuss);
}
