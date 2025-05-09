package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.Essay;
import com.wx.genealogy.system.entity.FamilyClean;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-09
 */

@Transactional(rollbackFor = Exception.class)
public interface FamilyCleanService extends IService<FamilyClean>{
    JsonResult insertFamilyClean(FamilyClean familyClean) throws  Exception;
    JsonResult updateFamilyUnlockById(FamilyClean familyClean,int mi,int minval)throws  Exception;
    JsonResult selectFamilyClean(Integer page, Integer limit,Integer familyId)throws  Exception;
}

