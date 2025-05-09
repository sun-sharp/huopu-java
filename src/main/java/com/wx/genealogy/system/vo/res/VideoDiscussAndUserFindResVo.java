package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.VideoDiscuss;
import lombok.Data;

import java.util.List;

/**
 * @ClassName VideoDiscussAndUserFindResVo
 * @Author weixin
 * @Data 2021/10/18 11:10
 * @Description
 * @Version 1.0
 **/
@Data
public class VideoDiscussAndUserFindResVo {

    private VideoDiscuss videoDiscuss;

    private UserFindResVo userFindResVo;
}
