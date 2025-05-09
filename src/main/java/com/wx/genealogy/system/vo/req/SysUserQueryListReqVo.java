package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName SysUserQueryListReqVo
 * @Author hangyi
 * @Data 2020/6/17 15:02
 * @Description 条件查询用户列表
 * @Version 1.0
 **/
@Data
public class SysUserQueryListReqVo {

    private String userName;

    private Integer status;

    private Integer roleId;

    private Integer limit;

    private Integer page;
}
