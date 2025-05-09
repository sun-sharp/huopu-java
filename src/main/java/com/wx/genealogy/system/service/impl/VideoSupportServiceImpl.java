package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.Video;
import com.wx.genealogy.system.entity.VideoSupport;
import com.wx.genealogy.system.entity.RiceRecord;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.VideoDao;
import com.wx.genealogy.system.mapper.VideoSupportDao;
import com.wx.genealogy.system.mapper.RiceRecordDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.VideoSupportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.system.vo.res.VideoSupportAndUserSelectResVo;
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
public class VideoSupportServiceImpl extends ServiceImpl<VideoSupportDao, VideoSupport> implements VideoSupportService {

    @Autowired
    private VideoSupportDao videoSupportDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private RiceRecordDao riceRecordDao;

    @Override
    public JsonResult insertVideoSupport(VideoSupport videoSupport) throws Exception {
        //先查询是否存在
        QueryWrapper<VideoSupport> videoSupportFind = new QueryWrapper<VideoSupport>();
        videoSupportFind.eq("video_id",videoSupport.getVideoId());
        videoSupportFind.eq("user_id",videoSupport.getUserId());
        VideoSupport videoSupportData = videoSupportDao.selectOne(videoSupportFind);

        //再查询文章是否存在
        Video video = videoDao.selectById(videoSupport.getVideoId());
        if(video==null){
            return ResponseUtil.fail("文章不存在");
        }

        long nowTime = System.currentTimeMillis()/1000;

        //不存在
        if(videoSupportData==null){
            int result=videoSupportDao.insert(videoSupport);
            if(result==0){
                throw new Exception("点赞失败");
            }
            Video videoUpdate = new Video();
            videoUpdate.setId(video.getId());
            videoUpdate.setPraiseNumber(video.getPraiseNumber()+1);
            videoUpdate.setKnitStartTime(nowTime);
            videoUpdate.setKnitEndTime(nowTime+video.getKnitCycle());
            result = videoDao.updateById(videoUpdate);
            if(result==0){
                throw new Exception("点赞失败");
            }

            //如果不存在说明从来没点赞过，这种情况才下发米
            //分别给作者和点赞人下发1米
            User user1 = new User();
            user1.setId(video.getUserId());
            user1.setRice(1);
            result = userDao.setInc(user1);
            if(result==0){
                throw new Exception("点赞失败");
            }

            User user2 = new User();
            user2.setId(videoSupport.getUserId());
            user2.setRice(1);
            result = userDao.setInc(user2);
            if(result==0){
                throw new Exception("点赞失败");
            }

            List<RiceRecord> riceRecordList = new ArrayList<RiceRecord>();
            RiceRecord riceRecord1 = new RiceRecord();
            riceRecord1.setUserId(video.getUserId());
            riceRecord1.setRice(1);
            riceRecord1.setContent("收到点赞");
            riceRecord1.setCreateTime(videoSupport.getCreateTime());
            riceRecordList.add(riceRecord1);
            RiceRecord riceRecord2 = new RiceRecord();
            riceRecord2.setUserId(videoSupport.getUserId());
            riceRecord2.setRice(1);
            riceRecord2.setContent("点赞文章");
            riceRecord2.setCreateTime(videoSupport.getCreateTime());
            riceRecordList.add(riceRecord2);

            result = riceRecordDao.insertRiceRecordList(riceRecordList);
            if(result==0){
                throw new Exception("点赞失败");
            }


            return ResponseUtil.ok("点赞成功");
        }
        //已经存在
        videoSupportData.setStatus(1);
        videoSupportData.setCreateTime(videoSupport.getCreateTime());
        int result=videoSupportDao.updateById(videoSupportData);
        if(result==0){
            throw new Exception("点赞失败");
        }

        Video videoUpdate = new Video();
        videoUpdate.setId(video.getId());
        videoUpdate.setPraiseNumber(video.getPraiseNumber()+1);
        videoUpdate.setKnitStartTime(nowTime);
        videoUpdate.setKnitEndTime(nowTime+video.getKnitCycle());
        result = videoDao.updateById(videoUpdate);
        if(result==0){
            throw new Exception("点赞失败");
        }

        return ResponseUtil.ok("点赞成功");

    }

    @Override
    public JsonResult updateVideoSupportById(VideoSupport videoSupport) throws Exception {
        int result=videoSupportDao.updateById(videoSupport);
        if(result==0){
            throw new Exception("修改失败");
        }

        Video video = new Video();
        video.setId(videoSupport.getVideoId());
        if(videoSupport.getStatus()==0){
            video.setPraiseNumber(1);
            result = videoDao.setDec(video);
            if(result==0){
                throw new Exception("点赞失败");
            }
        }else{
            video.setPraiseNumber(1);
            result = videoDao.setInc(video);
            if(result==0){
                throw new Exception("点赞失败");
            }
        }


        return ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult selectVideoSupportAndUser(Integer page, Integer limit, VideoSupport videoSupport) {
        IPage<VideoSupport> pageData =new Page<>(page, limit);
        QueryWrapper<VideoSupport> videoSupportSelect = new QueryWrapper<VideoSupport>();
        videoSupportSelect.eq("video_id",videoSupport.getVideoId());
        videoSupportSelect.eq("is_status",videoSupport.getStatus());

        pageData=videoSupportDao.selectPage(pageData, videoSupportSelect);
        List<VideoSupport> videoSupportList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if(videoSupportList==null||videoSupportList.size()==0){
            map.put("videoSupportAndUserSelectResVoList", null);
            return ResponseUtil.ok("获取成功",map);
        }

        //有数据，开始准备in查询
        TreeSet<Integer> userIdList = new TreeSet<Integer>();
        for(int i=0;i<videoSupportList.size();i++){
            userIdList.add(videoSupportList.get(i).getUserId());
        }
        List<User> userList=userDao.selectBatchIds(userIdList);

        List<VideoSupportAndUserSelectResVo> videoSupportAndUserSelectResVoList = new ArrayList<VideoSupportAndUserSelectResVo>();

        for(int i=0;i<videoSupportList.size();i++){
            VideoSupportAndUserSelectResVo videoSupportAndUserSelectResVo = new VideoSupportAndUserSelectResVo();
            for(int j=0;j<userList.size();j++){
                if(videoSupportList.get(i).getUserId().equals(userList.get(j).getId())){
                    videoSupportAndUserSelectResVo.setVideoSupport(videoSupportList.get(i));
                    videoSupportAndUserSelectResVo.setUser(userList.get(j));
                    break;
                }
            }
            videoSupportAndUserSelectResVoList.add(videoSupportAndUserSelectResVo);
        }

        map.put("videoSupportAndUserSelectResVoList", videoSupportAndUserSelectResVoList);
        return ResponseUtil.ok("获取成功",map);
    }

    @Override
    public JsonResult findVideoSupport(VideoSupport videoSupport) {
        QueryWrapper<VideoSupport> videoSupportFind = new QueryWrapper<VideoSupport>();
        videoSupportFind.eq("video_id",videoSupport.getVideoId());
        videoSupportFind.eq("user_id",videoSupport.getUserId());
        VideoSupport videoSupportData = videoSupportDao.selectOne(videoSupportFind);
        return ResponseUtil.ok("获取成功",videoSupportData);
    }
}
