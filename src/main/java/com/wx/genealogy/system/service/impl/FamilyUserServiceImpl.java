package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.exception.ServiceException;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.mapper.*;
import com.wx.genealogy.system.service.FamilyMessageService;
import com.wx.genealogy.system.service.FamilyUserService;
import com.wx.genealogy.system.service.UserService;
import com.wx.genealogy.system.vo.req.FamilyUserPageVo;
import com.wx.genealogy.system.vo.res.FamilyUserAndFamilySelectResVo;
import com.wx.genealogy.system.vo.res.FamilyUserAndUserSelectResVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 家族用户关联 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@Service
public class FamilyUserServiceImpl extends ServiceImpl<FamilyUserDao, FamilyUser> implements FamilyUserService {

    @Autowired
    private FamilyUserDao familyUserDao;

    @Autowired
    private FamilyMessageService familyMessageService;

    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private FamilyMessageDao familyMessageDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private FamilyManageLogDao familyManageLogDao;

    @Autowired
    private RiceRecordDao riceRecordDao;

    @Autowired
    private FamilyGenealogyDao familyGenealogyDao;

    @Autowired
    private FamilyGenealogyReceiveDao familyGenealogyReceiveDao;

    @Resource
    private UserService userService;

    @Override
    public JsonResult insertFamilyUser(FamilyUser familyUser) throws Exception {
        int result = 0;


        //下发审批消息


        //先查询是否存在
        QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
        familyUserFind.eq("user_id", familyUser.getUserId());
        familyUserFind.eq("family_id", familyUser.getFamilyId());
        FamilyUser familyUserData = familyUserDao.selectOne(familyUserFind);
        //不存在
        if (familyUserData == null) {
            result = familyUserDao.insert(familyUser);
            if (result == 0) return ResponseUtil.fail("申请提交失败");

        } else {
            //已经存在
            FamilyUser familyUserDatas = new FamilyUser();
            familyUserDatas.setUserId(familyUser.getUserId());
            familyUserDatas.setFamilyId(familyUser.getFamilyId());
            familyUserDatas.setId(familyUserData.getId());
            familyUserDatas.setStatus(1);
            familyUserDatas.setCreateTime(DateUtils.getDateTime());
            result = familyUserDao.updateById(familyUserDatas);
            if (result == 0) return ResponseUtil.fail("申请提交失败");
        }


        QueryWrapper<FamilyUser> familyuser_find = new QueryWrapper<FamilyUser>();
        familyuser_find.eq("family_id", familyUser.getFamilyId());
        familyuser_find.gt("user_id", 0);
        familyuser_find.lt("level", 3);

        List<FamilyUser> family_find = familyUserDao.selectList(familyuser_find);
        for (int i = 0; i < family_find.size(); i++) {
            QueryWrapper<FamilyMessage> familymessage = new QueryWrapper<FamilyMessage>();
            familymessage.eq("family_id", familyUser.getFamilyId());
            familymessage.eq("user_id", family_find.get(i).getUserId());
            FamilyMessage familydata = familyMessageDao.selectOne(familymessage);
            if (familydata == null) {
                FamilyMessage familyMessage = new FamilyMessage();
                familyMessage.setFamilyshenMessage(1);
                familyMessage.setFamilyId(familyUser.getFamilyId());
                familyMessage.setUserId(family_find.get(i).getUserId());
                familyMessageDao.insert(familyMessage);
            } else {
                FamilyMessage familyMessage = new FamilyMessage();
                familyMessage.setId(familydata.getId());
                familyMessage.setFamilyshenMessage(familydata.getFamilyshenMessage() + 1);
                familyMessage.setFamilyId(familyUser.getFamilyId());
                familyMessage.setUserId(family_find.get(i).getUserId());
                familyMessageDao.updateById(familyMessage);
            }

            FamilyUser fami = new FamilyUser();
            fami.setId(family_find.get(i).getId());
            fami.setUpdateTime(DateUtils.getDateTime());
            familyUserDao.updateById(fami);


        }
        return ResponseUtil.ok("申请提交成功");


    }

    @Override
    public JsonResult updateFamilyupdates(Integer id, Integer status, Integer userId, Integer familyId, Integer generation, String name, Integer level, Integer joins) {
        System.out.println("=====================>" + joins);
        FamilyUser familyUser = new FamilyUser();
        familyUser.setId(id);
        familyUser.setUpdatestatus(1);
        familyUser.setStatus(status);
        familyUser.setGeneration(generation);
        familyUser.setGenealogyName(name);
        if (joins != null) {
            familyUser.setJoins(joins);
            if (joins == 1) {
                FamilyMessage familyMessage = new FamilyMessage();
                familyMessage.setFamilyId(familyId);
                familyMessage.setFamilyshenMessage3(1);
                System.out.println(familyMessage);
                familyMessageDao.updateFamilyMessage(familyMessage);
            }

        }
        familyUserDao.updateById(familyUser);


//        需要跟新日志
//        FamilyMessage familyMessage = new FamilyMessage();
//        familyMessage.setFamilyshenMessage(1);
//        familyMessage.setFamilyId(familyUser.getFamilyId());
//        familyMessage.setUserId(userId);
//        familyMessageDao.updateById(familyMessage);
        return ResponseUtil.ok("操作成功");


    }


