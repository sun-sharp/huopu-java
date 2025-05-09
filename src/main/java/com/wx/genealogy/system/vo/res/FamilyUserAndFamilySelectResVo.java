package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.Family;
import com.wx.genealogy.system.entity.FamilyMessage;
import com.wx.genealogy.system.entity.FamilyUser;
import lombok.Data;

/**
 * @ClassName FamilyUserAndFamilySelectResVo
 * @Author weixin
 * @Data 2021/9/10 14:16
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyUserAndFamilySelectResVo {

    private FamilyUser familyUser;

    private Family family;

    private FamilyMessage familyMessage;
}
