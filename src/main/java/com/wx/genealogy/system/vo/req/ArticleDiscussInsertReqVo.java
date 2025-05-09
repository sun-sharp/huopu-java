package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName ArticleDiscussInsertReqVo
 * @Author weixin
 * @Data 2021/10/18 10:35
 * @Description
 * @Version 1.0
 **/
@Data
public class ArticleDiscussInsertReqVo {

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 文章评论id(二级评论需要)
     */
    private Integer articleDiscussId;

    /**
     * 等级：1一级评论2二级评论
     */
    private Integer level;

    /**
     * 评论内容
     */
    private String content;
}
