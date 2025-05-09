package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.mapper.*;
import com.wx.genealogy.system.service.TombstoneService;

import com.wx.genealogy.system.vo.res.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;


@Service
public class TombstoneServicelmpl extends ServiceImpl<TombstoneDao, Tombstone> implements TombstoneService {
    @Autowired
    private TombstoneDao tombstoneDao;
    @Autowired
    FamilyUserDao familyUserDao;
    @Autowired
    TombstoneFlowerDao tombstoneFlowerDao;
    @Autowired
    TombstoneMessageDao tombstoneMessageDao;
    @Autowired
    TombstoneSweepDao tombstoneSweepDao;
    @Autowired
    UserDao userDao;
    @Autowired
    RiceRecordDao riceRecordDao;
    @Autowired
    TombstoneUsergiftDao tombstoneUsergiftDao;
    @Autowired
    TombstoneGiftDao tombstoneGiftDao;

    @Autowired
    TombstoneEssayDao tombstoneEssayDao;

    @Autowired
    EssayDao essayDao;

    @Autowired
    EssayImgDao essayImgDao;
    @Autowired
    FamilyDao familyDao;
    @Override
    public JsonResult selectTomstoneByfamilyId(Integer page, Integer limit, Integer familyId) throws Exception {
        IPage<Tombstone> pageData = new Page<>(page, limit);
        QueryWrapper<Tombstone> tombstone = new QueryWrapper<Tombstone>();
        tombstone.eq("family_id", familyId);
        pageData = tombstoneDao.selectPage(pageData, tombstone);
        List<Tombstone> tombstonelist = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if (tombstonelist == null || tombstonelist.size() == 0) {
            map.put("FamilyUserAndFamilySelectResVo", null);
            return ResponseUtil.ok("获取成功", map);
        }


        //有数据，开始准备in查询
        TreeSet<Integer> familyIdList = new TreeSet<Integer>();
        for (int i = 0; i < tombstonelist.size(); i++) {
            familyIdList.add(tombstonelist.get(i).getFamilyId());
        }
        QueryWrapper<FamilyUser> familyUser = new QueryWrapper<FamilyUser>();
        familyUser.in("family_id",familyIdList);

        List<FamilyUser> familyUserList = familyUserDao.selectList(familyUser);


        List<SelectTomstoneByfamilyIdResVo> selectTomstoneByfamilyIdResVoList = new ArrayList<SelectTomstoneByfamilyIdResVo>();

        for (int i = 0; i < tombstonelist.size(); i++) {
            SelectTomstoneByfamilyIdResVo selectTomstoneByfamilyIdResVo = new SelectTomstoneByfamilyIdResVo();
            selectTomstoneByfamilyIdResVo.setTombstone(tombstonelist.get(i));


            selectTomstoneByfamilyIdResVoList.add(selectTomstoneByfamilyIdResVo);
        }

        map.put("FamilyUserAndFamilySelectResVo", selectTomstoneByfamilyIdResVoList);
        return ResponseUtil.ok("获取成功", map);
    }




    @Override
    public JsonResult insertTomstone(Tombstone tombstone) throws Exception {
        tombstone.setCreateTime(DateUtils.getDateTime());
        QueryWrapper<Tombstone>  tombstonedaata= new QueryWrapper<Tombstone>();
        tombstonedaata.eq("user_name",tombstone.getUserName());
        tombstonedaata.eq("family_id",tombstone.getFamilyId());

        Tombstone data =  tombstoneDao.selectOne(tombstonedaata);

        if(data !=null) return ResponseUtil.fail("用户已经存在");
        int result = tombstoneDao.insert(tombstone);
        if(result==0){
            return ResponseUtil.fail("添加失败");
        }
        return ResponseUtil.ok("添加成功");
    }

