package com.wx.genealogy.system.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @ClassName SysRolePermissionInsertReqVo
 * @Author hangyi
 * @Data 2020/6/19 17:17
 * @Description
 * @Version 1.0
 **/
@Data
public class SysRolePermissionInsertReqVo {

    private Integer roleId;

    private List<Integer> permissionIds;

}
