package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.Article;
import com.wx.genealogy.system.entity.ArticleSupport;
import com.wx.genealogy.system.service.ArticleSupportService;
import com.wx.genealogy.system.vo.req.ArticleSupportInsertReqVo;
import com.wx.genealogy.system.vo.req.ArticleSupportUpdateReqVo;
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
@RequestMapping("/articleSupport")
@Api(tags = "文章点赞接口")
public class ArticleSupportController {

    @Autowired
    private ArticleSupportService articleSupportService;

    @ApiOperation(value = "新增点赞")
    @RequestMapping(value = "/insertArticleSupport", method = RequestMethod.POST)
    public JsonResult insertArticleSupport(@RequestBody ArticleSupportInsertReqVo articleSupportInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(articleSupportInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(articleSupportInsertReqVo.getArticleId())) {
            throw new MissingServletRequestParameterException("articleId", "number");
        }

        ArticleSupport articleSupportInsert=new ArticleSupport();
        ObjectUtil.copyByName(articleSupportInsertReqVo,articleSupportInsert);
        articleSupportInsert.setStatus(1);
        articleSupportInsert.setCreateTime(DateUtils.getDateTime());
        return articleSupportService.insertArticleSupport(articleSupportInsert);
    }

    @ApiOperation(value = "根据id修改点赞")
    @RequestMapping(value = "/updateArticleSupportById", method = RequestMethod.PUT)
    public JsonResult updateArticleSupportById(@RequestBody ArticleSupportUpdateReqVo articleSupportUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(articleSupportUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        ArticleSupport articleSupportUpdate=new ArticleSupport();
        ObjectUtil.copyByName(articleSupportUpdateReqVo,articleSupportUpdate);
        return articleSupportService.updateArticleSupportById(articleSupportUpdate);
    }

    @ApiOperation(value = "根据文章id分页查询点赞记录")
    @RequestMapping(value = "/selectArticleSupportAndUser", method = RequestMethod.GET)
    public JsonResult selectArticleSupportAndUser(@RequestParam(value = "limit") Integer limit,
                                                @RequestParam(value = "page") Integer page,
                                                @RequestParam(value = "articleId") Integer articleId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(articleId)) {
            throw new MissingServletRequestParameterException("articleId", "number");
        }
        ArticleSupport articleSupportSelect=new ArticleSupport();
        articleSupportSelect.setArticleId(articleId);
        articleSupportSelect.setStatus(1);
        return articleSupportService.selectArticleSupportAndUser(page ,limit,articleSupportSelect);
    }

    @ApiOperation(value = "根据文章id和浏览者id查询单个点赞情况")
    @RequestMapping(value = "/findArticleSupport", method = RequestMethod.GET)
    public JsonResult findArticleSupport(@RequestParam(value = "userId") Integer userId,
                                       @RequestParam(value = "articleId") Integer articleId) throws Exception {
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(articleId)) {
            throw new MissingServletRequestParameterException("articleId", "number");
        }
        ArticleSupport articleSupport=new ArticleSupport();
        articleSupport.setArticleId(articleId);
        articleSupport.setUserId(userId);
        return articleSupportService.findArticleSupport(articleSupport);
    }

}