    public Boolean claim(Integer userId, Integer familyId) {
        if (Objects.isNull(userId)) {
            return false;
        }
        //查询该用户有没有认领
        FamilyGenealogy familyGenealogy = new FamilyGenealogy();
        familyGenealogy.setFamilyId(familyId);
        familyGenealogy.setUserId(userId);
        List list = familyGenealogyDao.list(familyGenealogy);

        if (list.size() > 0) {
            return false;
        }
        //查询有没有待审核
//        QueryWrapper<FamilyGenealogyReceive> queryWrapperReceive = new QueryWrapper<>();
//        queryWrapperReceive.eq("family_id",familyId);
//        queryWrapperReceive.eq("user_id",userId);
//        queryWrapperReceive.eq("status",0);
//        List listf = familyGenealogyReceiveDao.selectList(queryWrapperReceive);
//        if(listf.size()>0){
//            return false;
//        }
        System.out.println("可以认领");
        return true;
    }

    @Override
    public JsonResult updateFamilyUp(Integer userId, Integer familyIdUserId, Integer generation, String genealogyName, Integer relation, Integer familyId) {
        boolean aa = claim(userId, familyId);
        System.out.println("认领状态:" + aa);
        if (aa == false) {
            FamilyGenealogy familyGenealogy = new FamilyGenealogy();
            familyGenealogy.setGeneration(generation);
            familyGenealogy.setGenealogyName(genealogyName);
            familyGenealogy.setFamilyId(familyId);
            if (familyGenealogyDao.selectNameGenerationList(familyGenealogy) > 0) {
                throw new ServiceException("家谱名重复！");
            }
            QueryWrapper<FamilyGenealogy> familyGenealogyQueryWrapper = new QueryWrapper<FamilyGenealogy>();
            familyGenealogyQueryWrapper.eq("family_id", familyId);
            familyGenealogyQueryWrapper.eq("user_id", userId);
            FamilyGenealogy familyGenealogy1 = familyGenealogyDao.selectList(familyGenealogyQueryWrapper).get(0);
            familyGenealogy1.setGenealogyName(genealogyName);
            familyGenealogyDao.updateById(familyGenealogy1);

            //已经认领了就需要查询


        }


        FamilyUser familyUser = new FamilyUser();
        familyUser.setFamilyId(familyId);
        familyUser.setId(familyIdUserId);
        familyUser.setGenealogyName(genealogyName);
        familyUser.setGeneration(generation);
        familyUser.setRelation(relation);
        familyUserDao.updateById(familyUser);


//        需要跟新日志
//        FamilyMessage familyMessage = new FamilyMessage();
//        familyMessage.setFamilyshenMessage(1);
//        familyMessage.setFamilyId(familyUser.getFamilyId());
//        familyMessage.setUserId(userId);
//        familyMessageDao.updateById(familyMessage);
        return ResponseUtil.ok("操作成功");


    }


    @Override
    public JsonResult updateFamilydai(Integer generation, Integer familyId, Integer userId, String puming, String hunyin, Integer relation) {
        QueryWrapper<FamilyUser> familyUserQueryWrapper = new QueryWrapper<FamilyUser>();
        familyUserQueryWrapper.eq("family_id", familyId);
        familyUserQueryWrapper.eq("user_id", userId);
        FamilyUser familyUser = familyUserDao.selectOne(familyUserQueryWrapper);


//        QueryWrapper<FamilyGenealogy> familyGenealogyQueryWrapper = new QueryWrapper<FamilyGenealogy>();
//        familyGenealogyQueryWrapper.eq("family_id",familyId);
//        familyGenealogyQueryWrapper.eq("family_user_id",familyUser.getId());
//        familyGenealogyQueryWrapper.eq("user_id",userId);
//        FamilyGenealogy familyGenealogy = familyGenealogyDao.selectOne(familyGenealogyQueryWrapper);
//

        FamilyUser familyUser1 = new FamilyUser();
        familyUser1.setGeneration(generation);
        familyUser1.setRelation(relation);
        familyUser1.setId(familyUser.getId());
        familyUserDao.updateById(familyUser1);


        Family familyQueryWrapper = new Family();
        familyQueryWrapper.setId(familyId);
        familyQueryWrapper.setHunname(hunyin);
        familyQueryWrapper.setPuname(puming);
        familyDao.updateById(familyQueryWrapper);

//
//        if(familyGenealogy==null){
//            FamilyGenealogy familyGenealogys = new FamilyGenealogy();
//            familyGenealogys.setFamilyId(familyId);
//            familyGenealogys.setFamilyUserId(familyUser.getId());
//            familyGenealogys.setFamilyLianId(familyUser.getId());
//            familyGenealogys.setUserId(userId);
//            familyGenealogys.setIdentity(1);
//            familyGenealogys.setSex(1);
//            QueryWrapper<User> user = new QueryWrapper<User>();
//            user.eq("id",userId);
//            User user1 = userDao.selectOne(user);
//            familyGenealogys.setGenealogyName(user1.getRealName());
//            familyGenealogys.setRelation(1);
//            familyGenealogys.setCreateTime(DateUtils.getDateTime());
//            familyGenealogys.setGeneration(generation);
//            familyGenealogyDao.insert(familyGenealogys);
//        }else{
//            FamilyGenealogy familyGenealogy1 = new FamilyGenealogy();
//            familyGenealogy1.setGeneration(generation);
//            familyGenealogy1.setId(familyGenealogy.getId());
//            familyGenealogyDao.updateById(familyGenealogy1);
//        }


        return ResponseUtil.ok("success");

    }

