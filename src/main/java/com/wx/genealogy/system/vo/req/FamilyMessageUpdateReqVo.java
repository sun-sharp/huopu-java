package com.wx.genealogy.system.vo.req;

import lombok.Data;

@Data
public class FamilyMessageUpdateReqVo {
    private Integer userId;

    private Integer familyId;

    private Integer essayMessage;

    private Integer articleMessage;

    private Integer videoMessage;

    private Integer familyuserMessage;
    private Integer familyshenMessage;
    private Integer familyshenMessage2;
    private Integer familyshenMessage3;
    private Integer familymanageMessage;

    private Integer familylogMessage;

}
