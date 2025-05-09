package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName FamilyInsertReqVo
 * @Author weixin
 * @Data 2021/9/7 15:52
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyInsertReqVo {

    private Integer userId;

    private String logo;

    private String name;
    
    /**
     * 家谱名
     */
    private String genealogyName;

    /**
     * 和家族关系1直亲2婚姻3表亲
     */
    private Integer relation;
    private String puname;
    private String hunname;
}
