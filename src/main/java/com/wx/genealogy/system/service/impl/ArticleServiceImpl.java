package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.mapper.*;
import com.wx.genealogy.system.service.ArticleService;
import com.wx.genealogy.system.vo.res.ArticleAndImgSelectResVo;
import com.wx.genealogy.system.vo.res.ArticleFindResVo;
import com.wx.genealogy.system.vo.res.UserFindResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-09
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ArticleDao articleDao;
  //  @Autowired
  //  private ArticleShareDao articleShareDao;

    @Autowired
    private ArticleImgDao articleImgDao;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private FamilyUserDao familyUserDao;

    @Autowired
    private UserFamilyFollowDao userFamilyFollowDao;

    @Autowired
    private ArticleSupportDao articleSupportDao;

    @Autowired
    private RiceRecordDao riceRecordDao;
    @Autowired
    private FamilyMessageDao familyMessageDao;

    @Override
    @Transactional
    public JsonResult insertArticle(Article article, List<ArticleImg> articleImgList) throws Exception {

        //判断是不是管理员才可以发
        LambdaQueryWrapper<FamilyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyUser::getFamilyId, article.getFamilyId());
        wrapper.eq(FamilyUser::getUserId, article.getUserId());
        FamilyUser familyUser = this.familyUserDao.selectOne(wrapper);
        if (Objects.isNull(familyUser)) {
            return ResponseUtil.fail("未查询到您的管理员身份,不可发布");
        }
        // 身份等级：1是创建者2是管理员3是会员
        if (familyUser.getLevel() == 3) {
            return ResponseUtil.fail("未查询到您的管理员身份,不可发布");
        }

        int result = articleDao.insert(article);
        if (result == 0) {
            throw new Exception("发布失败");
        }
        //添加文章消息
        //1.查找家族有哪些成员的

        QueryWrapper<FamilyUser> familyuser_find = new QueryWrapper<FamilyUser>();
        familyuser_find.eq("family_id", article.getFamilyId());
        familyuser_find.gt("user_id", 0);
        List<FamilyUser> family_find = familyUserDao.selectList(familyuser_find);

        for (int i = 0; i < family_find.size(); i++) {
            QueryWrapper<FamilyMessage> familymessage = new QueryWrapper<FamilyMessage>();
            familymessage.eq("family_id", article.getFamilyId());
            familymessage.eq("user_id", family_find.get(i).getUserId());
            FamilyMessage familydata = familyMessageDao.selectOne(familymessage);

            if (familydata == null) {

                FamilyMessage familyMessage = new FamilyMessage();
                familyMessage.setArticleMessage(1);
                familyMessage.setFamilyId(article.getFamilyId());
                familyMessage.setUserId(family_find.get(i).getUserId());

                familyMessageDao.insert(familyMessage);
            } else {


                FamilyMessage familyMessage = new FamilyMessage();
                familyMessage.setId(familydata.getId());
                familyMessage.setArticleMessage(familydata.getArticleMessage() + 1);
                familyMessage.setFamilyId(article.getFamilyId());
                familyMessage.setUserId(family_find.get(i).getUserId());

                familyMessageDao.updateById(familyMessage);
            }


            FamilyUser fami = new FamilyUser();
            fami.setId(family_find.get(i).getId());
            fami.setUpdateTime(DateUtils.getDateTime());
            familyUserDao.updateById(fami);
        }


//        FamilyUser familyUser = new FamilyUser();
//        familyUser.setMessageNumber(1);
//        familyUser.setFamilyId(article.getFamilyId());
//        familyUserDao.setInc(familyUser);

//        if (articleImgList == null || articleImgList.size() == 0) {
//            return ResponseUtil.ok("发布成功");
//        }
//        //给图片赋予文章id
//        if (articleImgList != null && articleImgList.size() > 0) {
//            for (int i = 0; i < articleImgList.size(); i++) {
//                articleImgList.get(i).setArticleId(article.getId());
//            }
//            //接下来批量插入图片记录
//            result = articleImgDao.insertArticleImgList(articleImgList);
//            if (result == 0) {
//                throw new Exception("发布失败");
//            }
//        }

        /*************再更新下加入者和关注者的消息状态***************/

