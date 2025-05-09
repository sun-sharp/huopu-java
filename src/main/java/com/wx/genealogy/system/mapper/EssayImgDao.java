package com.wx.genealogy.system.mapper;

import com.wx.genealogy.system.entity.EssayImg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 帖子图片表 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2021-10-12
 */
@Mapper
public interface EssayImgDao extends BaseMapper<EssayImg> {

    int insertEssayImgList(List<EssayImg> essayImgList);
}
