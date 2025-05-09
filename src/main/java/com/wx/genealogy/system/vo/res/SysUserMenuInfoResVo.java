package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.SysRole;
import lombok.Data;

/**
 * @ClassName SysUserMenuInfoResVo
 * @Author hangyi
 * @Data 2020/6/12 17:34
 * @Description 用户菜单信息
 * @Version 1.0
 **/
@Data
public class SysUserMenuInfoResVo {

    private String nick_name;

    private SysRole sysRole;

    private String[] permissions;
}
