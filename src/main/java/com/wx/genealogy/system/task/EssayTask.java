package com.wx.genealogy.system.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wx.genealogy.system.entity.Essay;
import com.wx.genealogy.system.entity.EssayImg;
import com.wx.genealogy.system.mapper.EssayDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName EssayTask
 * @Author weixin
 * @Data 2021/11/11 16:42
 * @Description
 * @Version 1.0
 **/
@Component
public class EssayTask {

    @Autowired
    private EssayDao essayDao;

    //每10分钟一次
    @Scheduled(cron = "0 */10 * * * ?")
    private void deleteEssay()  throws Exception{
        System.out.println("删除过期的文章");
        long nowTime = System.currentTimeMillis()/1000;//当前时间戳

        //自动删除未解锁且365天后的帖子
        QueryWrapper<Essay> essayDelete = new QueryWrapper<Essay>();
        essayDelete.eq("is_knit",1);
        essayDelete.le("auto_remove_time",nowTime);
        essayDao.delete(essayDelete);

        //开始对满足条件的未加锁帖子进行加锁
        QueryWrapper<Essay> essayUpdate = new QueryWrapper<Essay>();
        essayUpdate.eq("is_knit",0);
        essayUpdate.le("knit_end_time",nowTime);
        Essay essay = new Essay();
        essay.setKnit(1);//加锁
        essay.setAutoRemoveTime(nowTime+2592000);//自动删除时间（测试时用2天，上线365天）
        essayDao.update(essay,essayUpdate);

    }
}
