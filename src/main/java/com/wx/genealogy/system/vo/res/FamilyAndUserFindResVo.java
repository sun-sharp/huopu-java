package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName FamilyAndUserFindResVo
 * @Author weixin
 * @Data 2021/9/8 11:14
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyAndUserFindResVo {

    private Family family;

    private FamilyUser familyUser;

    private UserFamilyFollow userFamilyFollow;

    private UserFindResVo userFindResVo;

    private UserFindResVo author;

    private FamilyClean familyClean;
    private String nowDate;
    private FamilyMessage familyMessage;
    private Integer peoplenumber;

    private List<Map<Object, Object>> familyMap;

    private FamilyGenealogy familyGenealogy;

    private boolean show;

}
