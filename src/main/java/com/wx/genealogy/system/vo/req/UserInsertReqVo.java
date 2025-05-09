package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName UserInsertReqVo
 * @Author weixin
 * @Data 2021/9/7 11:02
 * @Description
 * @Version 1.0
 **/
@Data
public class UserInsertReqVo {

    private String openid;

    private String avatar;

    private String nickName;
}
