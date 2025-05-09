package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName FamilyUpdateReqVo
 * @Author weixin
 * @Data 2021/9/7 15:56
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyUpdateReqVo {
    private Integer id;

    private String logo;

    private String name;

    private String mark;
    private String puname;
    private String hunname;
    private String audio;
}
