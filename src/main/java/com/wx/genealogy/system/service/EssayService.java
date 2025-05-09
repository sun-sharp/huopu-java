package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.Essay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wx.genealogy.system.entity.EssayImg;
import com.wx.genealogy.system.entity.FamilyUser;
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
public interface EssayService extends IService<Essay> {

    JsonResult insertEssay(Essay essay, List<EssayImg> essayImgList) throws Exception;

    JsonResult selectEssayByUserId(Integer page,Integer limit,Essay essay);

    JsonResult selectEssayByOpen(Integer page,Integer limit,Essay essay,Integer sort);

    JsonResult selectEssay(Integer page,Integer limit,Essay essay,Integer sort);

    JsonResult selectEssayByFamilyId(Integer page, Integer limit,Essay essay, FamilyUser familyUser,Integer sort,String searchData);

    JsonResult findEssayById(Essay essay);

    JsonResult updateEssayById(Essay essay) throws Exception;

    JsonResult updateEssayBrowseNumberById(Essay essay) throws Exception;

    JsonResult updateEssayUnlockById(Essay essay) throws Exception;

    JsonResult deleteEssayById(Integer id) throws Exception;
    JsonResult selectotherEssay(Integer limit,Integer page,Integer id,Integer userId) throws Exception;

    /**
     * 根据id查询帖子
     * @param id 帖子id
     * @return
     */
    Essay selectEssayById(Integer id);
}
