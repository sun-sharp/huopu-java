package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.Video;
import com.wx.genealogy.system.entity.VideoImg;
import com.wx.genealogy.system.entity.VideoSupport;
import lombok.Data;

import java.util.List;

/**
 * @ClassName VideoFindResVo
 * @Author weixin
 * @Data 2021/10/14 16:29
 * @Description
 * @Version 1.0
 **/
@Data
public class VideoFindResVo {

    private Video video;

    private List<VideoImg> videoImgList;

    private UserFindResVo userFindResVo;

    private VideoSupport videoSupport;
}
