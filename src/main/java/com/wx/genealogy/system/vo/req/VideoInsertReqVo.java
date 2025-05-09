package com.wx.genealogy.system.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @ClassName EssayInsertReqVo
 * @Author weixin
 * @Data 2021/9/9 16:20
 * @Description
 * @Version 1.0
 **/
@Data
public class VideoInsertReqVo {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 家族id
     */
    private Integer familyId;

    /**
     * 内容
     */
    private String content;

    private String title;
    /**
     * 开放1保密2家族内公开3完全公开
     */
    private Integer open;

    private List<VideoImgInsertReqVo> videoImgInsertReqVoList;
}
