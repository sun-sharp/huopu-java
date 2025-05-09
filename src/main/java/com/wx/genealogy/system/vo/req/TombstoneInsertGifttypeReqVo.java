package com.wx.genealogy.system.vo.req;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class TombstoneInsertGifttypeReqVo {
    private  String name;
    private  String picture;
    private  String message;
    private  Integer tombstoneId;

}
