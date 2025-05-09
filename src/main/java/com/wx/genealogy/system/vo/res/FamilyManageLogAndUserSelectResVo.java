package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.FamilyManageLog;
import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.entity.User;
import lombok.Data;

/**
 * @ClassName FamilyManageLogAndUserSelectResVo
 * @Author weixin
 * @Data 2021/10/13 14:23
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyManageLogAndUserSelectResVo {
    private FamilyManageLog familyManageLog;

    private FamilyUser familyUser;

    private User user;
}
