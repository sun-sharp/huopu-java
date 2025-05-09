package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.Article;
import com.wx.genealogy.system.entity.ArticleSupport;
import com.wx.genealogy.system.entity.RiceRecord;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.ArticleDao;
import com.wx.genealogy.system.mapper.ArticleSupportDao;
import com.wx.genealogy.system.mapper.RiceRecordDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.ArticleSupportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.system.vo.res.ArticleSupportAndUserSelectResVo;
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
 * @since 2021-10-15
 */
@Service
public class ArticleSupportServiceImpl extends ServiceImpl<ArticleSupportDao, ArticleSupport> implements ArticleSupportService {

    @Autowired
    private ArticleSupportDao articleSupportDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private RiceRecordDao riceRecordDao;

    @Override
    public JsonResult insertArticleSupport(ArticleSupport articleSupport) throws Exception {
        //先查询是否存在
        QueryWrapper<ArticleSupport> articleSupportFind = new QueryWrapper<ArticleSupport>();
        articleSupportFind.eq("article_id",articleSupport.getArticleId());
        articleSupportFind.eq("user_id",articleSupport.getUserId());
        ArticleSupport articleSupportData = articleSupportDao.selectOne(articleSupportFind);

        //再查询文章是否存在
        Article article = articleDao.selectById(articleSupport.getArticleId());
        if(article==null){
            return ResponseUtil.fail("文章不存在");
        }

        long nowTime = System.currentTimeMillis()/1000;

        //不存在
        if(articleSupportData==null){
            int result=articleSupportDao.insert(articleSupport);
            if(result==0){
                throw new Exception("点赞失败");
            }
            Article articleUpdate = new Article();
            articleUpdate.setId(article.getId());
            articleUpdate.setPraiseNumber(article.getPraiseNumber()+1);
            articleUpdate.setKnitStartTime(nowTime);
            articleUpdate.setKnitEndTime(nowTime+article.getKnitCycle());
            result = articleDao.updateById(articleUpdate);
            if(result==0){
                throw new Exception("点赞失败");
            }

            //如果不存在说明从来没点赞过，这种情况才下发米
            //分别给作者和点赞人下发1米
            User user1 = new User();
            user1.setId(article.getUserId());
            user1.setRice(1);
            result = userDao.setInc(user1);
            if(result==0){
                throw new Exception("点赞失败");
            }

            User user2 = new User();
            user2.setId(articleSupport.getUserId());
            user2.setRice(1);
            result = userDao.setInc(user2);
            if(result==0){
                throw new Exception("点赞失败");
            }

            List<RiceRecord> riceRecordList = new ArrayList<RiceRecord>();
            RiceRecord riceRecord1 = new RiceRecord();
            riceRecord1.setUserId(article.getUserId());
            riceRecord1.setRice(1);
            riceRecord1.setContent("收到点赞");
            riceRecord1.setCreateTime(articleSupport.getCreateTime());
            riceRecordList.add(riceRecord1);
            RiceRecord riceRecord2 = new RiceRecord();
            riceRecord2.setUserId(articleSupport.getUserId());
            riceRecord2.setRice(1);
            riceRecord2.setContent("点赞文章");
            riceRecord2.setCreateTime(articleSupport.getCreateTime());
            riceRecordList.add(riceRecord2);

            result = riceRecordDao.insertRiceRecordList(riceRecordList);
            if(result==0){
                throw new Exception("点赞失败");
            }


            return ResponseUtil.ok("点赞成功");
        }
        //已经存在
        articleSupportData.setStatus(1);
        articleSupportData.setCreateTime(articleSupport.getCreateTime());
        int result=articleSupportDao.updateById(articleSupportData);
        if(result==0){
            throw new Exception("点赞失败");
        }

        Article articleUpdate = new Article();
        articleUpdate.setId(article.getId());
        articleUpdate.setPraiseNumber(article.getPraiseNumber()+1);
        articleUpdate.setKnitStartTime(nowTime);
        articleUpdate.setKnitEndTime(nowTime+article.getKnitCycle());
        result = articleDao.updateById(articleUpdate);
        if(result==0){
            throw new Exception("点赞失败");
        }

        return ResponseUtil.ok("点赞成功");

    }

    @Override
    public JsonResult updateArticleSupportById(ArticleSupport articleSupport) throws Exception {
        int result=articleSupportDao.updateById(articleSupport);
        if(result==0){
            throw new Exception("修改失败");
        }

        Article article = new Article();
        article.setId(articleSupport.getArticleId());
        if(articleSupport.getStatus()==0){
            article.setPraiseNumber(1);
            result = articleDao.setDec(article);
            if(result==0){
                throw new Exception("点赞失败");
            }
        }else{
            article.setPraiseNumber(1);
            result = articleDao.setInc(article);
            if(result==0){
                throw new Exception("点赞失败");
            }
        }


        return ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult selectArticleSupportAndUser(Integer page, Integer limit, ArticleSupport articleSupport) {
        IPage<ArticleSupport> pageData =new Page<>(page, limit);
        QueryWrapper<ArticleSupport> articleSupportSelect = new QueryWrapper<ArticleSupport>();
        articleSupportSelect.eq("article_id",articleSupport.getArticleId());
        articleSupportSelect.eq("is_status",articleSupport.getStatus());

        pageData=articleSupportDao.selectPage(pageData, articleSupportSelect);
        List<ArticleSupport> articleSupportList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if(articleSupportList==null||articleSupportList.size()==0){
            map.put("articleSupportAndUserSelectResVoList", null);
            return ResponseUtil.ok("获取成功",map);
        }

        //有数据，开始准备in查询
        TreeSet<Integer> userIdList = new TreeSet<Integer>();
        for(int i=0;i<articleSupportList.size();i++){
            userIdList.add(articleSupportList.get(i).getUserId());
        }
        List<User> userList=userDao.selectBatchIds(userIdList);

        List<ArticleSupportAndUserSelectResVo> articleSupportAndUserSelectResVoList = new ArrayList<ArticleSupportAndUserSelectResVo>();

        for(int i=0;i<articleSupportList.size();i++){
            ArticleSupportAndUserSelectResVo articleSupportAndUserSelectResVo = new ArticleSupportAndUserSelectResVo();
            for(int j=0;j<userList.size();j++){
                if(articleSupportList.get(i).getUserId().equals(userList.get(j).getId())){
                    articleSupportAndUserSelectResVo.setArticleSupport(articleSupportList.get(i));
                    articleSupportAndUserSelectResVo.setUser(userList.get(j));
                    break;
                }
            }
            articleSupportAndUserSelectResVoList.add(articleSupportAndUserSelectResVo);
        }

        map.put("articleSupportAndUserSelectResVoList", articleSupportAndUserSelectResVoList);
        return ResponseUtil.ok("获取成功",map);
    }

    @Override
    public JsonResult findArticleSupport(ArticleSupport articleSupport) {
        QueryWrapper<ArticleSupport> articleSupportFind = new QueryWrapper<ArticleSupport>();
        articleSupportFind.eq("article_id",articleSupport.getArticleId());
        articleSupportFind.eq("user_id",articleSupport.getUserId());
        ArticleSupport articleSupportData = articleSupportDao.selectOne(articleSupportFind);
        return ResponseUtil.ok("获取成功",articleSupportData);
    }
}
