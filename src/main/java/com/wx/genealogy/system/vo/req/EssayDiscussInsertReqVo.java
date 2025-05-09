package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName EssayDiscussInsertReqVo
 * @Author weixin
 * @Data 2021/10/18 10:35
 * @Description
 * @Version 1.0
 **/
@Data
public class EssayDiscussInsertReqVo {

    /**
     * 文章id
     */
    private Integer essayId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 文章评论id(二级评论需要)
     */
    private Integer essayDiscussId;

    /**
     * 等级：1一级评论2二级评论
     */
    private Integer level;

    /**
     * 评论内容
     */
    private String content;
}
