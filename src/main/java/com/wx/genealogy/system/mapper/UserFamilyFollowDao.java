package com.wx.genealogy.system.mapper;

import com.wx.genealogy.system.entity.UserFamilyFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户关注家族 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@Mapper
public interface UserFamilyFollowDao extends BaseMapper<UserFamilyFollow> {
    int setInc(UserFamilyFollow userFamilyFollow);
}
