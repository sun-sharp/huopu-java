package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.Family;
import com.wx.genealogy.system.entity.UserFamilyFollow;
import lombok.Data;

/**
 * @ClassName UserFamilyFollowAndFamilySelectResVo
 * @Author weixin
 * @Data 2021/9/10 14:22
 * @Description
 * @Version 1.0
 **/
@Data
public class UserFamilyFollowAndFamilySelectResVo {
    private UserFamilyFollow userFamilyFollow;
    private Family family;
}
