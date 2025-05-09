package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.controller.EssayController;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.mapper.*;
import com.wx.genealogy.system.service.FamilyGenealogyService;
import com.wx.genealogy.system.service.FamilyService;
import com.wx.genealogy.system.vo.res.FamilyAndUserFindResVo;
import com.wx.genealogy.system.vo.res.UserFindResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 家族 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@Service
public class FamilyServiceImpl extends ServiceImpl<FamilyDao, Family> implements FamilyService {

    @Autowired
    private FamilyDao familyDao;

    @Autowired
    private FamilyUserDao familyUserDao;

    @Autowired
    private UserFamilyFollowDao userFamilyFollowDao;

    @Autowired
    private UserDao userDao;
    @Autowired
    private EssayShareDao essayShareDao;
    @Autowired
    private FamilyGenealogyService familyGenealogyService;


    @Autowired
    private RiceRecordDao riceRecordDao;

    @Autowired
    private EssayDao essayDao;

    @Autowired
    private FamilyCleanDao familyCleanDao;

    @Autowired
    private EssayImgDao essayImgDao;
    @Autowired
    private FamilyMessageDao familyMessageDao;
    @Autowired
    private FamilyGenealogyDao familyGenealogyDao;

    @Autowired
    EssayController EssayController;
    private Object essaydata;

    @Override
    public JsonResult insertFamily(Family family, FamilyUser familyUser) throws Exception {
        QueryWrapper<Family> familySelect = new QueryWrapper<Family>();
        familySelect.eq("name", family.getName());
        Integer familyCount = familyDao.selectCount(familySelect);
        if (familyCount != null && familyCount > 0) {
            return ResponseUtil.fail("家族名称重复");
        }

        //每个账号最多10个家族
        familySelect = new QueryWrapper<Family>();
        familySelect.eq("user_id", family.getUserId());
        familyCount = familyDao.selectCount(familySelect);
        if (familyCount != null && familyCount >= 10) {
            return ResponseUtil.fail("每个账号最多创建10个家族");
        }

        //创建家族
        int result = familyDao.insert(family);
        if (result == 0) {
            throw new Exception("创建失败");
        }
        //家族创建成功默认创建者为管理员
        familyUser.setFamilyId(family.getId());
        familyUser.setLevel(1);
        familyUser.setRelation(1);
        familyUser.setStatus(2);
        familyUser.setJoins(2);
        familyUser.setGeneration(0);
        familyUser.setCreateTime(DateUtils.getDateTime());
        familyUser.setGenealogyName(familyUser.getGenealogyName());
        familyUser.setIntroduce(familyUser.getGenealogyName());
        familyUserDao.insert(familyUser);
        if (result == 0) {
            throw new Exception("创建失败");
        }

        //创建家谱
//        FamilyGenealogy familyGenealogy = new FamilyGenealogy();
//        familyGenealogy.setIdentity(1);
//        familyGenealogy.setFamilyId(family.getId());
//        familyGenealogy.setRelation(1);
//        familyGenealogy.setUid(1);
//        familyGenealogy.setGeneration(1);
//        familyGenealogy.setIsAlive(1);
//        familyGenealogy.setUserId(familyUser.getUserId());
//        familyGenealogyDao.insert(familyGenealogy);


        //下发200米
        User user = new User();
        user.setId(family.getUserId());
        user.setRice(200);
        result = userDao.setInc(user);
        if (result == 0) {
            throw new Exception("创建失败");
        }

        //记录到米收支明细
        RiceRecord riceRecord = new RiceRecord();
        riceRecord.setUserId(family.getUserId());
        riceRecord.setRice(200);
        riceRecord.setContent("创建家族");
        riceRecord.setCreateTime(familyUser.getCreateTime());
        result = riceRecordDao.insert(riceRecord);
        if (result == 0) {
            throw new Exception("创建失败");
        }

        // 创建用户消息提示数据
        FamilyMessage familyMessage = new FamilyMessage();
        familyMessage.setUserId(family.getUserId());
        familyMessage.setFamilyId(familyUser.getFamilyId());
        System.out.println("***");
        System.out.println(familyMessage);
        familyMessageDao.insert(familyMessage);
        familyGenealogyService.upFamilyGenealogyChart(family.getId());
        return ResponseUtil.ok("创建成功");
    }

