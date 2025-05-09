package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName FamilyUserInsertReqVo
 * @Author weixin
 * @Data 2021/9/7 16:53
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyUserInsertReqVo {

    private Integer familyId;

    private Integer userId;

    private  Integer generation;

    private String genealogyName;

    private Integer relation;

    private Integer sex;

    private String introduce;

}
