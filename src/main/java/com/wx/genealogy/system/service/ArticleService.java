package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.system.entity.ArticleImg;
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
public interface ArticleService extends IService<Article> {

    JsonResult insertArticle(Article article, List<ArticleImg> articleImgList) throws Exception;

    JsonResult selectArticleByUserId(Integer page,Integer limit,Article article);

    JsonResult selectArticleByOpen(Integer page,Integer limit,Article article,Integer sort);

    JsonResult selectArticle(Integer page,Integer limit,Article article,Integer sort);

    JsonResult selectArticleByFamilyId(Integer page, Integer limit,Article article, FamilyUser familyUser,Integer sort,String searchData);

    JsonResult findArticleById(Article article);

    JsonResult updateArticleById(Article article) throws Exception;

    JsonResult updateArticleBrowseNumberById(Article article) throws Exception;

    JsonResult updateArticleUnlockById(Article article) throws Exception;

    JsonResult deleteArticleById(Integer id) throws Exception;
    JsonResult selectotherArticle(Integer limit,Integer page,Integer id,Integer userId) throws Exception;

    /**
     * 根据id查询帖子
     * @param id 帖子id
     * @return
     */
    Article selectArticleById(Integer id);
}
