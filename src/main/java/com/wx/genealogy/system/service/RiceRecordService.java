package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.RiceRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 米收支明细 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-10-20
 */
@Transactional(rollbackFor = Exception.class)
public interface RiceRecordService extends IService<RiceRecord> {

    JsonResult selectRiceRecord(Integer page , Integer limit,Integer type, RiceRecord riceRecord);

    /**
     * 查询所有明细记录
     * @param page
     * @param limit
     * @return
     */
    JsonResult selectRiceRecordList(Integer page, Integer limit);
}
