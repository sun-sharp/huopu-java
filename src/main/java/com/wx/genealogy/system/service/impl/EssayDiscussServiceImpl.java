package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.Essay;
import com.wx.genealogy.system.entity.EssayDiscuss;
import com.wx.genealogy.system.entity.RiceRecord;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.EssayDao;
import com.wx.genealogy.system.mapper.EssayDiscussDao;
import com.wx.genealogy.system.mapper.RiceRecordDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.EssayDiscussService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.system.vo.res.EssayDiscussAndUserFindResVo;
import com.wx.genealogy.system.vo.res.EssayDiscussAndUserSelectResVo;
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
public class EssayDiscussServiceImpl extends ServiceImpl<EssayDiscussDao, EssayDiscuss> implements EssayDiscussService {

    @Autowired
    private EssayDiscussDao essayDiscussDao;

    @Autowired
    private EssayDao essayDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RiceRecordDao riceRecordDao;

    @Override
    public JsonResult insertEssayDiscuss(EssayDiscuss essayDiscuss) throws Exception {
        //不能在同一个帖子回复同一样的内容
        QueryWrapper<EssayDiscuss> essayDiscussFind = new QueryWrapper<EssayDiscuss>();
        essayDiscussFind.eq("essay_id",essayDiscuss.getEssayId());
        essayDiscussFind.eq("user_id",essayDiscuss.getUserId());
        essayDiscussFind.eq("content",essayDiscuss.getContent());
        EssayDiscuss essayDiscussData =essayDiscussDao.selectOne(essayDiscussFind);
        if(essayDiscussData!=null){
            return ResponseUtil.fail("请不要发布重复内容");
        }

        //查出帖子
        Essay essay = essayDao.selectById(essayDiscuss.getEssayId());
        if(essay==null){
            return ResponseUtil.fail("文章不存在");
        }

        //插入评论
        int result=essayDiscussDao.insert(essayDiscuss);
        if(result==0){
            throw new Exception("评论失败");
        }

        //修改文章评论数+1
        Essay essayUpdate = new Essay();
        essayUpdate.setId(essay.getId());
        essayUpdate.setDiscussNumber(essay.getDiscussNumber()+1);
        result = essayDao.updateById(essayUpdate);
        if(result==0){
            throw new Exception("评论失败");
        }

        //分别给作者和评论人下发2米
        User user1 = new User();
        user1.setId(essay.getUserId());
        user1.setRice(2);
        result = userDao.setInc(user1);
        if(result==0){
            throw new Exception("评论失败");
        }

        User user2 = new User();
        user2.setId(essayDiscuss.getUserId());
        user2.setRice(2);
        result = userDao.setInc(user2);
        if(result==0){
            throw new Exception("评论失败");
        }


        List<RiceRecord> riceRecordList = new ArrayList<RiceRecord>();
        RiceRecord riceRecord1 = new RiceRecord();
        riceRecord1.setUserId(essay.getUserId());
        riceRecord1.setRice(2);
        riceRecord1.setContent("收获评论");
        riceRecord1.setCreateTime(essayDiscuss.getCreateTime());
        riceRecordList.add(riceRecord1);
        RiceRecord riceRecord2 = new RiceRecord();
        riceRecord2.setUserId(essayDiscuss.getUserId());
        riceRecord2.setRice(2);
        riceRecord2.setContent("发表评论");
        riceRecord2.setCreateTime(essayDiscuss.getCreateTime());
        riceRecordList.add(riceRecord2);

        result = riceRecordDao.insertRiceRecordList(riceRecordList);
        if(result==0){
            throw new Exception("评论失败");
        }

        return ResponseUtil.ok("评论成功");
    }