//        if (article.getOpen() == 3) {
//            UserFamilyFollow userFamilyFollow = new UserFamilyFollow();
//            userFamilyFollow.setMessageNumber(1);
//            userFamilyFollow.setFamilyId(article.getFamilyId());
//            userFamilyFollowDao.setInc(userFamilyFollow);
//        }

        //下发米，文字3，图片5
        int rice = 3;
//        if (articleImgList == null || articleImgList.size() == 0) {
//            rice = 3;
//        } else {
//            rice = 5;
//        }
        User user = new User();
        user.setId(article.getUserId());
        user.setRice(rice);
        result = userDao.setInc(user);
        if (result == 0) {
            throw new Exception("发布失败");
        }

        //记录到米收支明细
        RiceRecord riceRecord = new RiceRecord();
        riceRecord.setUserId(article.getUserId());
        riceRecord.setRice(rice);
        riceRecord.setContent("发表帖子");
        riceRecord.setCreateTime(article.getCreateTime());
        result = riceRecordDao.insert(riceRecord);
        if (result == 0) {
            throw new Exception("发布失败");
        }

        return ResponseUtil.ok("发布成功");
    }

    @Override
    public JsonResult selectArticleByUserId(Integer page, Integer limit, Article article) {
        IPage<Article> pageData = new Page<>(page, limit);
        QueryWrapper<Article> articleSelect = new QueryWrapper<Article>();
        articleSelect.eq("user_id", article.getUserId());
        articleSelect.orderByDesc("create_time");

        pageData = articleDao.selectPage(pageData, articleSelect);
        List<Article> articleList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        if (articleList == null || articleList.size() == 0) {
            map.put("articleList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        //拿一下该作者信息，这里因为是一个作者的所有帖子，查一次即可
        UserFindResVo userFindResVo = new UserFindResVo();
        User user = userDao.selectById(article.getUserId());
        ObjectUtil.copyByName(user, userFindResVo);

        List<ArticleAndImgSelectResVo> articleAndImgSelectResVoList = new ArrayList<ArticleAndImgSelectResVo>();
        for (int i = 0; i < articleList.size(); i++) {
            ArticleAndImgSelectResVo articleAndImgSelectResVo = new ArticleAndImgSelectResVo();
            //文章内容
            articleAndImgSelectResVo.setArticle(articleList.get(i));
            //文章图片
            QueryWrapper<ArticleImg> articleImgSelect = new QueryWrapper<ArticleImg>();
            articleImgSelect.eq("article_id", articleList.get(i).getId());

            articleAndImgSelectResVo.setArticleImgList(articleImgDao.selectList(articleImgSelect));
            //家族信息
            articleAndImgSelectResVo.setFamily(familyDao.selectById(articleList.get(i).getFamilyId()));
            //点赞信息
            QueryWrapper<ArticleSupport> articleSupportFind = new QueryWrapper<ArticleSupport>();
            articleSupportFind.eq("article_id", articleList.get(i).getId());
            articleSupportFind.eq("user_id", article.getUserId());
            ArticleSupport articleSupportData = articleSupportDao.selectOne(articleSupportFind);
            articleAndImgSelectResVo.setArticleSupport(articleSupportData);

            //作者信息
            articleAndImgSelectResVo.setUserFindResVo(userFindResVo);

            articleAndImgSelectResVoList.add(articleAndImgSelectResVo);
        }
        map.put("articleList", articleAndImgSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult selectArticleByOpen(Integer page, Integer limit, Article article, Integer sort) {

        //解释下这里的逻辑，如果通过关系找文章会特别复杂，而且检索效果可能会因为用户关联或者关注家族多的问题而影响，这里干脆正常查文章，再对文章做个过滤，唯一损失就是可能每次弹出的数目会有差距，但是量大的情况下用户是无感知的
        IPage<Article> pageData = new Page<>(page, limit);
        QueryWrapper<Article> articleSelect = new QueryWrapper<Article>();
        //1按浏览量 2点赞量 3评论数
        if (sort != null && sort == 1) {
            articleSelect.orderByDesc("browse_number");
        } else if (sort != null && sort == 2) {
            articleSelect.orderByDesc("praise_number");
        } else if (sort != null && sort == 3) {
            articleSelect.orderByDesc("discuss_number");
        } else if (sort != null && sort == 4) {
            articleSelect.orderByDesc("is_knit");
        } else {
            articleSelect.orderByDesc("id");
        }
        pageData = articleDao.selectPage(pageData, articleSelect);
        List<Article> articleList = pageData.getRecords();

        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        if (articleList == null || articleList.size() == 0) {
            map.put("articleList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        List<ArticleAndImgSelectResVo> articleAndImgSelectResVoList = new ArrayList<ArticleAndImgSelectResVo>();
        //对文章进行过滤
        for (int i = 0; i < articleList.size(); i++) {
            if (1==2) {
                //文章保密，我加入的家族并且不是旁亲
                QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
                familyUserFind.eq("family_id", articleList.get(i).getFamilyId());
                familyUserFind.eq("user_id", article.getUserId());
                familyUserFind.eq("status", 2);
                familyUserFind.le("relation", 2);
                FamilyUser familyUser = familyUserDao.selectOne(familyUserFind);
                if (familyUser != null) {
                    ArticleAndImgSelectResVo articleAndImgSelectResVo = new ArticleAndImgSelectResVo();
                    //文章内容
                    articleAndImgSelectResVo.setArticle(articleList.get(i));
                    //文章图片
                    QueryWrapper<ArticleImg> articleImgSelect = new QueryWrapper<ArticleImg>();
                    articleImgSelect.eq("article_id", articleList.get(i).getId());
                    articleAndImgSelectResVo.setArticleImgList(articleImgDao.selectList(articleImgSelect));
                    //家族信息
                    articleAndImgSelectResVo.setFamily(familyDao.selectById(articleList.get(i).getFamilyId()));
                    //浏览者点赞信息
                    QueryWrapper<ArticleSupport> articleSupportFind = new QueryWrapper<ArticleSupport>();
                    articleSupportFind.eq("article_id", articleList.get(i).getId());
                    articleSupportFind.eq("user_id", familyUser.getUserId());
                    ArticleSupport articleSupportData = articleSupportDao.selectOne(articleSupportFind);
                    articleAndImgSelectResVo.setArticleSupport(articleSupportData);
                    //作者信息
                    User user = userDao.selectById(articleList.get(i).getUserId());
                    if (user != null) {
                        UserFindResVo userFindResVo = new UserFindResVo();
                        userFindResVo.setAvatar(user.getAvatar());
                        userFindResVo.setNickName(user.getNickName());
                        userFindResVo.setRealName(user.getRealName());
                        articleAndImgSelectResVo.setUserFindResVo(userFindResVo);
                    }
                    articleAndImgSelectResVoList.add(articleAndImgSelectResVo);
                }

            } else if (1==1) {
                //文章家族内可看，家族成员就可以看
                QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
                familyUserFind.eq("family_id", articleList.get(i).getFamilyId());
                familyUserFind.eq("user_id", article.getUserId());
                familyUserFind.eq("status", 2);
                FamilyUser familyUser = familyUserDao.selectOne(familyUserFind);
                if (familyUser != null) {
                    ArticleAndImgSelectResVo articleAndImgSelectResVo = new ArticleAndImgSelectResVo();
                    //文章内容
                    articleAndImgSelectResVo.setArticle(articleList.get(i));
                    //文章图片
                    QueryWrapper<ArticleImg> articleImgSelect = new QueryWrapper<ArticleImg>();
                    articleImgSelect.eq("article_id", articleList.get(i).getId());
                    articleAndImgSelectResVo.setArticleImgList(articleImgDao.selectList(articleImgSelect));
                    //家族信息
                    articleAndImgSelectResVo.setFamily(familyDao.selectById(articleList.get(i).getFamilyId()));
                    //浏览者点赞信息
                    QueryWrapper<ArticleSupport> articleSupportFind = new QueryWrapper<ArticleSupport>();
                    articleSupportFind.eq("article_id", articleList.get(i).getId());
                    articleSupportFind.eq("user_id", familyUser.getUserId());
                    ArticleSupport articleSupportData = articleSupportDao.selectOne(articleSupportFind);
                    articleAndImgSelectResVo.setArticleSupport(articleSupportData);
                    //作者信息
                    User user = userDao.selectById(articleList.get(i).getUserId());
                    if (user != null) {
                        UserFindResVo userFindResVo = new UserFindResVo();
                        userFindResVo.setAvatar(user.getAvatar());
                        userFindResVo.setNickName(user.getNickName());
                        userFindResVo.setRealName(user.getRealName());
                        articleAndImgSelectResVo.setUserFindResVo(userFindResVo);
                    }
                    articleAndImgSelectResVoList.add(articleAndImgSelectResVo);
                }

            } else {
                //完全公开，家族成员或者关注就可看
                QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
                familyUserFind.eq("family_id", articleList.get(i).getFamilyId());
                familyUserFind.eq("user_id", articleList.get(i).getUserId());
                familyUserFind.eq("status", 2);
                FamilyUser familyUser = familyUserDao.selectOne(familyUserFind);
                if (familyUser != null) {
                    ArticleAndImgSelectResVo articleAndImgSelectResVo = new ArticleAndImgSelectResVo();
                    //文章内容
                    articleAndImgSelectResVo.setArticle(articleList.get(i));
                    //文章图片
                    QueryWrapper<ArticleImg> articleImgSelect = new QueryWrapper<ArticleImg>();
                    articleImgSelect.eq("article_id", articleList.get(i).getId());
                    articleAndImgSelectResVo.setArticleImgList(articleImgDao.selectList(articleImgSelect));
                    //家族信息
                    articleAndImgSelectResVo.setFamily(familyDao.selectById(articleList.get(i).getFamilyId()));
                    //浏览者点赞信息
                    QueryWrapper<ArticleSupport> articleSupportFind = new QueryWrapper<ArticleSupport>();
                    articleSupportFind.eq("article_id", articleList.get(i).getId());
                    articleSupportFind.eq("user_id", familyUser.getUserId());
                    ArticleSupport articleSupportData = articleSupportDao.selectOne(articleSupportFind);
                    articleAndImgSelectResVo.setArticleSupport(articleSupportData);
                    //作者信息
                    User user = userDao.selectById(articleList.get(i).getUserId());
                    if (user != null) {
                        UserFindResVo userFindResVo = new UserFindResVo();
                        userFindResVo.setAvatar(user.getAvatar());
                        userFindResVo.setNickName(user.getNickName());
                        userFindResVo.setRealName(user.getRealName());
                        articleAndImgSelectResVo.setUserFindResVo(userFindResVo);
                    }
                    articleAndImgSelectResVoList.add(articleAndImgSelectResVo);
                    continue;
                }
//                QueryWrapper<UserFamilyFollow> userFamilyFollowFind = new QueryWrapper<UserFamilyFollow>();
//                userFamilyFollowFind.eq("family_id",articleList.get(i).getFamilyId());
//                userFamilyFollowFind.eq("user_id",article.getUserId());
//                userFamilyFollowFind.eq("is_status",1);
//                UserFamilyFollow userFamilyFollow = userFamilyFollowDao.selectOne(userFamilyFollowFind);
//                if(userFamilyFollow!=null){
//                    ArticleAndImgSelectResVo articleAndImgSelectResVo = new ArticleAndImgSelectResVo();
//                    //文章内容
//                    articleAndImgSelectResVo.setArticle(articleList.get(i));
//                    //文章图片
//                    QueryWrapper<ArticleImg> articleImgSelect = new QueryWrapper<ArticleImg>();
//                    articleImgSelect.eq("article_id", articleList.get(i).getId());
//                    articleAndImgSelectResVo.setArticleImgList(articleImgDao.selectList(articleImgSelect));
//                    //家族信息
//                    articleAndImgSelectResVo.setFamily(familyDao.selectById(articleList.get(i).getFamilyId()));
//                    //浏览者点赞信息
//                    QueryWrapper<ArticleSupport> articleSupportFind = new QueryWrapper<ArticleSupport>();
//                    articleSupportFind.eq("article_id",articleList.get(i).getId());
//                    articleSupportFind.eq("user_id",familyUser.getUserId());
//                    ArticleSupport articleSupportData = articleSupportDao.selectOne(articleSupportFind);
//                    articleAndImgSelectResVo.setArticleSupport(articleSupportData);
//                    //作者信息
//                    User user = userDao.selectById(articleList.get(i).getUserId());
//                    if(user!=null){
//                        UserFindResVo userFindResVo = new UserFindResVo();
//                        userFindResVo.setAvatar(user.getAvatar());
//                        userFindResVo.setNickName(user.getNickName());
//                        userFindResVo.setRealName(user.getRealName());
//                        articleAndImgSelectResVo.setUserFindResVo(userFindResVo);
//                    }
//                    articleAndImgSelectResVoList.add(articleAndImgSelectResVo);
//                }

            }
        }
        map.put("articleList", articleAndImgSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult selectArticle(Integer page, Integer limit, Article article, Integer sort) {
        IPage<Article> pageData = new Page<>(page, limit);
        QueryWrapper<Article> articleSelect = new QueryWrapper<Article>();
        //1按浏览量 2点赞量 3评论数
        if (sort != null && sort == 1) {
            articleSelect.orderByDesc("browse_number");
        } else if (sort != null && sort == 2) {
            articleSelect.orderByDesc("praise_number");
        } else if (sort != null && sort == 3) {
            articleSelect.orderByDesc("discuss_number");
        } else if (sort != null && sort == 4) {
            articleSelect.orderByDesc("is_knit");
        } else {
            articleSelect.orderByDesc("id");
        }

        pageData = articleDao.selectPage(pageData, articleSelect);
        List<Article> articleList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        if (articleList == null || articleList.size() == 0) {
            map.put("articleList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        List<ArticleAndImgSelectResVo> articleAndImgSelectResVoList = new ArrayList<ArticleAndImgSelectResVo>();
        for (int i = 0; i < articleList.size(); i++) {
            ArticleAndImgSelectResVo articleAndImgSelectResVo = new ArticleAndImgSelectResVo();
            //文章内容
            articleAndImgSelectResVo.setArticle(articleList.get(i));
            //文章图片
            QueryWrapper<ArticleImg> articleImgSelect = new QueryWrapper<ArticleImg>();
            articleImgSelect.eq("article_id", articleList.get(i).getId());
            articleAndImgSelectResVo.setArticleImgList(articleImgDao.selectList(articleImgSelect));
            //家族信息
            articleAndImgSelectResVo.setFamily(familyDao.selectById(articleList.get(i).getFamilyId()));

            //作者信息
            User user = userDao.selectById(articleList.get(i).getUserId());
            if (user != null) {
                UserFindResVo userFindResVo = new UserFindResVo();
                userFindResVo.setAvatar(user.getAvatar());
                userFindResVo.setNickName(user.getNickName());
                userFindResVo.setRealName(user.getRealName());
                articleAndImgSelectResVo.setUserFindResVo(userFindResVo);
            }
            articleAndImgSelectResVoList.add(articleAndImgSelectResVo);
        }
        map.put("articleList", articleAndImgSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }


    @Override
    public JsonResult selectArticleByFamilyId(Integer page, Integer limit, Article article, FamilyUser familyUser, Integer sort, String searchData) {
        //先查浏览者身份
        QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
        familyUserFind.eq("family_id", article.getFamilyId());
        familyUserFind.eq("user_id", familyUser.getUserId());
        FamilyUser familyUserData = familyUserDao.selectOne(familyUserFind);

        IPage<Article> pageData = new Page<>(page, limit);
        QueryWrapper<Article> articleSelect = new QueryWrapper<Article>();
        if (article != null && article.getUserId() != null) {
            articleSelect.eq("user_id", article.getUserId());
        }
        if (familyUserData == null) {
            articleSelect.eq("open", 3);
        }
        if (familyUserData != null && familyUserData.getRelation() == 3) {
            articleSelect.gt("open", 1);
        }

        articleSelect.eq("family_id", article.getFamilyId());
        //1按浏览量 2点赞量 3评论数
        if (sort != null && sort == 1) {
            articleSelect.orderByDesc("browse_number");
        } else if (sort != null && sort == 2) {
            articleSelect.orderByDesc("praise_number");
        } else if (sort != null && sort == 3) {
            articleSelect.orderByDesc("discuss_number");
        } else if (sort != null && sort == 4) {
            articleSelect.orderByDesc("is_knit");
            articleSelect.orderByDesc("create_time");

        } else {
            articleSelect.orderByDesc("id");
        }

//        QueryWrapper<ArticleShare> articlesharedata = new QueryWrapper<ArticleShare>();
//        articlesharedata.eq("family_id", article.getFamilyId());
//        List<ArticleShare> da = articleShareDao.selectList(articlesharedata);
//        for (int i = 0; i < da.size(); i++) {
//            articleSelect.or().eq("id", da.get(i).getArticleId());
//        }
        articleSelect.like("content", searchData);
        pageData = articleDao.selectPage(pageData, articleSelect);

        List<Article> articleList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        if (articleList == null || articleList.size() == 0) {
            map.put("articleList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        List<ArticleAndImgSelectResVo> articleAndImgSelectResVoList = new ArrayList<ArticleAndImgSelectResVo>();
        for (int i = 0; i < articleList.size(); i++) {
            ArticleAndImgSelectResVo articleAndImgSelectResVo = new ArticleAndImgSelectResVo();
            //文章内容
            articleAndImgSelectResVo.setArticle(articleList.get(i));
            //文章图片
            QueryWrapper<ArticleImg> articleImgSelect = new QueryWrapper<ArticleImg>();
            articleImgSelect.eq("article_id", articleList.get(i).getId());
            articleAndImgSelectResVo.setArticleImgList(articleImgDao.selectList(articleImgSelect));
            //浏览者点赞信息
            QueryWrapper<ArticleSupport> articleSupportFind = new QueryWrapper<ArticleSupport>();
            articleSupportFind.eq("article_id", articleList.get(i).getId());
            articleSupportFind.eq("user_id", familyUser.getUserId());
            ArticleSupport articleSupportData = articleSupportDao.selectOne(articleSupportFind);
            articleAndImgSelectResVo.setArticleSupport(articleSupportData);
            //作者信息
            User user = userDao.selectById(articleList.get(i).getUserId());
            if (user != null) {
                UserFindResVo userFindResVo = new UserFindResVo();
                userFindResVo.setAvatar(user.getAvatar());
                userFindResVo.setNickName(user.getNickName());
                userFindResVo.setRealName(user.getRealName());
                articleAndImgSelectResVo.setUserFindResVo(userFindResVo);
            }
            articleAndImgSelectResVoList.add(articleAndImgSelectResVo);
        }
        map.put("articleList", articleAndImgSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult findArticleById(Article article) {
        //文章内容
        Article articleData = articleDao.selectById(article.getId());
        ArticleFindResVo articleFindResVo = new ArticleFindResVo();
        articleFindResVo.setArticle(articleData);

        //文章图片
        QueryWrapper<ArticleImg> articleImgSelect = new QueryWrapper<ArticleImg>();
        articleImgSelect.eq("article_id", article.getId());
        articleFindResVo.setArticleImgList(articleImgDao.selectList(articleImgSelect));

        //作者信息
        User user = userDao.selectById(articleData.getUserId());
        UserFindResVo userFindResVo = new UserFindResVo();
        ObjectUtil.copyByName(user, userFindResVo);
        articleFindResVo.setUserFindResVo(userFindResVo);

        //浏览者点赞情况
        QueryWrapper<ArticleSupport> articleSupportFind = new QueryWrapper<ArticleSupport>();
        articleSupportFind.eq("article_id", article.getId());
        articleSupportFind.eq("user_id", article.getUserId());
        ArticleSupport articleSupportData = articleSupportDao.selectOne(articleSupportFind);
        articleFindResVo.setArticleSupport(articleSupportData);

        return ResponseUtil.ok("获取成功", articleFindResVo);
    }

    @Override
    public JsonResult updateArticleById(Article article) throws Exception {
        int result = articleDao.updateById(article);
        return result == 0 ? ResponseUtil.fail("修改失败") : ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult updateArticleBrowseNumberById(Article article) throws Exception {

        //再查询文章是否存在
        Article articleData = articleDao.selectById(article.getId());
        if (articleData == null) {
            return ResponseUtil.fail("文章不存在");
        }
        Article articleUpdate = new Article();
        articleUpdate.setId(article.getId());
        articleUpdate.setBrowseNumber(article.getBrowseNumber());
        articleUpdate.setKnitStartTime(article.getKnitStartTime());
        articleUpdate.setKnitEndTime(article.getKnitStartTime() + articleData.getKnitCycle());//刷新加锁倒计时结束日期
        int result = articleDao.updateById(articleUpdate);
        if (result == 0) {
            throw new Exception("修改失败");
        }

        //分别给作者和浏览人下发1米
        User user1 = new User();
        user1.setId(articleData.getUserId());
        user1.setRice(1);
        result = userDao.setInc(user1);
        if (result == 0) {
            throw new Exception("浏览失败");
        }

        User user2 = new User();
        user2.setId(article.getUserId());
        user2.setRice(1);
        result = userDao.setInc(user2);
        if (result == 0) {
            throw new Exception("浏览失败");
        }


        List<RiceRecord> riceRecordList = new ArrayList<RiceRecord>();
        RiceRecord riceRecord1 = new RiceRecord();
        riceRecord1.setUserId(articleData.getUserId());
        riceRecord1.setRice(1);
        riceRecord1.setContent("文章被浏览");
        riceRecord1.setCreateTime(DateUtils.getDateTime());
        riceRecordList.add(riceRecord1);
        RiceRecord riceRecord2 = new RiceRecord();
        riceRecord2.setUserId(article.getUserId());
        riceRecord2.setRice(1);
        riceRecord2.setContent("浏览文章");
        riceRecord2.setCreateTime(DateUtils.getDateTime());
        riceRecordList.add(riceRecord2);

        result = riceRecordDao.insertRiceRecordList(riceRecordList);
        if (result == 0) {
            throw new Exception("浏览失败");
        }

        return result == 0 ? ResponseUtil.fail("修改失败") : ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult updateArticleUnlockById(Article article) throws Exception {
        //看看用户米够不够
        User user = userDao.selectById(article.getUserId());
        if (user == null) {
            return ResponseUtil.fail("解锁失败");
        }

        int multiple = article.getKnitCycle() / 2592000;//倍数

        //500米为基本单位
        if (user.getRice() < 500 * multiple) {
            return ResponseUtil.fail("米不足");
        }

        //这里计算要花的米
        User userUpdate = new User();
        userUpdate.setId(user.getId());
        userUpdate.setRice(500 * multiple);
        int result = userDao.setDec(userUpdate);
        if (result == 0) {
            throw new Exception("解锁失败");
        }

        RiceRecord riceRecord = new RiceRecord();
        riceRecord.setUserId(user.getId());
        riceRecord.setRice(-500 * multiple);
        riceRecord.setContent("解锁支出");
        riceRecord.setCreateTime(DateUtils.getDateTime());
        result = riceRecordDao.insert(riceRecord);
        if (result == 0) {
            throw new Exception("解锁失败");
        }

        //开始解锁
        Article articleUpdate = new Article();
        articleUpdate.setId(article.getId());
        articleUpdate.setKnit(article.getKnit());
        articleUpdate.setKnitCycle(article.getKnitCycle());
        articleUpdate.setKnitStartTime(article.getKnitStartTime());
        articleUpdate.setKnitEndTime(article.getKnitEndTime());
        result = articleDao.updateById(articleUpdate);
        if (result == 0) {
            throw new Exception("解锁失败");
        }
        return ResponseUtil.ok("解锁成功");
    }


    @Override
    public JsonResult deleteArticleById(Integer id) throws Exception {
        //删除帖子
        int result = articleDao.deleteById(id);
        if (result == 0) {
            throw new Exception("删除失败");
        }

        //删除文章图片
        QueryWrapper<ArticleImg> articleImgDelete = new QueryWrapper<ArticleImg>();
        articleImgDelete.eq("article_id", id);
        articleImgDao.delete(articleImgDelete);


        //点赞和评论就不删除了，有点多，避免卡死风险
        return ResponseUtil.ok("删除成功");
    }


    @Override
    public JsonResult selectotherArticle(Integer limit, Integer page, Integer sheuserId, Integer userId) throws Exception {
        QueryWrapper<FamilyUser> familyUserQueryWrapper = new QueryWrapper<FamilyUser>();
        familyUserQueryWrapper.eq("user_id", sheuserId);
        familyUserQueryWrapper.eq("status", 2);
        List<FamilyUser> familyUsers = familyUserDao.selectList(familyUserQueryWrapper);


        QueryWrapper<FamilyUser> familyUserQueryWrapper1 = new QueryWrapper<FamilyUser>();
        familyUserQueryWrapper1.eq("user_id", userId);
        familyUserQueryWrapper1.eq("status", 2);
        List<FamilyUser> familyUsers1 = familyUserDao.selectList(familyUserQueryWrapper1);

        IPage<Article> pageData = new Page<>(page, limit);
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<Article>();
        for (int i = 0; i < familyUsers.size(); i++) {
            for (int j = 0; j < familyUsers1.size(); j++) {
                if (familyUsers.get(i).getFamilyId() == familyUsers1.get(j).getFamilyId()) {
                    articleQueryWrapper.or().eq("family_id", familyUsers.get(i).getFamilyId()).eq("user_id", sheuserId);


                }
            }
        }
        articleQueryWrapper.orderByDesc("is_knit");

        articleQueryWrapper.or().eq("user_id", sheuserId).eq("open", 3);
        articleQueryWrapper.orderByDesc("create_time");

        pageData = articleDao.selectPage(pageData, articleQueryWrapper);
        List<Article> articleList = pageData.getRecords();
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        if (articleList == null || articleList.size() == 0) {
            map.put("articleList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        //拿一下该作者信息，这里因为是一个作者的所有帖子，查一次即可
        UserFindResVo userFindResVo = new UserFindResVo();
        User user = userDao.selectById(sheuserId);
        ObjectUtil.copyByName(user, userFindResVo);

        List<ArticleAndImgSelectResVo> articleAndImgSelectResVoList = new ArrayList<ArticleAndImgSelectResVo>();
        for (int i = 0; i < articleList.size(); i++) {
            ArticleAndImgSelectResVo articleAndImgSelectResVo = new ArticleAndImgSelectResVo();
            //文章内容
            articleAndImgSelectResVo.setArticle(articleList.get(i));
            //文章图片
            QueryWrapper<ArticleImg> articleImgSelect = new QueryWrapper<ArticleImg>();
            articleImgSelect.eq("article_id", articleList.get(i).getId());
            articleAndImgSelectResVo.setArticleImgList(articleImgDao.selectList(articleImgSelect));
            //家族信息
            articleAndImgSelectResVo.setFamily(familyDao.selectById(articleList.get(i).getFamilyId()));
            //点赞信息
            QueryWrapper<ArticleSupport> articleSupportFind = new QueryWrapper<ArticleSupport>();
            articleSupportFind.eq("article_id", articleList.get(i).getId());
            articleSupportFind.eq("user_id", sheuserId);
            ArticleSupport articleSupportData = articleSupportDao.selectOne(articleSupportFind);
            articleAndImgSelectResVo.setArticleSupport(articleSupportData);

            //作者信息
            articleAndImgSelectResVo.setUserFindResVo(userFindResVo);

            articleAndImgSelectResVoList.add(articleAndImgSelectResVo);
        }
        map.put("articleList", articleAndImgSelectResVoList);

        return ResponseUtil.ok("成功", map);

    }

    /**
     * 根据id查询帖子
     *
     * @param id 帖子id
     * @return
     */
    @Override
    public Article selectArticleById(Integer id) {
        return articleDao.selectById(id);
    }
}
