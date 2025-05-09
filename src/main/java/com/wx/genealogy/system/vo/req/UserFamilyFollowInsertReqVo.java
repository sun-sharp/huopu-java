package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName UserFamilyFollowInsertReqVo
 * @Author weixin
 * @Data 2021/9/7 16:21
 * @Description
 * @Version 1.0
 **/
@Data
public class UserFamilyFollowInsertReqVo {

    private  Integer userId;

    private  Integer familyId;
}