    @Override
    public JsonResult updateFamilyById(Family family) throws Exception {
        if (family.getName() != null && family.getName().equals("") == false) {
            QueryWrapper<Family> familySelect = new QueryWrapper<Family>();
            familySelect.eq("name", family.getName());
            Family familyData = familyDao.selectOne(familySelect);
            if (familyData != null) {
                if (familyData.getId().equals(family.getId()) == false) {
                    return ResponseUtil.fail("家族名称重复");
                }
            }
        }
        int result = familyDao.updateById(family);
        return result == 0 ? ResponseUtil.fail("修改失败") : ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult selectFamilyByName(Integer page, Integer limit, String name) {
        IPage<Family> pageData = new Page<>(page, limit);
        QueryWrapper<Family> familySelect = new QueryWrapper<Family>();
        familySelect.like("name", name);

        pageData = familyDao.selectPage(pageData, familySelect);
        List<Family> familyList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("familyList", familyList);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult findFamilyByIdAndUserId(Integer id, Integer userId, Integer authorId, Integer familyGenealogyId) {
        Family familyData = familyDao.selectById(id);
        if (familyData == null) {
            return ResponseUtil.fail("家族信息不存在");
        }

        //准备
        FamilyAndUserFindResVo familyAndUserFindResVo = new FamilyAndUserFindResVo();
        //家族信息
        familyAndUserFindResVo.setFamily(familyData);
        //个人系统信息
        if (null != familyGenealogyId && familyGenealogyId > 0) {
            FamilyGenealogy familyGenealogy = familyGenealogyDao.selectById(familyGenealogyId);
            if (null != familyGenealogy) {
                familyAndUserFindResVo.setFamilyGenealogy(familyGenealogy);
            }
        } else {
            QueryWrapper<FamilyGenealogy> familyGenealogyQueryWrapper = new QueryWrapper<FamilyGenealogy>();
            familyGenealogyQueryWrapper.eq("family_id", id);
            if (null != authorId && authorId > 0) {
                familyGenealogyQueryWrapper.eq("user_id", authorId);
                List<FamilyGenealogy> list = familyGenealogyDao.selectList(familyGenealogyQueryWrapper);
                if (null != list && list.size() > 0) {
                    familyAndUserFindResVo.setFamilyGenealogy(list.get(0));
                }
            }
        }
        if (null != authorId && authorId > 0) {
            User user = userDao.selectById(authorId);
            UserFindResVo userFindResVo = new UserFindResVo();
            ObjectUtil.copyByName(user, userFindResVo);
            familyAndUserFindResVo.setAuthor(userFindResVo);
        }

        User user = userDao.selectById(userId);
        UserFindResVo userFindResVo = new UserFindResVo();
        ObjectUtil.copyByName(user, userFindResVo);
        familyAndUserFindResVo.setUserFindResVo(userFindResVo);
        //判断是否关注
        QueryWrapper<UserFamilyFollow> userFamilyFollowFind = new QueryWrapper<UserFamilyFollow>();
        userFamilyFollowFind.eq("user_id", userId);
        userFamilyFollowFind.eq("family_id", familyData.getId());
        UserFamilyFollow userFamilyFollowData = userFamilyFollowDao.selectOne(userFamilyFollowFind);
        familyAndUserFindResVo.setUserFamilyFollow(userFamilyFollowData);
        //判断是否加入
        QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
        familyUserFind.eq("user_id", userId);
        familyUserFind.eq("family_id", familyData.getId());
        FamilyUser familyUserData = familyUserDao.selectOne(familyUserFind);
        familyAndUserFindResVo.setFamilyUser(familyUserData);
        //查找最后一次打扫时间
        QueryWrapper<FamilyClean> familyCleanFind = new QueryWrapper<FamilyClean>();
        familyCleanFind.eq("family_id", familyData.getId());
        familyCleanFind.orderByDesc("clean_time");
        familyCleanFind.last("limit 1");

        FamilyClean familyCleanData = familyCleanDao.selectOne(familyCleanFind);
        familyAndUserFindResVo.setFamilyClean(familyCleanData);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        familyAndUserFindResVo.setNowDate(df.format(new Date()));

        //查找家族成员的个数
        QueryWrapper<FamilyGenealogy> familyGenealogyQueryWrapper = new QueryWrapper<FamilyGenealogy>();
        familyGenealogyQueryWrapper.eq("family_id", id);
        List<FamilyGenealogy> xxx = familyGenealogyDao.selectList(familyGenealogyQueryWrapper);
        TreeSet<Integer> familyIdList = new TreeSet<Integer>();
        for (int i = 0; i < xxx.size(); i++) {
            FamilyGenealogy familyGenealogy = xxx.get(i);
            familyIdList.add(familyGenealogy.getFamilyLianId());
        }

        QueryWrapper<FamilyUser> familyUserSelect = new QueryWrapper<FamilyUser>();
        familyUserSelect.eq("family_id", id);
        familyUserSelect.eq("status", 2);

        if (familyIdList.size() > 0) {
            familyUserSelect.or().in("id", familyIdList);
        }

        List<FamilyUser> ccc = familyUserDao.selectList(familyUserSelect);
        familyAndUserFindResVo.setPeoplenumber(ccc.size());//家族人数总数
        //获取家族信息
        QueryWrapper<FamilyMessage> familyMessage = new QueryWrapper<FamilyMessage>();
        familyMessage.eq("family_id", familyData.getId());
        familyMessage.eq("user_id", userId);
        FamilyMessage familydata = familyMessageDao.selectOne(familyMessage);
        familyAndUserFindResVo.setFamilyMessage(familydata);
        if (familyData.getUserId() == userId) {
            // 查询该family_id下的所有message记录
            QueryWrapper<FamilyMessage> sumQuery = new QueryWrapper<>();
            sumQuery.eq("family_id", familyData.getId());

            // 查询所有符合条件的记录
            List<FamilyMessage> messages = familyMessageDao.selectList(sumQuery);

            // 计算editapply_message和pually_message的总和
            int totalEditApply = 0;
            int totalPually = 0;

            for (FamilyMessage message : messages) {
                totalEditApply += message.getEditapplyMessage(); // 假设字段名是editapplyMessage
                totalPually += message.getPuapplyMessage();     // 假设字段名是puallyMessage
            }

            // 将统计结果设置到familydata中（假设FamilyMessage类有对应的setter方法）
            familydata.setEditapplyMessage(totalEditApply);
            familydata.setPuapplyMessage(totalPually);

            // 更新到VO
            familyAndUserFindResVo.setFamilyMessage(familydata);
        }


        //获取多少代和人数
        List<Map<Object, Object>> mapList = familyGenealogyDao.countPeopleByGeneration(familyData.getId());
        familyAndUserFindResVo.setFamilyMap(mapList);

        //查询authorId和userId是否在同一家族
//        QueryWrapper<Family> familyQueryWrapper = new QueryWrapper<Family>();
//        familyQueryWrapper.eq("user_id",userId);
//        List<Family> familyList = familyDao.selectList(familyQueryWrapper);
//        boolean show = familyList.stream().anyMatch(family -> family.getId().equals(id));

        familyAndUserFindResVo.setShow(null == familyAndUserFindResVo.getFamilyGenealogy() ? false : true);
        if (familyAndUserFindResVo.isShow()) {
            familyAndUserFindResVo.getUserFindResVo().setAvatar(familyAndUserFindResVo.getFamilyGenealogy().getHeadImg());
        }
        return ResponseUtil.ok("获取成功", familyAndUserFindResVo);
    }

    @Override
    public JsonResult selectFamily(Integer page, Integer limit, Family family) {
        Page<Family> pageData = new Page<>(page, limit);
        pageData = familyDao.selectPageList(pageData, family);
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("familyList", pageData.getRecords());
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        return ResponseUtil.ok("获取成功", map);
    }


    public JsonResult deleteFamilyById(Integer id, Integer Userid) {
        QueryWrapper<FamilyUser> familyUserQueryWrapper = new QueryWrapper<FamilyUser>();

        familyUserQueryWrapper.eq("family_id", id);
        List<FamilyUser> familyLIstdata = familyUserDao.selectList(familyUserQueryWrapper);
        for (int i = 0; i < familyLIstdata.size(); i++) {
            FamilyUser familyUser = new FamilyUser();
            familyUser.setId(familyLIstdata.get(i).getId());
            familyUser.setFamilyId(0);
            familyUserDao.updateById(familyUser);
        }

        Family familyData = familyDao.selectById(id);
        if (familyData == null) {
            return ResponseUtil.fail("家族信息不存在");
        }
        int result = familyDao.deleteById(id);
        if (result == 0) {
            return ResponseUtil.fail("删除失败");
        }

//
//        QueryWrapper<FamilyUser> familyUserSelect = new QueryWrapper<FamilyUser>();
//        familyUserSelect.eq("family_id", id);
//        result = familyUserDao.delete(familyUserSelect);
//        if(result==0){
//            return ResponseUtil.fail("删除失败");
//        }


        QueryWrapper<EssayImg> essayImgDelete = new QueryWrapper<EssayImg>();
        QueryWrapper<Essay> EssaySelect = new QueryWrapper<Essay>();
        EssaySelect.eq("family_id", id);
        essayDao.delete(EssaySelect);

        EssaySelect.select("id");


        List<Essay> essayList = essayDao.selectList(EssaySelect);
        for (int i = 0; i < essayList.size(); i++) {
            essayImgDelete.eq("essay_id", essayList.get(i).getId());
            essayImgDao.delete(essayImgDelete);//删除文章图片
        }


        QueryWrapper<EssayShare> essayshareDelete = new QueryWrapper<EssayShare>();
        essayshareDelete.eq("family_id", id);
        essayShareDao.delete(essayshareDelete);


        return ResponseUtil.ok("删除成功");


        //点赞和评论就不删除了，有点多，避免卡死风险
        //return ResponseUtil.ok("删除成功");

    }


}
