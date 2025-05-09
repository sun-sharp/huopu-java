package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName SysPermissionInsertReqVo
 * @Author hangyi
 * @Data 2020/6/17 16:17
 * @Description 添加权限
 * @Version 1.0
 **/
@Data
public class SysPermissionInsertReqVo {

    private String permission;

    private String name;
}
