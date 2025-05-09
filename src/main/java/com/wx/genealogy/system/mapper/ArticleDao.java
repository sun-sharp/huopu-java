package com.wx.genealogy.system.mapper;

import com.wx.genealogy.system.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-09-09
 */

@Mapper

public interface ArticleDao extends BaseMapper<Article> {

    int setInc(Article article);

    int setDec(Article article);
}
