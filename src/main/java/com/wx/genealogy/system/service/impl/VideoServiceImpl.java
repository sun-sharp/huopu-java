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
import com.wx.genealogy.system.service.VideoService;
import com.wx.genealogy.system.vo.res.VideoAndImgSelectResVo;
import com.wx.genealogy.system.vo.res.VideoFindResVo;
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
public class VideoServiceImpl extends ServiceImpl<VideoDao, Video> implements VideoService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private VideoDao videoDao;
  //  @Autowired
  //  private VideoShareDao videoShareDao;

    @Autowired
    private VideoImgDao videoImgDao;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private FamilyUserDao familyUserDao;

    @Autowired
    private UserFamilyFollowDao userFamilyFollowDao;

    @Autowired
    private VideoSupportDao videoSupportDao;

    @Autowired
    private RiceRecordDao riceRecordDao;
    @Autowired
    private FamilyMessageDao familyMessageDao;

    @Override
    @Transactional
    public JsonResult insertVideo(Video video, List<VideoImg> videoImgList) throws Exception {

        //判断是不是管理员才可以发
        LambdaQueryWrapper<FamilyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyUser::getFamilyId, video.getFamilyId());
        wrapper.eq(FamilyUser::getUserId, video.getUserId());
        FamilyUser familyUser = this.familyUserDao.selectOne(wrapper);
//        if (Objects.isNull(familyUser)) {
//            return ResponseUtil.fail("未查询到您的管理员身份,不可发布");
//        }
//        // 身份等级：1是创建者2是管理员3是会员
//        if (familyUser.getLevel() == 3) {
//            return ResponseUtil.fail("未查询到您的管理员身份,不可发布");
//        }

        int result = videoDao.insert(video);


        if (result == 0) {
            throw new Exception("发布失败");
        }
        //添加文章消息
        //1.查找家族有哪些成员的

        QueryWrapper<FamilyUser> familyuser_find = new QueryWrapper<FamilyUser>();
        familyuser_find.eq("family_id", video.getFamilyId());
        familyuser_find.gt("user_id", 0);
        List<FamilyUser> family_find = familyUserDao.selectList(familyuser_find);

        for (int i = 0; i < family_find.size(); i++) {
            QueryWrapper<FamilyMessage> familymessage = new QueryWrapper<FamilyMessage>();
            familymessage.eq("family_id", video.getFamilyId());
            familymessage.eq("user_id", family_find.get(i).getUserId());
            FamilyMessage familydata = familyMessageDao.selectOne(familymessage);

            if (familydata == null) {

                FamilyMessage familyMessage = new FamilyMessage();
                familyMessage.setVideoMessage(1);
                familyMessage.setFamilyId(video.getFamilyId());
                familyMessage.setUserId(family_find.get(i).getUserId());

                familyMessageDao.insert(familyMessage);
            } else {


                FamilyMessage familyMessage = new FamilyMessage();
                familyMessage.setId(familydata.getId());
                //familyMessage.setVideoMessage(familydata.getVideoMessage() + 1);
                familyMessage.setVideoMessage(familydata.getVideoMessage() != null ? familydata.getVideoMessage() + 1 : 1);
                familyMessage.setFamilyId(video.getFamilyId());
                familyMessage.setUserId(family_find.get(i).getUserId());

              familyMessageDao.updateById(familyMessage);
            }


            FamilyUser fami = new FamilyUser();
            fami.setId(family_find.get(i).getId());
            fami.setUpdateTime(DateUtils.getDateTime());

            familyUserDao.updateById(fami);
        }

        FamilyUser familyUser1 = new FamilyUser();
        familyUser1.setMessageNumber(1);
        familyUser1.setFamilyId(video.getFamilyId());
        familyUserDao.setInc(familyUser1);
        if (videoImgList == null || videoImgList.size() == 0) {
            return ResponseUtil.ok("发布成功");
        }
        //给图片赋予文章id
        if (videoImgList != null && videoImgList.size() > 0) {
            for (int i = 0; i < videoImgList.size(); i++) {
                videoImgList.get(i).setVideoId(video.getId());
            }
            //接下来批量插入图片记录
//            result = videoImgDao.insertVideoImgList(videoImgList);

            try {
                // 批量插入图片记录
                result = videoImgDao.insertVideoImgList(videoImgList);
                if (result == 0) {
                    throw new Exception("发布失败");
                }
            } catch (Exception e) {
                // 记录异常信息
                return ResponseUtil.ok("发布成功",e.getMessage());

            }



            if (result == 0) {
                throw new Exception("发布失败");
            }
        }

        /*************再更新下加入者和关注者的消息状态***************/

