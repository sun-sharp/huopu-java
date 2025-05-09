package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName SysUserUpdateReqVo
 * @Author hangyi
 * @Data 2020/6/17 10:10
 * @Description
 * @Version 1.0
 **/
@Data
public class SysUserUpdateReqVo {

    private Integer id;

    private String nickName;

    private String headImgUrl;

    private Integer roleId;
}
