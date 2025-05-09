package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.Essay;
import com.wx.genealogy.system.entity.EssayImg;
import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.service.EssayService;
import com.wx.genealogy.system.service.FamilyUserService;
import com.wx.genealogy.system.vo.req.EssayInsertReqVo;
import com.wx.genealogy.system.vo.req.EssayUpdateReqVo;
import com.wx.genealogy.system.vo.req.UnlockEssayUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-09
 */
@RestController
@RequestMapping("/essay")
@Api(tags = "文章接口")
public class EssayController {

    @Autowired
    private EssayService essayService;

    @Resource
    private FamilyUserService familyUserService;

    @ApiOperation(value = "发布文章")
    @RequestMapping(value = "/insertEssay", method = RequestMethod.POST)
    public JsonResult insertEssay(@RequestBody EssayInsertReqVo essayInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(essayInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(essayInsertReqVo.getOpen())) {
            throw new MissingServletRequestParameterException("open", "number");
        }
        if (ObjectUtil.isNull(essayInsertReqVo.getFamilyId())) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }

        long nowTime = System.currentTimeMillis() / 1000;

        Essay essayInsert = new Essay();
        essayInsert.setUserId(essayInsertReqVo.getUserId());
        essayInsert.setFamilyId(essayInsertReqVo.getFamilyId());
        essayInsert.setContent(essayInsertReqVo.getContent());
        essayInsert.setAddress(essayInsertReqVo.getAddress());
        essayInsert.setOpen(essayInsertReqVo.getOpen());
        essayInsert.setCreateTime(DateUtils.getDateTime());
        essayInsert.setKnitCycle(2592000);//30天（测试时建议用一天）
        essayInsert.setKnitStartTime(nowTime);
        essayInsert.setKnitEndTime(nowTime + essayInsert.getKnitCycle());//加上周期就是被锁触发时间

        List<EssayImg> essayImgListInsert = new ArrayList<EssayImg>();
        for (int i = 0; i < essayInsertReqVo.getEssayImgInsertReqVoList().size(); i++) {
            EssayImg essayImg = new EssayImg();
            essayImg.setImg(essayInsertReqVo.getEssayImgInsertReqVoList().get(i).getImg());
            essayImgListInsert.add(essayImg);
        }