//        if (video.getOpen() == 3) {
//            UserFamilyFollow userFamilyFollow = new UserFamilyFollow();
//            userFamilyFollow.setMessageNumber(1);
//            userFamilyFollow.setFamilyId(video.getFamilyId());
//            userFamilyFollowDao.setInc(userFamilyFollow);
//        }

        //下发米，文字3，图片5
        int rice = 3;
        if (videoImgList == null || videoImgList.size() == 0) {
            rice = 3;
        } else {
            rice = 5;
        }
        User user = new User();
        user.setId(video.getUserId());
        user.setRice(rice);
        result = userDao.setInc(user);
        if (result == 0) {
            throw new Exception("发布失败");
        }

        //记录到米收支明细
        RiceRecord riceRecord = new RiceRecord();
        riceRecord.setUserId(video.getUserId());
        riceRecord.setRice(rice);
        riceRecord.setContent("发表帖子");
        riceRecord.setCreateTime(video.getCreateTime());
        result = riceRecordDao.insert(riceRecord);
        if (result == 0) {
            throw new Exception("发布失败");
        }

        return ResponseUtil.ok("发布成功");
    }

    @Override
    public JsonResult selectVideoByUserId(Integer page, Integer limit, Video video) {
        IPage<Video> pageData = new Page<>(page, limit);
        QueryWrapper<Video> videoSelect = new QueryWrapper<Video>();
        videoSelect.eq("user_id", video.getUserId());
        videoSelect.orderByDesc("create_time");

        pageData = videoDao.selectPage(pageData, videoSelect);
        List<Video> videoList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        if (videoList == null || videoList.size() == 0) {
            map.put("videoList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        //拿一下该作者信息，这里因为是一个作者的所有帖子，查一次即可
        UserFindResVo userFindResVo = new UserFindResVo();
        User user = userDao.selectById(video.getUserId());
        ObjectUtil.copyByName(user, userFindResVo);

        List<VideoAndImgSelectResVo> videoAndImgSelectResVoList = new ArrayList<VideoAndImgSelectResVo>();
        for (int i = 0; i < videoList.size(); i++) {
            VideoAndImgSelectResVo videoAndImgSelectResVo = new VideoAndImgSelectResVo();
            //文章内容
            videoAndImgSelectResVo.setVideo(videoList.get(i));
            //文章图片
            QueryWrapper<VideoImg> videoImgSelect = new QueryWrapper<VideoImg>();
            videoImgSelect.eq("video_id", videoList.get(i).getId());

            videoAndImgSelectResVo.setVideoImgList(videoImgDao.selectList(videoImgSelect));
            //家族信息
            videoAndImgSelectResVo.setFamily(familyDao.selectById(videoList.get(i).getFamilyId()));
            //点赞信息
            QueryWrapper<VideoSupport> videoSupportFind = new QueryWrapper<VideoSupport>();
            videoSupportFind.eq("video_id", videoList.get(i).getId());
            videoSupportFind.eq("user_id", video.getUserId());
            VideoSupport videoSupportData = videoSupportDao.selectOne(videoSupportFind);
            videoAndImgSelectResVo.setVideoSupport(videoSupportData);

            //作者信息
            videoAndImgSelectResVo.setUserFindResVo(userFindResVo);

            videoAndImgSelectResVoList.add(videoAndImgSelectResVo);
        }
        map.put("videoList", videoAndImgSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult selectVideoByOpen(Integer page, Integer limit, Video video, Integer sort) {

        //解释下这里的逻辑，如果通过关系找文章会特别复杂，而且检索效果可能会因为用户关联或者关注家族多的问题而影响，这里干脆正常查文章，再对文章做个过滤，唯一损失就是可能每次弹出的数目会有差距，但是量大的情况下用户是无感知的
        IPage<Video> pageData = new Page<>(page, limit);
        QueryWrapper<Video> videoSelect = new QueryWrapper<Video>();
        //1按浏览量 2点赞量 3评论数
        if (sort != null && sort == 1) {
            videoSelect.orderByDesc("browse_number");
        } else if (sort != null && sort == 2) {
            videoSelect.orderByDesc("praise_number");
        } else if (sort != null && sort == 3) {
            videoSelect.orderByDesc("discuss_number");
        } else if (sort != null && sort == 4) {
            videoSelect.orderByDesc("is_knit");
        } else {
            videoSelect.orderByDesc("id");
        }
        pageData = videoDao.selectPage(pageData, videoSelect);
        List<Video> videoList = pageData.getRecords();

        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        if (videoList == null || videoList.size() == 0) {
            map.put("videoList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        List<VideoAndImgSelectResVo> videoAndImgSelectResVoList = new ArrayList<VideoAndImgSelectResVo>();
        //对文章进行过滤
        for (int i = 0; i < videoList.size(); i++) {
            if (1==2) {
                //文章保密，我加入的家族并且不是旁亲
                QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
                familyUserFind.eq("family_id", videoList.get(i).getFamilyId());
                familyUserFind.eq("user_id", video.getUserId());
                familyUserFind.eq("status", 2);
                familyUserFind.le("relation", 2);
                FamilyUser familyUser = familyUserDao.selectOne(familyUserFind);
                if (familyUser != null) {
                    VideoAndImgSelectResVo videoAndImgSelectResVo = new VideoAndImgSelectResVo();
                    //文章内容
                    videoAndImgSelectResVo.setVideo(videoList.get(i));
                    //文章图片
                    QueryWrapper<VideoImg> videoImgSelect = new QueryWrapper<VideoImg>();
                    videoImgSelect.eq("video_id", videoList.get(i).getId());
                    videoAndImgSelectResVo.setVideoImgList(videoImgDao.selectList(videoImgSelect));
                    //家族信息
                    videoAndImgSelectResVo.setFamily(familyDao.selectById(videoList.get(i).getFamilyId()));
                    //浏览者点赞信息
                    QueryWrapper<VideoSupport> videoSupportFind = new QueryWrapper<VideoSupport>();
                    videoSupportFind.eq("video_id", videoList.get(i).getId());
                    videoSupportFind.eq("user_id", familyUser.getUserId());
                    VideoSupport videoSupportData = videoSupportDao.selectOne(videoSupportFind);
                    videoAndImgSelectResVo.setVideoSupport(videoSupportData);
                    //作者信息
                    User user = userDao.selectById(videoList.get(i).getUserId());
                    if (user != null) {
                        UserFindResVo userFindResVo = new UserFindResVo();
                        userFindResVo.setAvatar(user.getAvatar());
                        userFindResVo.setNickName(user.getNickName());
                        userFindResVo.setRealName(user.getRealName());
                        videoAndImgSelectResVo.setUserFindResVo(userFindResVo);
                    }
                    videoAndImgSelectResVoList.add(videoAndImgSelectResVo);
                }

            } else if (1==1) {
                //文章家族内可看，家族成员就可以看
                QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
                familyUserFind.eq("family_id", videoList.get(i).getFamilyId());
                familyUserFind.eq("user_id", video.getUserId());
                familyUserFind.eq("status", 2);
                FamilyUser familyUser = familyUserDao.selectOne(familyUserFind);
                if (familyUser != null) {
                    VideoAndImgSelectResVo videoAndImgSelectResVo = new VideoAndImgSelectResVo();
                    //文章内容
                    videoAndImgSelectResVo.setVideo(videoList.get(i));
                    //文章图片
                    QueryWrapper<VideoImg> videoImgSelect = new QueryWrapper<VideoImg>();
                    videoImgSelect.eq("video_id", videoList.get(i).getId());
                    videoAndImgSelectResVo.setVideoImgList(videoImgDao.selectList(videoImgSelect));
                    //家族信息
                    videoAndImgSelectResVo.setFamily(familyDao.selectById(videoList.get(i).getFamilyId()));
                    //浏览者点赞信息
                    QueryWrapper<VideoSupport> videoSupportFind = new QueryWrapper<VideoSupport>();
                    videoSupportFind.eq("video_id", videoList.get(i).getId());
                    videoSupportFind.eq("user_id", familyUser.getUserId());
                    VideoSupport videoSupportData = videoSupportDao.selectOne(videoSupportFind);
                    videoAndImgSelectResVo.setVideoSupport(videoSupportData);
                    //作者信息
                    User user = userDao.selectById(videoList.get(i).getUserId());
                    if (user != null) {
                        UserFindResVo userFindResVo = new UserFindResVo();
                        userFindResVo.setAvatar(user.getAvatar());
                        userFindResVo.setNickName(user.getNickName());
                        userFindResVo.setRealName(user.getRealName());
                        videoAndImgSelectResVo.setUserFindResVo(userFindResVo);
                    }
                    videoAndImgSelectResVoList.add(videoAndImgSelectResVo);
                }

            } else {
                //完全公开，家族成员或者关注就可看
                QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
                familyUserFind.eq("family_id", videoList.get(i).getFamilyId());
                familyUserFind.eq("user_id", videoList.get(i).getUserId());
                familyUserFind.eq("status", 2);
                FamilyUser familyUser = familyUserDao.selectOne(familyUserFind);
                if (familyUser != null) {
                    VideoAndImgSelectResVo videoAndImgSelectResVo = new VideoAndImgSelectResVo();
                    //文章内容
                    videoAndImgSelectResVo.setVideo(videoList.get(i));
                    //文章图片
                    QueryWrapper<VideoImg> videoImgSelect = new QueryWrapper<VideoImg>();
                    videoImgSelect.eq("video_id", videoList.get(i).getId());
                    videoAndImgSelectResVo.setVideoImgList(videoImgDao.selectList(videoImgSelect));
                    //家族信息
                    videoAndImgSelectResVo.setFamily(familyDao.selectById(videoList.get(i).getFamilyId()));
                    //浏览者点赞信息
                    QueryWrapper<VideoSupport> videoSupportFind = new QueryWrapper<VideoSupport>();
                    videoSupportFind.eq("video_id", videoList.get(i).getId());
                    videoSupportFind.eq("user_id", familyUser.getUserId());
                    VideoSupport videoSupportData = videoSupportDao.selectOne(videoSupportFind);
                    videoAndImgSelectResVo.setVideoSupport(videoSupportData);
                    //作者信息
                    User user = userDao.selectById(videoList.get(i).getUserId());
                    if (user != null) {
                        UserFindResVo userFindResVo = new UserFindResVo();
                        userFindResVo.setAvatar(user.getAvatar());
                        userFindResVo.setNickName(user.getNickName());
                        userFindResVo.setRealName(user.getRealName());
                        videoAndImgSelectResVo.setUserFindResVo(userFindResVo);
                    }
                    videoAndImgSelectResVoList.add(videoAndImgSelectResVo);
                    continue;
                }
//                QueryWrapper<UserFamilyFollow> userFamilyFollowFind = new QueryWrapper<UserFamilyFollow>();
//                userFamilyFollowFind.eq("family_id",videoList.get(i).getFamilyId());
//                userFamilyFollowFind.eq("user_id",video.getUserId());
//                userFamilyFollowFind.eq("is_status",1);
//                UserFamilyFollow userFamilyFollow = userFamilyFollowDao.selectOne(userFamilyFollowFind);
//                if(userFamilyFollow!=null){
//                    VideoAndImgSelectResVo videoAndImgSelectResVo = new VideoAndImgSelectResVo();
//                    //文章内容
//                    videoAndImgSelectResVo.setVideo(videoList.get(i));
//                    //文章图片
//                    QueryWrapper<VideoImg> videoImgSelect = new QueryWrapper<VideoImg>();
//                    videoImgSelect.eq("video_id", videoList.get(i).getId());
//                    videoAndImgSelectResVo.setVideoImgList(videoImgDao.selectList(videoImgSelect));
//                    //家族信息
//                    videoAndImgSelectResVo.setFamily(familyDao.selectById(videoList.get(i).getFamilyId()));
//                    //浏览者点赞信息
//                    QueryWrapper<VideoSupport> videoSupportFind = new QueryWrapper<VideoSupport>();
//                    videoSupportFind.eq("video_id",videoList.get(i).getId());
//                    videoSupportFind.eq("user_id",familyUser.getUserId());
//                    VideoSupport videoSupportData = videoSupportDao.selectOne(videoSupportFind);
//                    videoAndImgSelectResVo.setVideoSupport(videoSupportData);
//                    //作者信息
//                    User user = userDao.selectById(videoList.get(i).getUserId());
//                    if(user!=null){
//                        UserFindResVo userFindResVo = new UserFindResVo();
//                        userFindResVo.setAvatar(user.getAvatar());
//                        userFindResVo.setNickName(user.getNickName());
//                        userFindResVo.setRealName(user.getRealName());
//                        videoAndImgSelectResVo.setUserFindResVo(userFindResVo);
//                    }
//                    videoAndImgSelectResVoList.add(videoAndImgSelectResVo);
//                }

            }
        }
        map.put("videoList", videoAndImgSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult selectVideo(Integer page, Integer limit, Video video, Integer sort) {
        IPage<Video> pageData = new Page<>(page, limit);
        QueryWrapper<Video> videoSelect = new QueryWrapper<Video>();
        //1按浏览量 2点赞量 3评论数
        if (sort != null && sort == 1) {
            videoSelect.orderByDesc("browse_number");
        } else if (sort != null && sort == 2) {
            videoSelect.orderByDesc("praise_number");
        } else if (sort != null && sort == 3) {
            videoSelect.orderByDesc("discuss_number");
        } else if (sort != null && sort == 4) {
            videoSelect.orderByDesc("is_knit");
        } else {
            videoSelect.orderByDesc("id");
        }

        pageData = videoDao.selectPage(pageData, videoSelect);
        List<Video> videoList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        if (videoList == null || videoList.size() == 0) {
            map.put("videoList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        List<VideoAndImgSelectResVo> videoAndImgSelectResVoList = new ArrayList<VideoAndImgSelectResVo>();
        for (int i = 0; i < videoList.size(); i++) {
            VideoAndImgSelectResVo videoAndImgSelectResVo = new VideoAndImgSelectResVo();
            //文章内容
            videoAndImgSelectResVo.setVideo(videoList.get(i));
            //文章图片
            QueryWrapper<VideoImg> videoImgSelect = new QueryWrapper<VideoImg>();
            videoImgSelect.eq("video_id", videoList.get(i).getId());
            videoAndImgSelectResVo.setVideoImgList(videoImgDao.selectList(videoImgSelect));
            //家族信息
            videoAndImgSelectResVo.setFamily(familyDao.selectById(videoList.get(i).getFamilyId()));

            //作者信息
            User user = userDao.selectById(videoList.get(i).getUserId());
            if (user != null) {
                UserFindResVo userFindResVo = new UserFindResVo();
                userFindResVo.setAvatar(user.getAvatar());
                userFindResVo.setNickName(user.getNickName());
                userFindResVo.setRealName(user.getRealName());
                videoAndImgSelectResVo.setUserFindResVo(userFindResVo);
            }
            videoAndImgSelectResVoList.add(videoAndImgSelectResVo);
        }
        map.put("videoList", videoAndImgSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }


    @Override
    public JsonResult selectVideoByFamilyId(Integer page, Integer limit, Video video, FamilyUser familyUser, Integer sort, String searchData) {
        //先查浏览者身份
        QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
        familyUserFind.eq("family_id", video.getFamilyId());
        familyUserFind.eq("user_id", familyUser.getUserId());
        FamilyUser familyUserData = familyUserDao.selectOne(familyUserFind);

        IPage<Video> pageData = new Page<>(page, limit);
        QueryWrapper<Video> videoSelect = new QueryWrapper<Video>();
        if (video != null && video.getUserId() != null) {
            videoSelect.eq("user_id", video.getUserId());
        }
//        if (familyUserData == null) {
//            videoSelect.eq("open", 3);
//        }
//        if (familyUserData != null && familyUserData.getRelation() == 3) {
//            videoSelect.gt("open", 1);
//        }

        videoSelect.eq("family_id", video.getFamilyId());
        //1按浏览量 2点赞量 3评论数
        if (sort != null && sort == 1) {
            videoSelect.orderByDesc("browse_number");
        } else if (sort != null && sort == 2) {
            videoSelect.orderByDesc("praise_number");
        } else if (sort != null && sort == 3) {
            videoSelect.orderByDesc("discuss_number");
        } else if (sort != null && sort == 4) {
            videoSelect.orderByDesc("is_knit");
            videoSelect.orderByDesc("create_time");

        } else {
            videoSelect.orderByDesc("id");
        }

//        QueryWrapper<VideoShare> videosharedata = new QueryWrapper<VideoShare>();
//        videosharedata.eq("family_id", video.getFamilyId());
//        List<VideoShare> da = videoShareDao.selectList(videosharedata);
//        for (int i = 0; i < da.size(); i++) {
//            videoSelect.or().eq("id", da.get(i).getVideoId());
//        }
       // videoSelect.like("content", searchData);
        pageData = videoDao.selectPage(pageData, videoSelect);

        List<Video> videoList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        if (videoList == null || videoList.size() == 0) {
            map.put("videoList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        List<VideoAndImgSelectResVo> videoAndImgSelectResVoList = new ArrayList<VideoAndImgSelectResVo>();
        for (int i = 0; i < videoList.size(); i++) {
            VideoAndImgSelectResVo videoAndImgSelectResVo = new VideoAndImgSelectResVo();
            //文章内容
            videoAndImgSelectResVo.setVideo(videoList.get(i));
            //文章图片
            QueryWrapper<VideoImg> videoImgSelect = new QueryWrapper<VideoImg>();
            videoImgSelect.eq("video_id", videoList.get(i).getId());
            videoAndImgSelectResVo.setVideoImgList(videoImgDao.selectList(videoImgSelect));
            //浏览者点赞信息
            QueryWrapper<VideoSupport> videoSupportFind = new QueryWrapper<VideoSupport>();
            videoSupportFind.eq("video_id", videoList.get(i).getId());
            videoSupportFind.eq("user_id", familyUser.getUserId());
            VideoSupport videoSupportData = videoSupportDao.selectOne(videoSupportFind);
            videoAndImgSelectResVo.setVideoSupport(videoSupportData);
            //作者信息
            User user = userDao.selectById(videoList.get(i).getUserId());
            if (user != null) {
                UserFindResVo userFindResVo = new UserFindResVo();
                userFindResVo.setAvatar(user.getAvatar());
                userFindResVo.setNickName(user.getNickName());
                userFindResVo.setRealName(user.getRealName());
                videoAndImgSelectResVo.setUserFindResVo(userFindResVo);
            }
            videoAndImgSelectResVoList.add(videoAndImgSelectResVo);
        }
        map.put("videoList", videoAndImgSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult findVideoById(Video video) {
        //文章内容
        Video videoData = videoDao.selectById(video.getId());
        VideoFindResVo videoFindResVo = new VideoFindResVo();
        videoFindResVo.setVideo(videoData);

        //文章图片
        QueryWrapper<VideoImg> videoImgSelect = new QueryWrapper<VideoImg>();
        videoImgSelect.eq("video_id", video.getId());
        videoFindResVo.setVideoImgList(videoImgDao.selectList(videoImgSelect));

        //作者信息
        User user = userDao.selectById(videoData.getUserId());
        UserFindResVo userFindResVo = new UserFindResVo();
        ObjectUtil.copyByName(user, userFindResVo);
        videoFindResVo.setUserFindResVo(userFindResVo);

        //浏览者点赞情况
        QueryWrapper<VideoSupport> videoSupportFind = new QueryWrapper<VideoSupport>();
        videoSupportFind.eq("video_id", video.getId());
        videoSupportFind.eq("user_id", video.getUserId());
        VideoSupport videoSupportData = videoSupportDao.selectOne(videoSupportFind);
        videoFindResVo.setVideoSupport(videoSupportData);

        return ResponseUtil.ok("获取成功", videoFindResVo);
    }

    @Override
    public JsonResult updateVideoById(Video video) throws Exception {
        int result = videoDao.updateById(video);
        return result == 0 ? ResponseUtil.fail("修改失败") : ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult updateVideoBrowseNumberById(Video video) throws Exception {

        //再查询文章是否存在
        Video videoData = videoDao.selectById(video.getId());
        if (videoData == null) {
            return ResponseUtil.fail("文章不存在");
        }
        Video videoUpdate = new Video();
        videoUpdate.setId(video.getId());
        videoUpdate.setBrowseNumber(video.getBrowseNumber());
        videoUpdate.setKnitStartTime(video.getKnitStartTime());
        videoUpdate.setKnitEndTime(video.getKnitStartTime() + videoData.getKnitCycle());//刷新加锁倒计时结束日期
        int result = videoDao.updateById(videoUpdate);
        if (result == 0) {
            throw new Exception("修改失败");
        }

        //分别给作者和浏览人下发1米
        User user1 = new User();
        user1.setId(videoData.getUserId());
        user1.setRice(1);
        result = userDao.setInc(user1);
        if (result == 0) {
            throw new Exception("浏览失败");
        }

        User user2 = new User();
        user2.setId(video.getUserId());
        user2.setRice(1);
        result = userDao.setInc(user2);
        if (result == 0) {
            throw new Exception("浏览失败");
        }


        List<RiceRecord> riceRecordList = new ArrayList<RiceRecord>();
        RiceRecord riceRecord1 = new RiceRecord();
        riceRecord1.setUserId(videoData.getUserId());
        riceRecord1.setRice(1);
        riceRecord1.setContent("文章被浏览");
        riceRecord1.setCreateTime(DateUtils.getDateTime());
        riceRecordList.add(riceRecord1);
        RiceRecord riceRecord2 = new RiceRecord();
        riceRecord2.setUserId(video.getUserId());
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
    public JsonResult updateVideoUnlockById(Video video) throws Exception {
        //看看用户米够不够
        User user = userDao.selectById(video.getUserId());
        if (user == null) {
            return ResponseUtil.fail("解锁失败");
        }

        int multiple = video.getKnitCycle() / 2592000;//倍数

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
        Video videoUpdate = new Video();
        videoUpdate.setId(video.getId());
        videoUpdate.setKnit(video.getKnit());
        videoUpdate.setKnitCycle(video.getKnitCycle());
        videoUpdate.setKnitStartTime(video.getKnitStartTime());
        videoUpdate.setKnitEndTime(video.getKnitEndTime());
        result = videoDao.updateById(videoUpdate);
        if (result == 0) {
            throw new Exception("解锁失败");
        }
        return ResponseUtil.ok("解锁成功");
    }


    @Override
    public JsonResult deleteVideoById(Integer id) throws Exception {
        //删除帖子
        int result = videoDao.deleteById(id);
        if (result == 0) {
            throw new Exception("删除失败");
        }

        //删除文章图片
        QueryWrapper<VideoImg> videoImgDelete = new QueryWrapper<VideoImg>();
        videoImgDelete.eq("video_id", id);
        videoImgDao.delete(videoImgDelete);


        //点赞和评论就不删除了，有点多，避免卡死风险
        return ResponseUtil.ok("删除成功");
    }


    @Override
    public JsonResult selectotherVideo(Integer limit, Integer page, Integer sheuserId, Integer userId) throws Exception {
        QueryWrapper<FamilyUser> familyUserQueryWrapper = new QueryWrapper<FamilyUser>();
        familyUserQueryWrapper.eq("user_id", sheuserId);
        familyUserQueryWrapper.eq("status", 2);
        List<FamilyUser> familyUsers = familyUserDao.selectList(familyUserQueryWrapper);


        QueryWrapper<FamilyUser> familyUserQueryWrapper1 = new QueryWrapper<FamilyUser>();
        familyUserQueryWrapper1.eq("user_id", userId);
        familyUserQueryWrapper1.eq("status", 2);
        List<FamilyUser> familyUsers1 = familyUserDao.selectList(familyUserQueryWrapper1);

        IPage<Video> pageData = new Page<>(page, limit);
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<Video>();
        for (int i = 0; i < familyUsers.size(); i++) {
            for (int j = 0; j < familyUsers1.size(); j++) {
                if (familyUsers.get(i).getFamilyId() == familyUsers1.get(j).getFamilyId()) {
                    videoQueryWrapper.or().eq("family_id", familyUsers.get(i).getFamilyId()).eq("user_id", sheuserId);


                }
            }
        }
        videoQueryWrapper.orderByDesc("is_knit");

        videoQueryWrapper.or().eq("user_id", sheuserId).eq("open", 3);
        videoQueryWrapper.orderByDesc("create_time");

        pageData = videoDao.selectPage(pageData, videoQueryWrapper);
        List<Video> videoList = pageData.getRecords();
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        if (videoList == null || videoList.size() == 0) {
            map.put("videoList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        //拿一下该作者信息，这里因为是一个作者的所有帖子，查一次即可
        UserFindResVo userFindResVo = new UserFindResVo();
        User user = userDao.selectById(sheuserId);
        ObjectUtil.copyByName(user, userFindResVo);

        List<VideoAndImgSelectResVo> videoAndImgSelectResVoList = new ArrayList<VideoAndImgSelectResVo>();
        for (int i = 0; i < videoList.size(); i++) {
            VideoAndImgSelectResVo videoAndImgSelectResVo = new VideoAndImgSelectResVo();
            //文章内容
            videoAndImgSelectResVo.setVideo(videoList.get(i));
            //文章图片
            QueryWrapper<VideoImg> videoImgSelect = new QueryWrapper<VideoImg>();
            videoImgSelect.eq("video_id", videoList.get(i).getId());
            videoAndImgSelectResVo.setVideoImgList(videoImgDao.selectList(videoImgSelect));
            //家族信息
            videoAndImgSelectResVo.setFamily(familyDao.selectById(videoList.get(i).getFamilyId()));
            //点赞信息
            QueryWrapper<VideoSupport> videoSupportFind = new QueryWrapper<VideoSupport>();
            videoSupportFind.eq("video_id", videoList.get(i).getId());
            videoSupportFind.eq("user_id", sheuserId);
            VideoSupport videoSupportData = videoSupportDao.selectOne(videoSupportFind);
            videoAndImgSelectResVo.setVideoSupport(videoSupportData);

            //作者信息
            videoAndImgSelectResVo.setUserFindResVo(userFindResVo);

            videoAndImgSelectResVoList.add(videoAndImgSelectResVo);
        }
        map.put("videoList", videoAndImgSelectResVoList);

        return ResponseUtil.ok("成功", map);

    }

    /**
     * 根据id查询帖子
     *
     * @param id 帖子id
     * @return
     */
    @Override
    public Video selectVideoById(Integer id) {
        return videoDao.selectById(id);
    }
}
