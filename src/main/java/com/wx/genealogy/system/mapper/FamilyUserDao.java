package com.wx.genealogy.system.mapper;

import com.wx.genealogy.system.entity.FamilyUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 家族用户关联 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-10-09
 */
@Mapper
public interface FamilyUserDao extends BaseMapper<FamilyUser> {

    int setInc(FamilyUser familyUser);

    int updateFamilyUser(FamilyUser familyUser);

    FamilyUser selectManagerByFamilyIdNo(Integer familyId);

    List<FamilyUser> selectStatusByFamilyId(Integer familyId);

    int updateJoins(FamilyUser familyUser);


}
