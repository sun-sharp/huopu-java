package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName SysUserInsertReqVo
 * @Author hangyi
 * @Data 2020/6/16 17:52
 * @Description 添加系统用户类
 * @Version 1.0
 **/
@Data
public class SysUserInsertReqVo {

    private String userName;

    private String nickName;

    private Integer roleId;

    private String password;
}
