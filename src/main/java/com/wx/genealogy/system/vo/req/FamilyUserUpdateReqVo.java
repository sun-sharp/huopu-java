package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName FamilyUserUpdateReqVo
 * @Author weixin
 * @Data 2021/9/7 16:57
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyUserUpdateReqVo {
    private Integer id;

    private Integer level;

    private Integer generation;

    private String genealogyName;

    private Integer relation;

    private Integer sex;

    private Integer status;

    private String remarks;

    private Integer messageNumber;

    private FamilyManageLogInsertReqVo familyManageLogInsertReqVo;

}
