package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ResponseUtil;

import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.DouDeliverDao;
import com.wx.genealogy.system.mapper.FamilyUserDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;

    @Resource
    private DouDeliverDao douDeliverDao;

    @Autowired
    private FamilyUserDao familyUserDao;

    @Override
    public JsonResult findUser(User user) {
        QueryWrapper<User> userFind = new QueryWrapper<User>();
        userFind.eq("openid", user.getOpenid());
        User userData = userDao.selectOne(userFind);



        //未注册过
        if(userData==null){
            return ResponseUtil.ok("未注册",user);
        }
        if(userData.getFamily_index() == 0) {

            QueryWrapper<FamilyUser> familyUserQuery = new QueryWrapper<>();
            familyUserQuery.select("family_id")
                    .eq("user_id", userData.getId())
                    .gt("family_id", 0)
                    .last("limit 1");

            FamilyUser familyUser = familyUserDao.selectOne(familyUserQuery);
            if(familyUser != null) {
                userData.setFamily_index(familyUser.getFamilyId());
            }
        }



        //登录成功后还需要更新下登录时间
        User userUpdate = new User();
        userUpdate.setId(userData.getId());
        userUpdate.setLastLoginTime(DateUtils.getDateTime());
        userDao.updateById(userUpdate);
        //最新登录时间传回前端
        userData.setLastLoginTime(userUpdate.getLastLoginTime());

        return ResponseUtil.ok("获取成功",userData);
    }

    public JsonResult updateFamilyIndex(User user) throws Exception{
        //修改之前检查真实姓名是否存在，如果存在则提示
        QueryWrapper<User> userFind = new QueryWrapper<User>();

        int result= userDao.updateById(user);
        return result==0?ResponseUtil.fail("修改失败"):ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult insertUser(User user) throws Exception {
        //插入前检查下是否注册过以防万一
        QueryWrapper<User> userFind = new QueryWrapper<User>();
        userFind.eq("openid", user.getOpenid());
        User userData = userDao.selectOne(userFind);
        //已经注册过
        if(userData!=null){
            return ResponseUtil.ok("已经注册",userData);
        }
        //未注册过进行注册
        int result=userDao.insert(user);
        return result==0?ResponseUtil.fail("注册失败"):ResponseUtil.ok("注册成功",user);
    }


    @Override
    public JsonResult updateUserById(User user) throws Exception{
        //修改之前检查真实姓名是否存在，如果存在则提示
        QueryWrapper<User> userFind = new QueryWrapper<User>();
        userFind.eq("real_name", user.getRealName());
        userFind.ne("id", user.getId());
        User userData = userDao.selectOne(userFind);
        //已经注册过
        if(userData!=null){
            return ResponseUtil.fail("真实姓名已存在");
        }



        int result= userDao.updateById(user);
        return result==0?ResponseUtil.fail("修改失败"):ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult findUserById(Integer id) {
        User userData = userDao.selectById(id);
        Integer amount = douDeliverDao.getAmountByUserId(id);
        userData.setDou(null != amount ? amount : 0);
        return  ResponseUtil.ok("获取成功",userData);
    }

    @Override
    public JsonResult selectUser(Integer page, Integer limit, User user) {
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

        userSelect.orderByDesc("dou");

        pageData = userDao.selectPage(pageData, userSelect);
        List<User> userList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("userList", userList);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());
        return ResponseUtil.ok("获取成功", map);
    }

    /**
     * 查询用户根据id
     * @param userId
     * @return
     */
    @Override
    public User selectUserById(Integer userId) {
        return userDao.selectById(userId);
    }


}
