package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName FamilyManageLogInsertReqVo
 * @Author weixin
 * @Data 2021/10/12 14:49
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyManageLogInsertReqVo {

    private Integer familyId;

    /**
     * 家族成员id
     */
    private Integer familyUserId;

    private Integer userId;

    /**
     * 行为
     */
    private String action;

    /**
     * 内容
     */
    private String content;

    //是否记录到数据库
    private Integer record;
}
