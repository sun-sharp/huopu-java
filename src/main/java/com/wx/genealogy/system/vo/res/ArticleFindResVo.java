package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.Article;
import com.wx.genealogy.system.entity.ArticleImg;
import com.wx.genealogy.system.entity.ArticleSupport;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ArticleFindResVo
 * @Author weixin
 * @Data 2021/10/14 16:29
 * @Description
 * @Version 1.0
 **/
@Data
public class ArticleFindResVo {

    private Article article;

    private List<ArticleImg> articleImgList;

    private UserFindResVo userFindResVo;

    private ArticleSupport articleSupport;
}