    @Override
    public JsonResult insertTomstoneMessage(TombstoneMessage tombstoneMessage) throws Exception {
        tombstoneMessage.setCreateTime(DateUtils.getDateTime());

        int result = tombstoneMessageDao.insert(tombstoneMessage);
        if(result==0){
            return ResponseUtil.fail("添加失败");
        }
        return ResponseUtil.ok("添加成功");
    }
    @Override
    public JsonResult insertTomstoneSweep(TombstoneSweep tombstoneSweep) throws Exception {
        User user1 = userDao.selectById(tombstoneSweep.getUserId());
        if(user1==null){
            return ResponseUtil.fail("扫墓失败");
        }
        if(user1.getRice()<50){
            return ResponseUtil.fail("米不足");
        }

        //支付米
        User user = new User();
        user.setId(tombstoneSweep.getUserId());

        user.setRice(50);
       int  result = userDao.setDec(user);

        if(result==0){
            return ResponseUtil.fail("扫墓失败");
        }
        RiceRecord riceRecord = new RiceRecord();
        riceRecord.setUserId(tombstoneSweep.getUserId());
        riceRecord.setRice(-50);
        riceRecord.setContent("扫墓支出");
        riceRecord.setCreateTime(DateUtils.getDateTime());
        riceRecordDao.insert(riceRecord);

        tombstoneSweep.setCreateTime(DateUtils.getDateTime());
         result = tombstoneSweepDao.insert(tombstoneSweep);


        if(result==0){
            return ResponseUtil.fail("扫墓失败");
        }
        return ResponseUtil.ok("扫墓成功");
    }


    @Override
    public JsonResult insertTomstoneFlower(TombstoneFlower tombstoneFlower) throws Exception {
        User user1 = userDao.selectById(tombstoneFlower.getUserId());
        if(user1==null){
            return ResponseUtil.fail("献花失败");
        }
        if(user1.getRice()<50){
            return ResponseUtil.fail("米不足");
        }

        //支付米
        User user = new User();
        user.setId(tombstoneFlower.getUserId());

        user.setRice(50);
        int  result = userDao.setDec(user);

        if(result==0){
            return ResponseUtil.fail("献花失败");
        }
        RiceRecord riceRecord = new RiceRecord();
        riceRecord.setUserId(tombstoneFlower.getUserId());
        riceRecord.setRice(-50);
        riceRecord.setContent("献花支出");
        riceRecord.setCreateTime(DateUtils.getDateTime());
        riceRecordDao.insert(riceRecord);

        tombstoneFlower.setCreateTime(DateUtils.getDateTime());
        result = tombstoneFlowerDao.insert(tombstoneFlower);


        if(result==0){
            return ResponseUtil.fail("献花失败");
        }

        return ResponseUtil.ok("献花成功");
    }



    @Override
    public JsonResult insertTomstonegiftuser(TombstoneUsergift tombstoneUsergift) throws Exception {
        User user1 = userDao.selectById(tombstoneUsergift.getUserid());
        if(user1==null){
            return ResponseUtil.fail("献祭失败");
        }
        if(user1.getRice()<50){
            return ResponseUtil.fail("米不足");
        }

        //支付米
        User user = new User();
        user.setId(tombstoneUsergift.getUserid());

        user.setRice(50);
        int  result = userDao.setDec(user);

        if(result==0){
            return ResponseUtil.fail("献祭失败");
        }
        RiceRecord riceRecord = new RiceRecord();
        riceRecord.setUserId(tombstoneUsergift.getUserid());
        riceRecord.setRice(-50);
        riceRecord.setContent("献花支出");
        riceRecord.setCreateTime(DateUtils.getDateTime());
        riceRecordDao.insert(riceRecord);

        tombstoneUsergift.setCreateTime(DateUtils.getDateTime());
        result = tombstoneUsergiftDao.insert(tombstoneUsergift);


        if(result==0){
            return ResponseUtil.fail("献祭失败");
        }

        return ResponseUtil.ok("献祭成功");
    }


    @Override
    public JsonResult insertTomstoneGifttype(TombstoneGift tombstoneGift) throws  Exception{
        tombstoneGift.setCreateTime(DateUtils.getDateTime());
        tombstoneGiftDao.insert(tombstoneGift);
        return ResponseUtil.ok("添加成功");
    }

