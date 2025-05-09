package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.Video;
import com.wx.genealogy.system.entity.VideoDiscuss;
import com.wx.genealogy.system.entity.RiceRecord;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.VideoDao;
import com.wx.genealogy.system.mapper.VideoDiscussDao;
import com.wx.genealogy.system.mapper.RiceRecordDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.VideoDiscussService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.system.vo.res.VideoDiscussAndUserFindResVo;
import com.wx.genealogy.system.vo.res.VideoDiscussAndUserSelectResVo;
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
public class VideoDiscussServiceImpl extends ServiceImpl<VideoDiscussDao, VideoDiscuss> implements VideoDiscussService {

    @Autowired
    private VideoDiscussDao videoDiscussDao;

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RiceRecordDao riceRecordDao;

    @Override
    public JsonResult insertVideoDiscuss(VideoDiscuss videoDiscuss) throws Exception {
        //不能在同一个帖子回复同一样的内容
        QueryWrapper<VideoDiscuss> videoDiscussFind = new QueryWrapper<VideoDiscuss>();
        videoDiscussFind.eq("video_id",videoDiscuss.getVideoId());
        videoDiscussFind.eq("user_id",videoDiscuss.getUserId());
        videoDiscussFind.eq("content",videoDiscuss.getContent());
        VideoDiscuss videoDiscussData =videoDiscussDao.selectOne(videoDiscussFind);
        if(videoDiscussData!=null){
            return ResponseUtil.fail("请不要发布重复内容");
        }

        //查出帖子
        Video video = videoDao.selectById(videoDiscuss.getVideoId());
        if(video==null){
            return ResponseUtil.fail("文章不存在");
        }

        //插入评论
        int result=videoDiscussDao.insert(videoDiscuss);
        if(result==0){
            throw new Exception("评论失败");
        }

        //修改文章评论数+1
        Video videoUpdate = new Video();
        videoUpdate.setId(video.getId());
        videoUpdate.setDiscussNumber(video.getDiscussNumber()+1);
        result = videoDao.updateById(videoUpdate);
        if(result==0){
            throw new Exception("评论失败");
        }

        //分别给作者和评论人下发2米
        User user1 = new User();
        user1.setId(video.getUserId());
        user1.setRice(2);
        result = userDao.setInc(user1);
        if(result==0){
            throw new Exception("评论失败");
        }

        User user2 = new User();
        user2.setId(videoDiscuss.getUserId());
        user2.setRice(2);
        result = userDao.setInc(user2);
        if(result==0){
            throw new Exception("评论失败");
        }


        List<RiceRecord> riceRecordList = new ArrayList<RiceRecord>();
        RiceRecord riceRecord1 = new RiceRecord();
        riceRecord1.setUserId(video.getUserId());
        riceRecord1.setRice(2);
        riceRecord1.setContent("收获评论");
        riceRecord1.setCreateTime(videoDiscuss.getCreateTime());
        riceRecordList.add(riceRecord1);
        RiceRecord riceRecord2 = new RiceRecord();
        riceRecord2.setUserId(videoDiscuss.getUserId());
        riceRecord2.setRice(2);
        riceRecord2.setContent("发表评论");
        riceRecord2.setCreateTime(videoDiscuss.getCreateTime());
        riceRecordList.add(riceRecord2);

        result = riceRecordDao.insertRiceRecordList(riceRecordList);
        if(result==0){
            throw new Exception("评论失败");
        }

        return ResponseUtil.ok("评论成功");
    }

