package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.Article;
import com.wx.genealogy.system.entity.ArticleImg;
import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.service.ArticleService;
import com.wx.genealogy.system.service.FamilyUserService;
import com.wx.genealogy.system.vo.req.ArticleInsertReqVo;
import com.wx.genealogy.system.vo.req.ArticleUpdateReqVo;
import com.wx.genealogy.system.vo.req.UnlockArticleUpdateReqVo;
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
@RequestMapping("/article")
@Api(tags = "家族文章接口")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Resource
    private FamilyUserService familyUserService;

    @ApiOperation(value = "发布文章")
    @RequestMapping(value = "/insertArticle", method = RequestMethod.POST)
    public JsonResult insertArticle(@RequestBody ArticleInsertReqVo articleInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(articleInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }

        if (ObjectUtil.isNull(articleInsertReqVo.getFamilyId())) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }

        long nowTime = System.currentTimeMillis() / 1000;

        Article articleInsert = new Article();
        articleInsert.setUserId(articleInsertReqVo.getUserId());
        articleInsert.setFamilyId(articleInsertReqVo.getFamilyId());
        articleInsert.setContent(articleInsertReqVo.getContent());
        articleInsert.setTitle(articleInsertReqVo.getTitle());
        articleInsert.setCreateTime(DateUtils.getDateTime());
        articleInsert.setKnitCycle(2592000);//30天（测试时建议用一天）
        articleInsert.setKnitStartTime(nowTime);
        articleInsert.setKnitEndTime(nowTime + articleInsert.getKnitCycle());//加上周期就是被锁触发时间

        List<ArticleImg> articleImgListInsert = new ArrayList<ArticleImg>();

