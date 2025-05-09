package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.mapper.*;
import com.wx.genealogy.system.service.FamilyGenealogyReceiveService;
import com.wx.genealogy.system.service.FamilyGenealogyService;
import com.wx.genealogy.system.service.FamilyService;
import com.wx.genealogy.system.service.UserService;
import com.wx.genealogy.system.vo.req.FamilyInsertReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Wrapper;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 认领族谱图申请 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2023-03-03
 */
@Service
public class FamilyGenealogyReceiveServiceImpl extends ServiceImpl<FamilyGenealogyReceiveDao, FamilyGenealogyReceive> implements FamilyGenealogyReceiveService {


    @Resource
    private FamilyGenealogyService familyGenealogyService;


    @Autowired
    private FamilyGenealogyDao familyGenealogyDao;

    @Autowired
    private  FamilyMessageDao familyMessageDao;

    @Autowired
    private FamilyManageLogDao familyManageLogDao;

    @Autowired
    private FamilyUserDao familyUserDao;


    @Resource
    private UserService userService;

    @Resource
    private FamilyGenealogyReceiveDao familyGenealogyReceiveDao;

    @Override
    @Transactional
    public JsonResult claim(Integer userId,Integer familyId) {
        if (Objects.isNull(userId)) {
            return ResponseUtil.ok("未登录",false);
        }
//        QueryWrapper<FamilyGenealogy> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("family_id",familyId);
//        queryWrapper.eq("user_id",userId);
        FamilyGenealogy familyGenealogy = new FamilyGenealogy();
        familyGenealogy.setFamilyId(familyId);
        familyGenealogy.setUserId(userId);
        List<FamilyGenealogy> list = familyGenealogyDao.list(familyGenealogy);
        if(list.size()>0){
            return ResponseUtil.ok("已认领不可再认领",false);
        }

        QueryWrapper<FamilyGenealogyReceive> queryWrapperReceive = new QueryWrapper<>();
        queryWrapperReceive.eq("family_id",familyId);
        queryWrapperReceive.eq("user_id",userId);
        queryWrapperReceive.eq("status",0);
        List listf = familyGenealogyReceiveDao.selectList(queryWrapperReceive);
        if(listf.size()>0){
            return ResponseUtil.ok("认领审核中",false);
        }

        return ResponseUtil.ok("可显示",true);
    }


    /**
     * 认领
     *
     * @param userId            当前用户id
     * @param familyGenealogyId 认领数据id
     * @return
     */
    @Override
    @Transactional
    public JsonResult fetch(Integer userId, Integer familyGenealogyId,Integer generation,Integer familyId,String phone,String text,Integer relation) {
        if (Objects.isNull(userId)) {
            return ResponseUtil.fail("未登录");
        }

        if (Objects.isNull(familyGenealogyId)) {
            return ResponseUtil.fail("认领数据id不能为空");
        }

        FamilyGenealogy familyGenealogy = this.familyGenealogyService.getById(familyGenealogyId);
        if (Objects.isNull(familyGenealogy)) {
            return ResponseUtil.fail("认领数据不存在");
        }

        if (Objects.nonNull(familyGenealogy.getUserId())) {
            return ResponseUtil.fail("当前数据已被人认领");
        }



        User fetchUser = this.userService.getById(userId);
        QueryWrapper<FamilyGenealogy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("family_id",familyId);
        queryWrapper.eq("user_id",userId);
        List<FamilyGenealogy> list = familyGenealogyDao.selectList(queryWrapper);
        if(list.size()>0){
            return ResponseUtil.fail("已认领不可再认领");
        }else{
            //如果没有被认领则进行认领方法
            //根据用户id查询用户数据
            QueryWrapper<FamilyUser> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("family_id",familyId);
            queryWrapper1.eq("user_id",userId);
            FamilyUser fu  = familyUserDao.selectList(queryWrapper1).get(0);
            //绑定认领userId
            FamilyGenealogy familyGenealogy1 = new FamilyGenealogy();
            familyGenealogy1.setId(familyGenealogy.getId());
            familyGenealogy1.setStatus(1);
            familyGenealogy1.setUserId(userId);
            familyGenealogy1.setHeadImg(fetchUser.getAvatar());
            familyGenealogyDao.updateById(familyGenealogy1);
            //修改用户id
            FamilyUser familyUser = new FamilyUser();
            familyUser.setId(fu.getId());
            familyUser.setFamilyId(familyGenealogy.getFamilyId());
            familyUser.setGeneration(familyGenealogy.getGeneration());
            familyUser.setGenealogyName(familyGenealogy.getGenealogyName());
            familyUser.setRelation(familyGenealogy.getRelation());
            familyUser.setJoins(2);//
            familyUserDao.updateById(familyUser);


        }




        if (Objects.isNull(fetchUser)) {
            return ResponseUtil.fail("未查询到登录人信息");
        }
        // 增加日志
//        FamilyMessage familyMessage = new FamilyMessage();
//        familyMessage.setFamilyId(familyId);
//        familyMessage.setFamilyshenMessage2(1);
//        System.out.println(familyMessage);
//        familyMessageDao.updateFamilyMessage(familyMessage);




        return ResponseUtil.ok("认领成功,请耐心等待管理员审核");
    }

