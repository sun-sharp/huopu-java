package com.wx.genealogy.system.vo.res;

import lombok.Data;

import java.util.List;

/**
 * @ClassName EssayDiscussAndUserSelectResVo
 * @Author weixin
 * @Data 2021/10/18 11:24
 * @Description
 * @Version 1.0
 **/
@Data
public class EssayDiscussAndUserSelectResVo {

   private  EssayDiscussAndUserFindResVo essayDiscussAndUserFindResVo;

   private List<EssayDiscussAndUserFindResVo> essayDiscussAndUserFindResVoList;
}
