package com.wx.genealogy.system.vo.req;

import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
public class TomstoneEssayReqVo {
    private  Integer tombstoneId;
    private  Integer userId;
    private Integer essayId;
}
