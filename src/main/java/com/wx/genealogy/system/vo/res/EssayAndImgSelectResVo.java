package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.*;
import lombok.Data;

import java.util.List;

/**
 * @ClassName EssayAndImgSelectResVo
 * @Author weixin
 * @Data 2021/10/13 17:48
 * @Description
 * @Version 1.0
 **/
@Data
public class EssayAndImgSelectResVo {
    private Essay essay;

    private UserFindResVo userFindResVo;

    private Family family;

    private List<EssayImg> essayImgList;

    private EssaySupport essaySupport;
}
