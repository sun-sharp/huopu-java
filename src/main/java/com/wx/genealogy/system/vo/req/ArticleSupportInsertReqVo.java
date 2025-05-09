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
public class ArticleSupportInsertReqVo {

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 用户id
     */
    private Integer userId;
}