    @Override
    public JsonResult insertTomstoneessay(TombstoneEssay tombstoneEssay) throws  Exception{

        QueryWrapper<TombstoneEssay>  tombstoneEssayQueryWrapper= new QueryWrapper<TombstoneEssay>();
        tombstoneEssayQueryWrapper.eq("essay_id",tombstoneEssay.getEssayId());
        tombstoneEssayQueryWrapper.eq("tombstone_id",tombstoneEssay.getTombstoneId());
        List<TombstoneEssay> tombstoneEssays = tombstoneEssayDao.selectList(tombstoneEssayQueryWrapper);
        if(tombstoneEssays.size()<=0){
            tombstoneEssay.setAddTime(DateUtils.getDateTime());
            tombstoneEssayDao.insert(tombstoneEssay);
        }

        return ResponseUtil.ok("添加成功");

    }
    @Override
    public JsonResult getTomstoneessay(TombstoneEssay tombstoneEssay) throws  Exception{
        QueryWrapper<TombstoneEssay>  tombstoneEssayQueryWrapper= new QueryWrapper<TombstoneEssay>();
        tombstoneEssayQueryWrapper.eq("tombstone_id",tombstoneEssay.getTombstoneId());
         List<TombstoneEssay> tombstoneEssays =   tombstoneEssayDao.selectList(tombstoneEssayQueryWrapper);



        TreeSet<Integer> familyIdList = new TreeSet<Integer>();
        for (int i = 0; i < tombstoneEssays.size(); i++) {
            familyIdList.add(tombstoneEssays.get(i).getEssayId());

        }
        if(tombstoneEssays.size()>0){
            HashMap<String, Object> map = new HashMap<>(8);

            List<Essay> essayList = essayDao.selectBatchIds(familyIdList);
            List<EssayAndImgSelectResVo> essayAndImgSelectResVoList = new ArrayList<EssayAndImgSelectResVo>();
            for(int i=0;i<essayList.size();i++){
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
                if(user!=null){
                    UserFindResVo userFindResVo = new UserFindResVo();
                    userFindResVo.setAvatar(user.getAvatar());
                    userFindResVo.setNickName(user.getNickName());
                    userFindResVo.setRealName(user.getRealName());
                    essayAndImgSelectResVo.setUserFindResVo(userFindResVo);
                }
                essayAndImgSelectResVoList.add(essayAndImgSelectResVo);
            }
            map.put("essayList", essayAndImgSelectResVoList);
            return ResponseUtil.ok("获取成功",map);
        }

        return ResponseUtil.ok("获取成功");

    }


    @Override
    public JsonResult getTomstonegift(Integer tombsoneid) throws  Exception{
        QueryWrapper<TombstoneGift>  tombstoneGiftQueryWrapper= new QueryWrapper<TombstoneGift>();
        tombstoneGiftQueryWrapper.eq("tombstone_id",tombsoneid);
        List<TombstoneGift> tombstoneGifts = tombstoneGiftDao.selectList(tombstoneGiftQueryWrapper);

        List<GetTomstonegistResVo> essayAndImgSelectResVoList = new ArrayList<GetTomstonegistResVo>();

        for(int i=0;i<tombstoneGifts.size();i++){
            GetTomstonegistResVo getTomstonegistResVo = new GetTomstonegistResVo();
            getTomstonegistResVo.setTombstoneGift(tombstoneGifts.get(i));

            QueryWrapper<TombstoneUsergift>  tombstoneUsergiftQueryWrapper= new QueryWrapper<TombstoneUsergift>();
            tombstoneUsergiftQueryWrapper.eq("gift_id",tombstoneGifts.get(i).getId());
            tombstoneUsergiftQueryWrapper.eq("tombstone_id",tombsoneid);
            tombstoneUsergiftQueryWrapper.orderByDesc("create_time");
            tombstoneUsergiftQueryWrapper.last("limit 1");

            TombstoneUsergift tombstoneUsergifts = tombstoneUsergiftDao.selectOne(tombstoneUsergiftQueryWrapper);


            QueryWrapper<TombstoneUsergift>  tombstoneUsergiftQueryWrapper1= new QueryWrapper<TombstoneUsergift>();
            tombstoneUsergiftQueryWrapper1.eq("gift_id",tombstoneGifts.get(i).getId());
            tombstoneUsergiftQueryWrapper1.eq("tombstone_id",tombsoneid);

            List <TombstoneUsergift> tombstoneUsergifts1 = tombstoneUsergiftDao.selectList(tombstoneUsergiftQueryWrapper1);


            if(tombstoneUsergifts!=null){
                QueryWrapper<User>  user= new QueryWrapper<User>();
                user.eq("id",tombstoneUsergifts.getUserid());
               User user1 =  userDao.selectOne(user);
               user1.setClean(tombstoneUsergifts1.size());
                getTomstonegistResVo.setUser(user1);
            }



            getTomstonegistResVo.setTombstoneUsergift(tombstoneUsergifts);
            essayAndImgSelectResVoList.add(getTomstonegistResVo);
        }

        return ResponseUtil.ok("获取数据",essayAndImgSelectResVoList);

    }

