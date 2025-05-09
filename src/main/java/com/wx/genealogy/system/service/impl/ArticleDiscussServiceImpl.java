package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.Article;
import com.wx.genealogy.system.entity.ArticleDiscuss;
import com.wx.genealogy.system.entity.RiceRecord;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.ArticleDao;
import com.wx.genealogy.system.mapper.ArticleDiscussDao;
import com.wx.genealogy.system.mapper.RiceRecordDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.ArticleDiscussService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.system.vo.res.ArticleDiscussAndUserFindResVo;
import com.wx.genealogy.system.vo.res.ArticleDiscussAndUserSelectResVo;
import com.wx.genealogy.system.vo.res.UserFindResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-10-18
 */
@Service
public class ArticleDiscussServiceImpl extends ServiceImpl<ArticleDiscussDao, ArticleDiscuss> implements ArticleDiscussService {

    @Autowired
    private ArticleDiscussDao articleDiscussDao;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RiceRecordDao riceRecordDao;

    @Override
    public JsonResult insertArticleDiscuss(ArticleDiscuss articleDiscuss) throws Exception {
        //不能在同一个帖子回复同一样的内容
        QueryWrapper<ArticleDiscuss> articleDiscussFind = new QueryWrapper<ArticleDiscuss>();
        articleDiscussFind.eq("article_id",articleDiscuss.getArticleId());
        articleDiscussFind.eq("user_id",articleDiscuss.getUserId());
        articleDiscussFind.eq("content",articleDiscuss.getContent());
        ArticleDiscuss articleDiscussData =articleDiscussDao.selectOne(articleDiscussFind);
        if(articleDiscussData!=null){
            return ResponseUtil.fail("请不要发布重复内容");
        }

        //查出帖子
        Article article = articleDao.selectById(articleDiscuss.getArticleId());
        if(article==null){
            return ResponseUtil.fail("文章不存在");
        }

        //插入评论
        int result=articleDiscussDao.insert(articleDiscuss);
        if(result==0){
            throw new Exception("评论失败");
        }

        //修改文章评论数+1
        Article articleUpdate = new Article();
        articleUpdate.setId(article.getId());
        articleUpdate.setDiscussNumber(article.getDiscussNumber()+1);
        result = articleDao.updateById(articleUpdate);
        if(result==0){
            throw new Exception("评论失败");
        }

        //分别给作者和评论人下发2米
        User user1 = new User();
        user1.setId(article.getUserId());
        user1.setRice(2);
        result = userDao.setInc(user1);
        if(result==0){
            throw new Exception("评论失败");
        }

        User user2 = new User();
        user2.setId(articleDiscuss.getUserId());
        user2.setRice(2);
        result = userDao.setInc(user2);
        if(result==0){
            throw new Exception("评论失败");
        }


        List<RiceRecord> riceRecordList = new ArrayList<RiceRecord>();
        RiceRecord riceRecord1 = new RiceRecord();
        riceRecord1.setUserId(article.getUserId());
        riceRecord1.setRice(2);
        riceRecord1.setContent("收获评论");
        riceRecord1.setCreateTime(articleDiscuss.getCreateTime());
        riceRecordList.add(riceRecord1);
        RiceRecord riceRecord2 = new RiceRecord();
        riceRecord2.setUserId(articleDiscuss.getUserId());
        riceRecord2.setRice(2);
        riceRecord2.setContent("发表评论");
        riceRecord2.setCreateTime(articleDiscuss.getCreateTime());
        riceRecordList.add(riceRecord2);

        result = riceRecordDao.insertRiceRecordList(riceRecordList);
        if(result==0){
            throw new Exception("评论失败");
        }

        return ResponseUtil.ok("评论成功");
    }

