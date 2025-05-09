package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.system.entity.FamilyGenealogyImg;
import com.wx.genealogy.system.mapper.FamilyGenealogyImgDao;
import com.wx.genealogy.system.service.FamilyGenealogyImgService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 个人简介图片表 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2024-10-8
 */
@Service
public class FamilyGenealogyImgServiceImpl extends ServiceImpl<FamilyGenealogyImgDao, FamilyGenealogyImg> implements FamilyGenealogyImgService {

    @Resource
    private FamilyGenealogyImgDao familyGenealogyImgDao;

    @Override
    public int deleteByFamilyGenealogyId(Integer familyGenealogyId) {
        return familyGenealogyImgDao.deleteByFamilyGenealogyId(familyGenealogyId);
    }
}
