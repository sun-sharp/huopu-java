package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName EssayUpdateReqVo
 * @Author weixin
 * @Data 2021/10/15 10:16
 * @Description
 * @Version 1.0
 **/
@Data
public class EssayUpdateReqVo {
    private Integer id;

    private Integer userId;
    /**
     * 浏览量
     */
    private Integer browseNumber;

    private Integer lock;
}