    @Override
    public JsonResult selectArticleDiscussAndUser(Integer page, Integer limit, ArticleDiscuss articleDiscuss) {
        //查询文章信息
        Article articleData = articleDao.selectById(articleDiscuss.getArticleId());
        if(articleData==null){
            return ResponseUtil.fail("文章信息不存在");
        }
        //查询作者信息
        User userData = userDao.selectById(articleData.getUserId());
        UserFindResVo userFindResVo1 = new UserFindResVo();
        userFindResVo1.setId(userData.getId());
        userFindResVo1.setAvatar(userData.getAvatar());
        userFindResVo1.setNickName(userData.getNickName());
        userFindResVo1.setRealName(userData.getRealName());

        //获取一级评论
        IPage<ArticleDiscuss> pageData =new Page<>(page, limit);
        QueryWrapper<ArticleDiscuss> articleDiscussSelect = new QueryWrapper<ArticleDiscuss>();
        articleDiscussSelect.eq("article_id",articleDiscuss.getArticleId());
        articleDiscussSelect.eq("level",1);
        articleDiscussSelect.orderByDesc("id");
        pageData=articleDiscussDao.selectPage(pageData, articleDiscussSelect);
        List<ArticleDiscuss> articleDiscussList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if(articleDiscussList==null||articleDiscussList.size()==0){
            map.put("articleDiscussAndUserSelectResVoList", null);
            return ResponseUtil.ok("获取成功",map);
        }

        List<ArticleDiscussAndUserSelectResVo> articleDiscussAndUserSelectResVoList = new ArrayList<ArticleDiscussAndUserSelectResVo>();

        for(int i = 0;i<articleDiscussList.size();i++){
            ArticleDiscussAndUserSelectResVo articleDiscussAndUserSelectResVo = new ArticleDiscussAndUserSelectResVo();

            /******一级评论*******/
            ArticleDiscussAndUserFindResVo articleDiscussAndUserFindResVo1 = new ArticleDiscussAndUserFindResVo();
            //查询一级评论者信息
            userData = userDao.selectById(articleDiscussList.get(i).getUserId());
            UserFindResVo userFindResVo2 = new UserFindResVo();
            userFindResVo2.setId(userData.getId());
            userFindResVo2.setAvatar(userData.getAvatar());
            userFindResVo2.setNickName(userData.getNickName());
            userFindResVo2.setRealName(userData.getRealName());
            //一级评论组装完成
            articleDiscussAndUserFindResVo1.setUserFindResVo(userFindResVo2);
            articleDiscussAndUserFindResVo1.setArticleDiscuss(articleDiscussList.get(i));
            articleDiscussAndUserSelectResVo.setArticleDiscussAndUserFindResVo(articleDiscussAndUserFindResVo1);

            /***********二级评论*************/
            articleDiscussSelect = new QueryWrapper<ArticleDiscuss>();
            articleDiscussSelect.eq("article_id",articleDiscussList.get(i).getArticleId());
            articleDiscussSelect.eq("article_discuss_id",articleDiscussList.get(i).getId());
            articleDiscussSelect.eq("level",2);
            articleDiscussSelect.orderByAsc("id");
            List<ArticleDiscuss> articleDiscusses = articleDiscussDao.selectList(articleDiscussSelect);

            List<ArticleDiscussAndUserFindResVo> articleDiscussAndUserFindResVoList = new ArrayList<ArticleDiscussAndUserFindResVo>();
            for(int j = 0;j<articleDiscusses.size();j++){
                ArticleDiscussAndUserFindResVo articleDiscussAndUserFindResVo2 = new ArticleDiscussAndUserFindResVo();
                if(articleDiscusses.get(j).getUserId().equals(userFindResVo1.getId())){
                    articleDiscussAndUserFindResVo2.setUserFindResVo(userFindResVo1);
                }
                if(articleDiscusses.get(j).getUserId().equals(userFindResVo2.getId())){
                    articleDiscussAndUserFindResVo2.setUserFindResVo(userFindResVo2);
                }
                articleDiscussAndUserFindResVo2.setArticleDiscuss(articleDiscusses.get(j));
                articleDiscussAndUserFindResVoList.add(articleDiscussAndUserFindResVo2);
            }

            articleDiscussAndUserSelectResVo.setArticleDiscussAndUserFindResVoList(articleDiscussAndUserFindResVoList);
            articleDiscussAndUserSelectResVoList.add(articleDiscussAndUserSelectResVo);
        }

        map.put("articleDiscussAndUserSelectResVoList", articleDiscussAndUserSelectResVoList);
        return ResponseUtil.ok("获取成功",map);
    }
}
