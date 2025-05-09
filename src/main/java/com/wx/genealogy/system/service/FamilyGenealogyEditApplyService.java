package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.system.entity.FamilyGenealogyEditApply;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户编辑申请表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2024-10-8
 */
@Transactional(rollbackFor = Exception.class)
public interface FamilyGenealogyEditApplyService extends IService<FamilyGenealogyEditApply> {

}
