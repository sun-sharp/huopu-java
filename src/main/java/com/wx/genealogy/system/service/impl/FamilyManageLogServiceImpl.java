package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.FamilyManageLog;
import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.FamilyManageLogDao;
import com.wx.genealogy.system.mapper.FamilyUserDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.FamilyManageLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.system.vo.res.FamilyManageLogAndUserSelectResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * <p>
 * 家族管理日志 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-10-12
 */
@Service
public class FamilyManageLogServiceImpl extends ServiceImpl<FamilyManageLogDao, FamilyManageLog> implements FamilyManageLogService {

    @Autowired
    private FamilyManageLogDao familyManageLogDao;

    @Autowired
    private FamilyUserDao familyUserDao;

    @Autowired
    private UserDao userDao;


    public JsonResult selectFamilyManageLogs(Integer page, Integer limit, FamilyManageLog familyManageLog) {
        IPage<FamilyManageLog> pageData =new Page<FamilyManageLog>(page, limit);
        QueryWrapper<FamilyManageLog> familyManageLogSelect = new QueryWrapper<FamilyManageLog>();
        familyManageLogSelect.eq("user_id", familyManageLog.getUserId());
        pageData=familyManageLogDao.selectPage(pageData, familyManageLogSelect);
        List<FamilyManageLog> familyManageLogList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if(familyManageLogList==null||familyManageLogList.size()==0){
            map.put("familyManageLogAndUserSelectResVoList", null);
            return ResponseUtil.ok("获取成功",map);
        }

        //有数据，开始准备in查询
        TreeSet<Integer> familyUserIdList = new TreeSet<Integer>();
        TreeSet<Integer> userIdList = new TreeSet<Integer>();
        for(int i=0;i<familyManageLogList.size();i++){
            familyUserIdList.add(familyManageLogList.get(i).getFamilyUserId());
            userIdList.add(familyManageLogList.get(i).getUserId());
        }

        List<FamilyUser> familyUserList=familyUserDao.selectBatchIds(familyUserIdList);
        List<User> userList=userDao.selectBatchIds(userIdList);

        List<FamilyManageLogAndUserSelectResVo> familyManageLogAndUserSelectResVoList = new ArrayList<FamilyManageLogAndUserSelectResVo>();
        for(int i=0;i<familyManageLogList.size();i++){
            FamilyManageLogAndUserSelectResVo familyManageLogAndUserSelectResVo = new FamilyManageLogAndUserSelectResVo();
            for(int j=0;j<familyUserList.size();j++){
                if(familyManageLogList.get(i).getFamilyUserId().equals(familyUserList.get(j).getId())){
                    familyManageLogAndUserSelectResVo.setFamilyManageLog(familyManageLogList.get(i));
                    familyManageLogAndUserSelectResVo.setFamilyUser(familyUserList.get(j));
                    break;
                }
            }
            familyManageLogAndUserSelectResVoList.add(familyManageLogAndUserSelectResVo);
        }

        for(int i=0;i<familyManageLogAndUserSelectResVoList.size();i++){
            for(int j=0;j<userList.size();j++){
                if(familyManageLogAndUserSelectResVoList.get(i).getFamilyManageLog().getUserId().equals(userList.get(j).getId())){
                    familyManageLogAndUserSelectResVoList.get(i).setUser(userList.get(j));
                    break;
                }
            }
        }

        map.put("familyManageLogAndUserSelectResVoList", familyManageLogAndUserSelectResVoList);
        return ResponseUtil.ok("获取成功",map);
    }

    @Override
    public JsonResult selectFamilyManageLog(Integer page, Integer limit, FamilyManageLog familyManageLog) {
        IPage<FamilyManageLog> pageData =new Page<FamilyManageLog>(page, limit);
        QueryWrapper<FamilyManageLog> familyManageLogSelect = new QueryWrapper<FamilyManageLog>();
        if(familyManageLog.getFamilyId()!=null&&familyManageLog.getFamilyId()>0){
            familyManageLogSelect.eq("family_id", familyManageLog.getFamilyId());
            familyManageLogSelect.orderByDesc("create_time"); // 按 orderTime 字段降序排序
        }
        pageData=familyManageLogDao.selectPage(pageData, familyManageLogSelect);
        List<FamilyManageLog> familyManageLogList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if(familyManageLogList==null||familyManageLogList.size()==0){
            map.put("familyManageLogAndUserSelectResVoList", null);
            return ResponseUtil.ok("获取成功",map);
        }

        //有数据，开始准备in查询
        TreeSet<Integer> familyUserIdList = new TreeSet<Integer>();
        TreeSet<Integer> userIdList = new TreeSet<Integer>();
        for(int i=0;i<familyManageLogList.size();i++){
            familyUserIdList.add(familyManageLogList.get(i).getFamilyUserId());
            userIdList.add(familyManageLogList.get(i).getUserId());
        }

        List<FamilyUser> familyUserList=familyUserDao.selectBatchIds(familyUserIdList);
        List<User> userList=userDao.selectBatchIds(userIdList);

        List<FamilyManageLogAndUserSelectResVo> familyManageLogAndUserSelectResVoList = new ArrayList<FamilyManageLogAndUserSelectResVo>();
        for(int i=0;i<familyManageLogList.size();i++){
            FamilyManageLogAndUserSelectResVo familyManageLogAndUserSelectResVo = new FamilyManageLogAndUserSelectResVo();
            for(int j=0;j<familyUserList.size();j++){
                if(familyManageLogList.get(i).getFamilyUserId().equals(familyUserList.get(j).getId())){
                    familyManageLogAndUserSelectResVo.setFamilyManageLog(familyManageLogList.get(i));
                    familyManageLogAndUserSelectResVo.setFamilyUser(familyUserList.get(j));
                    break;
                }
            }
            if (familyManageLogAndUserSelectResVo.getFamilyManageLog() != null || familyManageLogAndUserSelectResVo.getFamilyUser() != null){
                familyManageLogAndUserSelectResVoList.add(familyManageLogAndUserSelectResVo);
            }
        }

        for(int i=0;i<familyManageLogAndUserSelectResVoList.size();i++){
            for(int j=0;j<userList.size();j++){
                if(familyManageLogAndUserSelectResVoList.get(i).getFamilyManageLog().getUserId().equals(userList.get(j).getId())){
                    familyManageLogAndUserSelectResVoList.get(i).setUser(userList.get(j));
                    break;
                }
            }
        }

        map.put("familyManageLogAndUserSelectResVoList", familyManageLogAndUserSelectResVoList);
        return ResponseUtil.ok("获取成功",map);
    }
}
