package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName EssaySupportUpdateReqVo
 * @Author weixin
 * @Data 2021/10/15 15:04
 * @Description
 * @Version 1.0
 **/
@Data
public class EssaySupportUpdateReqVo {
    private Integer id;

    /**
     * 文章id
     */
    private Integer essayId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 点赞状态0取消1确实
     */
    private Integer status;
}