    @Override
    public JsonResult getTomstonegiftuser(Integer tombonedid,Integer giftId) throws Exception{
        HashMap<String, Object> map = new HashMap<>(8);

        QueryWrapper<TombstoneUsergift>  tombstoneUsergiftQueryWrapper= new QueryWrapper<TombstoneUsergift>();
        tombstoneUsergiftQueryWrapper.eq("tombstone_id",tombonedid);
        tombstoneUsergiftQueryWrapper.eq("gift_id",giftId);
        List<TombstoneUsergift> tombstoneUsergifts = tombstoneUsergiftDao.selectList(tombstoneUsergiftQueryWrapper);
        map.put("tombstoneUsergifts",tombstoneUsergifts);


        //有数据，开始准备in查询
        TreeSet<Integer> familyIdList3 = new TreeSet<Integer>();
        for (int i = 0; i < tombstoneUsergifts.size(); i++) {
            familyIdList3.add(tombstoneUsergifts.get(i).getUserid());
        }
        if(tombstoneUsergifts.size()>0){
            List<User> tombstoneGift_user = userDao.selectBatchIds(familyIdList3);
            map.put("tombstoneGift_user",tombstoneGift_user);

        }

        QueryWrapper<TombstoneGift>  tombstoneQueryWrapper= new QueryWrapper<TombstoneGift>();
        tombstoneQueryWrapper.eq("id",giftId);
        tombstoneQueryWrapper.eq("tombstone_id",tombonedid);

        TombstoneGift tombstone = tombstoneGiftDao.selectOne(tombstoneQueryWrapper);
        map.put("tombstonegift",tombstone);

        return ResponseUtil.ok("添加成功",map);

    }


