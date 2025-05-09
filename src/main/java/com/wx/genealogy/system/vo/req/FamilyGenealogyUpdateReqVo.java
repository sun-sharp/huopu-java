package com.wx.genealogy.system.vo.req;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @ClassName FamilyGenealogyUpdateReqVo
 * @Author weixin
 * @Data 2021/10/21 14:57
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyGenealogyUpdateReqVo {

    private Integer id;

    /**
     * 家族成员id
     */
    private Integer familyUserId;

    /**
     * 用户id
     */
    private Integer userId;

    private Integer fatherId;

    /**
     * 和家族关系1直亲2婚姻3表亲
     */
    private Integer relation;

    /**
     * 第几代
     */
    private Integer generation;

    /**
     * 家谱名
     */
    private String genealogyName;

    /**
     * 性别1男2女
     */
    private Integer sex;

    private String introduce;

    private Integer status;





}
