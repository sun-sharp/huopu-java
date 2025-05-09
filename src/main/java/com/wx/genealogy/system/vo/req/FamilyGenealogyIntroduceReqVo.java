package com.wx.genealogy.system.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @Author lx
 * @Data 2024/10/8
 * @Description 用户简介
 * @Version 1.0
 **/
@Data
public class FamilyGenealogyIntroduceReqVo {

    private Integer id;

    private String introduction;//简介

    private String tags;//标签

    private String resume;//简历

    private List<EssayImgInsertReqVo> essayImgInsertReqVoList;

}
