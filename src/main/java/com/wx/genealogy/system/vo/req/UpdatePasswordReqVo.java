package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName UpdatePasswordReqVo
 * @Author hangyi
 * @Data 2020/6/17 11:43
 * @Description
 * @Version 1.0
 **/
@Data
public class UpdatePasswordReqVo {

    private String userName;

    private String newPassword;

    private String oldPassword;
}
