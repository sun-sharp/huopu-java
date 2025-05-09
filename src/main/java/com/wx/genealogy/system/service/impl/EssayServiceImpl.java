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
import com.wx.genealogy.system.service.EssayService;
import com.wx.genealogy.system.vo.res.EssayAndImgSelectResVo;
import com.wx.genealogy.system.vo.res.EssayFindResVo;
import com.wx.genealogy.system.vo.res.UserFindResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-09
 */
@Service
public class EssayServiceImpl extends ServiceImpl<EssayDao, Essay> implements EssayService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private EssayDao essayDao;
    @Autowired
    private EssayShareDao essayShareDao;

    @Autowired
    private EssayImgDao essayImgDao;

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private FamilyUserDao familyUserDao;

    @Autowired
    private UserFamilyFollowDao userFamilyFollowDao;

    @Autowired
    private EssaySupportDao essaySupportDao;

    @Autowired
    private RiceRecordDao riceRecordDao;
    @Autowired
    private FamilyMessageDao familyMessageDao;

    @Override
    @Transactional
    public JsonResult insertEssay(Essay essay, List<EssayImg> essayImgList) throws Exception {

        //判断是不是管理员才可以发
        LambdaQueryWrapper<FamilyUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyUser::getFamilyId, essay.getFamilyId());
        wrapper.eq(FamilyUser::getUserId, essay.getUserId());
        FamilyUser familyUser = this.familyUserDao.selectOne(wrapper);