    @Override
    public JsonResult selectVideoDiscussAndUser(Integer page, Integer limit, VideoDiscuss videoDiscuss) {
        //查询文章信息
        Video videoData = videoDao.selectById(videoDiscuss.getVideoId());
        if(videoData==null){
            return ResponseUtil.fail("文章信息不存在");
        }
        //查询作者信息
        User userData = userDao.selectById(videoData.getUserId());
        UserFindResVo userFindResVo1 = new UserFindResVo();
        userFindResVo1.setId(userData.getId());
        userFindResVo1.setAvatar(userData.getAvatar());
        userFindResVo1.setNickName(userData.getNickName());
        userFindResVo1.setRealName(userData.getRealName());

        //获取一级评论
        IPage<VideoDiscuss> pageData =new Page<>(page, limit);
        QueryWrapper<VideoDiscuss> videoDiscussSelect = new QueryWrapper<VideoDiscuss>();
        videoDiscussSelect.eq("video_id",videoDiscuss.getVideoId());
        videoDiscussSelect.eq("level",1);
        videoDiscussSelect.orderByDesc("id");
        pageData=videoDiscussDao.selectPage(pageData, videoDiscussSelect);
        List<VideoDiscuss> videoDiscussList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if(videoDiscussList==null||videoDiscussList.size()==0){
            map.put("videoDiscussAndUserSelectResVoList", null);
            return ResponseUtil.ok("获取成功",map);
        }

        List<VideoDiscussAndUserSelectResVo> videoDiscussAndUserSelectResVoList = new ArrayList<VideoDiscussAndUserSelectResVo>();

        for(int i = 0;i<videoDiscussList.size();i++){
            VideoDiscussAndUserSelectResVo videoDiscussAndUserSelectResVo = new VideoDiscussAndUserSelectResVo();

            /******一级评论*******/
            VideoDiscussAndUserFindResVo videoDiscussAndUserFindResVo1 = new VideoDiscussAndUserFindResVo();
            //查询一级评论者信息
            userData = userDao.selectById(videoDiscussList.get(i).getUserId());
            UserFindResVo userFindResVo2 = new UserFindResVo();
            userFindResVo2.setId(userData.getId());
            userFindResVo2.setAvatar(userData.getAvatar());
            userFindResVo2.setNickName(userData.getNickName());
            userFindResVo2.setRealName(userData.getRealName());
            //一级评论组装完成
            videoDiscussAndUserFindResVo1.setUserFindResVo(userFindResVo2);
            videoDiscussAndUserFindResVo1.setVideoDiscuss(videoDiscussList.get(i));
            videoDiscussAndUserSelectResVo.setVideoDiscussAndUserFindResVo(videoDiscussAndUserFindResVo1);

            /***********二级评论*************/
            videoDiscussSelect = new QueryWrapper<VideoDiscuss>();
            videoDiscussSelect.eq("video_id",videoDiscussList.get(i).getVideoId());
            videoDiscussSelect.eq("video_discuss_id",videoDiscussList.get(i).getId());
            videoDiscussSelect.eq("level",2);
            videoDiscussSelect.orderByAsc("id");
            List<VideoDiscuss> videoDiscusses = videoDiscussDao.selectList(videoDiscussSelect);

            List<VideoDiscussAndUserFindResVo> videoDiscussAndUserFindResVoList = new ArrayList<VideoDiscussAndUserFindResVo>();
            for(int j = 0;j<videoDiscusses.size();j++){
                VideoDiscussAndUserFindResVo videoDiscussAndUserFindResVo2 = new VideoDiscussAndUserFindResVo();
                if(videoDiscusses.get(j).getUserId().equals(userFindResVo1.getId())){
                    videoDiscussAndUserFindResVo2.setUserFindResVo(userFindResVo1);
                }
                if(videoDiscusses.get(j).getUserId().equals(userFindResVo2.getId())){
                    videoDiscussAndUserFindResVo2.setUserFindResVo(userFindResVo2);
                }
                videoDiscussAndUserFindResVo2.setVideoDiscuss(videoDiscusses.get(j));
                videoDiscussAndUserFindResVoList.add(videoDiscussAndUserFindResVo2);
            }

            videoDiscussAndUserSelectResVo.setVideoDiscussAndUserFindResVoList(videoDiscussAndUserFindResVoList);
            videoDiscussAndUserSelectResVoList.add(videoDiscussAndUserSelectResVo);
        }

        map.put("videoDiscussAndUserSelectResVoList", videoDiscussAndUserSelectResVoList);
        return ResponseUtil.ok("获取成功",map);
    }
}
