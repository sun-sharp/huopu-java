package com.wx.genealogy.system.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.exception.ServiceException;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ExcelUtils.ExcelUtils;
import com.wx.genealogy.common.util.JavaMailUntil;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.convert.FamilyGenealogyConvert;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.mapper.FamilyDownloadDao;
import com.wx.genealogy.system.mapper.FamilyGenealogyDao;
import com.wx.genealogy.system.mapper.FamilyUserDao;
import com.wx.genealogy.system.mapper.UserDao;
import com.wx.genealogy.system.mapper.RiceRecordDao;
import com.wx.genealogy.system.service.*;
import com.wx.genealogy.system.vo.req.*;
import com.wx.genealogy.system.vo.res.FamilyGenealogyTreeResVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 家谱图 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-10-20
 */
@Service
@Slf4j
public class FamilyGenealogyServiceImpl extends ServiceImpl<FamilyGenealogyDao, FamilyGenealogy> implements FamilyGenealogyService {

    @Autowired
    private UserDao userDao;

    @Autowired
    FamilyDownloadDao familyDownloadDao;

    @Autowired
    private FamilyGenealogyDao familyGenealogyDao;

    @Autowired
    private RiceRecordDao riceRecordDao;

    @Autowired
    private FamilyGenealogyReceiveService familyGenealogyReceiveService;

    @Autowired
    private FamilyUserDao familyUserDao;

    @Autowired
    private FamilyGenealogyService familyGenealogyService;

    @Resource
    private UserService userService;

    @Resource
    private FamilyService familyService;

    @Resource
    private FamilyGenealogyImgService familyGenealogyImgService;

    @Resource
    private FamilyGenealogyEditApplyService familyGenealogyEditApplyService;

    public JsonResult insertData(FamilyGenealogy familyGenealogy) {

        if (familyGenealogy.getGenealogyName() == null) {
            throw new ServiceException("家谱名不能为空");
        }
        if (familyGenealogy.getSex() == null) {
            throw new ServiceException("性别不能为空");
        }
        if (familyGenealogy.getGeneration() == null) {
            throw new ServiceException("代数不能为空");
        }
        if (familyGenealogy.getIsAlive() == null) {
            throw new ServiceException("是否在世不能为空");
        }
        if (familyGenealogy.getIdentity() == null) {
            throw new ServiceException("身份不能为空");
        }
        if (familyGenealogy.getRelation() == null) {
            throw new ServiceException("关系不能为空");
        }
        if (familyGenealogy.getFamilyId() == null) {
            throw new ServiceException("家族不能为空");
        }
        if (familyGenealogyDao.selectNameGenerationList(familyGenealogy) > 0) {
            throw new ServiceException("家谱名重复");
        }

        int uid = 1;
        if (familyGenealogyDao.selectUidMax(familyGenealogy.getFamilyId()) != null) {
            uid = familyGenealogyDao.selectUidMax(familyGenealogy.getFamilyId()) + 1;
        }
        System.out.println("当前uid:" + uid);
        familyGenealogy.setUid(uid);
        System.out.println(familyGenealogy);
        familyGenealogyDao.insert(familyGenealogy);
//        this.upFamilyGenealogyChart(familyGenealogy.getFamilyId());
//        this.upFamilyGenealogyChartByIdOne(familyGenealogy.getId(),);

        return ResponseUtil.ok("创建成功");
    }

    @Override
    public JsonResult insertFamilyGenealogy(FamilyGenealogy familyGenealogy) throws Exception {

        int result;
        if (familyGenealogy.getUserId().equals(-1)) {
            //先查询是否家族名重复
            QueryWrapper<FamilyUser> familyUserFind = new QueryWrapper<FamilyUser>();
            familyUserFind.eq("family_id", familyGenealogy.getFamilyId());
            familyUserFind.eq("genealogy_name", familyGenealogy.getGenealogyName());
            FamilyUser familyUserData = familyUserDao.selectOne(familyUserFind);
            if (familyUserData != null) {
                throw new Exception("家谱名重复");
            }

            FamilyUser familyUserInsert = new FamilyUser();
            familyUserInsert.setFamilyId(familyGenealogy.getFamilyId());
            familyUserInsert.setUserId(0);
            familyUserInsert.setGeneration(familyGenealogy.getGeneration());
            familyUserInsert.setGenealogyName(familyGenealogy.getGenealogyName());
            familyUserInsert.setRelation(familyGenealogy.getRelation());
            familyUserInsert.setSex(familyGenealogy.getSex());
            familyUserInsert.setIntroduce("用户录入");
            familyUserInsert.setLevel(3);
            familyUserInsert.setStatus(2);
            familyUserInsert.setCreateTime(DateUtils.getDateTime());
            result = familyUserDao.insert(familyUserInsert);
            if (result == 0) {
                throw new Exception("创建失败");
            }
            familyGenealogy.setUserId(0);
        }

        //先查询是否自己的家谱图有被占用，如果占用的代数不覆盖，没有则补齐
        FamilyGenealogy familyGenealogyData = familyGenealogyDao.selectOne(new QueryWrapper<FamilyGenealogy>().eq("family_id", familyGenealogy.getFamilyId()).eq("family_user_id", familyGenealogy.getFamilyUserId()).eq("generation", familyGenealogy.getGeneration()).eq("identity", familyGenealogy.getIdentity()));

        if (familyGenealogyData != null) {
            throw new Exception("此位置已分配成员");
        }
        result = familyGenealogyDao.insert(familyGenealogy);
        if (result == 0) {
            throw new Exception("创建失败");
        }

        //判断家族中其他家谱图是否有相同代数的相同成员
        if (familyGenealogy.getIdentity().equals(1)) {
            List<FamilyGenealogy> otherList = familyGenealogyDao.selectList(new QueryWrapper<FamilyGenealogy>().eq("family_id", familyGenealogy.getFamilyId()).ne("family_user_id", familyGenealogy.getFamilyUserId()).eq("generation", familyGenealogy.getGeneration()).eq("identity", familyGenealogy.getIdentity()).eq("user_id", familyGenealogy.getUserId()).eq("genealogy_name", familyGenealogy.getGenealogyName()));
            if (!otherList.isEmpty()) {
                otherList.sort(Comparator.comparing(FamilyGenealogy::getCreateTime));
                FamilyGenealogy familyGenealogyBefore = otherList.get(0);
                for (int i = familyGenealogy.getGeneration(); i >= 1; i--) {
                    //判断当前家谱当前代的家谱位置和婚姻位置是否为null
                    FamilyGenealogy familyGenealogy1 = familyGenealogyDao.selectOne(new QueryWrapper<FamilyGenealogy>().eq("family_id", familyGenealogy.getFamilyId()).eq("family_user_id", familyGenealogy.getFamilyUserId()).eq("generation", i).eq("identity", 1));

                    if (ObjectUtil.isNull(familyGenealogy1)) {
                        //查询旧家谱的当前位置的信息
                        FamilyGenealogy familyGenealogy1Before = familyGenealogyDao.selectOne(new QueryWrapper<FamilyGenealogy>().eq("family_id", familyGenealogy.getFamilyId()).eq("family_user_id", familyGenealogyBefore.getFamilyUserId()).eq("generation", i).eq("identity", 1));
                        if (ObjectUtil.isNotNull(familyGenealogy1Before)) {
                            familyGenealogy1Before.setId(0);
                            familyGenealogy1Before.setFamilyUserId(familyGenealogy.getFamilyUserId());
                            familyGenealogy1Before.setCreateTime(DateUtils.getDateTime());
                            result = familyGenealogyDao.insert(familyGenealogy1Before);
                            if (result == 0) {
                                throw new Exception("创建失败");
                            }
                        }
                    }

                    FamilyGenealogy familyGenealogy2 = familyGenealogyDao.selectOne(new QueryWrapper<FamilyGenealogy>().eq("family_id", familyGenealogy.getFamilyId()).eq("family_user_id", familyGenealogy.getFamilyUserId()).eq("generation", i).eq("identity", 2));
                    if (ObjectUtil.isNull(familyGenealogy2)) {
                        //查询旧家谱的当前位置的信息
                        FamilyGenealogy familyGenealogy2Before = familyGenealogyDao.selectOne(new QueryWrapper<FamilyGenealogy>().eq("family_id", familyGenealogy.getFamilyId()).eq("family_user_id", familyGenealogyBefore.getFamilyUserId()).eq("generation", i).eq("identity", 2));
                        if (ObjectUtil.isNotNull(familyGenealogy2Before)) {
                            familyGenealogy2Before.setId(0);
                            familyGenealogy2Before.setFamilyUserId(familyGenealogy.getFamilyUserId());
                            familyGenealogy2Before.setCreateTime(DateUtils.getDateTime());
                            result = familyGenealogyDao.insert(familyGenealogy2Before);
                            if (result == 0) {
                                throw new Exception("创建失败");
                            }
                        }
                    }
                }
            }
        }
        return ResponseUtil.ok("创建成功");
    }

    @Override
    public JsonResult updateFamilyGenealogyById(FamilyGenealogy familyGenealogy) throws Exception {
        int result = familyGenealogyDao.updateById(familyGenealogy);
        return result == 0 ? ResponseUtil.fail("修改失败") : ResponseUtil.ok("修改成功");
    }

    @Override
    public JsonResult deleteFamilyGenealogyById(FamilyGenealogy familyGenealogy) throws Exception {
        QueryWrapper<FamilyGenealogy> familyGenealogyDelete = new QueryWrapper<FamilyGenealogy>();
        familyGenealogyDelete.eq("family_id", familyGenealogy.getFamilyId());
        familyGenealogyDelete.eq("family_user_id", familyGenealogy.getFamilyUserId());
        int result = familyGenealogyDao.delete(familyGenealogyDelete);
        return result == 0 ? ResponseUtil.fail("删除失败") : ResponseUtil.ok("删除成功");
    }

    @Override
    public JsonResult selectFamilyGenealogy(FamilyGenealogy familyGenealogy) {
        QueryWrapper<FamilyGenealogy> familyGenealogySelect = new QueryWrapper<FamilyGenealogy>();
        familyGenealogySelect.eq("family_id", familyGenealogy.getFamilyId());
        if (familyGenealogy.getFamilyUserId() != null) {
            familyGenealogySelect.eq("family_user_id", familyGenealogy.getFamilyUserId());
        }
        if (familyGenealogy.getRelation() != null) {
            familyGenealogySelect.eq("relation", familyGenealogy.getRelation());
        }
        if (familyGenealogy.getGeneration() != null) {
            familyGenealogySelect.eq("generation", familyGenealogy.getGeneration());
        }
        familyGenealogySelect.orderByAsc("generation");
        familyGenealogy.setStatus(1);
        List<FamilyGenealogy> familyGenealogyList = familyGenealogyDao.selectFamilyGenealogyList(familyGenealogy);

        return ResponseUtil.ok("获取成功", familyGenealogyList);
    }


