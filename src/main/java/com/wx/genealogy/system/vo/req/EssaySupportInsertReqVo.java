package com.wx.genealogy.system.vo.req;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName EssaySupportInsertReqVo
 * @Author weixin
 * @Data 2021/10/15 14:38
 * @Description
 * @Version 1.0
 **/
@Data
public class EssaySupportInsertReqVo {

    /**
     * 文章id
     */
    private Integer essayId;

    /**
     * 用户id
     */
    private Integer userId;
}
