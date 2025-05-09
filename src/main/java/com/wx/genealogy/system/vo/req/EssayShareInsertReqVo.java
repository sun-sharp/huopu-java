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
public class EssayShareInsertReqVo {

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
    private Integer essayId;


}