//        for (int i = 0; i < articleInsertReqVo.getArticleImgInsertReqVoList().size(); i++) {
//            ArticleImg articleImg = new ArticleImg();
//
//            articleImg.setImg(articleInsertReqVo.getArticleImgInsertReqVoList().get(i).getImg());
//            articleImgListInsert.add(articleImg);
//        }

        return articleService.insertArticle(articleInsert, articleImgListInsert);
    }

    @ApiOperation(value = "分享文章")
    @RequestMapping(value = "/shareArticle", method = RequestMethod.POST)
    public JsonResult shareArticle(@RequestBody ArticleInsertReqVo articleInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(articleInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(articleInsertReqVo.getOpen())) {
            throw new MissingServletRequestParameterException("open", "number");
        }
        if (ObjectUtil.isNull(articleInsertReqVo.getFamilyId())) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }

        long nowTime = System.currentTimeMillis() / 1000;

        Article articleInsert = new Article();
        articleInsert.setUserId(articleInsertReqVo.getUserId());
        articleInsert.setFamilyId(articleInsertReqVo.getFamilyId());
        articleInsert.setContent(articleInsertReqVo.getContent());
       // articleInsert.setAddress(articleInsertReqVo.getAddress());
       // articleInsert.setOpen(articleInsertReqVo.getOpen());
        articleInsert.setCreateTime(DateUtils.getDateTime());
        articleInsert.setKnitCycle(2592000);//30天（测试时建议用一天）
        articleInsert.setKnitStartTime(nowTime);
        articleInsert.setKnitEndTime(nowTime + articleInsert.getKnitCycle());//加上周期就是被锁触发时间

        List<ArticleImg> articleImgListInsert = new ArrayList<ArticleImg>();
        for (int i = 0; i < articleInsertReqVo.getArticleImgInsertReqVoList().size(); i++) {
            ArticleImg articleImg = new ArticleImg();
            articleImg.setImg(articleInsertReqVo.getArticleImgInsertReqVoList().get(i).getImg());
            articleImgListInsert.add(articleImg);
        }

        return articleService.insertArticle(articleInsert, articleImgListInsert);
    }


    @ApiOperation(value = "根据用户id分页查询")
    @RequestMapping(value = "/selectArticleByUserId", method = RequestMethod.GET)
    public JsonResult selectArticleByUserId(@RequestParam(value = "limit") Integer limit,
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
        Article article = new Article();
        article.setUserId(userId);
        return articleService.selectArticleByUserId(page, limit, article);
    }

    @ApiOperation(value = "根据家族id和浏览者id分页查询")
    @RequestMapping(value = "/selectArticleByFamilyId", method = RequestMethod.GET)
    public JsonResult selectArticleByFamilyId(@RequestParam(value = "limit") Integer limit,
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
        Article article = new Article();
        article.setUserId(authorId);
        article.setFamilyId(familyId);

        FamilyUser familyUser = new FamilyUser();
        familyUser.setUserId(userId);

        return articleService.selectArticleByFamilyId(page, limit, article, familyUser, sort, searchData);
    }

    @ApiOperation(value = "根据文章id和浏览者id查询单个")
    @RequestMapping(value = "/findArticleById", method = RequestMethod.GET)
    public JsonResult findArticleById(@RequestParam(value = "id") Integer id,
                                    @RequestParam(value = "userId") Integer userId) throws Exception {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        Article article = new Article();
        article.setId(id);
        article.setUserId(userId);//当前id为浏览者id
        return articleService.findArticleById(article);
    }

    @ApiOperation(value = "根据用户id分页查询加入和关注的(首页专供)")
    @RequestMapping(value = "/selectArticle", method = RequestMethod.GET)
    public JsonResult selectArticle(@RequestParam(value = "limit") Integer limit,
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
        Article article = new Article();
        article.setUserId(userId);
        return articleService.selectArticleByOpen(page, limit, article, sort);
    }

    @ApiOperation(value = "根据id修改文章")
    @RequestMapping(value = "/updateArticleById", method = RequestMethod.PUT)
    public JsonResult updateArticleById(@RequestBody ArticleUpdateReqVo articleUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(articleUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        Article articleUpdate = new Article();
        ObjectUtil.copyByName(articleUpdateReqVo, articleUpdate);
        return articleService.updateArticleById(articleUpdate);
    }

    @ApiOperation(value = "根据id修改文章访问量")
    @RequestMapping(value = "/updateArticleBrowseNumberById", method = RequestMethod.PUT)
    public JsonResult updateArticleBrowseNumberById(@RequestBody ArticleUpdateReqVo articleUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(articleUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        if (ObjectUtil.isNull(articleUpdateReqVo.getBrowseNumber())) {
            throw new MissingServletRequestParameterException("browseNumber", "number");
        }

        long nowTime = System.currentTimeMillis() / 1000;

        Article articleUpdate = new Article();
        articleUpdate.setId(articleUpdateReqVo.getId());
        articleUpdate.setUserId(articleUpdateReqVo.getUserId());//这里的id是浏览者id
        articleUpdate.setBrowseNumber(articleUpdateReqVo.getBrowseNumber());
        articleUpdate.setKnitStartTime(nowTime);
        return articleService.updateArticleBrowseNumberById(articleUpdate);
    }

    @ApiOperation(value = "根据id解锁文章")
    @RequestMapping(value = "/unlockArticleById", method = RequestMethod.PUT)
    public JsonResult unlockArticleById(@RequestBody UnlockArticleUpdateReqVo unlockArticleUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(unlockArticleUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }

        long nowTime = System.currentTimeMillis() / 1000;

        Article article = new Article();
        article.setId(unlockArticleUpdateReqVo.getId());
        article.setUserId(unlockArticleUpdateReqVo.getUserId());
        article.setKnit(0);
        article.setKnitCycle(unlockArticleUpdateReqVo.getMultiple() * 2592000);//测试是1天为基本单位
        article.setKnitStartTime(nowTime);
        article.setKnitEndTime(nowTime + article.getKnitCycle());
        return articleService.updateArticleUnlockById(article);
    }

    @RequestMapping(value = "/deleteArticleById", method = RequestMethod.DELETE)
    @ApiOperation("根据id删除单个功能")
    public JsonResult deleteArticleById(@RequestParam("id") Integer id) throws Exception {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        return articleService.deleteArticleById(id);

    }

    @RequestMapping(value = "/selectotherArticle", method = RequestMethod.GET)
    @ApiOperation("查找别人的article")
    public JsonResult selectotherArticle(@RequestParam(value = "limit") Integer limit,
                                       @RequestParam(value = "page") Integer page, @RequestParam("sheuserId") Integer sheuserId, @RequestParam("userId") Integer userId) throws Exception {
        if (ObjectUtil.isNull(sheuserId)) {
            throw new MissingServletRequestParameterException("sheuserId", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        return articleService.selectotherArticle(limit, page, sheuserId, userId);

    }

    /**
     * 修改文章开放/保密权限
     * @param id
     * @param userId
     * @param open
     * @return
     * @throws Exception
     */
    @GetMapping("/upArticleOpenById")
    @ApiOperation(value = "修改文章开放/保密权限")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "当前数据id", dataType = "java.lang.Integer", paramType = "path", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "java.lang.Integer", paramType = "path", required = true),
            @ApiImplicitParam(name = "open", value = "是否开放(开放1保密2家族内公开3完全公开)", dataType = "java.lang.Integer", paramType = "path", required = true),
    })
    public JsonResult upArticleOpenById(Integer id, Integer userId,Integer open) throws Exception {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id,", "为空");
        }

        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "为空");
        }

        Article articleById = articleService.selectArticleById(id);

        if (ObjectUtil.isNull(articleById)) {
            throw new MissingServletRequestParameterException("articleById", "数据不存在");
        }

        //判断是创建者
        if (articleById.getUserId().equals(userId)) {
          //  articleById.setOpen(open);
            if (articleService.updateById(articleById)) {
                return ResponseUtil.ok("操作成功");
            }
        }

        //判断是否是家族管理员
        FamilyUser familyUserSelect = new FamilyUser();
        familyUserSelect.setUserId(articleById.getUserId());
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
                           // articleById.setOpen(open);
                            if (articleService.updateById(articleById)) {
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

