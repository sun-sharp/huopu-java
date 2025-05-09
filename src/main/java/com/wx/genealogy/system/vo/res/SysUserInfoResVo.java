package com.wx.genealogy.system.vo.res;

import lombok.Data;

/**
 * @ClassName SysUserInfoResVo
 * @Author hangyi
 * @Data 2020/6/17 15:30
 * @Description 用户详细信息
 * @Version 1.0
 **/
@Data
public class SysUserInfoResVo {

    private Integer id;

    private String userName;

    private String headImgUrl;

    private String nickName;

    private Integer status;

    private String roleName;

    /**
     * 性别（0：男 1：女）
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 联系方式
     */
    private String contactInformation;
}
