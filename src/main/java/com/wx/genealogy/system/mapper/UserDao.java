package com.wx.genealogy.system.mapper;

import com.wx.genealogy.system.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

    int setInc(User user);

    int setDec(User user);

    int setStatus(User user);
}
