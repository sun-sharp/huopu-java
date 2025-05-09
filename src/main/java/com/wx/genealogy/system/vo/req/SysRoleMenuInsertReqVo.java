package com.wx.genealogy.system.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @ClassName SysRoleMenuInsertReqVo
 * @Author hangyi
 * @Data 2020/6/19 17:15
 * @Description
 * @Version 1.0
 **/
@Data
public class SysRoleMenuInsertReqVo {

    private Integer roleId;

    private List<Integer> menuIds;

}