    @Override
    public JsonResult findTomstoneByfamilyId(Integer Id) throws Exception {
        HashMap<String, Object> map = new HashMap<>(8);
        QueryWrapper<Tombstone>  tombstone= new QueryWrapper<Tombstone>();
        tombstone.eq("id",Id);
       Tombstone tombstonedata =  tombstoneDao.selectOne(tombstone);


       //查找家族有哪些贴文
        QueryWrapper<Essay>  essayQueryWrapper= new QueryWrapper<Essay>();
        essayQueryWrapper.eq("family_id",tombstonedata.getFamilyId());
        List<Essay> essaydata = essayDao.selectList(essayQueryWrapper);
        map.put("essaydata",essaydata);

        //查找祠堂有哪些贴文
        QueryWrapper<TombstoneEssay>  tombstoneEssayQueryWrapper= new QueryWrapper<TombstoneEssay>();
        tombstoneEssayQueryWrapper.eq("tombstone_id",Id);
        List<TombstoneEssay> tombstoneEssays = tombstoneEssayDao.selectList(tombstoneEssayQueryWrapper);
//有数据，开始准备in查询




//        TreeSet<Integer> familyIdList7 = new TreeSet<Integer>();
//        for (int i = 0; i < tombstoneEssays.size(); i++) {
//            familyIdList7.add(tombstoneEssays.get(i).getEssayId());
//        }
//        List<EssayAndImgSelectResVo> essayAndImgSelectResVoList = new ArrayList<EssayAndImgSelectResVo>();
//
//        if(tombstoneEssays.size()>0){
//            List<Essay> tombstoneMessagedata_user1 = essayDao.selectBatchIds(familyIdList7);
//
//            for (int i = 0; i < tombstoneMessagedata_user1.size(); i++) {
//                EssayAndImgSelectResVo essayAndImgSelectResVo = new EssayAndImgSelectResVo();
//                essayAndImgSelectResVo.setEssay(tombstoneMessagedata_user1.get(i));
//                essayAndImgSelectResVoList.add(essayAndImgSelectResVo);
//            }
//
//            map.put("tombstoneEssays_essay",essayAndImgSelectResVoList);
//        }



        //查找留言
        QueryWrapper<TombstoneMessage>  tombstoneMessage= new QueryWrapper<TombstoneMessage>();
        tombstoneMessage.eq("tombstone_id",Id);
        tombstoneMessage.select("*");
        List<TombstoneMessage> tombstoneMessagedata =  tombstoneMessageDao.selectList(tombstoneMessage);
        //有数据，开始准备in查询
        TreeSet<Integer> familyIdList = new TreeSet<Integer>();
        for (int i = 0; i < tombstoneMessagedata.size(); i++) {
            familyIdList.add(tombstoneMessagedata.get(i).getUserId());

        }
        if(tombstoneMessagedata.size()>0){
            List<User> tombstoneMessagedata_user = userDao.selectBatchIds(familyIdList);
            map.put("tombstoneMessagedata_user",tombstoneMessagedata_user);
        }






        //查找献花
        QueryWrapper<TombstoneFlower>  tombstoneFlower= new QueryWrapper<TombstoneFlower>();
        tombstoneFlower.eq("tombstone_id",Id);
        tombstoneFlower.select("*");
        List<TombstoneFlower> tombstoneFlowerdata =  tombstoneFlowerDao.selectList(tombstoneFlower);
        //有数据，开始准备in查询
        TreeSet<Integer> familyIdList1 = new TreeSet<Integer>();
        for (int i = 0; i < tombstoneFlowerdata.size(); i++) {
            familyIdList1.add(tombstoneFlowerdata.get(i).getUserId());
        }
        if(tombstoneFlowerdata.size()>0){
            List<User> tombstoneFlower_user = userDao.selectBatchIds(familyIdList1);
            map.put("tombstoneFlower_user",tombstoneFlower_user);

        }






        //查找扫墓
        QueryWrapper<TombstoneSweep>  tombstoneSweep= new QueryWrapper<TombstoneSweep>();
        tombstoneSweep.eq("tombstone_id",Id);
        tombstoneSweep.select("*");
        List<TombstoneSweep> tombstoneSweepdata =  tombstoneSweepDao.selectList(tombstoneSweep);

        //有数据，开始准备in查询
        TreeSet<Integer> familyIdList2 = new TreeSet<Integer>();
        for (int i = 0; i < tombstoneSweepdata.size(); i++) {
            familyIdList2.add(tombstoneSweepdata.get(i).getUserId());
        }
        if(tombstoneSweepdata.size()>0){
            List<User> tombstoneSweep_user = userDao.selectBatchIds(familyIdList2);
            map.put("tombstoneSweep_user",tombstoneSweep_user);

        }






        //查找献祭列表
        QueryWrapper<TombstoneUsergift>  tombstoneUsergiftQueryWrapper= new QueryWrapper<TombstoneUsergift>();

        tombstoneUsergiftQueryWrapper.eq("tombstone_id",Id);
        tombstoneUsergiftQueryWrapper.select("*");

        List<TombstoneUsergift> tombstoneSweeps =  tombstoneUsergiftDao.selectList(tombstoneUsergiftQueryWrapper);


        //有数据，开始准备in查询
//        TreeSet<Integer> familyIdList3 = new TreeSet<Integer>();
//        for (int i = 0; i < tombstoneSweeps.size(); i++) {
//            familyIdList3.add(tombstoneSweeps.get(i).getUserid());
//        }
//        if(tombstoneSweeps.size()>0){
//            List<User> tombstoneGift_user = userDao.selectBatchIds(familyIdList2);
//            map.put("tombstoneGift_user",tombstoneGift_user);
//        }



        //查找用户
//        QueryWrapper<FamilyUser>  familyUser= new QueryWrapper<FamilyUser>();
//        familyUser.eq("id",tombstonedata.getFamilyuserId());
//      FamilyUser familyUserdata =  familyUserDao.selectOne(familyUser);
//


        map.put("tombstonedata",tombstonedata);
        map.put("tombstoneMessagedata",tombstoneMessagedata);
        map.put("tombstoneFlowerdata",tombstoneFlowerdata);
        map.put("tombstoneSweepdata",tombstoneSweepdata);
        map.put("tombstoneGiftdata",tombstoneSweeps);

//        map.put("familyUserdata",familyUserdata);

        return ResponseUtil.ok("添加成功",map);
    }





}