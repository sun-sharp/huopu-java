package com.wx.genealogy.system.service.impl;

import com.wx.genealogy.system.entity.FamilyPedigreeApply;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.FamilyPedigreeApplyDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.FamilyPedigreeApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FamilyPedigreeApplyServiceImpl implements FamilyPedigreeApplyService {

    @Autowired
    private FamilyPedigreeApplyDao familyPedigreeApplyDao;


    @Autowired
    private UserDao userDao;

    /**
     * 查询【请填写功能名称】
     *
     * @param applyId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public FamilyPedigreeApply selectFamilyPedigreeApplyByApplyId(Long applyId)
    {
        return familyPedigreeApplyDao.selectFamilyPedigreeApplyByApplyId(applyId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param familyPedigreeApply 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<FamilyPedigreeApply> selectFamilyPedigreeApplyList(FamilyPedigreeApply familyPedigreeApply)
    {
        return familyPedigreeApplyDao.selectFamilyPedigreeApplyList(familyPedigreeApply);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param familyPedigreeApply 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertFamilyPedigreeApply(FamilyPedigreeApply familyPedigreeApply)
    {
        return familyPedigreeApplyDao.insertFamilyPedigreeApply(familyPedigreeApply);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param familyPedigreeApply 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateFamilyPedigreeApply(FamilyPedigreeApply familyPedigreeApply)
    {
        Date date = new Date();
        familyPedigreeApply.setTime(date);
//        User user = new User();

//        userDao.setStatus(user);
        System.out.println(familyPedigreeApply);
        System.out.println("类型："+familyPedigreeApply.getType());
        System.out.println("id："+familyPedigreeApply.getUserId().intValue());

        User user = new User();
        user.setId(1046);
        user.setStatus((long)1);
        userDao.setStatus(user);
        return familyPedigreeApplyDao.updateFamilyPedigreeApply(familyPedigreeApply);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param applyIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteFamilyPedigreeApplyByApplyIds(Long[] applyIds)
    {
        return familyPedigreeApplyDao.deleteFamilyPedigreeApplyByApplyIds(applyIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param applyId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteFamilyPedigreeApplyByApplyId(Long applyId)
    {
        return familyPedigreeApplyDao.deleteFamilyPedigreeApplyByApplyId(applyId);
    }
}
