package com.wx.genealogy.system.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @ClassName FamilyGenealogyUpdateReqVo
 * @Author weixin
 * @Data 2021/10/21 14:57
 * @Description
 * @Version 1.0
 **/
@Data
public class FamilyGenealogyUpdateResumeReqVo {

    private Integer id;

    private String introduction;//简介

    private String tags;//标签

    private String resume;//简历

    private List<EssayImgInsertReqVo> essayImgInsertReqVoList;

}
