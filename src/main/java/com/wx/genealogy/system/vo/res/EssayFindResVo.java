package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.Essay;
import com.wx.genealogy.system.entity.EssayImg;
import com.wx.genealogy.system.entity.EssaySupport;
import lombok.Data;

import java.util.List;

/**
 * @ClassName EssayFindResVo
 * @Author weixin
 * @Data 2021/10/14 16:29
 * @Description
 * @Version 1.0
 **/
@Data
public class EssayFindResVo {

    private Essay essay;

    private List<EssayImg> essayImgList;

    private UserFindResVo userFindResVo;

    private EssaySupport essaySupport;
}
