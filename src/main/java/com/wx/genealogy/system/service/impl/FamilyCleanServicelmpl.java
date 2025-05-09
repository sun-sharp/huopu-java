package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.FamilyClean;
import com.wx.genealogy.system.entity.RiceRecord;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.FamilyCleanDao;
import com.wx.genealogy.system.mapper.RiceRecordDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.FamilyCleanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeSet;


@Service
public class FamilyCleanServicelmpl extends ServiceImpl<FamilyCleanDao, FamilyClean> implements FamilyCleanService {

    @Autowired
    private FamilyCleanDao familyCleanDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RiceRecordDao riceRecordDao;

    @Override
    public JsonResult insertFamilyClean(FamilyClean familyClean) throws Exception {
        familyClean.setCleanTime(DateUtils.getDateTime());
        int result = familyCleanDao.insert(familyClean);
       User user = userDao.selectById(familyClean.getUserId());
        //这里计算要花的米
        User userUpdate = new User();

        userUpdate.setId(familyClean.getUserId());
        userUpdate.setRice(user.getRice()-50);
        userUpdate.setClean(user.getClean()+1);
        int result1=userDao.updateById(userUpdate);
        if(result1==0){
            throw new Exception("打扫失败");
        }

        RiceRecord riceRecord = new RiceRecord();
        riceRecord.setUserId(familyClean.getUserId());
        riceRecord.setRice(-50);
        riceRecord.setContent("打扫家族支出");
        riceRecord.setCreateTime(DateUtils.getDateTime());
        int result2=riceRecordDao.insert(riceRecord);


        return ResponseUtil.ok("打扫成功");
    }


    @Override
    public JsonResult selectFamilyClean(Integer page, Integer limit,Integer familyId) throws Exception {
        QueryWrapper<FamilyClean> familyCleanQueryWrapper = new QueryWrapper<FamilyClean>();
        familyCleanQueryWrapper.eq("family_id",familyId);
        List<FamilyClean> familyclean = familyCleanDao.selectList(familyCleanQueryWrapper);
        //有数据，开始准备in查询
        TreeSet<Integer> familyIdList = new TreeSet<Integer>();
        for (int i = 0; i < familyclean.size(); i++) {
            familyIdList.add(familyclean.get(i).getUserId());
        }
        IPage<User> pageData =new Page<>(page, limit);
        QueryWrapper<User> Userselect = new QueryWrapper<User>();

        if(familyIdList.size()>0){
            Userselect.in("id",familyIdList);
        }else{
            Userselect.eq("id",0);

        }
        List<User> user_data = userDao.selectList(Userselect);
        for(int i=0; i<user_data.size();i++){
            for(int j=0;j<familyclean.size();j++){
                if(familyclean.get(j).getUserId().equals(user_data.get(i).getId())){
                    user_data.get(i).setClean(user_data.get(i).getClean()+1);
                }
            }
        }



//        pageData=userDao.selectPage(pageData, Userselect);
//
//        //准备
//        HashMap<String, Object> map = new HashMap<>(8);
//
//        map.put("data", pageData.getRecords());
//
//        map.put("pages", pageData.getPages());
//        map.put("total", pageData.getTotal());
        return ResponseUtil.ok("获取成功",user_data);
    }



    @Override
    public JsonResult updateFamilyUnlockById(FamilyClean familyClean,int mi,int multiple) throws Exception {

        //看看用户米够不够
        User user = userDao.selectById(familyClean.getUserId());
        if(user==null){
            return ResponseUtil.fail("解锁失败");
        }

        //500米为基本单位
        if(user.getRice()<mi*multiple){
            return ResponseUtil.fail("米不足");
        }

        //这里计算要花的米
        User userUpdate = new User();
        userUpdate.setId(user.getId());
        userUpdate.setRice(mi*multiple);
        int result=userDao.setDec(userUpdate);
        if(result==0){
            throw new Exception("解锁失败");
        }

        RiceRecord riceRecord = new RiceRecord();
        riceRecord.setUserId(user.getId());
        riceRecord.setRice(-mi*multiple);
        riceRecord.setContent("家族解锁支出");
        riceRecord.setCreateTime(DateUtils.getDateTime());
        result=riceRecordDao.insert(riceRecord);
        if(result==0){
            throw new Exception("解锁失败");
        }


          result= familyCleanDao.insert(familyClean);
        if(result==0){
            throw new Exception("解锁失败");
        }
        return ResponseUtil.ok("解锁成功");
    }





}
