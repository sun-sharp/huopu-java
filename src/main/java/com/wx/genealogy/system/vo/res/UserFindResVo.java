package com.wx.genealogy.system.vo.res;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName UserFindResVo
 * @Author weixin
 * @Data 2021/10/14 14:57
 * @Description
 * @Version 1.0
 **/
@Data
public class UserFindResVo {
    private Integer id;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 姓名
     */
    private String nickName;

    private String realName;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
}
