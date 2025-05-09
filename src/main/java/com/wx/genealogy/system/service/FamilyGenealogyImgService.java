package com.wx.genealogy.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.system.entity.FamilyGenealogyImg;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 个人简介图片表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2024-10-8
 */
@Transactional(rollbackFor = Exception.class)
public interface FamilyGenealogyImgService extends IService<FamilyGenealogyImg> {

    int deleteByFamilyGenealogyId(Integer familyGenealogyId);
}
