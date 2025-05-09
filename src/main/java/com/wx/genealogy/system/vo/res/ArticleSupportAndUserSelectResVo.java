package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.ArticleSupport;
import com.wx.genealogy.system.entity.User;
import lombok.Data;

/**
 * @ClassName ArticleSupportAndUserSelectRes
 * @Author weixin
 * @Data 2021/10/15 15:29
 * @Description
 * @Version 1.0
 **/
@Data
public class ArticleSupportAndUserSelectResVo {
    private ArticleSupport articleSupport;
    private User user;
}
