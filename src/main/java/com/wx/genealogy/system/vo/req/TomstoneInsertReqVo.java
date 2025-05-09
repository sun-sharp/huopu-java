package com.wx.genealogy.system.vo.req;

import lombok.Data;

import java.util.Date;

@Data
public class TomstoneInsertReqVo {
    /**
     * 用户id
     */
    private String userName;

    /**
     * 家族id
     */
    private Integer familyId;
    private String picture;

    private Date CreateTime;
    private String content;

}
