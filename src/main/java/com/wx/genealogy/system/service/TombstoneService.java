package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.*;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
public interface TombstoneService extends IService<Tombstone> {
    JsonResult selectTomstoneByfamilyId(Integer page, Integer limit,Integer familyId)throws  Exception;
    JsonResult insertTomstone( Tombstone tombstone)throws Exception;
    JsonResult findTomstoneByfamilyId(Integer Id)throws  Exception;
    JsonResult insertTomstoneMessage(TombstoneMessage tombstoneMessage) throws  Exception;
    JsonResult insertTomstoneSweep(TombstoneSweep tombstoneSweep) throws  Exception;
    JsonResult insertTomstoneFlower(TombstoneFlower tombstoneFlower) throws  Exception;
    JsonResult insertTomstonegiftuser(TombstoneUsergift tombstoneUsergift)throws  Exception;
    JsonResult insertTomstoneGifttype(TombstoneGift tombstoneGift)throws  Exception;

    JsonResult insertTomstoneessay(TombstoneEssay tombstone)throws  Exception;
    JsonResult getTomstoneessay(TombstoneEssay tombstone)throws  Exception;
    JsonResult getTomstonegift(Integer tombstoneid)throws  Exception;
    JsonResult getTomstonegiftuser(Integer tombstoneid,Integer giftId)throws  Exception;

}
