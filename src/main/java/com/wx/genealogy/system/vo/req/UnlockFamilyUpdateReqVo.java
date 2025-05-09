package com.wx.genealogy.system.vo.req;

import lombok.Data;

/**
 * @ClassName UnlockEssayUpdateReqVo
 * @Author weixin
 * @Data 2021/11/10 15:37
 * @Description
 * @Version 1.0
 **/
@Data
public class UnlockFamilyUpdateReqVo {
    private Integer userId;
    private Integer familyId;
    private Integer multiple;
    private Integer mi;
}
