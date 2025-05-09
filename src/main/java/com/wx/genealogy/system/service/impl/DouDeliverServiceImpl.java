package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.ssjwt.SecurityUtil;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.DouDeliver;
import com.wx.genealogy.system.entity.SysUser;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.DouDeliverDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.DouDeliverService;
import com.wx.genealogy.system.service.SysUserService;
import com.wx.genealogy.system.vo.req.DouDeliverReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 斗投递Service业务层处理
 *
 * @author leo
 * @date 2024-07-05
 */
@Service
public class DouDeliverServiceImpl extends ServiceImpl<DouDeliverDao, DouDeliver> implements DouDeliverService {

    @Resource
    private DouDeliverDao douDeliverDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public JsonResult selectUser(Integer page, Integer limit, User user) {
        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        try {
            IPage<User> pageData = new Page<>(page, limit);
            QueryWrapper<User> userSelect = new QueryWrapper<User>();
            if (user.getNickName() != null && user.getNickName().equals("") == false) {
                userSelect.like("nick_name", user.getNickName());
            }
            if (user.getSex() != null) {
                userSelect.eq("sex", user.getSex());
            }
            if (user.getRealName() != null && user.getRealName().equals("") == false) {
                userSelect.like("real_name", user.getRealName());
            }

            userSelect.orderByDesc("id");

            pageData = userDao.selectPage(pageData, userSelect);
            List<User> userList = pageData.getRecords();
            for (User u : userList) {
                Integer amount = douDeliverDao.getAmountByUserId(u.getId());
                u.setDou(null != amount ? amount : 0);
            }
            map.put("userList", userList);
            map.put("pages", pageData.getPages());
            map.put("total", pageData.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult updateUserByIds(DouDeliverReqVo deliver) throws Exception{
        if(null != deliver.getIds() && deliver.getIds().size() > 0) {
            for (Integer userId : deliver.getIds()) {
                User userData = userDao.selectById(userId);
                if (userData == null) {
                    throw new Exception("用户不存在");
                }

                SysUser sysUser = sysUserService.selectUserByUserName(SecurityUtil.getUserName());
                if (sysUser == null){
                    throw new Exception("账号不存在");
                }
                Date curDate = new Date();
                DouDeliver douDeliver = new DouDeliver();
                douDeliver.setAmount(deliver.getAmount());
                douDeliver.setReason(deliver.getReason());
                douDeliver.setValidYear(deliver.getValidYear());
                douDeliver.setUserId(userId);
                douDeliver.setCreateById(sysUser.getId());
                douDeliver.setCreateTime(curDate);
                douDeliver.setEndTime(DateUtils.getEndDate(curDate, deliver.getValidYear().intValue()));
                douDeliver.setUserName(userData.getRealName());
                douDeliver.setCreateByName(sysUser.getUserName());

                User user = userDao.selectById(userId);
                user.setDou(user.getDou() + deliver.getAmount().intValue());

                userDao.updateById(user);
                douDeliverDao.insert(douDeliver);
            }
        }
        return ResponseUtil.ok("投递成功");
    }

    @Override
    public JsonResult page(Integer page, Integer limit, DouDeliver deliver) {
        IPage<DouDeliver> pageData = new Page<>(page, limit);
        QueryWrapper<DouDeliver> queryWrapper = new QueryWrapper<DouDeliver>();
        if (deliver.getUserName() != null && deliver.getUserName().equals("") == false) {
            queryWrapper.like("user_name", deliver.getUserName());
        }
        queryWrapper.orderByDesc("id");

        pageData = douDeliverDao.selectPage(pageData, queryWrapper);
        List<DouDeliver> douList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("douList", douList);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        return ResponseUtil.ok("获取成功", map);
    }

    @Override
    public JsonResult list(Integer userId) {
        QueryWrapper<DouDeliver> queryWrapper = new QueryWrapper<DouDeliver>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("id");
        List<DouDeliver> douList = douDeliverDao.selectList(queryWrapper);
        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("douList", douList);
        return ResponseUtil.ok("获取成功", map);
    }
}
