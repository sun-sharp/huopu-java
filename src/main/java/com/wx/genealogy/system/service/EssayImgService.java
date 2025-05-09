package com.wx.genealogy.system.service;

import com.wx.genealogy.system.entity.EssayImg;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 帖子图片表 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-10-12
 */
@Transactional(rollbackFor = Exception.class)
public interface EssayImgService extends IService<EssayImg> {

}