        return essayService.insertEssay(essayInsert, essayImgListInsert);
    }

    @ApiOperation(value = "分享文章")
    @RequestMapping(value = "/shareEssay", method = RequestMethod.POST)
    public JsonResult shareEssay(@RequestBody EssayInsertReqVo essayInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(essayInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(essayInsertReqVo.getOpen())) {
            throw new MissingServletRequestParameterException("open", "number");
        }
        if (ObjectUtil.isNull(essayInsertReqVo.getFamilyId())) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }

        long nowTime = System.currentTimeMillis() / 1000;

        Essay essayInsert = new Essay();
        essayInsert.setUserId(essayInsertReqVo.getUserId());
        essayInsert.setFamilyId(essayInsertReqVo.getFamilyId());
        essayInsert.setContent(essayInsertReqVo.getContent());
        essayInsert.setAddress(essayInsertReqVo.getAddress());
        essayInsert.setOpen(essayInsertReqVo.getOpen());
        essayInsert.setCreateTime(DateUtils.getDateTime());
        essayInsert.setKnitCycle(2592000);//30天（测试时建议用一天）
        essayInsert.setKnitStartTime(nowTime);
        essayInsert.setKnitEndTime(nowTime + essayInsert.getKnitCycle());//加上周期就是被锁触发时间

        List<EssayImg> essayImgListInsert = new ArrayList<EssayImg>();
        for (int i = 0; i < essayInsertReqVo.getEssayImgInsertReqVoList().size(); i++) {
            EssayImg essayImg = new EssayImg();
            essayImg.setImg(essayInsertReqVo.getEssayImgInsertReqVoList().get(i).getImg());
            essayImgListInsert.add(essayImg);
        }

        return essayService.insertEssay(essayInsert, essayImgListInsert);
    }


    @ApiOperation(value = "根据用户id分页查询")
    @RequestMapping(value = "/selectEssayByUserId", method = RequestMethod.GET)
    public JsonResult selectEssayByUserId(@RequestParam(value = "limit") Integer limit,
                                          @RequestParam(value = "page") Integer page,
                                          @RequestParam(value = "userId") Integer userId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        Essay essay = new Essay();
        essay.setUserId(userId);
        return essayService.selectEssayByUserId(page, limit, essay);
    }

    @ApiOperation(value = "根据家族id和浏览者id分页查询")
    @RequestMapping(value = "/selectEssayByFamilyId", method = RequestMethod.GET)
    public JsonResult selectEssayByFamilyId(@RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "page") Integer page,
                                            @RequestParam(value = "familyId") Integer familyId,
                                            @RequestParam(value = "userId") Integer userId,
                                            @RequestParam(value = "authorId", required = false) Integer authorId,

                                            @RequestParam(value = "searchData", required = false) String searchData,

                                            @RequestParam(value = "sort", required = false) Integer sort) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        Essay essay = new Essay();
        essay.setUserId(authorId);
        essay.setFamilyId(familyId);

        FamilyUser familyUser = new FamilyUser();
        familyUser.setUserId(userId);

        return essayService.selectEssayByFamilyId(page, limit, essay, familyUser, sort, searchData);
    }

    @ApiOperation(value = "根据文章id和浏览者id查询单个")
    @RequestMapping(value = "/findEssayById", method = RequestMethod.GET)
    public JsonResult findEssayById(@RequestParam(value = "id") Integer id,
                                    @RequestParam(value = "userId") Integer userId) throws Exception {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        Essay essay = new Essay();
        essay.setId(id);
        essay.setUserId(userId);//当前id为浏览者id
        return essayService.findEssayById(essay);
    }

    @ApiOperation(value = "根据用户id分页查询加入和关注的(首页专供)")
    @RequestMapping(value = "/selectEssay", method = RequestMethod.GET)
    public JsonResult selectEssay(@RequestParam(value = "limit") Integer limit,
                                  @RequestParam(value = "page") Integer page,
                                  @RequestParam(value = "userId") Integer userId,
                                  @RequestParam(value = "sort", required = false) Integer sort) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        Essay essay = new Essay();
        essay.setUserId(userId);
        return essayService.selectEssayByOpen(page, limit, essay, sort);
    }

    @ApiOperation(value = "根据id修改文章")
    @RequestMapping(value = "/updateEssayById", method = RequestMethod.PUT)
    public JsonResult updateEssayById(@RequestBody EssayUpdateReqVo essayUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(essayUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        Essay essayUpdate = new Essay();
        ObjectUtil.copyByName(essayUpdateReqVo, essayUpdate);
        return essayService.updateEssayById(essayUpdate);
    }

    @ApiOperation(value = "根据id修改文章访问量")
    @RequestMapping(value = "/updateEssayBrowseNumberById", method = RequestMethod.PUT)
    public JsonResult updateEssayBrowseNumberById(@RequestBody EssayUpdateReqVo essayUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(essayUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        if (ObjectUtil.isNull(essayUpdateReqVo.getBrowseNumber())) {
            throw new MissingServletRequestParameterException("browseNumber", "number");
        }

        long nowTime = System.currentTimeMillis() / 1000;

        Essay essayUpdate = new Essay();
        essayUpdate.setId(essayUpdateReqVo.getId());
        essayUpdate.setUserId(essayUpdateReqVo.getUserId());//这里的id是浏览者id
        essayUpdate.setBrowseNumber(essayUpdateReqVo.getBrowseNumber());
        essayUpdate.setKnitStartTime(nowTime);
        return essayService.updateEssayBrowseNumberById(essayUpdate);
    }

    @ApiOperation(value = "根据id解锁文章")
    @RequestMapping(value = "/unlockEssayById", method = RequestMethod.PUT)
    public JsonResult unlockEssayById(@RequestBody UnlockEssayUpdateReqVo unlockEssayUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(unlockEssayUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }

        long nowTime = System.currentTimeMillis() / 1000;

        Essay essay = new Essay();
        essay.setId(unlockEssayUpdateReqVo.getId());
        essay.setUserId(unlockEssayUpdateReqVo.getUserId());
        essay.setKnit(0);
        essay.setKnitCycle(unlockEssayUpdateReqVo.getMultiple() * 2592000);//测试是1天为基本单位
        essay.setKnitStartTime(nowTime);
        essay.setKnitEndTime(nowTime + essay.getKnitCycle());
        return essayService.updateEssayUnlockById(essay);
    }

    @RequestMapping(value = "/deleteEssayById", method = RequestMethod.DELETE)
    @ApiOperation("根据id删除单个功能")
    public JsonResult deleteEssayById(@RequestParam("id") Integer id) throws Exception {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        return essayService.deleteEssayById(id);

    }

    @RequestMapping(value = "/selectotherEssay", method = RequestMethod.GET)
    @ApiOperation("查找别人的essay")
    public JsonResult selectotherEssay(@RequestParam(value = "limit") Integer limit,
                                       @RequestParam(value = "page") Integer page, @RequestParam("sheuserId") Integer sheuserId, @RequestParam("userId") Integer userId) throws Exception {
        if (ObjectUtil.isNull(sheuserId)) {
            throw new MissingServletRequestParameterException("sheuserId", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        return essayService.selectotherEssay(limit, page, sheuserId, userId);

    }

    /**
     * 修改文章开放/保密权限
     * @param id
     * @param userId
     * @param open
     * @return
     * @throws Exception
     */
    @GetMapping("/upEssayOpenById")
    @ApiOperation(value = "修改文章开放/保密权限")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "当前数据id", dataType = "java.lang.Integer", paramType = "path", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "java.lang.Integer", paramType = "path", required = true),
            @ApiImplicitParam(name = "open", value = "是否开放(开放1保密2家族内公开3完全公开)", dataType = "java.lang.Integer", paramType = "path", required = true),
    })
    public JsonResult upEssayOpenById(Integer id, Integer userId,Integer open) throws Exception {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id,", "为空");
        }

        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "为空");
        }

        Essay essayById = essayService.selectEssayById(id);

        if (ObjectUtil.isNull(essayById)) {
            throw new MissingServletRequestParameterException("essayById", "数据不存在");
        }

        //判断是创建者
        if (essayById.getUserId().equals(userId)) {
            essayById.setOpen(open);
            if (essayService.updateById(essayById)) {
                return ResponseUtil.ok("操作成功");
            }
        }

        //判断是否是家族管理员
        FamilyUser familyUserSelect = new FamilyUser();
        familyUserSelect.setUserId(essayById.getUserId());
        familyUserSelect.setStatus(2);

        //文章的创建者家族
        List<FamilyUser> familyUserList = familyUserService.selectFamilyByUserId(familyUserSelect);

        //修改者家族
        familyUserSelect = new FamilyUser();
        familyUserSelect.setUserId(userId);
        familyUserSelect.setStatus(2);
        List<FamilyUser> userList = familyUserService.selectFamilyByUserId(familyUserSelect);

        if (ObjectUtil.isNotNull(familyUserList) && ObjectUtil.isNotNull(userList)) {
            for (FamilyUser familyUser : familyUserList) {
                for (FamilyUser user : userList) {
                    //相同
                    if (familyUser.getFamilyId().equals(user.getFamilyId())) {
                        if (user.getLevel() == 1 || user.getLevel() == 2) {
                            essayById.setOpen(open);
                            if (essayService.updateById(essayById)) {
                                return ResponseUtil.ok("操作成功");
                            }
                        }

                    }
                }
            }
        }

        return ResponseUtil.fail("没有权限");
    }
}