//        if (Objects.isNull(familyUser)) {
//            return ResponseUtil.fail("未查询到您的管理员身份,不可发布");
//        }
//        // 身份等级：1是创建者2是管理员3是会员
//        if (familyUser.getLevel() == 3) {
//            return ResponseUtil.fail("未查询到您的管理员身份,不可发布");
//        }

        int result = essayDao.insert(essay);
        if (result == 0) {
            throw new Exception("发布失败");
        }
        //添加文章消息
        //1.查找家族有哪些成员的

        QueryWrapper<FamilyUser> familyuser_find = new QueryWrapper<FamilyUser>();
        familyuser_find.eq("family_id", essay.getFamilyId());
        familyuser_find.gt("user_id", 0);
        List<FamilyUser> family_find = familyUserDao.selectList(familyuser_find);

        for (int i = 0; i < family_find.size(); i++) {
            QueryWrapper<FamilyMessage> familymessage = new QueryWrapper<FamilyMessage>();
            familymessage.eq("family_id", essay.getFamilyId());
            familymessage.eq("user_id", family_find.get(i).getUserId());
            FamilyMessage familydata = familyMessageDao.selectOne(familymessage);
            if (familydata == null) {
                FamilyMessage familyMessage = new FamilyMessage();
                familyMessage.setEssayMessage(1);
                familyMessage.setFamilyId(essay.getFamilyId());
                familyMessage.setUserId(family_find.get(i).getUserId());

                familyMessageDao.insert(familyMessage);
            } else {
                FamilyMessage familyMessage = new FamilyMessage();
                familyMessage.setId(familydata.getId());
                familyMessage.setEssayMessage(familydata.getEssayMessage() + 1);
                familyMessage.setFamilyId(essay.getFamilyId());
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
//        familyUser.setFamilyId(essay.getFamilyId());
//        familyUserDao.setInc(familyUser);

        if (essayImgList == null || essayImgList.size() == 0) {
            return ResponseUtil.ok("发布成功");
        }
        //给图片赋予文章id
        if (essayImgList != null && essayImgList.size() > 0) {
            for (int i = 0; i < essayImgList.size(); i++) {
                essayImgList.get(i).setEssayId(essay.getId());
            }
            //接下来批量插入图片记录
            result = essayImgDao.insertEssayImgList(essayImgList);
            if (result == 0) {
                throw new Exception("发布失败");
            }
        }

        /*************再更新下加入者和关注者的消息状态***************/

        if (essay.getOpen() == 3) {
            UserFamilyFollow userFamilyFollow = new UserFamilyFollow();
            userFamilyFollow.setMessageNumber(1);
            userFamilyFollow.setFamilyId(essay.getFamilyId());
            userFamilyFollowDao.setInc(userFamilyFollow);
        }

        //下发米，文字3，图片5
        int rice = 0;
        if (essayImgList == null || essayImgList.size() == 0) {
            rice = 3;
        } else {
            rice = 5;
        }
        User user = new User();
        user.setId(essay.getUserId());
        user.setRice(rice);
        result = userDao.setInc(user);
        if (result == 0) {
            throw new Exception("发布失败");
        }

        //记录到米收支明细
        RiceRecord riceRecord = new RiceRecord();
        riceRecord.setUserId(essay.getUserId());
        riceRecord.setRice(rice);
        riceRecord.setContent("发表帖子");
        riceRecord.setCreateTime(essay.getCreateTime());
        result = riceRecordDao.insert(riceRecord);
        if (result == 0) {
            throw new Exception("发布失败");
        }

        return ResponseUtil.ok("发布成功");
    }

    @Override
    public JsonResult selectEssayByUserId(Integer page, Integer limit, Essay essay) {
        IPage<Essay> pageData = new Page<>(page, limit);
        QueryWrapper<Essay> essaySelect = new QueryWrapper<Essay>();
        essaySelect.eq("user_id", essay.getUserId());
        essaySelect.orderByDesc("create_time");

        pageData = essayDao.selectPage(pageData, essaySelect);
        List<Essay> essayList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        if (essayList == null || essayList.size() == 0) {
            map.put("essayList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        //拿一下该作者信息，这里因为是一个作者的所有帖子，查一次即可
        UserFindResVo userFindResVo = new UserFindResVo();
        User user = userDao.selectById(essay.getUserId());
        ObjectUtil.copyByName(user, userFindResVo);

        List<EssayAndImgSelectResVo> essayAndImgSelectResVoList = new ArrayList<EssayAndImgSelectResVo>();
        for (int i = 0; i < essayList.size(); i++) {
            EssayAndImgSelectResVo essayAndImgSelectResVo = new EssayAndImgSelectResVo();
            //文章内容
            essayAndImgSelectResVo.setEssay(essayList.get(i));
            //文章图片
            QueryWrapper<EssayImg> essayImgSelect = new QueryWrapper<EssayImg>();
            essayImgSelect.eq("essay_id", essayList.get(i).getId());
            essayAndImgSelectResVo.setEssayImgList(essayImgDao.selectList(essayImgSelect));
            //家族信息
            essayAndImgSelectResVo.setFamily(familyDao.selectById(essayList.get(i).getFamilyId()));
            //点赞信息
            QueryWrapper<EssaySupport> essaySupportFind = new QueryWrapper<EssaySupport>();
            essaySupportFind.eq("essay_id", essayList.get(i).getId());
            essaySupportFind.eq("user_id", essay.getUserId());
            EssaySupport essaySupportData = essaySupportDao.selectOne(essaySupportFind);
            essayAndImgSelectResVo.setEssaySupport(essaySupportData);

            //作者信息
            essayAndImgSelectResVo.setUserFindResVo(userFindResVo);

            essayAndImgSelectResVoList.add(essayAndImgSelectResVo);
        }
        map.put("essayList", essayAndImgSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult selectEssayByOpen(Integer page, Integer limit, Essay essay, Integer sort) {

        //解释下这里的逻辑，如果通过关系找文章会特别复杂，而且检索效果可能会因为用户关联或者关注家族多的问题而影响，这里干脆正常查文章，再对文章做个过滤，唯一损失就是可能每次弹出的数目会有差距，但是量大的情况下用户是无感知的
        IPage<Essay> pageData = new Page<>(page, limit);
        QueryWrapper<Essay> essaySelect = new QueryWrapper<Essay>();
        //1按浏览量 2点赞量 3评论数
        if (sort != null && sort == 1) {
            essaySelect.orderByDesc("browse_number");
        } else if (sort != null && sort == 2) {
            essaySelect.orderByDesc("praise_number");
        } else if (sort != null && sort == 3) {
            essaySelect.orderByDesc("discuss_number");
        } else if (sort != null && sort == 4) {
            essaySelect.orderByDesc("is_knit");
        } else {
            essaySelect.orderByDesc("id");
        }
        pageData = essayDao.selectPage(pageData, essaySelect);
        List<Essay> essayList = pageData.getRecords();

        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        if (essayList == null || essayList.size() == 0) {
            map.put("essayList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        List<EssayAndImgSelectResVo> essayAndImgSelectResVoList = new ArrayList<EssayAndImgSelectResVo>();
        //对文章进行过滤
        for (int i = 0; i < essayList.size(); i++) {
            if (essayList.get(i).getOpen() == 1) {
                //文章保密，我加入的家族并且不是旁亲
                QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
                familyUserFind.eq("family_id", essayList.get(i).getFamilyId());
                familyUserFind.eq("user_id", essay.getUserId());
                familyUserFind.eq("status", 2);
                familyUserFind.le("relation", 2);
                FamilyUser familyUser = familyUserDao.selectOne(familyUserFind);
                if (familyUser != null) {
                    EssayAndImgSelectResVo essayAndImgSelectResVo = new EssayAndImgSelectResVo();
                    //文章内容
                    essayAndImgSelectResVo.setEssay(essayList.get(i));
                    //文章图片
                    QueryWrapper<EssayImg> essayImgSelect = new QueryWrapper<EssayImg>();
                    essayImgSelect.eq("essay_id", essayList.get(i).getId());
                    essayAndImgSelectResVo.setEssayImgList(essayImgDao.selectList(essayImgSelect));
                    //家族信息
                    essayAndImgSelectResVo.setFamily(familyDao.selectById(essayList.get(i).getFamilyId()));
                    //浏览者点赞信息
                    QueryWrapper<EssaySupport> essaySupportFind = new QueryWrapper<EssaySupport>();
                    essaySupportFind.eq("essay_id", essayList.get(i).getId());
                    essaySupportFind.eq("user_id", familyUser.getUserId());
                    EssaySupport essaySupportData = essaySupportDao.selectOne(essaySupportFind);
                    essayAndImgSelectResVo.setEssaySupport(essaySupportData);
                    //作者信息
                    User user = userDao.selectById(essayList.get(i).getUserId());
                    if (user != null) {
                        UserFindResVo userFindResVo = new UserFindResVo();
                        userFindResVo.setAvatar(user.getAvatar());
                        userFindResVo.setNickName(user.getNickName());
                        userFindResVo.setRealName(user.getRealName());
                        essayAndImgSelectResVo.setUserFindResVo(userFindResVo);
                    }
                    essayAndImgSelectResVoList.add(essayAndImgSelectResVo);
                }

            } else if (essayList.get(i).getOpen() == 2) {
                //文章家族内可看，家族成员就可以看
                QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
                familyUserFind.eq("family_id", essayList.get(i).getFamilyId());
                familyUserFind.eq("user_id", essay.getUserId());
                familyUserFind.eq("status", 2);
                FamilyUser familyUser = familyUserDao.selectOne(familyUserFind);
                if (familyUser != null) {
                    EssayAndImgSelectResVo essayAndImgSelectResVo = new EssayAndImgSelectResVo();
                    //文章内容
                    essayAndImgSelectResVo.setEssay(essayList.get(i));
                    //文章图片
                    QueryWrapper<EssayImg> essayImgSelect = new QueryWrapper<EssayImg>();
                    essayImgSelect.eq("essay_id", essayList.get(i).getId());
                    essayAndImgSelectResVo.setEssayImgList(essayImgDao.selectList(essayImgSelect));
                    //家族信息
                    essayAndImgSelectResVo.setFamily(familyDao.selectById(essayList.get(i).getFamilyId()));
                    //浏览者点赞信息
                    QueryWrapper<EssaySupport> essaySupportFind = new QueryWrapper<EssaySupport>();
                    essaySupportFind.eq("essay_id", essayList.get(i).getId());
                    essaySupportFind.eq("user_id", familyUser.getUserId());
                    EssaySupport essaySupportData = essaySupportDao.selectOne(essaySupportFind);
                    essayAndImgSelectResVo.setEssaySupport(essaySupportData);
                    //作者信息
                    User user = userDao.selectById(essayList.get(i).getUserId());
                    if (user != null) {
                        UserFindResVo userFindResVo = new UserFindResVo();
                        userFindResVo.setAvatar(user.getAvatar());
                        userFindResVo.setNickName(user.getNickName());
                        userFindResVo.setRealName(user.getRealName());
                        essayAndImgSelectResVo.setUserFindResVo(userFindResVo);
                    }
                    essayAndImgSelectResVoList.add(essayAndImgSelectResVo);
                }

            } else {
                //完全公开，家族成员或者关注就可看
                QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
                familyUserFind.eq("family_id", essayList.get(i).getFamilyId());
                familyUserFind.eq("user_id", essayList.get(i).getUserId());
                familyUserFind.eq("status", 2);
                FamilyUser familyUser = familyUserDao.selectOne(familyUserFind);
                if (familyUser != null) {
                    EssayAndImgSelectResVo essayAndImgSelectResVo = new EssayAndImgSelectResVo();
                    //文章内容
                    essayAndImgSelectResVo.setEssay(essayList.get(i));
                    //文章图片
                    QueryWrapper<EssayImg> essayImgSelect = new QueryWrapper<EssayImg>();
                    essayImgSelect.eq("essay_id", essayList.get(i).getId());
                    essayAndImgSelectResVo.setEssayImgList(essayImgDao.selectList(essayImgSelect));
                    //家族信息
                    essayAndImgSelectResVo.setFamily(familyDao.selectById(essayList.get(i).getFamilyId()));
                    //浏览者点赞信息
                    QueryWrapper<EssaySupport> essaySupportFind = new QueryWrapper<EssaySupport>();
                    essaySupportFind.eq("essay_id", essayList.get(i).getId());
                    essaySupportFind.eq("user_id", familyUser.getUserId());
                    EssaySupport essaySupportData = essaySupportDao.selectOne(essaySupportFind);
                    essayAndImgSelectResVo.setEssaySupport(essaySupportData);
                    //作者信息
                    User user = userDao.selectById(essayList.get(i).getUserId());
                    if (user != null) {
                        UserFindResVo userFindResVo = new UserFindResVo();
                        userFindResVo.setAvatar(user.getAvatar());
                        userFindResVo.setNickName(user.getNickName());
                        userFindResVo.setRealName(user.getRealName());
                        essayAndImgSelectResVo.setUserFindResVo(userFindResVo);
                    }
                    essayAndImgSelectResVoList.add(essayAndImgSelectResVo);
                    continue;
                }
//                QueryWrapper<UserFamilyFollow> userFamilyFollowFind = new QueryWrapper<UserFamilyFollow>();
//                userFamilyFollowFind.eq("family_id",essayList.get(i).getFamilyId());
//                userFamilyFollowFind.eq("user_id",essay.getUserId());
//                userFamilyFollowFind.eq("is_status",1);
//                UserFamilyFollow userFamilyFollow = userFamilyFollowDao.selectOne(userFamilyFollowFind);
//                if(userFamilyFollow!=null){
//                    EssayAndImgSelectResVo essayAndImgSelectResVo = new EssayAndImgSelectResVo();
//                    //文章内容
//                    essayAndImgSelectResVo.setEssay(essayList.get(i));
//                    //文章图片
//                    QueryWrapper<EssayImg> essayImgSelect = new QueryWrapper<EssayImg>();
//                    essayImgSelect.eq("essay_id", essayList.get(i).getId());
//                    essayAndImgSelectResVo.setEssayImgList(essayImgDao.selectList(essayImgSelect));
//                    //家族信息
//                    essayAndImgSelectResVo.setFamily(familyDao.selectById(essayList.get(i).getFamilyId()));
//                    //浏览者点赞信息
//                    QueryWrapper<EssaySupport> essaySupportFind = new QueryWrapper<EssaySupport>();
//                    essaySupportFind.eq("essay_id",essayList.get(i).getId());
//                    essaySupportFind.eq("user_id",familyUser.getUserId());
//                    EssaySupport essaySupportData = essaySupportDao.selectOne(essaySupportFind);
//                    essayAndImgSelectResVo.setEssaySupport(essaySupportData);
//                    //作者信息
//                    User user = userDao.selectById(essayList.get(i).getUserId());
//                    if(user!=null){
//                        UserFindResVo userFindResVo = new UserFindResVo();
//                        userFindResVo.setAvatar(user.getAvatar());
//                        userFindResVo.setNickName(user.getNickName());
//                        userFindResVo.setRealName(user.getRealName());
//                        essayAndImgSelectResVo.setUserFindResVo(userFindResVo);
//                    }
//                    essayAndImgSelectResVoList.add(essayAndImgSelectResVo);
//                }

            }
        }
        map.put("essayList", essayAndImgSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult selectEssay(Integer page, Integer limit, Essay essay, Integer sort) {
        IPage<Essay> pageData = new Page<>(page, limit);
        QueryWrapper<Essay> essaySelect = new QueryWrapper<Essay>();
        //1按浏览量 2点赞量 3评论数
        if (sort != null && sort == 1) {
            essaySelect.orderByDesc("browse_number");
        } else if (sort != null && sort == 2) {
            essaySelect.orderByDesc("praise_number");
        } else if (sort != null && sort == 3) {
            essaySelect.orderByDesc("discuss_number");
        } else if (sort != null && sort == 4) {
            essaySelect.orderByDesc("is_knit");
        } else {
            essaySelect.orderByDesc("id");
        }

        pageData = essayDao.selectPage(pageData, essaySelect);
        List<Essay> essayList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        if (essayList == null || essayList.size() == 0) {
            map.put("essayList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        List<EssayAndImgSelectResVo> essayAndImgSelectResVoList = new ArrayList<EssayAndImgSelectResVo>();
        for (int i = 0; i < essayList.size(); i++) {
            EssayAndImgSelectResVo essayAndImgSelectResVo = new EssayAndImgSelectResVo();
            //文章内容
            essayAndImgSelectResVo.setEssay(essayList.get(i));
            //文章图片
            QueryWrapper<EssayImg> essayImgSelect = new QueryWrapper<EssayImg>();
            essayImgSelect.eq("essay_id", essayList.get(i).getId());
            essayAndImgSelectResVo.setEssayImgList(essayImgDao.selectList(essayImgSelect));
            //家族信息
            essayAndImgSelectResVo.setFamily(familyDao.selectById(essayList.get(i).getFamilyId()));

            //作者信息
            User user = userDao.selectById(essayList.get(i).getUserId());
            if (user != null) {
                UserFindResVo userFindResVo = new UserFindResVo();
                userFindResVo.setAvatar(user.getAvatar());
                userFindResVo.setNickName(user.getNickName());
                userFindResVo.setRealName(user.getRealName());
                essayAndImgSelectResVo.setUserFindResVo(userFindResVo);
            }
            essayAndImgSelectResVoList.add(essayAndImgSelectResVo);
        }
        map.put("essayList", essayAndImgSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }


    @Override
    public JsonResult selectEssayByFamilyId(Integer page, Integer limit, Essay essay, FamilyUser familyUser, Integer sort, String searchData) {
        //先查浏览者身份
        QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
        familyUserFind.eq("family_id", essay.getFamilyId());
        familyUserFind.eq("user_id", familyUser.getUserId());
        FamilyUser familyUserData = familyUserDao.selectOne(familyUserFind);

        IPage<Essay> pageData = new Page<>(page, limit);
        QueryWrapper<Essay> essaySelect = new QueryWrapper<Essay>();
        if (essay != null && essay.getUserId() != null) {
            essaySelect.eq("user_id", essay.getUserId());
        }
        if (familyUserData == null) {
            essaySelect.eq("open", 3);
        }
        if (familyUserData != null && familyUserData.getRelation() == 3) {
            essaySelect.gt("open", 1);
        }

        essaySelect.eq("family_id", essay.getFamilyId());
        //1按浏览量 2点赞量 3评论数
        if (sort != null && sort == 1) {
            essaySelect.orderByDesc("browse_number");
        } else if (sort != null && sort == 2) {
            essaySelect.orderByDesc("praise_number");
        } else if (sort != null && sort == 3) {
            essaySelect.orderByDesc("discuss_number");
        } else if (sort != null && sort == 4) {
            essaySelect.orderByDesc("is_knit");
            essaySelect.orderByDesc("create_time");

        } else {
            essaySelect.orderByDesc("id");
        }

        QueryWrapper<EssayShare> essaysharedata = new QueryWrapper<EssayShare>();
        essaysharedata.eq("family_id", essay.getFamilyId());
        List<EssayShare> da = essayShareDao.selectList(essaysharedata);
        for (int i = 0; i < da.size(); i++) {
            essaySelect.or().eq("id", da.get(i).getEssayId());
        }
        essaySelect.like("content", searchData);
        pageData = essayDao.selectPage(pageData, essaySelect);

        List<Essay> essayList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        if (essayList == null || essayList.size() == 0) {
            map.put("essayList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        List<EssayAndImgSelectResVo> essayAndImgSelectResVoList = new ArrayList<EssayAndImgSelectResVo>();
        for (int i = 0; i < essayList.size(); i++) {
            EssayAndImgSelectResVo essayAndImgSelectResVo = new EssayAndImgSelectResVo();
            //文章内容
            essayAndImgSelectResVo.setEssay(essayList.get(i));
            //文章图片
            QueryWrapper<EssayImg> essayImgSelect = new QueryWrapper<EssayImg>();
            essayImgSelect.eq("essay_id", essayList.get(i).getId());
            essayAndImgSelectResVo.setEssayImgList(essayImgDao.selectList(essayImgSelect));
            //浏览者点赞信息
            QueryWrapper<EssaySupport> essaySupportFind = new QueryWrapper<EssaySupport>();
            essaySupportFind.eq("essay_id", essayList.get(i).getId());
            essaySupportFind.eq("user_id", familyUser.getUserId());
            EssaySupport essaySupportData = essaySupportDao.selectOne(essaySupportFind);
            essayAndImgSelectResVo.setEssaySupport(essaySupportData);
            //作者信息
            User user = userDao.selectById(essayList.get(i).getUserId());
            if (user != null) {
                UserFindResVo userFindResVo = new UserFindResVo();
                userFindResVo.setAvatar(user.getAvatar());
                userFindResVo.setNickName(user.getNickName());
                userFindResVo.setRealName(user.getRealName());
                essayAndImgSelectResVo.setUserFindResVo(userFindResVo);
            }
            essayAndImgSelectResVoList.add(essayAndImgSelectResVo);
        }
        map.put("essayList", essayAndImgSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult findEssayById(Essay essay) {
        //文章内容
        Essay essayData = essayDao.selectById(essay.getId());
        EssayFindResVo essayFindResVo = new EssayFindResVo();
        essayFindResVo.setEssay(essayData);

        //文章图片
        QueryWrapper<EssayImg> essayImgSelect = new QueryWrapper<EssayImg>();
        essayImgSelect.eq("essay_id", essay.getId());
        essayFindResVo.setEssayImgList(essayImgDao.selectList(essayImgSelect));

        //作者信息
        User user = userDao.selectById(essayData.getUserId());
        UserFindResVo userFindResVo = new UserFindResVo();
        ObjectUtil.copyByName(user, userFindResVo);
        essayFindResVo.setUserFindResVo(userFindResVo);

        //浏览者点赞情况
        QueryWrapper<EssaySupport> essaySupportFind = new QueryWrapper<EssaySupport>();
        essaySupportFind.eq("essay_id", essay.getId());
        essaySupportFind.eq("user_id", essay.getUserId());
        EssaySupport essaySupportData = essaySupportDao.selectOne(essaySupportFind);
        essayFindResVo.setEssaySupport(essaySupportData);

        return ResponseUtil.ok("获取成功", essayFindResVo);
    }

    @Override
    public JsonResult updateEssayById(Essay essay) throws Exception {
        int result = essayDao.updateById(essay);
        return result == 0 ? ResponseUtil.fail("修改失败") : ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult updateEssayBrowseNumberById(Essay essay) throws Exception {

        //再查询文章是否存在
        Essay essayData = essayDao.selectById(essay.getId());
        if (essayData == null) {
            return ResponseUtil.fail("文章不存在");
        }
        Essay essayUpdate = new Essay();
        essayUpdate.setId(essay.getId());
        essayUpdate.setBrowseNumber(essay.getBrowseNumber());
        essayUpdate.setKnitStartTime(essay.getKnitStartTime());
        essayUpdate.setKnitEndTime(essay.getKnitStartTime() + essayData.getKnitCycle());//刷新加锁倒计时结束日期
        int result = essayDao.updateById(essayUpdate);
        if (result == 0) {
            throw new Exception("修改失败");
        }

        //分别给作者和浏览人下发1米
        User user1 = new User();
        user1.setId(essayData.getUserId());
        user1.setRice(1);
        result = userDao.setInc(user1);
        if (result == 0) {
            throw new Exception("浏览失败");
        }

        User user2 = new User();
        user2.setId(essay.getUserId());
        user2.setRice(1);
        result = userDao.setInc(user2);
        if (result == 0) {
            throw new Exception("浏览失败");
        }


        List<RiceRecord> riceRecordList = new ArrayList<RiceRecord>();
        RiceRecord riceRecord1 = new RiceRecord();
        riceRecord1.setUserId(essayData.getUserId());
        riceRecord1.setRice(1);
        riceRecord1.setContent("文章被浏览");
        riceRecord1.setCreateTime(DateUtils.getDateTime());
        riceRecordList.add(riceRecord1);
        RiceRecord riceRecord2 = new RiceRecord();
        riceRecord2.setUserId(essay.getUserId());
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
    public JsonResult updateEssayUnlockById(Essay essay) throws Exception {
        //看看用户米够不够
        User user = userDao.selectById(essay.getUserId());
        if (user == null) {
            return ResponseUtil.fail("解锁失败");
        }

        int multiple = essay.getKnitCycle() / 2592000;//倍数

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
        Essay essayUpdate = new Essay();
        essayUpdate.setId(essay.getId());
        essayUpdate.setKnit(essay.getKnit());
        essayUpdate.setKnitCycle(essay.getKnitCycle());
        essayUpdate.setKnitStartTime(essay.getKnitStartTime());
        essayUpdate.setKnitEndTime(essay.getKnitEndTime());
        result = essayDao.updateById(essayUpdate);
        if (result == 0) {
            throw new Exception("解锁失败");
        }
        return ResponseUtil.ok("解锁成功");
    }


    @Override
    public JsonResult deleteEssayById(Integer id) throws Exception {
        //删除帖子
        int result = essayDao.deleteById(id);
        if (result == 0) {
            throw new Exception("删除失败");
        }

        //删除文章图片
        QueryWrapper<EssayImg> essayImgDelete = new QueryWrapper<EssayImg>();
        essayImgDelete.eq("essay_id", id);
        essayImgDao.delete(essayImgDelete);


        //点赞和评论就不删除了，有点多，避免卡死风险
        return ResponseUtil.ok("删除成功");
    }


    @Override
    public JsonResult selectotherEssay(Integer limit, Integer page, Integer sheuserId, Integer userId) throws Exception {
        QueryWrapper<FamilyUser> familyUserQueryWrapper = new QueryWrapper<FamilyUser>();
        familyUserQueryWrapper.eq("user_id", sheuserId);
        familyUserQueryWrapper.eq("status", 2);
        List<FamilyUser> familyUsers = familyUserDao.selectList(familyUserQueryWrapper);


        QueryWrapper<FamilyUser> familyUserQueryWrapper1 = new QueryWrapper<FamilyUser>();
        familyUserQueryWrapper1.eq("user_id", userId);
        familyUserQueryWrapper1.eq("status", 2);
        List<FamilyUser> familyUsers1 = familyUserDao.selectList(familyUserQueryWrapper1);

        IPage<Essay> pageData = new Page<>(page, limit);
        QueryWrapper<Essay> essayQueryWrapper = new QueryWrapper<Essay>();
        for (int i = 0; i < familyUsers.size(); i++) {
            for (int j = 0; j < familyUsers1.size(); j++) {
                if (familyUsers.get(i).getFamilyId() == familyUsers1.get(j).getFamilyId()) {
                    essayQueryWrapper.or().eq("family_id", familyUsers.get(i).getFamilyId()).eq("user_id", sheuserId);


                }
            }
        }
        essayQueryWrapper.orderByDesc("is_knit");

        essayQueryWrapper.or().eq("user_id", sheuserId).eq("open", 3);
        essayQueryWrapper.orderByDesc("create_time");

        pageData = essayDao.selectPage(pageData, essayQueryWrapper);
        List<Essay> essayList = pageData.getRecords();
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        if (essayList == null || essayList.size() == 0) {
            map.put("essayList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        //拿一下该作者信息，这里因为是一个作者的所有帖子，查一次即可
        UserFindResVo userFindResVo = new UserFindResVo();
        User user = userDao.selectById(sheuserId);
        ObjectUtil.copyByName(user, userFindResVo);

        List<EssayAndImgSelectResVo> essayAndImgSelectResVoList = new ArrayList<EssayAndImgSelectResVo>();
        for (int i = 0; i < essayList.size(); i++) {
            EssayAndImgSelectResVo essayAndImgSelectResVo = new EssayAndImgSelectResVo();
            //文章内容
            essayAndImgSelectResVo.setEssay(essayList.get(i));
            //文章图片
            QueryWrapper<EssayImg> essayImgSelect = new QueryWrapper<EssayImg>();
            essayImgSelect.eq("essay_id", essayList.get(i).getId());
            essayAndImgSelectResVo.setEssayImgList(essayImgDao.selectList(essayImgSelect));
            //家族信息
            essayAndImgSelectResVo.setFamily(familyDao.selectById(essayList.get(i).getFamilyId()));
            //点赞信息
            QueryWrapper<EssaySupport> essaySupportFind = new QueryWrapper<EssaySupport>();
            essaySupportFind.eq("essay_id", essayList.get(i).getId());
            essaySupportFind.eq("user_id", sheuserId);
            EssaySupport essaySupportData = essaySupportDao.selectOne(essaySupportFind);
            essayAndImgSelectResVo.setEssaySupport(essaySupportData);

            //作者信息
            essayAndImgSelectResVo.setUserFindResVo(userFindResVo);

            essayAndImgSelectResVoList.add(essayAndImgSelectResVo);
        }
        map.put("essayList", essayAndImgSelectResVoList);

        return ResponseUtil.ok("成功", map);

    }

    /**
     * 根据id查询帖子
     *
     * @param id 帖子id
     * @return
     */
    @Override
    public Essay selectEssayById(Integer id) {
        return essayDao.selectById(id);
    }
}
