package com.wx.genealogy.system.vo.res;

import com.wx.genealogy.system.entity.ArticleDiscuss;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ArticleDiscussAndUserFindResVo
 * @Author weixin
 * @Data 2021/10/18 11:10
 * @Description
 * @Version 1.0
 **/
@Data
public class ArticleDiscussAndUserFindResVo {

    private ArticleDiscuss articleDiscuss;

    private UserFindResVo userFindResVo;
}
