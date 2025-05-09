package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.system.entity.Essay;
import com.wx.genealogy.system.entity.Fire;
import com.wx.genealogy.system.vo.req.FireInsertReqVo;
import com.wx.genealogy.system.vo.req.TradeInsertReqVo;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-09
 */
@Transactional(rollbackFor = Exception.class)
public interface FireService extends IService<Fire> {

    JsonResult insertFire(FireInsertReqVo fireInsertReqVo, String ip) throws Exception;
    JsonResult selectFireByUserId(Integer page,Integer limit,Fire fire);


    JsonResult selectFire(Integer page,Integer limit,Fire fire);


    JsonResult findFireById(Fire fire);

}
