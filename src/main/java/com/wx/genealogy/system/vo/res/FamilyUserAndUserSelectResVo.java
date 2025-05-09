package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.entity.User;
import lombok.Data;

/**
 * @ClassName FamilyUserAndUserSelectResVo
 * @Author weixin
 * @Data 2021/9/9 12:29
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyUserAndUserSelectResVo {

    private FamilyUser familyUser;

    private User user;
}
