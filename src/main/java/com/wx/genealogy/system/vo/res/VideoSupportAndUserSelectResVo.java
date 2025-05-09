package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.VideoSupport;
import com.wx.genealogy.system.entity.User;
import lombok.Data;

/**
 * @ClassName VideoSupportAndUserSelectRes
 * @Author weixin
 * @Data 2021/10/15 15:29
 * @Description
 * @Version 1.0
 **/
@Data
public class VideoSupportAndUserSelectResVo {
    private VideoSupport videoSupport;
    private User user;
}
