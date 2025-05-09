package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.DouDeliver;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.vo.req.DouDeliverReqVo;

/**
 * 斗投递Service接口
 *
 * @author leo
 * @date 2024-07-05
 */
public interface DouDeliverService extends IService<DouDeliver> {

    JsonResult selectUser(Integer page, Integer limit, User user);

    JsonResult updateUserByIds(DouDeliverReqVo deliver) throws Exception;

    JsonResult page(Integer page, Integer limit, DouDeliver dou);

    JsonResult list(Integer userId);
}
