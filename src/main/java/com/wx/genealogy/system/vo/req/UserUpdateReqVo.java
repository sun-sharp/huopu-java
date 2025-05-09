package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName UserUpdateReqVo
 * @Author weixin
 * @Data 2021/9/7 11:19
 * @Description
 * @Version 1.0
 **/
@Data
public class UserUpdateReqVo {

    private Integer userId;

    private String avatar;

    private String realName;
    private String secondName;

    private Integer sex;
}
