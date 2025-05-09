package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName FamilyGenealogyInsertReqVo
 * @Author weixin
 * @Data 2021/10/21 14:38
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyGenealogyInsertReqVo {

    private Integer familyId;

    private Integer familyUserId;

    private Integer userId;

    /**
     * 和家族关系1直亲2婚姻3表亲
     */
    private Integer relation;

    /**
     * 身份：1：直系，2：婚姻
     */
    private Integer identity;

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


    private Integer parentId;

    private Integer isAlive;

    private Integer ranking;
    /** 0妻子/1丈夫*/

    private Integer type;
    /** 生日 */

    private String birthday;
    /** 忌日 */

    private String mourningDay;
}