    @Override
    public JsonResult selectAllFamilyGenealogy(FamilyGenealogy familyGenealogy) {
        QueryWrapper<FamilyGenealogy> familyGenealogySelect = new QueryWrapper<FamilyGenealogy>();
        familyGenealogySelect.eq("family_id", familyGenealogy.getFamilyId());
        familyGenealogySelect.eq("family_user_id", familyGenealogy.getFamilyUserId());
        familyGenealogySelect.orderByAsc("generation");
        List<FamilyGenealogy> familyGenealogyList = familyGenealogyDao.selectList(familyGenealogySelect);

        return ResponseUtil.ok("获取成功", familyGenealogyList);
    }


    @Override
    public JsonResult deleteFamilyGenealogyBylevel(FamilyGenealogyDeleteReqVo familyGenealogyDeleteReqVo) throws Exception {
        QueryWrapper<FamilyGenealogy> familyGenealogyDelete = new QueryWrapper<FamilyGenealogy>();

        familyGenealogyDelete.in("generation", familyGenealogyDeleteReqVo.getGeneration());
        familyGenealogyDelete.eq("family_id", familyGenealogyDeleteReqVo.getFamilyId());
        familyGenealogyDelete.eq("family_user_id", familyGenealogyDeleteReqVo.getFamilyUserId());

        List<FamilyGenealogy> data1 = familyGenealogyDao.selectList(familyGenealogyDelete);
        if (data1.size() > 0) {
            for (int i = 0; i < data1.size(); i++) {
                if (data1.get(i).getUserId() != null && data1.get(i).getUserId() > 0) {
                    FamilyUser user = new FamilyUser();
                    user.setGeneration(0);
                    user.setGenealogyName("");
                    user.setId(data1.get(i).getFamilyUserId());
                    familyUserDao.updateById(user);
                }
            }
            int result = familyGenealogyDao.delete(familyGenealogyDelete);
        }
//        QueryWrapper<FamilyUser> familyusers1 = new QueryWrapper<FamilyUser>();
//
//        familyusers1.eq("family_id",familyGenealogyDeleteReqVo.getFamilyId());
//        familyusers1.in("generation",familyGenealogyDeleteReqVo.getGeneration());
//        familyUserDao.delete(familyusers1);


        return ResponseUtil.ok("删除成功");
    }

    @Override
    public JsonResult getFamilyGenealogyBylevel(Integer familyId, Integer familyUserId, Integer familyId1, Integer userId) throws Exception {

        QueryWrapper<FamilyUser> familyUserQueryWrapper = new QueryWrapper<FamilyUser>();
        familyUserQueryWrapper.eq("family_id", familyId1);
        familyUserQueryWrapper.eq("user_id", userId);
        FamilyUser familyUser = familyUserDao.selectOne(familyUserQueryWrapper);
        QueryWrapper<FamilyGenealogy> familyGenealogyDelete = new QueryWrapper<FamilyGenealogy>();

        familyGenealogyDelete.eq("family_id", familyId1);
        familyGenealogyDelete.eq("family_user_id", familyUser.getId());
        familyGenealogyDao.delete(familyGenealogyDelete);

        //修改我的family_user_id 的代数

        FamilyUser y = new FamilyUser();
        y.setId(familyUser.getId());
        y.setGeneration(0);
        familyUserDao.updateById(y);


        //查找要引用的族谱数据
        QueryWrapper<FamilyGenealogy> familyGenealogySelect = new QueryWrapper<FamilyGenealogy>();
        familyGenealogySelect.eq("family_id", familyId);
        familyGenealogySelect.eq("family_user_id", familyUserId);
        familyGenealogySelect.orderByAsc("generation");
        List<FamilyGenealogy> familyGenealogyList = familyGenealogyDao.selectList(familyGenealogySelect);

        for (int i = 0; i < familyGenealogyList.size(); i++) {
            FamilyGenealogy familyGenealogy = new FamilyGenealogy();
            familyGenealogy.setFamilyId(familyId1);
            familyGenealogy.setFamilyUserId(familyUser.getId());
            familyGenealogy.setFamilyLianId(familyGenealogyList.get(i).getFamilyLianId());
            familyGenealogy.setUserId(familyGenealogyList.get(i).getUserId());
            familyGenealogy.setIdentity(familyGenealogyList.get(i).getIdentity());
            familyGenealogy.setSex(familyGenealogyList.get(i).getSex());
            familyGenealogy.setGenealogyName(familyGenealogyList.get(i).getGenealogyName());
            familyGenealogy.setRelation(familyGenealogyList.get(i).getRelation());
            familyGenealogy.setCreateTime(DateUtils.getDateTime());
            familyGenealogy.setGeneration(familyGenealogyList.get(i).getGeneration());
            familyGenealogyDao.insert(familyGenealogy);

        }


        return ResponseUtil.ok("引用成功11111");
    }


    /**
     * 申请加入族谱
     *
     * @param familyGenealogy
     * @return
     */
    @Override
    public JsonResult apply(FamilyGenealogy familyGenealogy) {

        //判断当前审核是不是管理员
        // TODO: 2023/2/17 因为前端每次逗传userId,所有只能这样判断是管理员添加还是普通用户
        Boolean admin = this.isAdmin(familyGenealogy.getUserId(), familyGenealogy.getFamilyId());
        if (admin) {
            familyGenealogy.setUserId(null);
            familyGenealogy.setStatus(1);
        } else {
            familyGenealogy.setStatus(0);
        }

        Family byId = familyService.getById(familyGenealogy.getFamilyId());
        familyGenealogy.setGenealogyName(byId.getName());

        //判断是否已有申请
        LambdaQueryWrapper<FamilyGenealogy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyGenealogy::getFamilyId, familyGenealogy.getFamilyId());
        wrapper.eq(FamilyGenealogy::getApplyName, familyGenealogy.getApplyName());
        if (Objects.nonNull(familyGenealogy.getUserId())) {
            wrapper.eq(FamilyGenealogy::getUserId, familyGenealogy.getUserId());
        }
        wrapper.ne(FamilyGenealogy::getStatus, 2);

        FamilyGenealogy exist = this.familyGenealogyDao.selectOne(wrapper);
        if (Objects.nonNull(exist)) {
            return ResponseUtil.fail("您已申请过无需在申请!!!");
        }


        //判断是否申请过
        QueryWrapper<FamilyGenealogy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", familyGenealogy.getUserId());
        queryWrapper.eq("family_id", familyGenealogy.getFamilyId());
        List familyGenealogies = familyGenealogyDao.selectList(queryWrapper);
        if (familyGenealogies.size() > 0) {
            return ResponseUtil.fail("您已申请过无需在申请!!!");
        }