    /**
     * 原认领方法
     *
     * @param userId            当前用户id
     * @param familyGenealogyId 认领数据id
     * @return
     */
    @Transactional
    public JsonResult fetch2(Integer userId, Integer familyGenealogyId,Integer generation,Integer familyId,String phone,String text,Integer relation) {
        if (Objects.isNull(userId)) {
            return ResponseUtil.fail("未登录");
        }

        if (Objects.isNull(familyGenealogyId)) {
            return ResponseUtil.fail("认领数据id不能为空");
        }

        FamilyGenealogy familyGenealogy = this.familyGenealogyService.getById(familyGenealogyId);
        if (Objects.isNull(familyGenealogy)) {
            return ResponseUtil.fail("认领数据不存在");
        }

        if (Objects.nonNull(familyGenealogy.getUserId())) {
            return ResponseUtil.fail("当前数据已被人认领");
        }

        User fetchUser = this.userService.getById(userId);

        QueryWrapper<FamilyGenealogy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("family_id",familyId);
        queryWrapper.eq("user_id",userId);
        List<FamilyGenealogy> list = familyGenealogyDao.selectList(queryWrapper);
        if(list.size()>0){
            return ResponseUtil.fail("已认领不可再认领");
        }else{
            //如果没有认领则判断是否在审核中
            QueryWrapper<FamilyGenealogyReceive> queryWrapperReceive = new QueryWrapper<>();
            queryWrapperReceive.eq("family_id",familyId);
            queryWrapperReceive.eq("user_id",userId);
            queryWrapperReceive.eq("status",0);
            List listf = familyGenealogyReceiveDao.selectList(queryWrapperReceive);
            if(listf.size()>0){
                return ResponseUtil.fail("认领审核中");
            }
        }
        FamilyGenealogy familyGenealogy1 = new FamilyGenealogy();
        familyGenealogy1.setId(familyGenealogy.getId());
        familyGenealogy1.setStatus(4);
        familyGenealogyDao.updateById(familyGenealogy1);



        if (Objects.isNull(fetchUser)) {
            return ResponseUtil.fail("未查询到登录人信息");
        }
        QueryWrapper<FamilyUser> queryWrapperFamilyUser = new QueryWrapper<>();
        queryWrapperFamilyUser.eq("family_id",familyId);
        queryWrapperFamilyUser.eq("user_id",userId);
        FamilyUser familyUser = familyUserDao.selectList(queryWrapperFamilyUser).get(0);
        System.out.println(familyUser);
//        if (!familyGenealogy.getGenealogyName().equals(familyUser.getGenealogyName())) {
//        return ResponseUtil.fail("家谱名不一致!");
//      }
        if (familyGenealogy.getRelation() != familyUser.getRelation()) {
            return ResponseUtil.fail("关系不一致!");
        }
        FamilyGenealogyReceive receive = new FamilyGenealogyReceive();
        receive.setUserId(fetchUser.getId());
        receive.setGeneration(generation);
        receive.setFamilyId(familyGenealogy.getFamilyId());
        receive.setFamilyGenealogyId(familyGenealogy.getId());
        receive.setUserImg(fetchUser.getAvatar());
        receive.setApplyRemark(text);
        receive.setUserName(familyGenealogy.getGenealogyName());
        receive.setStatus(0);
        receive.setRelation(relation);
//        receive.setPhone(phone);
        receive.setCreateTime(LocalDateTime.now());


        // 增加日志
        FamilyMessage familyMessage = new FamilyMessage();
        familyMessage.setFamilyId(familyId);
        familyMessage.setFamilyshenMessage2(1);
        System.out.println(familyMessage);
        familyMessageDao.updateFamilyMessage(familyMessage);



        this.familyGenealogyReceiveDao.insert(receive);

        return ResponseUtil.ok("认领成功,请耐心等待管理员审核");
    }