    @Override
    public JsonResult insertFamilyUserNoApply(FamilyUser familyUser) throws Exception {
        //先查询是否家族名重复
        QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
        familyUserFind.eq("family_id", familyUser.getFamilyId());
        familyUserFind.eq("genealogy_name", familyUser.getGenealogyName());
        FamilyUser familyUserData = familyUserDao.selectOne(familyUserFind);
        if (familyUserData != null) {
            return ResponseUtil.fail("家谱名重复");
        }

        int result = familyUserDao.insert(familyUser);
        return result == 0 ? ResponseUtil.fail("新增失败") : ResponseUtil.ok("新增成功");
    }

    @Override
    public JsonResult updateFamilyUserById(FamilyUser familyUser, FamilyManageLog familyManageLog) throws Exception {


        //清空消息

        FamilyMessage familyUpdate = new FamilyMessage();
        familyUpdate.setId(familyManageLog.getId());
        familyUpdate.setFamilyshenMessage2(0);
        System.out.println("*****************************");
        System.out.println(familyUpdate);
        familyMessageDao.updateFamilyMessageEmpty(familyUpdate);


        int result = familyUserDao.updateById(familyUser);
        if (result == 0) {
            throw new Exception("修改失败");
        }


        if (familyManageLog != null && familyManageLog.getFamilyId() != null && familyManageLog.getFamilyId() > 0) {
            result = familyManageLogDao.insert(familyManageLog);

            //添加消息

            QueryWrapper<FamilyUser> familyuser_find = new QueryWrapper<FamilyUser>();
            familyuser_find.eq("family_id", familyManageLog.getFamilyId());
            familyuser_find.gt("user_id", 0);
            familyuser_find.lt("level", 3);

            List<FamilyUser> family_find = familyUserDao.selectList(familyuser_find);
            for (int i = 0; i < family_find.size(); i++) {
                QueryWrapper<FamilyMessage> familymessage = new QueryWrapper<FamilyMessage>();
                familymessage.eq("family_id", familyManageLog.getFamilyId());
                familymessage.eq("user_id", family_find.get(i).getUserId());
                FamilyMessage familydata = familyMessageDao.selectOne(familymessage);
                if (familydata == null) {
                    FamilyMessage familyMessage = new FamilyMessage();
                    familyMessage.setFamilylogMessage(1);
                    familyMessage.setFamilyId(familyManageLog.getFamilyId());
                    familyMessage.setUserId(family_find.get(i).getUserId());
                    familyMessageDao.insert(familyMessage);
                } else {
                    FamilyMessage familyMessage = new FamilyMessage();
                    familyMessage.setId(familydata.getId());
                    familyMessage.setFamilylogMessage(familydata.getFamilylogMessage() + 1);
                    familyMessage.setFamilyId(familyManageLog.getFamilyId());
                    familyMessage.setUserId(family_find.get(i).getUserId());
                    familyMessageDao.updateById(familyMessage);
                }

                FamilyUser fami = new FamilyUser();
                fami.setId(family_find.get(i).getId());
                fami.setUpdateTime(DateUtils.getDateTime());
                familyUserDao.updateById(fami);

            }

            /////////////////


            if (result == 0) {
                throw new Exception("日志记录失败");
            }
        }
        return ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult updateStatusById(FamilyUser familyUser, FamilyManageLog familyManageLog) throws Exception {
        FamilyUser familyUserData = familyUserDao.selectById(familyUser.getId());
        if (familyUserData == null) {
            throw new Exception("申请记录不存在");
        }
        Family familyData = familyDao.selectById(familyUserData.getFamilyId());
        if (familyData == null) {
            throw new Exception("家族不存在");
        }
        //看是不是审核通过，如果是family的人数+1，为了防止因为多次触发产生的问题，所以得查一下
        if (familyUser.getStatus() == 2) {
            if (familyUserData.getStatus() == 1) {
                int result = familyUserDao.updateById(familyUser);
                if (result == 1) {
                    Family familyUpdate = new Family();
                    familyUpdate.setId(familyData.getId());
                    familyUpdate.setPeopleNumber(familyData.getPeopleNumber() + 1);
                    familyUpdate.setExamineNumber(0);
                    result = familyDao.updateById(familyUpdate);
                    if (result == 0) {
                        throw new Exception("审核失败");
                    }
                    if (familyManageLog != null && familyManageLog.getFamilyId() != null && familyManageLog.getFamilyId() > 0) {
                        result = familyManageLogDao.insert(familyManageLog);

                        //添加消息

                        QueryWrapper<FamilyUser> familyuser_find = new QueryWrapper<FamilyUser>();
                        familyuser_find.eq("family_id", familyManageLog.getFamilyId());
                        familyuser_find.gt("user_id", 0);
                        familyuser_find.lt("level", 3);

                        List<FamilyUser> family_find = familyUserDao.selectList(familyuser_find);
                        for (int i = 0; i < family_find.size(); i++) {
                            QueryWrapper<FamilyMessage> familymessage = new QueryWrapper<FamilyMessage>();
                            familymessage.eq("family_id", familyManageLog.getFamilyId());
                            familymessage.eq("user_id", family_find.get(i).getUserId());
                            FamilyMessage familydata = familyMessageDao.selectOne(familymessage);
                            if (familydata == null) {
                                FamilyMessage familyMessage = new FamilyMessage();
                                familyMessage.setFamilylogMessage(1);
                                familyMessage.setFamilyId(familyManageLog.getFamilyId());
                                familyMessage.setUserId(family_find.get(i).getUserId());
                                familyMessageDao.insert(familyMessage);
                            } else {
                                FamilyMessage familyMessage = new FamilyMessage();
                                familyMessage.setId(familydata.getId());
                                familyMessage.setFamilylogMessage(familydata.getFamilylogMessage() + 1);
                                familyMessage.setFamilyId(familyManageLog.getFamilyId());
                                familyMessage.setUserId(family_find.get(i).getUserId());
                                familyMessageDao.updateById(familyMessage);
                            }

                            FamilyUser fami = new FamilyUser();
                            fami.setId(family_find.get(i).getId());
                            fami.setUpdateTime(DateUtils.getDateTime());
                            familyUserDao.updateById(fami);
                        }

                        /////////////////


                        if (result == 0) {
                            throw new Exception("日志记录失败");
                        }
                    }

                    //分别给加入家族的新会员100米，给审批的管理员10米
                    User user1 = new User();
                    user1.setId(familyManageLog.getUserId());
                    user1.setRice(10);
                    result = userDao.setInc(user1);
                    if (result == 0) {
                        throw new Exception("下发米失败");
                    }

                    User user2 = new User();
                    user2.setId(familyUserData.getUserId());
                    user2.setRice(100);
                    result = userDao.setInc(user2);
                    if (result == 0) {
                        throw new Exception("下发米失败");
                    }


                    List<RiceRecord> riceRecordList = new ArrayList<RiceRecord>();
                    RiceRecord riceRecord1 = new RiceRecord();
                    riceRecord1.setUserId(familyManageLog.getUserId());
                    riceRecord1.setRice(10);
                    riceRecord1.setContent("审批申请");
                    riceRecord1.setCreateTime(familyManageLog.getCreateTime());
                    riceRecordList.add(riceRecord1);
                    RiceRecord riceRecord2 = new RiceRecord();
                    riceRecord2.setUserId(familyUserData.getUserId());
                    riceRecord2.setRice(100);
                    riceRecord2.setContent("加入新家族");
                    riceRecord2.setCreateTime(familyManageLog.getCreateTime());
                    riceRecordList.add(riceRecord2);

                    result = riceRecordDao.insertRiceRecordList(riceRecordList);
                    if (result == 0) {
                        throw new Exception("下发米失败");
                    }

                    return ResponseUtil.ok("审核成功");
                }
            }
            return ResponseUtil.ok("审核失败");
        } else {
            int result = familyUserDao.updateById(familyUser);
            if (result == 0) {
                throw new Exception("审核失败");
            }
            Family familyUpdate = new Family();
            familyUpdate.setId(familyData.getId());
            familyUpdate.setExamineNumber(0);
            result = familyDao.updateById(familyUpdate);
            if (result == 0) {
                throw new Exception("审核失败");
            }
            if (familyManageLog != null && familyManageLog.getFamilyId() != null && familyManageLog.getFamilyId() > 0) {
                result = familyManageLogDao.insert(familyManageLog);

                //添加消息

                QueryWrapper<FamilyUser> familyuser_find = new QueryWrapper<FamilyUser>();
                familyuser_find.eq("family_id", familyManageLog.getFamilyId());
                familyuser_find.gt("user_id", 0);
                familyuser_find.lt("level", 3);

                List<FamilyUser> family_find = familyUserDao.selectList(familyuser_find);
                for (int i = 0; i < family_find.size(); i++) {
                    QueryWrapper<FamilyMessage> familymessage = new QueryWrapper<FamilyMessage>();
                    familymessage.eq("family_id", familyManageLog.getFamilyId());
                    familymessage.eq("user_id", family_find.get(i).getUserId());
                    FamilyMessage familydata = familyMessageDao.selectOne(familymessage);
                    if (familydata == null) {
                        FamilyMessage familyMessage = new FamilyMessage();
                        familyMessage.setFamilylogMessage(1);
                        familyMessage.setFamilyId(familyManageLog.getFamilyId());
                        familyMessage.setUserId(family_find.get(i).getUserId());
                        familyMessageDao.insert(familyMessage);
                    } else {
                        FamilyMessage familyMessage = new FamilyMessage();
                        familyMessage.setId(familydata.getId());
                        familyMessage.setFamilylogMessage(familydata.getFamilylogMessage() + 1);
                        familyMessage.setFamilyId(familyManageLog.getFamilyId());
                        familyMessage.setUserId(family_find.get(i).getUserId());
                        familyMessageDao.updateById(familyMessage);
                    }

                    FamilyUser fami = new FamilyUser();
                    fami.setId(family_find.get(i).getId());
                    fami.setUpdateTime(DateUtils.getDateTime());
                    familyUserDao.updateById(fami);
                }

                /////////////////


                if (result == 0) {
                    throw new Exception("日志记录失败");
                }
            }
            return ResponseUtil.ok("审核成功");
        }
    }


    @Override
    public JsonResult cepstrumFamily(Integer userId, Integer familyId) {


        return ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult selectCepstrumFamily(Integer familyId) {
        QueryWrapper<FamilyUser> familyUserSelect = new QueryWrapper<FamilyUser>();
        familyUserSelect.eq("joins", "1");
        return ResponseUtil.ok("查询成功", familyUserDao.selectList(familyUserSelect));
    }

    @Override
    public JsonResult selectFamilyUserAndFamily(Integer page, Integer limit, FamilyUser familyUser) {
        IPage<FamilyUser> pageData = new Page<>(page, limit);


        QueryWrapper<FamilyUser> familyUserSelect = new QueryWrapper<FamilyUser>();
        if (familyUser.getUserId() != null) {
            familyUserSelect.eq("user_id", familyUser.getUserId());
        }
        if (familyUser.getStatus() != null) {
            familyUserSelect.eq("status", familyUser.getStatus());
        }
        if (familyUser.getGenealogyName() != null) {
            familyUserSelect.like("genealogy_name", familyUser.getGenealogyName());
        }

        if (familyUser.getUpdatestatus() != null) {
            familyUserSelect.eq("updatestatus", familyUser.getUpdatestatus());
        }
        familyUserSelect.ne("family_id", 0);
        familyUserSelect.orderByDesc("update_time");
        familyUserSelect.orderByAsc("create_time");

        pageData = familyUserDao.selectPage(pageData, familyUserSelect);
        List<FamilyUser> familyUserList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if (familyUserList == null || familyUserList.size() == 0) {
            map.put("FamilyUserAndFamilySelectResVo", null);
            return ResponseUtil.ok("获取成功", map);
        }


        //有数据，开始准备in查询
        TreeSet<Integer> familyIdList = new TreeSet<Integer>();
        for (int i = 0; i < familyUserList.size(); i++) {
            familyIdList.add(familyUserList.get(i).getFamilyId());
        }
        List<Family> familyList = familyDao.selectBatchIds(familyIdList);
        QueryWrapper<FamilyMessage> familymessagedata = new QueryWrapper<FamilyMessage>();
        familymessagedata.eq("user_id", familyUser.getUserId());

        List<FamilyMessage> familymessage = familyMessageDao.selectList(familymessagedata);


        List<FamilyUserAndFamilySelectResVo> familyUserAndFamilySelectResVoList = new ArrayList<FamilyUserAndFamilySelectResVo>();

        for (FamilyUser user : familyUserList) {
            FamilyUserAndFamilySelectResVo familyUserAndFamilySelectResVo = new FamilyUserAndFamilySelectResVo();
            for (Family family : familyList) {
                if (user.getFamilyId().equals(family.getId())) {
                    familyUserAndFamilySelectResVo.setFamilyUser(user);
                    familyUserAndFamilySelectResVo.setFamily(family);
                    for (FamilyMessage familyMessage : familymessage) {
                        if (family.getId().equals(familyMessage.getFamilyId())) {
                            familyUserAndFamilySelectResVo.setFamilyMessage(familyMessage);
                            break;
                        }
                    }
                    break;
                }
            }
            familyUserAndFamilySelectResVoList.add(familyUserAndFamilySelectResVo);
        }

        map.put("FamilyUserAndFamilySelectResVo", familyUserAndFamilySelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult selectFamilyUserByFamilyId(Integer page, Integer limit, Integer sort, FamilyUser familyUser) {
        //通过family_id 去查找关联用户id
        QueryWrapper<FamilyGenealogy> familyGenealogyQueryWrapper = new QueryWrapper<FamilyGenealogy>();
        familyGenealogyQueryWrapper.eq("family_id", familyUser.getFamilyId());
        List<FamilyGenealogy> xxx = familyGenealogyDao.selectList(familyGenealogyQueryWrapper);
        TreeSet<Integer> familyIdList = new TreeSet<Integer>();
        for (int i = 0; i < xxx.size(); i++) {
            familyIdList.add(xxx.get(i).getFamilyLianId());
        }


        IPage<FamilyUser> pageData = new Page<>(page, limit);
        QueryWrapper<FamilyUser> familyUserSelect = new QueryWrapper<FamilyUser>();
        familyUserSelect.eq("family_id", familyUser.getFamilyId());
        if (familyIdList.size() > 0) {
            familyUserSelect.or().in("id", familyIdList);
        }


        if (familyUser.getGeneration() != null) {
            familyUserSelect.eq("generation", familyUser.getGeneration());
        }
        if (familyUser.getRelation() != null) {
            familyUserSelect.eq("relation", familyUser.getRelation());
        }
        if (familyUser.getSex() != null) {
            familyUserSelect.eq("sex", familyUser.getSex());
        }
        if (familyUser.getStatus() != null) {
            familyUserSelect.eq("status", familyUser.getStatus());
        }
        if (sort == 2) {
            familyUserSelect.orderByAsc("status");
        } else {
            familyUserSelect.orderByDesc("user_id");
        }

        pageData = familyUserDao.selectPage(pageData, familyUserSelect);
        List<FamilyUser> familyUserList = pageData.getRecords();
        System.out.println("==============================" + familyUserList.size());
        familyUserList.stream().forEach(r -> {
            User user = userService.selectUserById(r.getUserId());
            r.setGenealogyName(null != user && StringUtils.isNotEmpty(user.getRealName()) ? user.getRealName() : "");
            System.out.println(r.getId() + "------" + r.getGenealogyName());
        });
        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if (familyUserList == null || familyUserList.size() == 0) {
            map.put("familyUserAndUserSelectResVoList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        //有数据，开始准备in查询
        TreeSet<Integer> userIdList = new TreeSet<Integer>();
        for (int i = 0; i < familyUserList.size(); i++) {
            userIdList.add(familyUserList.get(i).getUserId());
        }
        List<User> userList = userDao.selectBatchIds(userIdList);

        List<FamilyUserAndUserSelectResVo> familyUserAndUserSelectResVoList = new ArrayList<>();
        for (int i = 0; i < familyUserList.size(); i++) {
            FamilyUserAndUserSelectResVo familyUserAndUserSelectResVo = new FamilyUserAndUserSelectResVo();
            familyUserAndUserSelectResVo.setFamilyUser(familyUserList.get(i));
            for (int j = 0; j < userList.size(); j++) {
                if (familyUserList.get(i).getUserId().equals(userList.get(j).getId())) {
                    familyUserAndUserSelectResVo.setUser(userList.get(j));
                    break;
                }
            }
            familyUserAndUserSelectResVoList.add(familyUserAndUserSelectResVo);
        }

        map.put("familyUserAndUserSelectResVoList", familyUserAndUserSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }

    public JsonResult selectManagerByFamilyIdNo(Integer familyId) {

        return ResponseUtil.ok("获取成功", familyUserDao.selectManagerByFamilyIdNo(familyId));
    }

    public JsonResult selectStatusByFamilyId(Integer familyId) {
        List<FamilyUserAndUserSelectResVo> familyUserAndUserSelectResVoList = new ArrayList<>();
        List<FamilyUser> familyUserList = familyUserDao.selectStatusByFamilyId(familyId);

//        for (FamilyUser item  : familyUserList ) {
////            userDao.selectById(item.getUserId());
////            FamilyUserAndUserSelectResVo familyUserAndUserSelectResVo = new FamilyUserAndUserSelectResVo();
////            System.out.println("*******");
////            System.out.println(item.getUserId());
////            familyUserAndUserSelectResVo.setUser(userDao.selectById(item.getUserId()));
////            familyUserAndUserSelectResVo.setFamilyUser(item);
////        }


        return ResponseUtil.ok("获取成功", familyUserList);
    }

    @Override
    public JsonResult getFamilyUserListPaging(Integer familyId, String genealogyName, Integer limit, Integer page) {
        QueryWrapper<FamilyUser> queryWrapper = new QueryWrapper<>();
        if (null != genealogyName) {
            queryWrapper.like("genealogy_name", genealogyName);
        }
        queryWrapper.orderBy(true, false, "generation");
        queryWrapper.eq("family_id", familyId);
        queryWrapper.eq("status", 2);
        Page<FamilyUser> Page = new Page<>(page, limit);
        Page<FamilyUser> resultPage = familyUserDao.selectPage(Page, queryWrapper);
        FamilyUserPageVo familyUserPageVo = new FamilyUserPageVo();
        resultPage.getRecords().stream().forEach(r -> {
            User user = userService.selectUserById(r.getUserId());
            r.setGenealogyName(null != user && StringUtils.isNotEmpty(user.getRealName()) ? user.getRealName() : "");

        });
        familyUserPageVo.setList(resultPage.getRecords());
        familyUserPageVo.setTotal(resultPage.getTotal());
        return ResponseUtil.ok("获取成功", familyUserPageVo);
    }

    @Override
    public JsonResult deleteFamilyUser(FamilyUser familyUser) {
        FamilyUser oldeFamilyUser = familyUserDao.selectById(familyUser.getId());
        if (Objects.isNull(oldeFamilyUser)) {
            return ResponseUtil.fail("删除失败，未找到该用户");
        }
        if (oldeFamilyUser.getLevel() == 1) {
            return ResponseUtil.fail("删除失败，该用户为家族创建者，不能删除");
        }
        //如果是删除管理员 则将其改为普通成员
        if (oldeFamilyUser.getLevel() == 2) {
            oldeFamilyUser.setLevel(3);
            familyUserDao.updateById(oldeFamilyUser);
            return ResponseUtil.ok("删除成功");
        }
        familyUserDao.deleteById(familyUser);
        QueryWrapper<FamilyGenealogy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("family_id", familyUser.getFamilyId());
        queryWrapper.eq("user_id", familyUser.getUserId());
        if (familyUser.getJoins() == 2) {
            FamilyGenealogy familyGenealogy = familyGenealogyDao.selectList(queryWrapper).get(0);
            FamilyGenealogy cancel = new FamilyGenealogy();
            cancel.setId(familyGenealogy.getId());
            familyGenealogyDao.updateUserIdIsNull(cancel);
        }

        return ResponseUtil.ok("删除成功");
    }

    @Override
    public JsonResult selectManagerByFamilyId(Integer page, Integer limit, FamilyUser familyUser) {
        IPage<FamilyUser> pageData = new Page<>(page, limit);
        QueryWrapper<FamilyUser> familyUserSelect = new QueryWrapper<FamilyUser>();
        familyUserSelect.eq("family_id", familyUser.getFamilyId());
        if (familyUser.getStatus() != null) {
            familyUserSelect.eq("status", familyUser.getStatus());
        }
        familyUserSelect.le("level", 2);
        pageData = familyUserDao.selectPage(pageData, familyUserSelect);
        List<FamilyUser> familyUserList = pageData.getRecords();
        familyUserList.stream().forEach(fu -> {
            User user = userService.selectUserById(fu.getUserId());
            fu.setGenealogyName(null != user && StringUtils.isNotEmpty(user.getRealName()) ? user.getRealName() : "");
        });
        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if (familyUserList == null || familyUserList.size() == 0) {
            map.put("familyUserAndUserSelectResVoList", null);
            return ResponseUtil.ok("获取成功", map);
        }

        //有数据，开始准备in查询
        TreeSet<Integer> userIdList = new TreeSet<Integer>();
        for (int i = 0; i < familyUserList.size(); i++) {
            userIdList.add(familyUserList.get(i).getUserId());
        }
        List<User> userList = userDao.selectBatchIds(userIdList);

        List<FamilyUserAndUserSelectResVo> familyUserAndUserSelectResVoList = new ArrayList<>();
        for (int i = 0; i < familyUserList.size(); i++) {
            FamilyUserAndUserSelectResVo familyUserAndUserSelectResVo = new FamilyUserAndUserSelectResVo();
            for (int j = 0; j < userList.size(); j++) {
                if (familyUserList.get(i).getUserId().equals(userList.get(j).getId())) {
                    familyUserAndUserSelectResVo.setFamilyUser(familyUserList.get(i));
                    familyUserAndUserSelectResVo.setUser(userList.get(j));
                    break;
                }
            }
            familyUserAndUserSelectResVoList.add(familyUserAndUserSelectResVo);
        }

        map.put("familyUserAndUserSelectResVoList", familyUserAndUserSelectResVoList);
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult updateFamilyUserByIdList(List<Integer> idList, FamilyUser familyUser, List<FamilyManageLog> familyManageLogList) throws Exception {

        QueryWrapper<FamilyUser> familyUserUpdate = new QueryWrapper<FamilyUser>();
        familyUserUpdate.in("id", idList);
        int result = familyUserDao.update(familyUser, familyUserUpdate);

        if (result == 0) {
            throw new Exception("修改失败");
        }


        if (familyManageLogList != null && familyManageLogList.size() > 0) {
            result = familyManageLogDao.insertFamilyManageLogList(familyManageLogList);


            //添加消息

            QueryWrapper<FamilyUser> familyuser_find = new QueryWrapper<FamilyUser>();
            familyuser_find.eq("family_id", familyManageLogList.get(0).getFamilyId());
            familyuser_find.gt("user_id", 0);
            familyuser_find.lt("level", 3);

            List<FamilyUser> family_find = familyUserDao.selectList(familyuser_find);
            for (int i = 0; i < family_find.size(); i++) {
                QueryWrapper<FamilyMessage> familymessage = new QueryWrapper<FamilyMessage>();
                familymessage.eq("family_id", familyManageLogList.get(0).getFamilyId());
                familymessage.eq("user_id", family_find.get(i).getUserId());
                FamilyMessage familydata = familyMessageDao.selectOne(familymessage);
                if (familydata == null) {
                    FamilyMessage familyMessage = new FamilyMessage();
                    familyMessage.setFamilymanageMessage(1);
                    familyMessage.setFamilylogMessage(1);
                    familyMessage.setFamilyId(familyManageLogList.get(0).getFamilyId());
                    familyMessage.setUserId(family_find.get(i).getUserId());
                    familyMessageDao.insert(familyMessage);
                } else {
                    FamilyMessage familyMessage = new FamilyMessage();
                    familyMessage.setId(familydata.getId());
                    familyMessage.setFamilymanageMessage(familydata.getFamilymanageMessage() + 1);
                    familyMessage.setFamilylogMessage(familydata.getFamilylogMessage() + 1);
                    familyMessage.setFamilyId(familyManageLogList.get(0).getFamilyId());
                    familyMessage.setUserId(family_find.get(i).getUserId());
                    familyMessageDao.updateById(familyMessage);
                }

                FamilyUser fami = new FamilyUser();
                fami.setId(familyManageLogList.get(0).getFamilyUserId());
                fami.setUpdateTime(DateUtils.getDateTime());
                familyUserDao.updateById(fami);


            }

            /////////////////


            if (result == 0) {
                throw new Exception("日志记录失败");
            }
        }
        return ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult selectFamilyUserByuserid(Integer FamilyId, Integer userId) throws Exception {
        QueryWrapper<FamilyUser> familyUserQueryWrapper = new QueryWrapper<FamilyUser>();
        familyUserQueryWrapper.eq("family_id", FamilyId);
        familyUserQueryWrapper.eq("user_id", userId);
        FamilyUser familyUser = familyUserDao.selectOne(familyUserQueryWrapper);
        return ResponseUtil.ok("修改成功", familyUser);

    }

    /**
     * 查询用户家族，根据用户id 申请状态
     *
     * @param familyUserSelect
     * @return
     */
    @Override
    public List<FamilyUser> selectFamilyByUserId(FamilyUser familyUserSelect) {
        QueryWrapper<FamilyUser> familyGenealogyx = new QueryWrapper<FamilyUser>();
        familyGenealogyx.eq("user_id", familyUserSelect.getUserId());
        familyGenealogyx.eq("status", familyUserSelect.getStatus());
        return familyUserDao.selectList(familyGenealogyx);
    }

    @Override
    public JsonResult selectFamilyUserBygenertion(Integer genealogy, Integer familyId, Integer familyUserId) throws Exception {
        HashMap<String, Object> map = new HashMap<>(8);
        QueryWrapper<FamilyGenealogy> familyGenealogyx = new QueryWrapper<FamilyGenealogy>();
        familyGenealogyx.eq("family_id", familyId);
        familyGenealogyx.eq("generation", genealogy);
        familyGenealogyx.eq("family_user_id", familyUserId);
        List<FamilyGenealogy> data = familyGenealogyDao.selectList(familyGenealogyx);
        if (data.size() > 0) {
            return ResponseUtil.fail("代数已存在");

        } else {


            QueryWrapper<FamilyGenealogy> familyGenealogy = new QueryWrapper<FamilyGenealogy>();
            familyGenealogy.eq("family_id", familyId);
            familyGenealogy.eq("generation", genealogy);
            List<FamilyGenealogy> familyGenealogydata = familyGenealogyDao.selectList(familyGenealogy);


            TreeSet<Integer> familyGenealogylist = new TreeSet<Integer>();
            for (int i = 0; i < familyGenealogydata.size(); i++) {
                if (familyGenealogydata.get(i).getFamilyLianId() > 0) {
                    familyGenealogylist.add(familyGenealogydata.get(i).getFamilyLianId());
                }

            }


            //查找自己在弟几代
            QueryWrapper<FamilyUser> mydata = new QueryWrapper<FamilyUser>();
            mydata.eq("id", familyUserId);

            FamilyUser mysaas = familyUserDao.selectOne(mydata);


            QueryWrapper<FamilyUser> familyUser = new QueryWrapper<FamilyUser>();
            familyUser.eq("family_id", familyId);
            familyUser.eq("generation", genealogy);
            if (mysaas.getGeneration() == null || mysaas.getGeneration() == 0) {
                familyUser.or().eq("id", familyUserId);
            }
            if (familyGenealogylist.size() > 0)
                familyUser.notIn("id", familyGenealogylist);

            List<FamilyUser> familyUserList = familyUserDao.selectList(familyUser);
            List<FamilyUserAndUserSelectResVo> familyUserAndUserSelectResVoList = new ArrayList<>();

            if (familyUserList.size() > 0) {
                TreeSet<Integer> userIdList = new TreeSet<Integer>();
                for (int i = 0; i < familyUserList.size(); i++) {

                    userIdList.add(familyUserList.get(i).getUserId());
                }

                List<User> userList = userDao.selectBatchIds(userIdList);


                for (int i = 0; i < familyUserList.size(); i++) {
                    FamilyUserAndUserSelectResVo familyUserAndUserSelectResVo = new FamilyUserAndUserSelectResVo();
                    familyUserAndUserSelectResVo.setFamilyUser(familyUserList.get(i));
                    for (int j = 0; j < userList.size(); j++) {
                        if (familyUserList.get(i).getUserId().equals(userList.get(j).getId())) {
                            familyUserAndUserSelectResVo.setUser(userList.get(j));
                            break;
                        }
                    }
                    familyUserAndUserSelectResVoList.add(familyUserAndUserSelectResVo);
                }

                map.put("familyUserAndUserSelectResVoList", familyUserAndUserSelectResVoList);


            }


            return ResponseUtil.ok("修改成功", map);
        }


    }


    @Override
    public JsonResult insertFamilyUserBygenertion(Integer family_userid, Integer number, Integer familyId, String genealogy, String genealogy_two) throws Exception {
        //比代数大的全部加1
        QueryWrapper<FamilyGenealogy> familyGenealogy = new QueryWrapper<FamilyGenealogy>();
        familyGenealogy.eq("family_id", familyId);
        familyGenealogy.eq("generation", number);
        familyGenealogy.eq("family_user_id", family_userid);
        List<FamilyGenealogy> data = familyGenealogyDao.selectList(familyGenealogy);
        FamilyGenealogy familyGenealogy3 = new FamilyGenealogy();

        if (false) {
            return ResponseUtil.fail("代数已存在");

        } else {
            FamilyUser familyUser1 = new FamilyUser();
            String genealogy_name = genealogy;

            if (!genealogy.equals("nonpersons")) {
//                genealogy_name = genealogy;
//                familyUser1.setUserId(0);
//                familyUser1.setFamilyId(familyId);
//                familyUser1.setLevel(3);
//                familyUser1.setRelation(1);
//                familyUser1.setStatus(2);
//                familyUser1.setSex(1);
//                familyUser1.setGeneration(number);
//                familyUser1.setCreateTime(DateUtils.getDateTime());
//                familyUser1.setGenealogyName(genealogy_name);
//                int result = familyUserDao.insert(familyUser1);
            }


            familyGenealogy3.setGeneration(number);
            familyGenealogy3.setFamilyUserId(family_userid);
            familyGenealogy3.setCreateTime(DateUtils.getDateTime());
            familyGenealogy3.setFamilyId(familyId);
            familyGenealogy3.setRelation(1);
            familyGenealogy3.setIdentity(1);
            familyGenealogy3.setFamilyLianId(0);
            familyGenealogy3.setSex(1);
            familyGenealogy3.setGenealogyName(genealogy_name);
            familyGenealogyDao.insert(familyGenealogy3);

            //婚姻

            FamilyGenealogy familyGenealogy4 = new FamilyGenealogy();

            FamilyUser familyUser12 = new FamilyUser();
            String genealogy_name2 = genealogy_two;

//            if(!genealogy_two.equals("nonpersons")){
//                familyUser12.setUserId(0);
//                genealogy_name2 = genealogy_two;
//                familyUser12.setFamilyId(familyId);
//                familyUser12.setLevel(3);
//                familyUser12.setRelation(2);
//                familyUser12.setStatus(2);
//                familyUser12.setSex(1);
//                familyUser12.setGeneration(number);
//                familyUser12.setCreateTime(DateUtils.getDateTime());
//                familyUser12.setGenealogyName(genealogy_name2);
//                int result2 = familyUserDao.insert(familyUser12);
//            }


            familyGenealogy4.setGeneration(number);
            familyGenealogy4.setGenealogyName(genealogy_name2);
            familyGenealogy4.setFamilyLianId(0);
            familyGenealogy4.setFamilyUserId(family_userid);
            familyGenealogy4.setCreateTime(DateUtils.getDateTime());
            familyGenealogy4.setFamilyId(familyId);
            familyGenealogy4.setRelation(1);
            familyGenealogy4.setSex(1);
            familyGenealogy4.setIdentity(2);


            familyGenealogyDao.insert(familyGenealogy4);

            return ResponseUtil.ok("修改成功");
        }

    }


}
