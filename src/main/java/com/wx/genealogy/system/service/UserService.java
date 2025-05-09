package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@Transactional(rollbackFor = Exception.class)
public interface UserService extends IService<User> {

    JsonResult findUser(User user);

    JsonResult insertUser(User user) throws Exception;

    JsonResult updateUserById(User user) throws Exception;
    JsonResult updateFamilyIndex(User user) throws Exception;

    JsonResult findUserById(Integer id);

    JsonResult selectUser(Integer page,Integer limit,User user);

    /**
     * 查询用户根据id
     * @return
     * @param userId
     */
    User selectUserById(Integer userId);



}
