package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.ArticleSupport;
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
public interface ArticleSupportService extends IService<ArticleSupport> {

    JsonResult insertArticleSupport(ArticleSupport articleSupport) throws  Exception;

    JsonResult updateArticleSupportById(ArticleSupport articleSupport) throws Exception;

    JsonResult selectArticleSupportAndUser(Integer page,Integer limit,ArticleSupport articleSupport);

    JsonResult findArticleSupport(ArticleSupport articleSupport);
}
