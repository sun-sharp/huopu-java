package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName UnlockArticleUpdateReqVo
 * @Author weixin
 * @Data 2021/11/10 15:37
 * @Description
 * @Version 1.0
 **/
@Data
public class UnlockArticleUpdateReqVo {

    private Integer id;

    private Integer userId;

    private Integer multiple;
}
