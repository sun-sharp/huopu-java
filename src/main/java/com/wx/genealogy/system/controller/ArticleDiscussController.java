package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.ArticleDiscuss;
import com.wx.genealogy.system.entity.ArticleSupport;
import com.wx.genealogy.system.service.ArticleDiscussService;
import com.wx.genealogy.system.vo.req.ArticleDiscussInsertReqVo;
import com.wx.genealogy.system.vo.req.ArticleSupportInsertReqVo;
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
 * @since 2021-10-18
 */
@RestController
@RequestMapping("/articleDiscuss")
@Api(tags = "文章评论接口")
public class ArticleDiscussController {

    @Autowired
    private ArticleDiscussService articleDiscussService;

    @ApiOperation(value = "新增评论")
    @RequestMapping(value = "/insertArticleDiscuss", method = RequestMethod.POST)
    public JsonResult insertArticleDiscuss(@RequestBody ArticleDiscussInsertReqVo articleDiscussInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(articleDiscussInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(articleDiscussInsertReqVo.getArticleId())) {
            throw new MissingServletRequestParameterException("articleId", "number");
        }
        if (ObjectUtil.isNull(articleDiscussInsertReqVo.getContent())) {
            throw new MissingServletRequestParameterException("content", "String");
        }

        ArticleDiscuss articleDiscussInsert=new ArticleDiscuss();
        ObjectUtil.copyByName(articleDiscussInsertReqVo,articleDiscussInsert);
        articleDiscussInsert.setCreateTime(DateUtils.getDateTime());
        return articleDiscussService.insertArticleDiscuss(articleDiscussInsert);
    }

    @ApiOperation(value = "根据文章id分页查询评论记录")
    @RequestMapping(value = "/selectArticleDiscussAndUser", method = RequestMethod.GET)
    public JsonResult selectArticleDiscussAndUser(@RequestParam(value = "limit") Integer limit,
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
        ArticleDiscuss articleDiscussSelect=new ArticleDiscuss();
        articleDiscussSelect.setArticleId(articleId);
        return articleDiscussService.selectArticleDiscussAndUser(page ,limit,articleDiscussSelect);
    }
}

