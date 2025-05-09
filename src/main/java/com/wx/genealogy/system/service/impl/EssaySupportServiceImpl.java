package com.wx.genealogy.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.Essay;
import com.wx.genealogy.system.entity.EssaySupport;
import com.wx.genealogy.system.entity.RiceRecord;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.mapper.EssayDao;
import com.wx.genealogy.system.mapper.EssaySupportDao;
import com.wx.genealogy.system.mapper.RiceRecordDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.service.EssaySupportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.system.vo.res.EssaySupportAndUserSelectResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-10-15
 */
@Service
public class EssaySupportServiceImpl extends ServiceImpl<EssaySupportDao, EssaySupport> implements EssaySupportService {

    @Autowired
    private EssaySupportDao essaySupportDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EssayDao essayDao;

    @Autowired
    private RiceRecordDao riceRecordDao;

    @Override
    public JsonResult insertEssaySupport(EssaySupport essaySupport) throws Exception {
        //先查询是否存在
        QueryWrapper<EssaySupport> essaySupportFind = new QueryWrapper<EssaySupport>();
        essaySupportFind.eq("essay_id",essaySupport.getEssayId());
        essaySupportFind.eq("user_id",essaySupport.getUserId());
        EssaySupport essaySupportData = essaySupportDao.selectOne(essaySupportFind);

        //再查询文章是否存在
        Essay essay = essayDao.selectById(essaySupport.getEssayId());
        if(essay==null){
            return ResponseUtil.fail("文章不存在");
        }

        long nowTime = System.currentTimeMillis()/1000;

        //不存在
        if(essaySupportData==null){
            int result=essaySupportDao.insert(essaySupport);
            if(result==0){
                throw new Exception("点赞失败");
            }
            Essay essayUpdate = new Essay();
            essayUpdate.setId(essay.getId());
            essayUpdate.setPraiseNumber(essay.getPraiseNumber()+1);
            essayUpdate.setKnitStartTime(nowTime);
            essayUpdate.setKnitEndTime(nowTime+essay.getKnitCycle());
            result = essayDao.updateById(essayUpdate);
            if(result==0){
                throw new Exception("点赞失败");
            }

            //如果不存在说明从来没点赞过，这种情况才下发米
            //分别给作者和点赞人下发1米
            User user1 = new User();
            user1.setId(essay.getUserId());
            user1.setRice(1);
            result = userDao.setInc(user1);
            if(result==0){
                throw new Exception("点赞失败");
            }

            User user2 = new User();
            user2.setId(essaySupport.getUserId());
            user2.setRice(1);
            result = userDao.setInc(user2);
            if(result==0){
                throw new Exception("点赞失败");
            }

            List<RiceRecord> riceRecordList = new ArrayList<RiceRecord>();
            RiceRecord riceRecord1 = new RiceRecord();
            riceRecord1.setUserId(essay.getUserId());
            riceRecord1.setRice(1);
            riceRecord1.setContent("收到点赞");
            riceRecord1.setCreateTime(essaySupport.getCreateTime());
            riceRecordList.add(riceRecord1);
            RiceRecord riceRecord2 = new RiceRecord();
            riceRecord2.setUserId(essaySupport.getUserId());
            riceRecord2.setRice(1);
            riceRecord2.setContent("点赞文章");
            riceRecord2.setCreateTime(essaySupport.getCreateTime());
            riceRecordList.add(riceRecord2);

            result = riceRecordDao.insertRiceRecordList(riceRecordList);
            if(result==0){
                throw new Exception("点赞失败");
            }


            return ResponseUtil.ok("点赞成功");
        }
        //已经存在
        essaySupportData.setStatus(1);
        essaySupportData.setCreateTime(essaySupport.getCreateTime());
        int result=essaySupportDao.updateById(essaySupportData);
        if(result==0){
            throw new Exception("点赞失败");
        }

        Essay essayUpdate = new Essay();
        essayUpdate.setId(essay.getId());
        essayUpdate.setPraiseNumber(essay.getPraiseNumber()+1);
        essayUpdate.setKnitStartTime(nowTime);
        essayUpdate.setKnitEndTime(nowTime+essay.getKnitCycle());
        result = essayDao.updateById(essayUpdate);
        if(result==0){
            throw new Exception("点赞失败");
        }

        return ResponseUtil.ok("点赞成功");

    }

    @Override
    public JsonResult updateEssaySupportById(EssaySupport essaySupport) throws Exception {
        int result=essaySupportDao.updateById(essaySupport);
        if(result==0){
            throw new Exception("修改失败");
        }

        Essay essay = new Essay();
        essay.setId(essaySupport.getEssayId());
        if(essaySupport.getStatus()==0){
            essay.setPraiseNumber(1);
            result = essayDao.setDec(essay);
            if(result==0){
                throw new Exception("点赞失败");
            }
        }else{
            essay.setPraiseNumber(1);
            result = essayDao.setInc(essay);
            if(result==0){
                throw new Exception("点赞失败");
            }
        }


        return ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult selectEssaySupportAndUser(Integer page, Integer limit, EssaySupport essaySupport) {
        IPage<EssaySupport> pageData =new Page<>(page, limit);
        QueryWrapper<EssaySupport> essaySupportSelect = new QueryWrapper<EssaySupport>();
        essaySupportSelect.eq("essay_id",essaySupport.getEssayId());
        essaySupportSelect.eq("is_status",essaySupport.getStatus());

        pageData=essaySupportDao.selectPage(pageData, essaySupportSelect);
        List<EssaySupport> essaySupportList = pageData.getRecords();

        //准备
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("pages", pageData.getPages());
        map.put("total", pageData.getTotal());

        //无数据
        if(essaySupportList==null||essaySupportList.size()==0){
            map.put("essaySupportAndUserSelectResVoList", null);
            return ResponseUtil.ok("获取成功",map);
        }

        //有数据，开始准备in查询
        TreeSet<Integer> userIdList = new TreeSet<Integer>();
        for(int i=0;i<essaySupportList.size();i++){
            userIdList.add(essaySupportList.get(i).getUserId());
        }
        List<User> userList=userDao.selectBatchIds(userIdList);

        List<EssaySupportAndUserSelectResVo> essaySupportAndUserSelectResVoList = new ArrayList<EssaySupportAndUserSelectResVo>();

        for(int i=0;i<essaySupportList.size();i++){
            EssaySupportAndUserSelectResVo essaySupportAndUserSelectResVo = new EssaySupportAndUserSelectResVo();
            for(int j=0;j<userList.size();j++){
                if(essaySupportList.get(i).getUserId().equals(userList.get(j).getId())){
                    essaySupportAndUserSelectResVo.setEssaySupport(essaySupportList.get(i));
                    essaySupportAndUserSelectResVo.setUser(userList.get(j));
                    break;
                }
            }
            essaySupportAndUserSelectResVoList.add(essaySupportAndUserSelectResVo);
        }

        map.put("essaySupportAndUserSelectResVoList", essaySupportAndUserSelectResVoList);
        return ResponseUtil.ok("获取成功",map);
    }

    @Override
    public JsonResult findEssaySupport(EssaySupport essaySupport) {
        QueryWrapper<EssaySupport> essaySupportFind = new QueryWrapper<EssaySupport>();
        essaySupportFind.eq("essay_id",essaySupport.getEssayId());
        essaySupportFind.eq("user_id",essaySupport.getUserId());
        EssaySupport essaySupportData = essaySupportDao.selectOne(essaySupportFind);
        return ResponseUtil.ok("获取成功",essaySupportData);
    }
}
