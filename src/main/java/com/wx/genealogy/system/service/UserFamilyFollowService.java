package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.UserFamilyFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户关注家族 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@Transactional(rollbackFor = Exception.class)
public interface UserFamilyFollowService extends IService<UserFamilyFollow> {

    JsonResult insertUserFamilyFollow(UserFamilyFollow userFamilyFollow) throws Exception;

    JsonResult updateUserFamilyFollowById(UserFamilyFollow userFamilyFollow) throws Exception;

    JsonResult selectUserFamilyFollowAndFamily(Integer page,Integer limit,UserFamilyFollow userFamilyFollow);
}
