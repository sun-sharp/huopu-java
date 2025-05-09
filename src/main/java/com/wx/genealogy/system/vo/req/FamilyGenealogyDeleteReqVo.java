package com.wx.genealogy.system.vo.req;

import lombok.Data;

import java.util.List;

@Data
public class FamilyGenealogyDeleteReqVo {

    private Integer familyId;
    private List<Integer> generation;
    private Integer userId;
    private Integer familyUserId;
}
