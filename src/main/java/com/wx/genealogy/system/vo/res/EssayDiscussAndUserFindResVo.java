package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.EssayDiscuss;
import lombok.Data;

import java.util.List;

/**
 * @ClassName EssayDiscussAndUserFindResVo
 * @Author weixin
 * @Data 2021/10/18 11:10
 * @Description
 * @Version 1.0
 **/
@Data
public class EssayDiscussAndUserFindResVo {

    private EssayDiscuss essayDiscuss;

    private UserFindResVo userFindResVo;
}