        familyGenealogy.setIdentity(1);
        familyGenealogy.setCreateTime(new Date());
        this.familyGenealogyDao.insert(familyGenealogy);
        return ResponseUtil.ok("申请成功,请等待审核!!!");
    }


    public JsonResult selectFamilyGenealogyByGeneration(FamilyGenealogy familyGenealogySelect, Integer limit, Integer page) {
        Integer userId = familyGenealogySelect.getUserId();
        //获取这个家族全部人员
        QueryWrapper<FamilyGenealogy> wrapperG = new QueryWrapper<FamilyGenealogy>();
        wrapperG.eq("family_id", familyGenealogySelect.getFamilyId());
        List<FamilyGenealogy> list = this.familyGenealogyDao.selectList(wrapperG);
        //获取当前代数家族全部人员
        QueryWrapper<FamilyGenealogy> allWrapper = new QueryWrapper<FamilyGenealogy>();
        allWrapper.eq("family_id", familyGenealogySelect.getFamilyId());
        allWrapper.orderByDesc("id");
        if (null != familyGenealogySelect.getGeneration()) {
            allWrapper.eq("generation", familyGenealogySelect.getGeneration());
        }
        if (familyGenealogySelect.getGenealogyName() != null) {
            allWrapper.and(w -> w.like("genealogy_name", familyGenealogySelect.getGenealogyName()).or().like("tags", familyGenealogySelect.getGenealogyName()))
            ;
        }
//        IPage<FamilyGenealogy> pageData = new Page<>(Optional.ofNullable(page).orElse(1), Optional.ofNullable(limit).orElse(Integer.MAX_VALUE));
//        IPage<FamilyGenealogy> familyGenealogyIPage = familyGenealogyDao.selectPage(pageData, allWrapper);
        List<FamilyGenealogy> familyList = familyGenealogyDao.selectList(allWrapper);
        List<FamilyGenealogy> updatedFamilyTree = null;
        FamilyGenealogy curFamilyGenealogy = null;
        if (userId != null) {
            //获取用户的族谱信息
            QueryWrapper<FamilyGenealogy> wrapper = new QueryWrapper<FamilyGenealogy>();
            wrapper.eq("user_id", userId);
            wrapper.eq("family_id", familyGenealogySelect.getFamilyId());
            List<FamilyGenealogy> weList = familyGenealogyDao.selectList(wrapper);
            updatedFamilyTree = familyList;
            if (weList.size() <= 0) {
                //没有入谱不计算代差
                for (FamilyGenealogy genealogy : familyList) {
                    genealogy.setGenerationNum(-1);
                    genealogy.setDistance(-1);
                }
            } else {
                curFamilyGenealogy = weList.get(0);
                //入谱计算代差
                int targetUid = weList.get(0).getUid(); // 当前用户的Uid
                int generationA = weList.get(0).getGeneration(); // 列表成员的代数
                //获取当前代数的
//                updatedFamilyTree = getDisparity2(list,familyList,targetUid);
//                updatedFamilyTree = getDisparity2(list,familyList,targetUid);
//                System.out.println("updatedFamilyTree:"+updatedFamilyTree);
            }
        }
        Map<Integer, FamilyGenealogy> map = list.stream().collect(Collectors.toMap(FamilyGenealogy::getUid, v -> v, (p1, p2) -> p1));
        if (updatedFamilyTree != null) {
            for (FamilyGenealogy f : updatedFamilyTree) {
                if (f.getUserId() != null) {
                    User user = userService.selectUserById(f.getUserId());
                    f.setHeadImg(null != user ? user.getAvatar() : "");
                }
                if (null != curFamilyGenealogy) {
                    //                f.setDistance(f.getGeneration());
                    calDistance(curFamilyGenealogy, f, map);
                }
            }
        }
        if (updatedFamilyTree != null) {
            updatedFamilyTree.sort(Comparator.comparing(FamilyGenealogy::getDistance).thenComparing(FamilyGenealogy::getGeneration));
        }
        FamilyGenealogyPageVo familyGenealogyPageVo = new FamilyGenealogyPageVo();
        if (familyGenealogySelect.getGenerationNum() != null) {
            // 使用流过滤数据
            List<FamilyGenealogy> filteredList = null;
            if (updatedFamilyTree != null) {
                filteredList = updatedFamilyTree.stream()
                        .filter(obj -> obj.getDistance() != null && obj.getDistance() >= 0 && obj.getDistance() <= familyGenealogySelect.getGenerationNum())
                        .collect(Collectors.toList());
                familyGenealogyPageVo.setList(paginate(filteredList, page, limit));
                familyGenealogyPageVo.setTotal((long) filteredList.size());
            }
            return ResponseUtil.ok("查询成功9", familyGenealogyPageVo);
        }
        familyGenealogyPageVo.setList(paginate(updatedFamilyTree, page, limit));
        if (updatedFamilyTree != null) {
            familyGenealogyPageVo.setTotal((long) updatedFamilyTree.size());
        } else {
            familyGenealogyPageVo.setTotal(0L);
        }
        return ResponseUtil.ok("查询成功", familyGenealogyPageVo);
    }

    public static <T> List<T> paginate(List<T> list, int currentPage, int pageSize) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        if (currentPage < 1) {
            currentPage = 1;
        }
        int startIndex = (currentPage - 1) * pageSize;

        return list.stream()
                .skip(startIndex)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    public class FamilyGenealogyComparator implements Comparator<FamilyGenealogy> {
        @Override
        public int compare(FamilyGenealogy obj1, FamilyGenealogy obj2) {
            Integer generationNum1 = obj1.getGenerationNum();
            Integer generationNum2 = obj2.getGenerationNum();

            if (generationNum1 == null && generationNum2 == null) {
                return 0; // 两者都为null，认为相等
            } else if (generationNum1 == null) {
                return 1; // obj1为null，排到最后
            } else if (generationNum2 == null) {
                return -1; // obj2为null，排到最后
            } else if (generationNum1.equals(-1)) {
                return 1; // obj1的generationNum为-1，排到最后
            } else if (generationNum2.equals(-1)) {
                return -1; // obj2的generationNum为-1，排到最后
            } else {
                // 比较非null且不为-1的情况
                return Integer.compare(generationNum1, generationNum2);
            }
        }
    }

    /**
     * 查询
     *
     * @param familyGenealogySelect
     * @return
     */
    @Override
    public JsonResult selectFamilyGenealogyList(FamilyGenealogy familyGenealogySelect) {

        Integer userId = familyGenealogySelect.getUserId();
        QueryWrapper<FamilyGenealogy> wrapperG = new QueryWrapper<FamilyGenealogy>();
        wrapperG.eq("family_id", familyGenealogySelect.getFamilyId());
//        wrapperG.eq("generation",familyGenealogySelect.getGeneration());
        List<FamilyGenealogy> list = this.familyGenealogyDao.selectList(wrapperG);
        //获取当前代数家族全部人员
        QueryWrapper<FamilyGenealogy> allWrapper = new QueryWrapper<FamilyGenealogy>();
        allWrapper.eq("family_id", familyGenealogySelect.getFamilyId());
        allWrapper.eq("generation", familyGenealogySelect.getGeneration());
        List<FamilyGenealogy> familyList = familyGenealogyDao.selectList(allWrapper);

        List<FamilyGenealogy> updatedFamilyTree = null;
        if (userId != null) {
            //获取用户的族谱信息
            QueryWrapper<FamilyGenealogy> wrapper = new QueryWrapper<FamilyGenealogy>();
            wrapper.eq("user_id", userId);
            wrapper.eq("family_id", familyGenealogySelect.getFamilyId());
            List<FamilyGenealogy> weList = familyGenealogyDao.selectList(wrapper);
            if (weList.size() <= 0) {
                //没有入谱不计算代差
                for (FamilyGenealogy genealogy : familyList) {
                    genealogy.setGenerationNum(-1);
                    genealogy.setDistance(-1);
                }
                updatedFamilyTree = familyList;
            } else {
                //入谱计算代差
                int targetUid = weList.get(0).getUid(); // 当前用户的Uid
                int generationA = weList.get(0).getGeneration(); // 列表成员的代数
                //获取当前代数的
                updatedFamilyTree = getDisparity2(list, familyList, targetUid);
                System.out.println("调用的getDisparity2");
//                System.out.println("updatedFamilyTree:"+updatedFamilyTree);
            }

        }
        return ResponseUtil.ok("查询成功", updatedFamilyTree);
    }


    public List<FamilyGenealogy> getDisparity2(List<FamilyGenealogy> familyTree, List<FamilyGenealogy> generationList, int uid) {
        //当前用户的直线距离
//        System.out.println("当前用户的u111id"+uid);
//        System.out.println("当前用户的u333id"+generationList);

//        for (FamilyGenealogy objA : list) {
//            for (FamilyGenealogy objB : listUser) {
//                if (objA.getUid() == objB.getUid()) {
//                    objA.setRelevance(1);
//                    break; // 如果找到匹配的uid，则不再继续比较listB的其他对象
//                }
//            }
//        }
        //需要对比的距离
        List<FamilyGenealogy> left = straightLine(familyTree, uid);//当前用户的直线距离
        for (FamilyGenealogy node : generationList) {
            if (uid == node.getUid()) {
                node.setGenerationNum(0);
            } else {
//                System.out.println("node.getUid()"+ node.getUid());
//                System.out.println("node.uid()"+ uid);
                List<FamilyGenealogy> right = straightLine(familyTree, node.getUid());
//            List<FamilyGenealogy> left = straightLine(familyTree,node.getUid());
//            List<FamilyGenealogy> right = straightLine(familyTree,uid);

//            System.out.println("当前用户的直线距离"+left);
//            System.out.println("当前比对的直线距离"+right);
                // 获取left集合中的parentId
                List<Integer> leftParentIds = left.stream()
                        .map(FamilyGenealogy::getParentId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                List<Integer> rightParentIds = right.stream()
                        .map(FamilyGenealogy::getParentId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());


                // 将parentId列表转换为字符串
                String leftParentIdsString = leftParentIds.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                String rightParentIdsString = rightParentIds.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));

                // 打印结果
                int i = 0;
                boolean listsEqual = leftParentIds.equals(rightParentIds);
//                System.out.println("lis====>:"+ listsEqual);
                // 根据条件设置 num 的值
                if (listsEqual) {
                    i = 1;
                } else {
                    i = calculateDistance(leftParentIds, rightParentIds) + 1;
                }
//                System.out.println("id=====>:"+ node.getId());
//                System.out.println("left: " + leftParentIds);
//                System.out.println("right: " + rightParentIds);
//                System.out.println("差距:"+ i);
                node.setGenerationNum(i);


            }

        }
        return generationList;
    }

    public static int calculateDistance(List<Integer> left, List<Integer> right) {
        int minIndex = 0;
        for (int i = 0; i < left.size(); i++) {
            for (int j = 0; j < right.size(); j++) {
                if (left.get(i).equals(right.get(j))) {
                    System.out.println("left.get(j): " + left.get(i));
                    System.out.println("right.get(j): " + right.get(j));

                    minIndex = Math.min(i, j); // 更新最小差距
                    return minIndex + 1;
                }
            }
        }

        return minIndex;
    }


    //获取代差
    public List<FamilyGenealogy> getDisparity(List<FamilyGenealogy> familyTree, List<FamilyGenealogy> generationList, int uid) {
        FamilyGenealogy user = new FamilyGenealogy();//用户的数据
        FamilyGenealogy familyGenealogy = new FamilyGenealogy();//用户的数据
        for (FamilyGenealogy node : familyTree) {
            if (node.getUid() == uid) {
                user = node;
            }
        }
        System.out.println(user);


        //先获取查看用户的谱
        Integer parentId = user.getParentId();
        boolean pid = true;
        int a = 1;
        List<FamilyGenealogy> listUser = new ArrayList<>();
        listUser.add(user);
        do {
            if (a > 500) {
                System.out.println("获取用户错误");
                return (List<FamilyGenealogy>) ResponseUtil.fail("获取用户错误");
            }
            a++;
            System.out.println("do循环:" + a);

            if (parentId == null || parentId == -1) {
                System.out.println("parentId为null");
                pid = false;
            } else {

                for (FamilyGenealogy node : familyTree) {
                    int noUid = node.getUid();
                    if (noUid == parentId) {
                        familyGenealogy = node;
                        break;
                    }
                }
//                if(familyGenealogy.getUid() == familyGenealogy.getParentId() ){
//                    pid = false;
//                }

                System.out.println(familyGenealogy);
                parentId = familyGenealogy.getParentId();
                listUser.add(familyGenealogy);
            }

        } while (pid);

        //获取每个成员的谱
        for (FamilyGenealogy item : generationList
        ) {
            int userUid = user.getUid();
            int itemUid = item.getUid();
            if (itemUid == userUid) {
                item.setGenerationNum(0);
            } else {
                List<FamilyGenealogy> list = new ArrayList<>();
                list.add(item);
                parentId = item.getParentId();
                pid = true;
                a = 1;

                do {
                    if (a > 500) {
                        System.out.println(listUser.toString());
                        System.out.println("获取代差数据异常");
                        return (List<FamilyGenealogy>) ResponseUtil.fail("获取代差数据异常");
                    }
                    a++;
                    if (parentId == null || parentId == -1) {
                        System.out.println("parentId为null");
                        pid = false;
                    } else {
                        System.out.println("当前查看的父级:" + parentId);
                        for (FamilyGenealogy node : familyTree) {
                            int noUid = node.getUid();
                            if (noUid == parentId) {
                                familyGenealogy = node;
                            }
                        }

//                    if(familyGenealogy.getUid() == familyGenealogy.getParentId() ){
//                        pid = false;
//                    }
                        System.out.println("循环判断：" + familyGenealogy.toString());
                        System.out.println(familyGenealogy);
                        parentId = familyGenealogy.getParentId();
                        list.add(familyGenealogy);
                    }

                } while (pid);


                System.out.println(listUser);
                System.out.println(list);
                int generationNum = 0;
                for (FamilyGenealogy objA : list) {
                    for (FamilyGenealogy objB : listUser) {
                        if (objA.getUid() == objB.getUid()) {
                            objA.setRelevance(1);
                            break; // 如果找到匹配的uid，则不再继续比较listB的其他对象
                        }
                    }
                }
                for (FamilyGenealogy f : list
                ) {
                    if (f.getRelevance() == 0) {
                        generationNum++;
                    }
                }
                item.setGenerationNum(generationNum);

            }


        }


        return generationList;
    }


    // 检查两个数组是否完全相等的方法
    private static boolean areArraysEqual(int[] arr1, int[] arr2) {
        if (arr1.length != arr2.length) {
            return false;
        }

        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }

        return true;
    }

    public static int calculateGenerationNum(List<FamilyGenealogy> left, List<FamilyGenealogy> right) {
        int generationNum = 0;
        Boolean add = false;
        int straightLine = 0;
        if (left.size() == 1 || right.size() == 1) {
            if (right.size() == 1) {
                if (left.get(left.size() - 1).getUid() == right.get(0).getUid()
                ) {
                    return 0;
                }
            }
            return -1;
        }


        if (left.size() > right.size()) {
            List<FamilyGenealogy> a = left;
            List<FamilyGenealogy> b = right;
            left = b;
            right = a;
        }


        for (FamilyGenealogy nodeLeft : left
        ) {
            int frequency = 0;//循环次数
            for (FamilyGenealogy nodeRight : right
            ) {
                frequency++;
                //对比右边的数据
                if (nodeLeft.getUid() == nodeRight.getUid()) { //找到相同的数据那么就要进行删除right 在这之前的数据，并且结束循环
                    //删除操作
                    int index = -1;
                    for (int i = 0; i < right.size(); i++) {
                        if (right.get(i).getUid() == nodeRight.getUid()) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) { // 确保目标对象存在于列表中
                        if (index == 0) {
                            right.remove(0);
                        } else {
                            right.subList(0, index).clear(); // 删除目标对象之前的对象
                        }
                    }
                    //删除以后结束循环
                    if (add) {
                        frequency++;
                        straightLine++;
                    }
                    break;
                } else {
                    add = true;
                }
            }
            if (frequency == right.size()) {//遍历完以后都没有找到相同那么generationNum+1
                generationNum++;
            }

        }
        if (add) {
//            generationNum++;
        }
        if (straightLine == left.size() * right.size()) {
            return 0;
        }
        return generationNum;
    }

    //计算出这个当前的直线距离
    public static List<FamilyGenealogy> straightLine(List<FamilyGenealogy> familyTree, int uid) {
        List<FamilyGenealogy> list = new ArrayList<>();
        FamilyGenealogy treeNode = null;//需要计算当前直线距离的数据

        for (FamilyGenealogy node : familyTree) {
            if (node.getUid() == uid) {
                treeNode = node;
            }
        }
        if (treeNode.getParentId() == null || treeNode.getParentId() == 0 || treeNode.getParentId() == treeNode.getUid()) {
            list.add(treeNode);//添加自己的值
            return list;
        }
        Boolean stop = true;
        int checkUid = treeNode.getParentId();
        int i = 0;
        list.add(treeNode);//添加自己的值
        //计算距离
        while (stop) {
            i++;
            for (FamilyGenealogy node : familyTree
            ) {
//                System.out.println(node);
                if (node.getUid() == checkUid) {
                    if (node.getParentId() == null || node.getParentId() == 0 || node.getParentId() == node.getUid()) {
                        list.add(node);
                        stop = false;
                    } else {
                        checkUid = node.getParentId();
                        list.add(node);
                    }
                }
                ;
            }
        }
        System.out.println("最终距离:" + list);
        //得到最终距离
        return list;
    }


    /**
     * 审核族谱图申请
     *
     * @param genealogy
     * @return
     */
    @Override
    @Transactional
    public JsonResult auditFamilyGenealogy(FamilyGenealogy genealogy) {
        if (Objects.isNull(genealogy.getUserId())) {
            return ResponseUtil.fail("审核人id不能为空");
        }
        if (Objects.isNull(genealogy.getId())) {
            return ResponseUtil.fail("审核数据id不能为空");
        }

        if (Objects.isNull(genealogy.getStatus())) {
            return ResponseUtil.fail("审核状态不能为空");
        }

        FamilyGenealogy byId = this.familyGenealogyDao.selectById(genealogy.getId());
        if (Objects.isNull(byId)) {
            return ResponseUtil.fail("当前审核数据不存在");
        }

        if (byId.getStatus() != 0) {
            return ResponseUtil.fail("当前数据状态不为待审核,不可审核");
        }

        //判断当前审核是不是管理员
        Boolean admin = this.isAdmin(genealogy.getUserId(), byId.getFamilyId());
        if (!admin) {
            return ResponseUtil.fail("未查询到您的管理员身份,不可审核");
        }

        byId.setStatus(genealogy.getStatus());
        byId.setRefuse(genealogy.getRefuse());

        this.familyGenealogyDao.updateById(byId);
        return ResponseUtil.ok("审核成功");
    }

    /**
     * 管理员修改族谱图信息
     *
     * @param familyGenealogy
     * @return
     */
    @Override
    @Transactional
    public JsonResult upFamilyGenealogyInfo(FamilyGenealogy familyGenealogy) {
        if (Objects.isNull(familyGenealogy.getId())) {
            return ResponseUtil.fail("修改数据id不能为空");
        }

        if (Objects.isNull(familyGenealogy.getUserId())) {
            return ResponseUtil.fail("审核人用户id不能为空");
        }

        FamilyGenealogy byId = this.familyGenealogyDao.selectById(familyGenealogy.getId());

        if (Objects.isNull(byId)) {
            return ResponseUtil.fail("修改的数据不存在");
        }

        //判断当前审核是不是管理员
        Boolean admin = this.isAdmin(familyGenealogy.getUserId(), byId.getFamilyId());
        if (!admin) {
            return ResponseUtil.fail("未查询到您的管理员身份,不可审核");
        }

        if (Objects.nonNull(familyGenealogy.getHeadImg())) {
            byId.setHeadImg(familyGenealogy.getHeadImg());
        }

        if (Objects.nonNull(familyGenealogy.getIsAlive())) {
            byId.setIsAlive(familyGenealogy.getIsAlive());
        }

        if (Objects.nonNull(familyGenealogy.getParentId())) {
            byId.setParentId(familyGenealogy.getParentId());
        }

        if (Objects.nonNull(familyGenealogy.getApplyName())) {
            byId.setApplyName(familyGenealogy.getApplyName());
        }

        this.familyGenealogyDao.updateById(byId);

        return ResponseUtil.ok("修改成功");
    }

    /**
     * 判断是不是家族管理员
     *
     * @param userId   当前用户id
     * @param familyId 家族id
     * @return
     */
    public Boolean isAdmin(Integer userId, Integer familyId) {
        LambdaQueryWrapper<FamilyUser> wrapperFamilyUser = new LambdaQueryWrapper<>();
        wrapperFamilyUser.eq(FamilyUser::getFamilyId, familyId);
        wrapperFamilyUser.eq(FamilyUser::getUserId, userId);
        FamilyUser familyUser = this.familyUserDao.selectOne(wrapperFamilyUser);

        if (Objects.isNull(familyUser)) {
            return false;
        }

        if (familyUser.getLevel() != 3) {
            return true;
        }
        return false;
    }

    @Override
    public JsonResult upFamilyGenealogyChart(Integer familyId) {


        QueryWrapper<FamilyGenealogy> familyGenealogySelect = new QueryWrapper<FamilyGenealogy>();
        familyGenealogySelect.eq("family_id", familyId);
        List<FamilyGenealogy> familyGenealogies = familyGenealogyDao.selectList(familyGenealogySelect);//这个家族成员全部数据
        FamilyGenealogy familyItem = new FamilyGenealogy();

        System.out.println(familyGenealogies);
        int i = 0;
        for (FamilyGenealogy item : familyGenealogies) {
            i++;
            System.out.println("开始循环");
            List<Map<Integer, String>> list = new ArrayList<>();
            Map m = new HashMap();
            m.put(item.getUid(), item.getGenealogyName());
            list.add(m);
            System.out.println(item);
            Integer parentId = item.getParentId();
            boolean pid = true;
            int a = 1;

            do {
                if (a > 500) {
                    return ResponseUtil.fail("数据异常");
                }
                a++;
                System.out.println("do循环:" + a);

                if (parentId == null || parentId == -1) {
                    System.out.println("parentId为null");
                    pid = false;
                } else {

                    FamilyGenealogy select = new FamilyGenealogy();
                    select.setFamilyId(familyId);
                    select.setUid(parentId);
                    System.out.println("当前查看的父级:" + parentId);
                    System.out.println("调用是家族id刷新");
                    familyItem = familyGenealogyDao.selectFG(select).get(0);
                    if (familyItem.getUid() == familyItem.getParentId()) {
                        pid = false;
                    }
                    System.out.println("循环判断：" + familyItem.toString());
                    System.out.println(familyItem);
                    parentId = familyItem.getParentId();
                    Map map = new HashMap();
                    map.put(familyItem.getUid(), familyItem.getGenealogyName());
                    list.add(map);
                }

            } while (pid);
            System.out.println("第" + i + "次：" + list.toString());
            if (list.size() > 0) {
                FamilyGenealogy update = new FamilyGenealogy();
                update.setId(item.getId());
                update.setChart(list.toString());
                familyGenealogyDao.updateChart(update);
            }

        }


        return ResponseUtil.ok("家族id:" + familyId + ",更新成功");
    }


    public JsonResult upFamilyGenealogyChartByIdOne(Integer id, Integer parentId) {
        System.out.println("族谱修改的id是:" + id);
        FamilyGenealogy familyGenealogy = familyGenealogyDao.selectById(id);
        QueryWrapper<FamilyGenealogy> familyGenealogySelect = new QueryWrapper<FamilyGenealogy>();
        familyGenealogySelect.eq("family_id", familyGenealogy.getFamilyId());

//        List<FamilyGenealogy> familyGenealogies = familyGenealogyDao.selectList(familyGenealogySelect);//这个家族成员全部数据

        //需要改动的数据

//        Integer uid = familyGenealogy.getUid();
        if (!(familyGenealogy.getUid() == familyGenealogy.getParentId() || familyGenealogy.getParentId() == null || familyGenealogy.getParentId() == 0)) {
            ArrayList uidArr = new ArrayList();
            uidArr.add(familyGenealogy.getUid());
            uidArr.add(parentId);
            System.out.println("当前数组");
            System.out.println(uidArr);
            do {

                ArrayList OldArr = uidArr;
                ArrayList NewArr = new ArrayList();
                System.out.println("OldArr.size()" + OldArr.size());
                for (int i = 0; i < OldArr.size(); i++) {
                    familyGenealogySelect.eq("parent_id", OldArr.get(i));

                    List<FamilyGenealogy> familyGenealogyList = familyGenealogyDao.selectList(familyGenealogySelect);

                    System.out.println(familyGenealogyList);
                    if (familyGenealogyList.size() < 1) {
//                    uid = 0;
                    } else {

                        upFamilyGenealogyChartById(familyGenealogy.getFamilyId(), familyGenealogyList);
                        System.out.println("xuyaoxiugaid");
                        System.out.println(familyGenealogyList);
                        for (FamilyGenealogy item : familyGenealogyList
                        ) {
                            if (item.getUid() != item.getParentId()) {
                                NewArr.add(item.getParentId());
                            }
                            if (item.getParentId() != null) {
                                NewArr.add(item.getParentId());
                            }
                            if (item.getParentId() != 0) {
                                NewArr.add(item.getParentId());
                            }
                        }

                    }
                }
                System.out.println("xxx2222" + NewArr);
                uidArr = NewArr;
//                uidArr.clear();

            } while (uidArr.size() != 0);
        }


        return ResponseUtil.ok("家族更新成功");
    }

    public JsonResult upFamilyGenealogyChartById(Integer familyId, List<FamilyGenealogy> familyGenealogies) {
        FamilyGenealogy familyItem = new FamilyGenealogy();

        System.out.println("需要更新的数据");
        System.out.println(familyGenealogies);
        int i = 0;
        for (FamilyGenealogy item : familyGenealogies) {
            i++;
            System.out.println("开始循环");
            List<Map<Integer, String>> list = new ArrayList<>();
            Map m = new HashMap();
            m.put(item.getUid(), item.getGenealogyName());
            list.add(m);
            System.out.println(item);
            Integer parentId = item.getParentId();
            boolean pid = true;
            int a = 1;

            do {
                if (a > 500) {
                    return ResponseUtil.fail("数据异常");
                }
                a++;
                System.out.println("do循环:" + a);

                if (parentId == null || parentId == -1) {
                    System.out.println("parentId为null");
                    pid = false;
                } else {

                    FamilyGenealogy select = new FamilyGenealogy();
                    select.setFamilyId(familyId);
                    select.setUid(parentId);
                    System.out.println("当前查看的父级:" + parentId);
                    System.out.println("调用是家谱id刷新");
                    familyItem = familyGenealogyDao.selectFG(select).get(0);
                    if (familyItem.getUid() == familyItem.getParentId()) {
                        pid = false;
                    }
                    System.out.println("循环判断：" + familyItem.toString());
                    System.out.println(familyItem);
                    parentId = familyItem.getParentId();
                    Map map = new HashMap();
                    map.put(familyItem.getUid(), familyItem.getGenealogyName());
                    list.add(map);
                }

            } while (pid);
            System.out.println("第" + i + "次：" + list.toString());
            if (list.size() > 0) {
                FamilyGenealogy update = new FamilyGenealogy();
                update.setId(item.getId());
                update.setChart(list.toString());
                familyGenealogyDao.updateChart(update);
            }

        }


        return ResponseUtil.ok("家族id:" + familyId + ",更新成功");
    }

    @Override
    public JsonResult importUser(MultipartFile file, Integer familyId) {

        List<FamilyGenealogyExcelVo> familyGenealogyExcelVoList = null;
        try {
            familyGenealogyExcelVoList = ExcelUtils.readMultipartFile(file, FamilyGenealogyExcelVo.class);
            System.out.println(familyGenealogyExcelVoList);
            hasDuplicateIds(familyGenealogyExcelVoList);//判断序号是否重复
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("含有重复序号");
        }
//        System.out.println("导入成功");
        QueryWrapper q = new QueryWrapper<FamilyGenealogy>();
        q.eq("family_id", familyId);
        int uidMax = 0;
        if (!(familyGenealogyDao.selectList(q).size() <= 0)) {
            uidMax = familyGenealogyDao.selectUidMax(familyId);
        }
//        System.out.println("================>maxUid:"+uidMax);

        int s = 1;
        for (FamilyGenealogyExcelVo familyGenealogyExcelVo : familyGenealogyExcelVoList) {
            FamilyGenealogy familyGenealogy = new FamilyGenealogy();
            familyGenealogy.setFamilyId(familyId);
            familyGenealogy.setSex(familyGenealogyExcelVo.getSex());
            familyGenealogy.setRelation(familyGenealogyExcelVo.getRelation());
            familyGenealogy.setGeneration(familyGenealogyExcelVo.getGeneration());
            familyGenealogy.setGenealogyName(familyGenealogyExcelVo.getGenealogyName());
//            System.out.println("******************>uid:"+familyGenealogy.getUid());
            //如果这个家族没有数据则直接赋值uid
            int uid = familyGenealogyExcelVo.getUid() + uidMax;
            familyGenealogy.setUid(uid);
            int fatherUid = 0;
            if (familyGenealogyExcelVo.getFatherUid() != null) {
                fatherUid = familyGenealogyExcelVo.getFatherUid();
                familyGenealogy.setParentId(fatherUid + uidMax);
            }
            //
            if (familyGenealogyExcelVo.getIsAlive() != null) {
                familyGenealogy.setIsAlive(familyGenealogyExcelVo.getIsAlive());
            } else {
                familyGenealogy.setIsAlive(1);
            }

//            System.out.println("==================>uid:"+familyGenealogy.getUid());


//            System.out.println(familyGenealogy);
            //判断同一代是否重名
            if (familyGenealogyDao.selectNameGenerationList(familyGenealogy) > 0) {
                throw new ServiceException("序号：" + familyGenealogyExcelVo.getUid() + "，姓名：" + familyGenealogyExcelVo.getGeneration() + ",重复！");
            }

            //判断是否为空
            if ("".equals(familyGenealogyExcelVo.getGeneration()) || familyGenealogyExcelVo.getGeneration() == null) {
                throw new ServiceException("序号：" + familyGenealogyExcelVo.getUid() + "，姓名为空！");
            }
            //判断排行 大于0 小于10
            Integer ranking = familyGenealogyExcelVo.getRanking();
            if (null != ranking) {
                if (ranking > 0 && ranking < 10) {
                    familyGenealogy.setRanking(familyGenealogyExcelVo.getRanking());
                } else {
                    throw new ServiceException("序号：" + familyGenealogyExcelVo.getUid() + "，排行(1-9)参数错误！");
                }
            }
            if (null != familyGenealogyExcelVo.getBirthday() && !"".equals(familyGenealogyExcelVo.getBirthday())) {
                if (null == DateUtils.strToDate(familyGenealogyExcelVo.getBirthday())) {
                    throw new ServiceException("序号：" + familyGenealogyExcelVo.getUid() + "，生日参数错误！");
                }
                familyGenealogy.setBirthday(familyGenealogyExcelVo.getBirthday());
            }
            if (null != familyGenealogyExcelVo.getMourningDay() && !"".equals(familyGenealogyExcelVo.getMourningDay())) {
                if (null == DateUtils.strToDate(familyGenealogyExcelVo.getMourningDay())) {
                    throw new ServiceException("序号：" + familyGenealogyExcelVo.getUid() + "，忌日参数错误！");
                }
                familyGenealogy.setMourningDay(familyGenealogyExcelVo.getMourningDay());
            }
            familyGenealogyDao.insert(familyGenealogy);

        }
        System.out.println("导入失败了");
        this.upFamilyGenealogyChart(familyId);
        return ResponseUtil.ok("导入成功");
    }

    // 检查List中的id是否有重复
    public boolean hasDuplicateIds(List<FamilyGenealogyExcelVo> familyList) throws Exception {
        Set<Integer> idSet = new HashSet<>();

        for (FamilyGenealogyExcelVo family : familyList) {
            Integer id = family.getUid(); // 假设getId()方法用于获取id值
            // 如果id已经存在于idSet中，说明有重复
            if (!idSet.add(id)) {
                throw new Exception("序号：" + id + "重复！");
            }
        }
        // 如果遍历完成后没有发现重复的id，返回false
        return false;
    }


    @Override
    public JsonResult selectByName(FamilyGenealogyInsertReqVo familyGenealogyInsertReqVo) {
        QueryWrapper<FamilyGenealogy> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("genealogy_name", familyGenealogyInsertReqVo.getGenealogyName());
        queryWrapper.eq("family_id", familyGenealogyInsertReqVo.getFamilyId());
        return ResponseUtil.ok("查询成功", familyGenealogyDao.selectList(queryWrapper));
    }

    @Override
    public JsonResult getFamilyGenealogyList(Integer familyId, Integer generation, String genealogyName) {
        QueryWrapper<FamilyGenealogy> queryWrapper = new QueryWrapper<>();
        if (generation != null) {
            queryWrapper.eq("generation", generation);
        }
        if (null != genealogyName) {
            queryWrapper.like("genealogy_name", genealogyName);
        }
        queryWrapper.eq("family_id", familyId);
        return ResponseUtil.ok("查询成功", familyGenealogyDao.selectList(queryWrapper));
    }

    @Override
    public JsonResult getFamilyGenealogyListNotClaim(Integer familyId, Integer generation, String genealogyName, Integer limit, Integer page) {
        QueryWrapper<FamilyGenealogy> queryWrapper = new QueryWrapper<>();
        if (generation != null) {
            queryWrapper.eq("generation", generation);
        }
        if (null != genealogyName) {
            queryWrapper.like("genealogy_name", genealogyName);
        }
        queryWrapper.orderBy(true, false, "generation");
        queryWrapper.eq("family_id", familyId);
        queryWrapper.isNull("user_id");
        queryWrapper.isNull("parent_id");
        Page<FamilyGenealogy> Page = new Page<>(page, limit);
        Page<FamilyGenealogy> resultPage = familyGenealogyDao.selectPage(Page, queryWrapper);
        List<FamilyGenealogy> list = Lists.newArrayList();
        resultPage.getRecords().stream().forEach(f -> {
            long cnt = familyGenealogyDao.selectCount(new LambdaQueryWrapper<FamilyGenealogy>().eq(FamilyGenealogy::getFamilyId, familyId)
                    .eq(FamilyGenealogy::getParentId, f.getUid()));
            if (cnt == 0) {
                list.add(f);
            }
        });
        FamilyGenealogyPageVo familyGenealogyPageVo = new FamilyGenealogyPageVo();
        familyGenealogyPageVo.setList(list);
        familyGenealogyPageVo.setTotal(resultPage.getTotal());
        return ResponseUtil.ok("查询成功", familyGenealogyPageVo);
    }

    @Override
    public JsonResult getFamilyGenealogyListPaging(Integer familyId, Integer generation, String genealogyName, Integer limit, Integer page) {
        QueryWrapper<FamilyGenealogy> queryWrapper = new QueryWrapper<>();
        if (generation != null) {
            queryWrapper.eq("generation", generation);
        }
        if (null != genealogyName) {
            queryWrapper.like("genealogy_name", genealogyName);
        }
        queryWrapper.orderBy(true, false, "generation");
        queryWrapper.eq("family_id", familyId);
        Page<FamilyGenealogy> Page = new Page<>(page, limit);
        Page<FamilyGenealogy> resultPage = familyGenealogyDao.selectPage(Page, queryWrapper);

        FamilyGenealogyPageVo familyGenealogyPageVo = new FamilyGenealogyPageVo();
        familyGenealogyPageVo.setList(resultPage.getRecords());
        familyGenealogyPageVo.setTotal(resultPage.getTotal());
        return ResponseUtil.ok("查询成功", familyGenealogyPageVo);
    }

    @Override
    public JsonResult getFamilyGenealogyListPagingGap(Integer familyId, Integer generation, String genealogyName, Integer userId, Integer limit, Integer page, Integer generationNum) {
        QueryWrapper<FamilyGenealogy> queryWrapper = new QueryWrapper<>();
        if (generation != null) {
            queryWrapper.eq("generation", generation);
        }
        if (null != genealogyName && !genealogyName.isEmpty()) {
            queryWrapper.like("genealogy_name", genealogyName);
        }
        queryWrapper.orderBy(true, false, "generation").orderByDesc("id");
        queryWrapper.eq("family_id", familyId);
        List<FamilyGenealogy> familyList = familyGenealogyDao.selectList(queryWrapper);
        //查询代差
        //获取这个家族全部人员
        QueryWrapper<FamilyGenealogy> wrapperG = new QueryWrapper<FamilyGenealogy>();
        wrapperG.eq("family_id", familyId);
        List<FamilyGenealogy> list = this.familyGenealogyDao.selectList(wrapperG);
        //获取当前代数家族全部人员
        FamilyGenealogy curFamilyGenealogy = null;
        if (userId != null) {
            //获取用户的族谱信息
            QueryWrapper<FamilyGenealogy> wrapper = new QueryWrapper<FamilyGenealogy>();
            wrapper.eq("user_id", userId);
            wrapper.eq("family_id", familyId);
            List<FamilyGenealogy> weList = familyGenealogyDao.selectList(wrapper);
            if (weList.size() <= 0) {
                //没有入谱不计算代差
                for (FamilyGenealogy genealogy : familyList) {
                    genealogy.setGenerationNum(-1);
                    genealogy.setDistance(-1);
                }
            } else {
                curFamilyGenealogy = weList.get(0);
                //入谱计算代差
                int targetUid = weList.get(0).getUid(); // 当前用户的Uid
                int generationA = weList.get(0).getGeneration(); // 列表成员的代数
                //获取当前代数的
                // updatedFamilyTree = getDisparity(list,familyList,targetUid);
//                updatedFamilyTree = getDisparity2(list,familyList,targetUid);
//                System.out.println("updatedFamilyTree:"+updatedFamilyTree);
            }
        }
        Map<Integer, FamilyGenealogy> map = list.stream().collect(Collectors.toMap(FamilyGenealogy::getUid, v -> v, (p1, p2) -> p1));
        for (FamilyGenealogy f : familyList) {
            if (f.getUserId() != null) {
                User user = userService.selectUserById(f.getUserId());
                f.setHeadImg(null != user ? user.getAvatar() : "");
            }
            if (null != curFamilyGenealogy) {
//                f.setDistance(f.getGeneration());
                calDistance(curFamilyGenealogy, f, map);
            }
        }
        familyList.sort(Comparator.comparing(FamilyGenealogy::getDistance).thenComparing(FamilyGenealogy::getGeneration));
        //            System.out.println(i + "==========" + t.getGenealogyName() + "=====" + t.getDistance() + "=====" + t.getGeneration());
        //        System.out.println("updatedFamilyTree");
//        System.out.println(updatedFamilyTree);

        FamilyGenealogyPageVo familyGenealogyPageVo = new FamilyGenealogyPageVo();
        familyGenealogyPageVo.setList(paginate(familyList, page, limit));
        familyGenealogyPageVo.setTotal((long) familyList.size());
        if (generationNum != null) {
            // 使用流过滤数据
            List<FamilyGenealogy> filteredList = familyList.stream()
                    .filter(obj -> obj.getDistance() != null && obj.getDistance() >= 0 && obj.getDistance() <= generationNum)
                    .collect(Collectors.toList());
            familyGenealogyPageVo.setList(paginate(filteredList, page, limit));
            familyGenealogyPageVo.setTotal((long) filteredList.size());
            return ResponseUtil.ok("查询成功9", familyGenealogyPageVo);
        }

        return ResponseUtil.ok("查询成功", familyGenealogyPageVo);
    }

    public JsonResult getFamilyGenealogyListPagingGap2(Integer familyId, Integer generation, String genealogyName, Integer userId, Integer limit, Integer page, Integer generationNum) {
        //获取这个家族全部人员
        QueryWrapper<FamilyGenealogy> wrapperG = new QueryWrapper<FamilyGenealogy>();
        wrapperG.eq("family_id", familyId);
        if (null != genealogyName && !"".equals(genealogyName)) {
            wrapperG.like("genealogy_name", genealogyName);
        }
        List<FamilyGenealogy> list = this.familyGenealogyDao.selectList(wrapperG);
        FamilyGenealogy curFamilyGenealogy = null;
        if (userId != null) {
            //获取用户的族谱信息
            QueryWrapper<FamilyGenealogy> wrapper = new QueryWrapper<FamilyGenealogy>();
            wrapper.eq("user_id", userId);
            wrapper.eq("family_id", familyId);
            List<FamilyGenealogy> weList = familyGenealogyDao.selectList(wrapper);
            if (null != weList && weList.size() > 0) {
                curFamilyGenealogy = weList.get(0);
            }
        }
        Map<Integer, FamilyGenealogy> map = list.stream().collect(Collectors.toMap(FamilyGenealogy::getUid, v -> v, (p1, p2) -> p1));
        for (FamilyGenealogy f : list) {
            if (f.getUserId() != null) {
                User user = userService.selectUserById(f.getUserId());
                f.setHeadImg(null != user ? user.getAvatar() : "");
            }
            f.setDistance(f.getGeneration());
            calDistance(curFamilyGenealogy, f, map);
        }
        list.sort(Comparator.comparing(FamilyGenealogy::getDistance).thenComparing(FamilyGenealogy::getGeneration));
        for (FamilyGenealogy t : list) {
            System.out.println("【#############】" + t.getGenealogyName() + "=====" + t.getDistance() + "=====" + t.getGeneration());
        }


        if (generationNum != null) {
            // 使用流过滤数据
            List<FamilyGenealogy> filteredList = list.stream()
                    .filter(obj -> obj.getDistance() != null && obj.getDistance() >= 0 && obj.getDistance() <= generationNum)
                    .collect(Collectors.toList());
            return ResponseUtil.ok("查询成功9", filteredList);
        }

        return ResponseUtil.ok("查询成功", list);
    }

    /**
     * 计算家族成员与当前登录人的距离：按两个人到重叠点的最短距离计算，在一条线的距离都是0
     * 先找重叠点，再分别计算两人到重叠点的距离
     *
     * @param me    当前登录人
     * @param other 其他家族成员
     */
    private void calDistance(FamilyGenealogy me, FamilyGenealogy other, Map<Integer, FamilyGenealogy> map) {
        try {
            List<FamilyGenealogy> meParentList = Lists.newArrayList();
            getParentList(me, meParentList, map);

            List<FamilyGenealogy> otherParentList = Lists.newArrayList();
            getParentList(other, otherParentList, map);
            boolean isOneLine = false;//先判断是否在一条家族线上
            if (!meParentList.isEmpty()) {
                for (FamilyGenealogy meParentGen : meParentList) {
                    if (Objects.equals(meParentGen.getUid(), other.getUid())) {
                        isOneLine = true;
                        break;
                    }
                }
            }
            if (!otherParentList.isEmpty()) {
                for (FamilyGenealogy otherParentGen : otherParentList) {
                    if (null != otherParentGen && Objects.equals(otherParentGen.getUid(), me.getUid())) {
                        isOneLine = true;
                        break;
                    }
                }
            }
            if (isOneLine) {
                other.setDistance(0);
                return;
            }
            boolean isOverlap = false;
            if (!meParentList.isEmpty()) {
                for (FamilyGenealogy meParentGen : meParentList) {
                    for (FamilyGenealogy otherParentGen : otherParentList) {
                        if (Objects.equals(meParentGen.getUid(), otherParentGen.getUid()) && !isOverlap) {//重叠点
                            isOverlap = true;
                            int meDis = me.getGeneration() - meParentGen.getGeneration();
                            int otherDis = other.getGeneration() - meParentGen.getGeneration();
                            other.setDistance(Math.min(meDis, otherDis));
                            break;
                        }
                    }
                }
            }
            if (!isOverlap) {
                other.setDistance(-1);
//            Syste
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getParentList(FamilyGenealogy me, List<FamilyGenealogy> meParentList, Map<Integer, FamilyGenealogy> map) {
        Set<Integer> visited = new HashSet<>();

        while (me != null && me.getParentId() != null && me.getParentId() != -1 && !visited.contains(me.getParentId())) {
            visited.add(me.getParentId());
            FamilyGenealogy parent = map.get(me.getParentId());
            if (parent != null) {
                meParentList.add(parent);
                me = parent; // 更新 me 为当前的父节点
            } else {
                break; // 如果找不到父节点，则停止
            }
        }
    }

    @Override
    public JsonResult upFamilyGenealogyPid(FamilyGenealogy familyGenealogy) {
        familyGenealogy.setUserId(null);
        System.out.println(familyGenealogy);
        familyGenealogyDao.upFamilyGenealogyPid(familyGenealogy);
        return ResponseUtil.ok("修改成功");
    }


    public JsonResult cancelClaim(FamilyGenealogy familyGenealogy) {
        FamilyGenealogy cancel = new FamilyGenealogy();
        cancel.setId(familyGenealogy.getId());
        familyGenealogyDao.updateUserIdIsNull(cancel);

        FamilyUser familyUser = new FamilyUser();
        QueryWrapper<FamilyUser> queryWrapper = new QueryWrapper<FamilyUser>();
        queryWrapper.eq("family_id", familyGenealogy.getFamilyId());
        queryWrapper.eq("user_id", familyGenealogy.getUserId());
        familyUser = familyUserDao.selectList(queryWrapper).get(0);

        FamilyUser updateFamilyUser = new FamilyUser();
        updateFamilyUser.setId(familyUser.getId());
        updateFamilyUser.setJoins(1);
        updateFamilyUser.setGeneration(0);
        familyUserDao.updateById(updateFamilyUser);

        return ResponseUtil.ok("取消成功");
    }

    @Override
    public JsonResult deriveExcle(Integer familyId, String mailbox, Integer userId) {

        //看看用户米够不够
        User user = userDao.selectById(userId);
        if (user == null) {
            return ResponseUtil.fail("导出失败");
        }
        if (user.getRice() < 1000) {
            return ResponseUtil.fail("米不足");
        }
        //消耗米
        User userUpdate = new User();
        userUpdate.setId(user.getId());
        userUpdate.setRice(1000);
        int result = userDao.setDec(userUpdate);
        if (result == 0) {
            throw new ServiceException("解锁失败");
        }

        LambdaQueryWrapper<FamilyGenealogy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyGenealogy::getFamilyId, familyId);

        Family family = this.familyService.getById(familyId);
        if (Objects.isNull(family)) {
            return ResponseUtil.fail("未查询到家族信息");
        }

        List<FamilyGenealogy> genealogyList = this.familyGenealogyService.list(wrapper);
        if (CollectionUtils.isEmpty(genealogyList)) {
            return ResponseUtil.fail("该家族暂时没有家谱图");
        }

        // 写法1 JDK8+
        // since: 3.0.0-beta1

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = now.format(formatter);

        UUID uuid = UUID.randomUUID();
        String fileName = "D:\\火谱-" + family.getName() + formattedDateTime + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可

        File file = new File(fileName);
        EasyExcel.write(file.getAbsolutePath(), FamilyGenealogy.class)
                .sheet("模板")
                .doWrite(genealogyList);


        //发送电子邮件
        try {
            //	创建会话
            Session session = JavaMailUntil.createSession();

            //	创建邮件
            MimeMessage message = new MimeMessage(session);
            message.setFrom("kt_zby@163.com");
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(mailbox));
            // message.setRecipients(MimeMessage.RecipientType.CC, new InternetAddress[] {new  InternetAddress("fieelv@qq.com"),new InternetAddress("fieelv@qq.com")});
            message.setSubject("火谱-" + family.getName() + formattedDateTime);
            message.setText("火谱-" + family.getName() + formattedDateTime); // 设置邮件文本内容
            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart file1 = new MimeBodyPart();
            DataHandler handler = new DataHandler(new FileDataSource(file.getAbsolutePath()));
            file1.setDataHandler(handler);
            //对文件名进行编码，防止出现乱码
            String fileNames = MimeUtility.encodeWord("火谱-" + family.getName() + formattedDateTime + ".xlsx", "utf-8", "B");
            file1.setFileName(fileNames);
            multipart.addBodyPart(file1);
            message.setContent(multipart);
            // 发送
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseUtil.ok("发送失败，请联系管理员重新发送");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseUtil.ok("发送失败，请联系管理员重新发送");

        }
        //删除文件
//        file.delete();
        return ResponseUtil.ok("已发送");
    }

    private static List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("家族");
        head0.add("导出时间");
        head0.add("家族代数");
        List<String> head1 = new ArrayList<String>();
        head1.add("运营部");
        head1.add("2024-08-20 18:34:33");
        head1.add("状态");
        List<String> head2 = new ArrayList<String>();
        head2.add("运营部");
        head2.add("2024-08-20 18:34:33");
        head2.add("家族人员姓名");
        List<String> head3 = new ArrayList<String>();
        head3.add("运营部");
        head3.add("2024-08-20 18:34:33");
        head3.add("人员家谱");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        return list;
    }


    @Override
    public JsonResult createConnect(Integer familyId, Integer userId) {

        //看看用户米够不够
        User user = userDao.selectById(userId);
        if (user == null) {
            return ResponseUtil.fail("导出失败");
        }
        if (user.getRice() < 1000) {
            return ResponseUtil.fail("米不足");
        }
        //消耗米
        User userUpdate = new User();
        userUpdate.setId(user.getId());
        userUpdate.setRice(1000);
        int result = userDao.setDec(userUpdate);
        if (result == 0) {
            throw new ServiceException("解锁失败");
        }

        UUID uuid = UUID.randomUUID();
        FamilyDownload familyDownload = new FamilyDownload();
        familyDownload.setUid(uuid.toString());
        familyDownload.setFamilyId(familyId);
        familyDownload.setUserId(userId);
        System.out.println(familyDownload);
        familyDownloadDao.insert(familyDownload);
        String url = "https://www.ohopu.com/api/familyDownload/connect?uid=" + uuid;
        System.out.println(url);
        return ResponseUtil.ok("解锁出层高", url);
    }

    @Override
    public JsonResult viewFamilyTreePay(Integer familyId, Integer userId) {

        // 米数数量
        int riceNum = 50;
        //看看用户米够不够
        User user = userDao.selectById(userId);
        if (user == null) {
            return ResponseUtil.fail("查看用户信息失败");
        }
        if (user.getRice() < riceNum) {
            return ResponseUtil.fail("米不足");
        }
        //消耗米
        User userUpdate = new User();
        userUpdate.setId(user.getId());
        userUpdate.setRice(riceNum);
        int result = userDao.setDec(userUpdate);
        if (result == 0) {
            throw new ServiceException("扣除米失败");
        }
        // 添加米支出记录
        RiceRecord riceRecord = new RiceRecord();
        riceRecord.setUserId(user.getId());
        riceRecord.setRice(-riceNum);
        riceRecord.setContent("查看树谱支出");
        riceRecord.setCreateTime(DateUtils.getDateTime());
        result=riceRecordDao.insert(riceRecord);
        if(result==0){
            throw new ServiceException("添加米支出记录");
        }
        return ResponseUtil.ok("查看家族树");
    }


    @Override
    public JsonResult selectFamilyGenealogyByGenerationId(FamilyGenealogy fg) {
        QueryWrapper<FamilyGenealogy> wrapper = new QueryWrapper<FamilyGenealogy>();
        wrapper.eq("user_id", fg.getUserId());
        wrapper.eq("family_id", fg.getFamilyId());
        FamilyGenealogy familyGenealogy = familyGenealogyDao.selectList(wrapper).get(0);
        if (null != familyGenealogy) {
            QueryWrapper<FamilyGenealogyImg> imgWrapper = new QueryWrapper<FamilyGenealogyImg>();
            imgWrapper.eq("family_genealogy_id", familyGenealogy.getId());
            List<FamilyGenealogyImg> imgList = familyGenealogyImgService.list(imgWrapper);
            if (CollectionUtils.isNotEmpty(imgList)) {
                familyGenealogy.setImgList(FamilyGenealogyConvert.INSTANCE.convertToResVo(imgList));
            }

            QueryWrapper<FamilyGenealogyEditApply> applyWrapper = new QueryWrapper<FamilyGenealogyEditApply>();
            applyWrapper.eq("family_genealogy_id", familyGenealogy.getId());
            List<Integer> typeArr = Lists.newArrayList();
            typeArr.add(0);
            typeArr.add(1);
            applyWrapper.in("type", typeArr);
            applyWrapper.orderByDesc("id");
            List<FamilyGenealogyEditApply> applyList = familyGenealogyEditApplyService.list(applyWrapper);
            if (CollectionUtils.isNotEmpty(applyList)) {
                familyGenealogy.setApply(applyList.get(0));
            }

            QueryWrapper<FamilyGenealogyEditApply> applyWrapperPu = new QueryWrapper<FamilyGenealogyEditApply>();
            applyWrapperPu.eq("family_genealogy_id", familyGenealogy.getId());
            applyWrapperPu.orderByDesc("id");
            typeArr.add(2);
            typeArr.add(3);
            applyWrapperPu.in("type", typeArr);
            List<FamilyGenealogyEditApply> applyListPu = familyGenealogyEditApplyService.list(applyWrapperPu);
            if (CollectionUtils.isNotEmpty(applyListPu)) {
                familyGenealogy.setApplyPu(applyListPu.get(0));
            }
        }
        return ResponseUtil.ok("获取成功", familyGenealogy);
    }

    @Override
    public JsonResult selectFamilyGenealogyParentList(FamilyGenealogy f) {//个人谱
        FamilyGenealogy familyGenealogy = familyGenealogyDao.selectById(f.getId());
        System.out.println(familyGenealogy.getChart());
        int familyId = familyGenealogy.getFamilyId();
//        String jsonString  =  familyGenealogy.getChart();
//        System.out.println("谱：" + jsonString);
        List<FamilyGenealogy> list = new ArrayList<>();
        list.add(familyGenealogy);
        Integer parentId = familyGenealogy.getParentId();
        boolean pid = true;
        int a = 1;

        do {
            if (a > 500) {
                return ResponseUtil.fail("数据异常");
            }
            a++;
            System.out.println("do循环:" + a);

            if (parentId == null || parentId == -1) {
                System.out.println("parentId为null");
                pid = false;
            } else {

                FamilyGenealogy select = new FamilyGenealogy();
                select.setFamilyId(familyId);
                select.setUid(parentId);
                System.out.println("当前查看的父级:" + parentId);
//                System.out.println("调用是家族id刷新");
                familyGenealogy = familyGenealogyDao.selectFG(select).get(0);
                if (familyGenealogy.getUid() == familyGenealogy.getParentId()) {
                    pid = false;
                }
                System.out.println("循环判断：" + familyGenealogy.toString());
                System.out.println(familyGenealogy);
                parentId = familyGenealogy.getParentId();
                list.add(familyGenealogy);
            }

        } while (pid);


        QueryWrapper<FamilyGenealogy> wrapper = new QueryWrapper<FamilyGenealogy>();
        wrapper.eq("user_id", f.getUserId());
        wrapper.eq("family_id", familyGenealogy.getFamilyId());
        List<FamilyGenealogy> list1 = familyGenealogyDao.selectList(wrapper);
        if (list1.size() < 1) {
            return ResponseUtil.ok("获取成功", list);
        } else {
            familyGenealogy = list1.get(0);
        }

        System.out.println(familyGenealogy);

        List<FamilyGenealogy> listUser = new ArrayList<>();
        listUser.add(familyGenealogy);
        parentId = familyGenealogy.getParentId();
        pid = true;
        a = 1;
        do {
            if (a > 500) {
                return ResponseUtil.fail("数据异常");
            }
            a++;
            System.out.println("do循环:" + a);

            if (parentId == null || parentId == -1) {
                System.out.println("parentId为null");
                pid = false;
            } else {

                FamilyGenealogy select = new FamilyGenealogy();
                select.setFamilyId(familyId);
                select.setUid(parentId);
                familyGenealogy = familyGenealogyDao.selectFG(select).get(0);
                if (familyGenealogy.getUid() == familyGenealogy.getParentId()) {
                    pid = false;
                }

                System.out.println(familyGenealogy);
                parentId = familyGenealogy.getParentId();
                listUser.add(familyGenealogy);
            }

        } while (pid);
        System.out.println("===================?");
        System.out.println(listUser);
        System.out.println(list);
        for (FamilyGenealogy objA : list) {
            for (FamilyGenealogy objB : listUser) {
                if (objA.getUid() == objB.getUid()) {
                    objA.setRelevance(1);
//                    break; // 如果找到匹配的uid，则不再继续比较listB的其他对象
                }
            }
        }


        return ResponseUtil.ok("获取成功", list);

        //        // 使用Gson库解析字符串为List<LinkedHashMap<Integer, String>>对象
//        Gson gson = new Gson();
//        List<LinkedHashMap<Integer, String>> list = gson.fromJson(jsonString, new TypeToken<List<LinkedHashMap<Integer, String>>>() {}.getType());
//
//        // 将List<Map>转换为一个Map
//        Map<Integer, String> resultMap = new LinkedHashMap <>();
//        for (Map<Integer, String> map : list) {
//            for (Integer key : map.keySet()) {
//                int integerKey = key;
//                String value = map.get(key);
//                System.out.println(integerKey + " -> " + value);
//                resultMap.put(integerKey,value);
//            }
//        }
//
//
//        List<FamilyGenealogy> lists = new ArrayList<FamilyGenealogy>();
//        // 提取并打印键
//        for (Integer key : resultMap.keySet()) {
//            QueryWrapper<FamilyGenealogy>  wrapper = new QueryWrapper<FamilyGenealogy>();
//            wrapper.eq("uid",key);
//            wrapper.eq("family_id",familyId);
//            FamilyGenealogy genealogy = familyGenealogyDao.selectList(wrapper).get(0);
//            lists.add(genealogy);
//        }
//

    }


    public JsonResult getClaimShow(Integer userId, Integer familyId) {
        QueryWrapper<FamilyGenealogy> wrapper = new QueryWrapper<FamilyGenealogy>();
        wrapper.eq("user_id", userId);
        wrapper.eq("family_id", familyId);
        FamilyGenealogy familyGenealogy = familyGenealogyDao.selectList(wrapper).get(0);
        return ResponseUtil.ok("获取成功", familyGenealogy);
    }


    @Override
    public JsonResult deleteFamilyGenealogy(Integer id, Integer familyId, Integer uid, Integer gUserId) {
        familyGenealogyDao.deleteById(id);
        if (null != uid) {
            FamilyGenealogy familyGenealogy = new FamilyGenealogy();
            familyGenealogy.setUid(uid);
            familyGenealogyDao.updatePidIsNull(familyGenealogy);
        }
        if (gUserId != null) {
            FamilyUser familyUser = new FamilyUser();
            familyUser.setId(gUserId);
            familyUserDao.updateJoins(familyUser);
        }
//        this.upFamilyGenealogyChart(familyId);

        return ResponseUtil.ok("删除成功");
    }

    @Override
    public JsonResult getFamilyGenealogyTreeByFamilyId(Integer familyId, Integer parentId, Integer level) {
        List<FamilyGenealogyTreeResVo> result = Lists.newArrayList();
        QueryWrapper<FamilyGenealogy> wrapper = new QueryWrapper<FamilyGenealogy>();
        wrapper.eq("family_id", familyId);
        if (null != parentId) {
            wrapper.eq("uid", parentId);
        } else {
            wrapper.isNull("parent_id").or()
                    .eq("parent_id", -1);
            wrapper.eq("generation", "1");
        }
        wrapper.orderByAsc("ranking");
        List<FamilyGenealogy> familyGenealogy = familyGenealogyDao.selectList(wrapper);
        FamilyGenealogyTreeResVo tmpVo = new FamilyGenealogyTreeResVo();
        tmpVo.setTotal(0);
        if (null != familyGenealogy && familyGenealogy.size() > 0) {
            boolean flag = familyGenealogy.stream().anyMatch(f -> null == f.getRanking() || f.getRanking() == 0);
            for (FamilyGenealogy child : familyGenealogy) {
                tmpVo.setTotal(tmpVo.getTotal() + 1);
                FamilyGenealogyTreeResVo vo = FamilyGenealogyConvert.INSTANCE.convertToTree(child);
                vo.setRanking(flag ? null : vo.getRanking());
                Integer userId = child.getUserId();
                if (null != userId) {
                    User user = userService.selectUserById(userId);
                    vo.setHeadImg(null != user ? user.getAvatar() : "");
                }
                vo.setColor(child.getSex());
                result.add(vo);
                getFamilyGenealogyParentId(vo, familyId, child.getUid(), level - 1, tmpVo);
            }

        }
        //设置颜色
        for (FamilyGenealogyTreeResVo vo : result) {
            vo.setColor(vo.getSex());
            if (vo.getSex() == 2) {
                setColor2(vo.getChildren());
            } else {
                setColor(vo.getChildren());
            }
            vo.setTotal(tmpVo.getTotal());
        }
        System.out.println(tmpVo.getTotal());
        return ResponseUtil.ok("获取成功", result);
    }

    /**
     * 递归设置女儿后代所有家族的颜色
     *
     * @param children
     */
    public void setColor2(List<FamilyGenealogyTreeResVo> children) {
        if (null != children && children.size() > 0) {
            for (FamilyGenealogyTreeResVo vo : children) {
                vo.setColor(2);
                setColor2(vo.getChildren());
            }
        }
    }

    public void setColor(List<FamilyGenealogyTreeResVo> children) {
        if (null != children && children.size() > 0) {
            for (FamilyGenealogyTreeResVo vo : children) {
                vo.setColor(vo.getSex());
                if (vo.getSex() == 2) {
                    setColor2(vo.getChildren());
                } else {
                    setColor(vo.getChildren());
                }
            }
        }
    }

    public void getFamilyGenealogyParentId(FamilyGenealogyTreeResVo treeVo, Integer familyId, Integer parentId, Integer n, FamilyGenealogyTreeResVo tmpVo) {
        List<FamilyGenealogyTreeResVo> result = Lists.newArrayList();
        QueryWrapper<FamilyGenealogy> wrapper = new QueryWrapper<FamilyGenealogy>();
        wrapper.eq("family_id", familyId);
        wrapper.eq("parent_id", parentId);
        wrapper.orderByAsc("ranking");
        List<FamilyGenealogy> children = familyGenealogyDao.selectList(wrapper);

        if (null != children && !children.isEmpty() && n > 0) {
            n--;
            boolean flag = children.stream().anyMatch(f -> null == f.getRanking() || f.getRanking() == 0);
            for (FamilyGenealogy child : children) {
                FamilyGenealogyTreeResVo vo = FamilyGenealogyConvert.INSTANCE.convertToTree(child);
                vo.setRanking(flag ? null : vo.getRanking());
                Integer userId = child.getUserId();
                if (null != userId) {
                    User user = userService.selectUserById(userId);
                    vo.setHeadImg(null != user ? user.getAvatar() : "");
                }
                tmpVo.setTotal(tmpVo.getTotal() + 1);
                result.add(vo);
//                System.out.println(n + "--" + child.getGeneration() + "--" + child.getGenealogyName() );
                getFamilyGenealogyParentId(vo, familyId, child.getUid(), n, tmpVo);
            }
            treeVo.setChildren(result);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult updateResumeById(FamilyGenealogyIntroduceReqVo introduceReqVo) throws Exception {
        FamilyGenealogy familyGenealogyUpdate = new FamilyGenealogy();
        ObjectUtil.copyByName(introduceReqVo, familyGenealogyUpdate);
        this.updateById(familyGenealogyUpdate);

        List<FamilyGenealogyImg> userImgList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(introduceReqVo.getEssayImgInsertReqVoList())) {
            long cnt = familyGenealogyImgService.count(Wrappers.lambdaQuery(FamilyGenealogyImg.class).eq(FamilyGenealogyImg::getFamilyGenealogyId, familyGenealogyUpdate.getId()));
            if (cnt > 0) {
                familyGenealogyImgService.deleteByFamilyGenealogyId(familyGenealogyUpdate.getId());
            }
            for (EssayImgInsertReqVo img : introduceReqVo.getEssayImgInsertReqVoList()) {
                FamilyGenealogyImg userImg = new FamilyGenealogyImg();
                userImg.setFamilyGenealogyId(familyGenealogyUpdate.getId());
                userImg.setImg(img.getImg());
                userImgList.add(userImg);
            }
            familyGenealogyImgService.saveBatch(userImgList);
        } else {
            long cnt = familyGenealogyImgService.count(Wrappers.lambdaQuery(FamilyGenealogyImg.class).eq(FamilyGenealogyImg::getFamilyGenealogyId, familyGenealogyUpdate.getId()));
            if (cnt > 0) {
                familyGenealogyImgService.deleteByFamilyGenealogyId(familyGenealogyUpdate.getId());
            }
        }

        return ResponseUtil.ok("修改成功");
    }

    public List<FamilyGenealogy> forDirectDescendants(List<FamilyGenealogy> list) {
        List<FamilyGenealogy> result = new ArrayList<>(list);
        for (FamilyGenealogy family : list) {
            QueryWrapper<FamilyGenealogy> wrapperG = new QueryWrapper<>();
            wrapperG.eq("family_id", family.getFamilyId());
            wrapperG.eq("parent_id", family.getUid());
            List<FamilyGenealogy> child = this.familyGenealogyDao.selectList(wrapperG);
            result.addAll(forDirectDescendants(child));
        }
        return result;
    }

    @Override
    public JsonResult countFamilyGenealogyDirectDescendants(Integer familyId, Integer uid) {
        QueryWrapper<FamilyGenealogy> wrapperG = new QueryWrapper<FamilyGenealogy>();
        wrapperG.eq("family_id", familyId);
        wrapperG.eq("uid", uid);
        List<FamilyGenealogy> list = this.familyGenealogyDao.selectList(wrapperG);
        if (list.isEmpty()) {
            return ResponseUtil.fail("查询家族图谱失败！");
        }
        List<FamilyGenealogy> newlist = new ArrayList<>(forDirectDescendants(list));
        newlist.remove(0);
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("list", newlist);
        map.put("total", newlist.size());
        map.put("aliveCount", newlist.stream()
                .filter(n -> n.getIsAlive() == 1)  // 条件：1 为 在世
                .count());
        map.put("deadCount", newlist.stream()
                .filter(n -> n.getIsAlive() == 0)  // 条件：0 为 去世
                .count());
        return ResponseUtil.ok("查询成功", map);
    }

}
