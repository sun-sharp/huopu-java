package com.wx.genealogy.system.vo.req;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class TomstoneInsertMessage {
    private Integer tombstoneId;
    private Integer userId;
    private String message;

}
