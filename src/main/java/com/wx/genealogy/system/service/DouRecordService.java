package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.DouRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 斗收入明细 服务类
 * </p>
 *
 * @author ${author}
 * @since 2022-07-12
 */
public interface DouRecordService extends IService<DouRecord> {

    /**
     * 查询所有斗明细记录
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    JsonResult selectDouRecordList(Integer page, Integer limit, Integer userId);

    /**
     * 兑换斗
     * @return
     */
    JsonResult exchangeDouRecord(DouRecord douRecord);
}
