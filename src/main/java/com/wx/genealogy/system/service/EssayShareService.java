package com.wx.genealogy.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.EssayShare;
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
public interface EssayShareService extends IService<EssayShare> {

    JsonResult insertEssayShare(EssayShare essayShare) throws Exception;

}
