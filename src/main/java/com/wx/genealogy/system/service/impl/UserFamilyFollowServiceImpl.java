package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.Family;
import com.wx.genealogy.system.entity.UserFamilyFollow;
import com.wx.genealogy.system.mapper.FamilyDao;
import com.wx.genealogy.system.mapper.UserFamilyFollowDao;
import com.wx.genealogy.system.service.UserFamilyFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.system.vo.res.UserFamilyFollowAndFamilySelectResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * <p>
 * 用户关注家族 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@Service
public class UserFamilyFollowServiceImpl extends ServiceImpl<UserFamilyFollowDao, UserFamilyFollow> implements UserFamilyFollowService {

    @Autowired
    private UserFamilyFollowDao userFamilyFollowDao;

    @Autowired
    private FamilyDao familyDao;

    @Override
    public JsonResult insertUserFamilyFollow(UserFamilyFollow userFamilyFollow) throws Exception {

        //先查询是否存在
        QueryWrapper<UserFamilyFollow> userFamilyFollowFind = new QueryWrapper<UserFamilyFollow>();
        userFamilyFollowFind.eq("user_id", userFamilyFollow.getUserId());
        userFamilyFollowFind.eq("family_id", userFamilyFollow.getFamilyId());
        UserFamilyFollow userFamilyFollowData = userFamilyFollowDao.selectOne(userFamilyFollowFind);
        //不存在
        if(userFamilyFollowData==null){
            int result=userFamilyFollowDao.insert(userFamilyFollow);
            return result==0? ResponseUtil.fail("关注失败"):ResponseUtil.ok("关注成功");
        }
        //已经存在
        userFamilyFollowData.setStatus(1);
        int result=userFamilyFollowDao.updateById(userFamilyFollowData);
        return result==0? ResponseUtil.fail("关注失败"):ResponseUtil.ok("关注成功");

    }

    @Override
    public JsonResult updateUserFamilyFollowById(UserFamilyFollow userFamilyFollow) throws Exception {
        int result=userFamilyFollowDao.updateById(userFamilyFollow);
        return result==0? ResponseUtil.fail("修改失败"):ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult selectUserFamilyFollowAndFamily(Integer page, Integer limit, UserFamilyFollow userFamilyFollow){

        IPage<UserFamilyFollow> pageData =new Page<>(page, limit);
        QueryWrapper<UserFamilyFollow> userFamilyFollowSelect = new QueryWrapper<UserFamilyFollow>();
        userFamilyFollowSelect.eq("user_id",userFamilyFollow.getUserId());
        userFamilyFollowSelect.eq("is_status",userFamilyFollow.getStatus());

        pageData=userFamilyFollowDao.selectPage(pageData, userFamilyFollowSelect);
        List<UserFamilyFollow> userFamilyFollowList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if(userFamilyFollowList==null||userFamilyFollowList.size()==0){
            map.put("userFamilyFollowAndFamilySelectResVoList", null);
            return ResponseUtil.ok("获取成功",map);
        }

        //有数据，开始准备in查询
        TreeSet<Integer> familyIdList = new TreeSet<Integer>();
        for(int i=0;i<userFamilyFollowList.size();i++){
            familyIdList.add(userFamilyFollowList.get(i).getFamilyId());
        }
        List<Family> familyList=familyDao.selectBatchIds(familyIdList);

        List<UserFamilyFollowAndFamilySelectResVo> userFamilyFollowAndFamilySelectResVoList = new ArrayList<UserFamilyFollowAndFamilySelectResVo>();

        for(int i=0;i<userFamilyFollowList.size();i++){
            UserFamilyFollowAndFamilySelectResVo userFamilyFollowAndFamilySelectResVo = new UserFamilyFollowAndFamilySelectResVo();
            for(int j=0;j<familyList.size();j++){
                if(userFamilyFollowList.get(i).getFamilyId().equals(familyList.get(j).getId())){
                    userFamilyFollowAndFamilySelectResVo.setUserFamilyFollow(userFamilyFollowList.get(i));
                    userFamilyFollowAndFamilySelectResVo.setFamily(familyList.get(j));
                    break;
                }
            }
            userFamilyFollowAndFamilySelectResVoList.add(userFamilyFollowAndFamilySelectResVo);
        }

        map.put("userFamilyFollowAndFamilySelectResVoList", userFamilyFollowAndFamilySelectResVoList);
        return ResponseUtil.ok("获取成功",map);
    }
}