    @Override
    public JsonResult selectEssayDiscussAndUser(Integer page, Integer limit, EssayDiscuss essayDiscuss) {
        //查询文章信息
        Essay essayData = essayDao.selectById(essayDiscuss.getEssayId());
        if(essayData==null){
            return ResponseUtil.fail("文章信息不存在");
        }
        //查询作者信息
        User userData = userDao.selectById(essayData.getUserId());
        UserFindResVo userFindResVo1 = new UserFindResVo();
        userFindResVo1.setId(userData.getId());
        userFindResVo1.setAvatar(userData.getAvatar());
        userFindResVo1.setNickName(userData.getNickName());
        userFindResVo1.setRealName(userData.getRealName());

        //获取一级评论
        IPage<EssayDiscuss> pageData =new Page<>(page, limit);
        QueryWrapper<EssayDiscuss> essayDiscussSelect = new QueryWrapper<EssayDiscuss>();
        essayDiscussSelect.eq("essay_id",essayDiscuss.getEssayId());
        essayDiscussSelect.eq("level",1);
        essayDiscussSelect.orderByDesc("id");
        pageData=essayDiscussDao.selectPage(pageData, essayDiscussSelect);
        List<EssayDiscuss> essayDiscussList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if(essayDiscussList==null||essayDiscussList.size()==0){
            map.put("essayDiscussAndUserSelectResVoList", null);
            return ResponseUtil.ok("获取成功",map);
        }

        List<EssayDiscussAndUserSelectResVo> essayDiscussAndUserSelectResVoList = new ArrayList<EssayDiscussAndUserSelectResVo>();

        for(int i = 0;i<essayDiscussList.size();i++){
            EssayDiscussAndUserSelectResVo essayDiscussAndUserSelectResVo = new EssayDiscussAndUserSelectResVo();

            /******一级评论*******/
            EssayDiscussAndUserFindResVo essayDiscussAndUserFindResVo1 = new EssayDiscussAndUserFindResVo();
            //查询一级评论者信息
            userData = userDao.selectById(essayDiscussList.get(i).getUserId());
            UserFindResVo userFindResVo2 = new UserFindResVo();
            userFindResVo2.setId(userData.getId());
            userFindResVo2.setAvatar(userData.getAvatar());
            userFindResVo2.setNickName(userData.getNickName());
            userFindResVo2.setRealName(userData.getRealName());
            //一级评论组装完成
            essayDiscussAndUserFindResVo1.setUserFindResVo(userFindResVo2);
            essayDiscussAndUserFindResVo1.setEssayDiscuss(essayDiscussList.get(i));
            essayDiscussAndUserSelectResVo.setEssayDiscussAndUserFindResVo(essayDiscussAndUserFindResVo1);

            /***********二级评论*************/
            essayDiscussSelect = new QueryWrapper<EssayDiscuss>();
            essayDiscussSelect.eq("essay_id",essayDiscussList.get(i).getEssayId());
            essayDiscussSelect.eq("essay_discuss_id",essayDiscussList.get(i).getId());
            essayDiscussSelect.eq("level",2);
            essayDiscussSelect.orderByAsc("id");
            List<EssayDiscuss> essayDiscusses = essayDiscussDao.selectList(essayDiscussSelect);

            List<EssayDiscussAndUserFindResVo> essayDiscussAndUserFindResVoList = new ArrayList<EssayDiscussAndUserFindResVo>();
            for(int j = 0;j<essayDiscusses.size();j++){
                EssayDiscussAndUserFindResVo essayDiscussAndUserFindResVo2 = new EssayDiscussAndUserFindResVo();
                if(essayDiscusses.get(j).getUserId().equals(userFindResVo1.getId())){
                    essayDiscussAndUserFindResVo2.setUserFindResVo(userFindResVo1);
                }
                if(essayDiscusses.get(j).getUserId().equals(userFindResVo2.getId())){
                    essayDiscussAndUserFindResVo2.setUserFindResVo(userFindResVo2);
                }
                essayDiscussAndUserFindResVo2.setEssayDiscuss(essayDiscusses.get(j));
                essayDiscussAndUserFindResVoList.add(essayDiscussAndUserFindResVo2);
            }

            essayDiscussAndUserSelectResVo.setEssayDiscussAndUserFindResVoList(essayDiscussAndUserFindResVoList);
            essayDiscussAndUserSelectResVoList.add(essayDiscussAndUserSelectResVo);
        }

        map.put("essayDiscussAndUserSelectResVoList", essayDiscussAndUserSelectResVoList);
        return ResponseUtil.ok("获取成功",map);
    }
}