    /**
     * 查询需要审核的认领申请
     *
     * @param familyId 当前家族id
     * @return
     */
    @Override
    public JsonResult selectAuditApply(Integer familyId) {

        if (ObjectUtil.isNull(familyId)) {
            return ResponseUtil.fail("当前家族id不能为空");
        }
        FamilyGenealogyReceive receive = new FamilyGenealogyReceive();
        receive.setStatus(0);
        receive.setFamilyId(familyId);
        return ResponseUtil.ok("查询成功", this.familyGenealogyReceiveDao.selectAuditApply(receive));
    }



    /**
     * 审核族谱图认领申请
     *
     * @param receive
     * @return
     */
    @Override
    @Transactional
    public JsonResult auditFamilyGenealogyReceive(FamilyGenealogyReceive receive) {
        if (Objects.isNull(receive.getUserId())) {
            return ResponseUtil.fail("审核人id不能为空");
        }
        if (Objects.isNull(receive.getId())) {
            return ResponseUtil.fail("审核数据id不能为空");
        }

        if (Objects.isNull(receive.getStatus())) {
            return ResponseUtil.fail("审核状态不能为空");
        }

        FamilyGenealogyReceive genealogyReceive = this.familyGenealogyReceiveDao.selectById(receive.getId());
        if (Objects.isNull(genealogyReceive)) {
            return ResponseUtil.fail("当前审核数据不存在");
        }

        if (genealogyReceive.getStatus() != 0) {
            return ResponseUtil.fail("当前数据状态不为待审核,不可审核");
        }

        //判断当前审核是不是管理员
        Boolean admin = this.familyGenealogyService.isAdmin(receive.getUserId(), genealogyReceive.getFamilyId());
        if (!admin) {
            return ResponseUtil.fail("未查询到您的管理员身份,不可审核");
        }

        genealogyReceive.setStatus(receive.getStatus());
        genealogyReceive.setRefuse(receive.getRefuse());
        genealogyReceive.setUpdateTime(LocalDateTime.now());

        this.familyGenealogyReceiveDao.updateById(genealogyReceive);

        //修改到族谱图里面
        FamilyGenealogy genealogy = new FamilyGenealogy();
        if (receive.getStatus() == 1) {
             genealogy = this.familyGenealogyService.getById(genealogyReceive.getFamilyGenealogyId());
            if (Objects.isNull(genealogy)) {
                return ResponseUtil.fail("未查询到族谱图信息");
            }
            genealogy.setStatus(1);
            genealogy.setUserId(genealogyReceive.getUserId());
            genealogy.setHeadImg(genealogyReceive.getUserImg());
            this.familyGenealogyService.updateById(genealogy);
        }


        FamilyManageLog familyManageLog = new FamilyManageLog();
        familyManageLog.setUserId(receive.getUserId());
        familyManageLog.setFamilyId(receive.getFamilyId());
        if(receive.getStatus()==1){
            FamilyGenealogyReceive familyGenealogyReceive = familyGenealogyReceiveDao.selectById(receive.getId());
            familyManageLog.setAction("同意认领");
            familyManageLog.setContent("管理员同意认领:"+familyGenealogyReceive.getUserName());
            familyManageLog.setFamilyId(familyGenealogyReceive.getFamilyId());
            familyManageLog.setUserId(familyGenealogyReceive.getUserId());
            familyManageLog.setCreateTime(new Date());
            QueryWrapper<FamilyUser> familyUserQueryWrapper = new QueryWrapper<FamilyUser>();
            familyUserQueryWrapper.eq("family_id",familyGenealogyReceive.getFamilyId());
            familyUserQueryWrapper.eq("user_id",familyGenealogyReceive.getUserId());
            List<FamilyUser> list = familyUserDao.selectList(familyUserQueryWrapper);
            FamilyUser familyUser = list.get(0);
            familyManageLog.setFamilyUserId(familyUser.getId());
            familyManageLogDao.insert(familyManageLog);
            FamilyUser updateFamilyUser = new FamilyUser();
            updateFamilyUser.setId(familyUser.getId());
            updateFamilyUser.setGeneration(genealogy.getGeneration());
            updateFamilyUser.setGenealogyName(genealogy.getGenealogyName());
            familyUserDao.updateById(updateFamilyUser);
        }else if(receive.getStatus()==2){
            FamilyGenealogyReceive familyGenealogyReceive = familyGenealogyReceiveDao.selectById(receive.getId());

            familyManageLog.setAction("拒绝认领");
            familyManageLog.setContent("管理员拒绝认领:"+familyGenealogyReceive.getUserName());
            familyManageLog.setCreateTime(new Date());
            familyManageLog.setFamilyId(familyGenealogyReceive.getFamilyId());
            familyManageLog.setUserId(familyGenealogyReceive.getUserId());

            QueryWrapper<FamilyUser> familyUserQueryWrapper = new QueryWrapper<FamilyUser>();
            familyUserQueryWrapper.eq("family_id",familyGenealogyReceive.getFamilyId());
            familyUserQueryWrapper.eq("user_id",familyGenealogyReceive.getUserId());
            List<FamilyUser> list = familyUserDao.selectList(familyUserQueryWrapper);
            familyManageLog.setFamilyUserId(list.get(0).getId());
            familyManageLogDao.insert(familyManageLog);
        }





        return ResponseUtil.ok("审核成功");
    }

    /**
     * 查询我的认领族谱图申请记录
     * @param receive
     * @return
     */
    @Override
    public JsonResult selectMyApply(FamilyGenealogyReceive receive) {
        return ResponseUtil.ok("查询成功",this.familyGenealogyReceiveDao.selectAuditApply(receive));
    }

    /**
     * 查询我的认领族谱图申请记录
     * @param receive
     * @return
     */
    @Override
    public JsonResult selectApply(FamilyGenealogyReceive receive) {
        System.out.println("*****************");
        System.out.println(receive);
        return ResponseUtil.ok("查询成功",this.familyGenealogyReceiveDao.selectApply(receive));
    }

    @Override
    public JsonResult getGenealogy(Integer userId,Integer familyId){
        QueryWrapper<FamilyGenealogy> familyGenealogyQueryWrapper = new QueryWrapper<>();
        familyGenealogyQueryWrapper.eq("user_id",userId);
        familyGenealogyQueryWrapper.eq("family_id",familyId);
        List<FamilyGenealogy> list =familyGenealogyDao.selectList(familyGenealogyQueryWrapper);
        if(list.size()<1){
            return ResponseUtil.fail("未认领族谱");
        }
        return ResponseUtil.ok("查询成功",list.get(0)
        );
    }
}
