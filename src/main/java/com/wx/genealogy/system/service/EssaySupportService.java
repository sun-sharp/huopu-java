package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.EssaySupport;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-10-15
 */
@Transactional(rollbackFor = Exception.class)
public interface EssaySupportService extends IService<EssaySupport> {

    JsonResult insertEssaySupport(EssaySupport essaySupport) throws  Exception;

    JsonResult updateEssaySupportById(EssaySupport essaySupport) throws Exception;

    JsonResult selectEssaySupportAndUser(Integer page,Integer limit,EssaySupport essaySupport);

    JsonResult findEssaySupport(EssaySupport essaySupport);
}
