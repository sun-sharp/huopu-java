package com.wx.genealogy.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ExcelUtil;
import com.wx.genealogy.common.util.ExcelUtils.EasyExcelCellWriteHandler;
import com.wx.genealogy.common.util.ExcelUtils.EasyExcelSheetWriteHandler;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.convert.FamilyGenealogyConvert;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.service.*;
import com.wx.genealogy.system.vo.req.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 家谱图 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-10-20
 */
@RestController
@RequestMapping("/familyGenealogy")
@Api(tags = "家谱图接口")
public class FamilyGenealogyController {


    @Autowired
    private FamilyGenealogyService familyGenealogyService;

    @Autowired
    FamilyPedigreeApplyService familyPedigreeApplyService;

    @Resource
    private FamilyService familyService;

    @Resource
    private UserService userService;

    @Autowired
    private FamilyGenealogyImgService familyGenealogyImgService;

    @Resource
    private FamilyGenealogyEditApplyService familyGenealogyEditApplyService;

    @Resource
    private FamilyMessageService familyMessageService;

    @ApiOperation(value = "创建族谱图")
    @RequestMapping(value = "/insertFamilyGenealogy", method = RequestMethod.POST)
    public JsonResult insertFamilyGenealogy(@RequestBody FamilyGenealogyInsertReqVo familyGenealogyInsertReqVo) throws Exception {
        if (ObjectUtil.isNull(familyGenealogyInsertReqVo)) {
            return ResponseUtil.fail("数据不能为空");
        }

        FamilyGenealogy familyGenealogyInsert = new FamilyGenealogy();

        ObjectUtil.copyByName(familyGenealogyInsertReqVo, familyGenealogyInsert);
        familyGenealogyInsert.setCreateTime(DateUtils.getDateTime());

        return familyGenealogyService.insertFamilyGenealogy(familyGenealogyInsert);
    }


    @ApiOperation(value = "取消认领")
    @RequestMapping(value = "/cancelClaim", method = RequestMethod.POST)
    public JsonResult cancelClaim(@RequestBody FamilyGenealogy familyGenealogy) {
        System.out.println(familyGenealogy);
        return familyGenealogyService.cancelClaim(familyGenealogy);
    }

    @ApiOperation(value = "创建族谱图")
    @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
    public JsonResult insert(@RequestBody FamilyGenealogy familyGenealogy) {
        System.out.println("***********");
        System.out.println(familyGenealogy.toString());
//        if (ObjectUtil.isNull(familyGenealogy)) {
//            return ResponseUtil.fail("数据不能为空");
//        }
        familyGenealogy.setUserId(null);
        return familyGenealogyService.insertData(familyGenealogy);
    }


    @ApiOperation(value = "认领按钮显示")
    @GetMapping(value = "/getDisparity")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "userId", value = "用户id", dataType = "java.lang.Integer", paramType = "query", required = true), @ApiImplicitParam(name = "familyId", value = "家族id", dataType = "java.lang.Integer", paramType = "query", required = true),})
    public JsonResult getClaimShow(Integer userId, Integer familyId) {

        return familyGenealogyService.getClaimShow(userId, familyId);
    }

    @ApiOperation(value = "删除家谱成员")
    @GetMapping(value = "/deleteFamilyGenealogy")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "userId", value = "用户id", dataType = "java.lang.Integer", paramType = "query", required = true), @ApiImplicitParam(name = "familyId", value = "家族id", dataType = "java.lang.Integer", paramType = "query", required = true), @ApiImplicitParam(name = "id", value = "id", dataType = "java.lang.Integer", paramType = "query", required = true), @ApiImplicitParam(name = "uid", value = "uid", dataType = "java.lang.Integer", paramType = "query", required = true), @ApiImplicitParam(name = "gUserId", value = "删除用户id", dataType = "java.lang.Integer", paramType = "query", required = true),})
    public JsonResult deleteFamilyGenealogy(Integer id, Integer familyId, Integer uid, Integer gUserId) {

        return familyGenealogyService.deleteFamilyGenealogy(id, familyId, uid, gUserId);
    }

    @ApiOperation(value = "根据家族id和家族成员id查询当前个人族谱图")
    @RequestMapping(value = "/selectFamilyGenealogyByFamilyId", method = RequestMethod.GET)
    public JsonResult selectFamilyGenealogyByFamilyId(@RequestParam(value = "familyId") Integer familyId, @RequestParam(value = "familyUserId") Integer familyUserId) throws Exception {
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }
//        if (ObjectUtil.isNull(familyUserId)) {
//            throw new MissingServletRequestParameterException("familyUserId", "number");
//        }
        FamilyGenealogy familyGenealogySelect = new FamilyGenealogy();
        familyGenealogySelect.setFamilyId(familyId);
        //familyGenealogySelect.setFamilyUserId(familyUserId);
        return familyGenealogyService.selectFamilyGenealogy(familyGenealogySelect);
    }


    @ApiOperation(value = "根据家族id和代数查询当代家谱图中的直系")
    @GetMapping(value = "/selectFamilyGenealogyByGeneration")
    public JsonResult selectFamilyGenealogyByGeneration(Integer familyId, Integer generation, String applyName, Integer userId, Integer generationNum, Integer limit, Integer page) throws Exception {
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }
//        if (ObjectUtil.isNull(generation)) {
//            throw new MissingServletRequestParameterException("generation", "number");
//        }

        FamilyGenealogy familyGenealogy = new FamilyGenealogy();
        familyGenealogy.setFamilyId(familyId);
        familyGenealogy.setGeneration(generation);
        familyGenealogy.setUserId(userId);
        if (!ObjectUtil.isNull(applyName)) {
            familyGenealogy.setGenealogyName(applyName);
        }
        if (!ObjectUtil.isNull(generationNum)) {
            familyGenealogy.setGenerationNum(generationNum);
        }
//        familyGenealogy.setStatus(1);
        return familyGenealogyService.selectFamilyGenealogyByGeneration(familyGenealogy, limit, page);
    }

    @ApiOperation(value = "根据家族id和用户查询家谱id")
    @GetMapping(value = "/selectFamilyGenealogyByGenerationId")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "familyId", value = "家族id", dataType = "java.lang.Integer", paramType = "query", required = true), @ApiImplicitParam(name = "userId", value = "userId", dataType = "java.lang.Integer", paramType = "query", required = true),})
    public JsonResult selectFamilyGenealogyByGenerationId(Integer familyId, Integer userId) throws Exception {
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        FamilyGenealogy familyGenealogy = new FamilyGenealogy();
        familyGenealogy.setFamilyId(familyId);
        familyGenealogy.setUserId(userId);
        return familyGenealogyService.selectFamilyGenealogyByGenerationId(familyGenealogy);
    }

    @ApiOperation(value = "根据谱id查询这个人的谱")
    @GetMapping(value = "/selectFamilyGenealogyParentList")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "id", value = "id", dataType = "java.lang.Integer", paramType = "query", required = true), @ApiImplicitParam(name = "userId", value = "userId", dataType = "java.lang.Integer", paramType = "query", required = true),})
    public JsonResult selectFamilyGenealogyParentList(Integer id, Integer userId) throws Exception {

        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        FamilyGenealogy familyGenealogy = new FamilyGenealogy();
        familyGenealogy.setUserId(userId);
        familyGenealogy.setId(id);
        return familyGenealogyService.selectFamilyGenealogyParentList(familyGenealogy);
    }


    @ApiOperation(value = "根据家族id和代数查询当代家谱图中的直系")
    @GetMapping(value = "/selectFamilyGenealogy")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "familyId", value = "家族id", dataType = "java.lang.Integer", paramType = "query", required = true), @ApiImplicitParam(name = "applyName", value = "申请人名称", dataType = "java.lang.String", paramType = "query"),})
    public JsonResult selectFamilyGenealogy(Integer familyId, String applyName) throws Exception {
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }

        FamilyGenealogy familyGenealogy = new FamilyGenealogy();
        familyGenealogy.setFamilyId(familyId);
        if (StringUtils.isNotBlank(applyName)) {
            familyGenealogy.setApplyName(applyName);
        }
        familyGenealogy.setStatus(1);
        return familyGenealogyService.selectFamilyGenealogyList(familyGenealogy);
    }

    @ApiOperation(value = "根据家族id和家族成员id查询当前个人族谱图所有")
    @RequestMapping(value = "/selectAllFamilyGenealogyByFamilyId", method = RequestMethod.GET)
    public JsonResult selectAllFamilyGenealogyByFamilyId(@RequestParam(value = "limit") Integer limit, @RequestParam(value = "page") Integer page, @RequestParam(value = "familyId") Integer familyId, @RequestParam(value = "familyUserId") Integer familyUserId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }
        if (ObjectUtil.isNull(familyUserId)) {
            throw new MissingServletRequestParameterException("familyUserId", "number");
        }
        FamilyGenealogy familyGenealogySelect = new FamilyGenealogy();
        familyGenealogySelect.setFamilyId(familyId);
        familyGenealogySelect.setFamilyUserId(familyUserId);
        return familyGenealogyService.selectAllFamilyGenealogy(familyGenealogySelect);
    }

    @ApiOperation(value = "根据id修改族谱图")
    @RequestMapping(value = "/updateFamilyGenealogyById", method = RequestMethod.PUT)
    public JsonResult updateFamilyGenealogyById(@RequestBody FamilyGenealogyUpdateReqVo familyGenealogyUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(familyGenealogyUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        FamilyGenealogy familyGenealogyUpdate = new FamilyGenealogy();
        ObjectUtil.copyByName(familyGenealogyUpdateReqVo, familyGenealogyUpdate);
        return familyGenealogyService.updateFamilyGenealogyById(familyGenealogyUpdate);
    }

    @ApiOperation(value = "根据家族id和家族成员id删除族谱图")
    @RequestMapping(value = "/deleteFamilyGenealogyByFamilyId", method = RequestMethod.DELETE)
    public JsonResult updateFamilyGenealogyById(@RequestParam(value = "familyId") Integer familyId, @RequestParam(value = "familyUserId") Integer familyUserId) throws Exception {

        FamilyGenealogy familyGenealogy = new FamilyGenealogy();
        familyGenealogy.setFamilyId(familyId);
        familyGenealogy.setFamilyUserId(familyUserId);
        return familyGenealogyService.deleteFamilyGenealogyById(familyGenealogy);
    }

    @ApiOperation(value = "根据家族id和家族成员id删除族谱图")
    @RequestMapping(value = "/deleteFamilyGenealogyBylevel", method = RequestMethod.POST)
    public JsonResult deleteFamilyGenealogyBylevel(@RequestBody FamilyGenealogyDeleteReqVo familyGenealogyDeleteReqVo) throws Exception {
        return familyGenealogyService.deleteFamilyGenealogyBylevel(familyGenealogyDeleteReqVo);
    }


    @ApiOperation(value = "引用")
    @RequestMapping(value = "/getFamilyGenealogyBylevel", method = RequestMethod.GET)
    public JsonResult getFamilyGenealogyBylevel(@RequestParam(value = "familyId") Integer familyId, @RequestParam(value = "familyId1") Integer familyId1, @RequestParam(value = "familyUserId") Integer familyUserId,

                                                @RequestParam(value = "userId") Integer userId

    ) throws Exception {
        return familyGenealogyService.getFamilyGenealogyBylevel(familyId, familyUserId, familyId1, userId);
    }


    @ApiOperation(value = "申请加入族谱图")
    @PostMapping("/apply")
    public JsonResult apply(@RequestBody FamilyGenealogy familyGenealogy) {
        return this.familyGenealogyService.apply(familyGenealogy);
    }

    @PostMapping("/auditFamilyGenealogy")
    @ApiOperation(value = "审核族谱图申请")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "userId", value = "当前审核人用户id", dataType = "java.lang.Integer", paramType = "body", required = true), @ApiImplicitParam(name = "id", value = "审核数据id", dataType = "java.lang.Integer", paramType = "body", required = true), @ApiImplicitParam(name = "status", value = "审核状态（0待审核 1审核通过 2拒绝）", dataType = "java.lang.Integer", paramType = "body", required = true), @ApiImplicitParam(name = "refuse", value = "拒绝原因（不必填）", dataType = "java.lang.String", paramType = "body"),})
    public JsonResult auditFamilyGenealogy(@RequestBody @ApiIgnore FamilyGenealogy genealogy) {
        return this.familyGenealogyService.auditFamilyGenealogy(genealogy);
    }


    /**
     * 查询我的族谱图申请记录
     *
     * @param userId 登录人id
     * @return
     */
    @GetMapping("/selectMyApply")
    @ApiOperation(value = "查询我的族谱图申请记录")
    @ApiImplicitParam(name = "userId", value = "当前审核人用户id", dataType = "java.lang.Integer", paramType = "query", required = true)
    public JsonResult selectMyApply(Integer userId) {
        if (ObjectUtil.isNull(userId)) {
            return ResponseUtil.fail("当前审核人用户id不能为空");
        }
        FamilyGenealogy familyGenealogy = new FamilyGenealogy();
        familyGenealogy.setUserId(userId);
        return familyGenealogyService.selectFamilyGenealogyList(familyGenealogy);
    }


    /**
     * 查询需要审核的申请
     *
     * @param familyId 当前家族id
     * @return
     */
    @GetMapping("/selectAuditApply")
    @ApiOperation(value = "查询需要审核的申请")
    @ApiImplicitParam(name = "familyId", value = "当前家族id", dataType = "java.lang.Integer", paramType = "query", required = true)
    public JsonResult selectAuditApply(Integer familyId) {

        if (ObjectUtil.isNull(familyId)) {
            return ResponseUtil.fail("当前家族id不能为空");
        }

        FamilyGenealogy familyGenealogy = new FamilyGenealogy();
        familyGenealogy.setFamilyId(familyId);
        familyGenealogy.setStatus(0);
        return familyGenealogyService.selectFamilyGenealogyList(familyGenealogy);
    }


    /**
     * 查询族谱图详情
     *
     * @param id 当前查询id
     * @return
     */
    @GetMapping("/getFamilyGenealogyById")
    @ApiOperation(value = "查询族谱图详情")
    @ApiImplicitParam(name = "id", value = "当前查询id", dataType = "java.lang.Integer", paramType = "query", required = true)
    public JsonResult getFamilyGenealogyById(Integer id) {

        if (ObjectUtil.isNull(id)) {
            return ResponseUtil.fail("id不能为空");
        }
        FamilyGenealogy familyGenealogy = this.familyGenealogyService.getById(id);
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
            List<Integer> typeArrPu = Lists.newArrayList();
            typeArrPu.add(2);
            typeArrPu.add(3);
            applyWrapperPu.in("type", typeArrPu);
            List<FamilyGenealogyEditApply> applyListPu = familyGenealogyEditApplyService.list(applyWrapperPu);
            if (CollectionUtils.isNotEmpty(applyListPu)) {
                familyGenealogy.setApplyPu(applyListPu.get(0));
            }

            /* 查询头像 */
            Integer userId = familyGenealogy.getUserId();
            if (userId != null) {
                User user = this.userService.getById(userId);
                if (null != user) {
                    familyGenealogy.setHeadImg(user.getAvatar());
                }
            }
        }
        return ResponseUtil.ok("查询成功", familyGenealogy);
    }


    /**
     * 查询当前族谱图人上一代
     *
     * @param parentId 当前数据的parentId
     * @return
     */
    @GetMapping("/getFamilyGenealogyUserUp")
    @ApiOperation(value = "查询当前族谱图人上一代")
    @ApiImplicitParam(name = "parentId", value = "当前数据的parentId", dataType = "java.lang.Integer", paramType = "query", required = true)
    public JsonResult getFamilyGenealogyUserUp(Integer parentId) {

        if (ObjectUtil.isNull(parentId)) {
            return ResponseUtil.fail("parentId不能为空");
        }
        return ResponseUtil.ok("查询成功", this.familyGenealogyService.getById(parentId));
    }


    /**
     * 查询当前族谱图人下一代
     *
     * @param id 当前查询id
     * @return
     */
    @GetMapping("/getFamilyGenealogyUserUnder")
    @ApiOperation(value = "查询当前族谱图人下一代")
    @ApiImplicitParam(name = "id", value = "当前数据id", dataType = "java.lang.Integer", paramType = "query", required = true)
    public JsonResult getFamilyGenealogyUserUnder(Integer id) {

        if (ObjectUtil.isNull(id)) {
            return ResponseUtil.fail("id不能为空");
        }
        LambdaQueryWrapper<FamilyGenealogy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyGenealogy::getParentId, id);

        return ResponseUtil.ok("查询成功", this.familyGenealogyService.list(wrapper));
    }

    /**
     * 统计当前人物的直系后代情况
     *
     * @param familyId 家族id
     * @param uid uid
     * @return
     */
    @GetMapping("/countFamilyGenealogyDirectDescendants")
    @ApiOperation(value = "统计当前人物的直系后代情况")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "familyId", value = "家族id", dataType = "java.lang.Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "uid", value = "uid", dataType = "java.lang.Integer", paramType = "query", required = true),
    })
    public JsonResult countFamilyGenealogyDirectDescendants(Integer familyId, Integer uid) {
        if (ObjectUtil.isNull(familyId)) {
            return ResponseUtil.fail("家族id不能为空");
        }
        if (ObjectUtil.isNull(uid)) {
            return ResponseUtil.fail("uid不能为空");
        }

        return this.familyGenealogyService.countFamilyGenealogyDirectDescendants(familyId, uid);
    }

    /**
     * 管理员修改族谱图信息
     *
     * @param familyGenealogy
     * @return
     */
    @PostMapping("/upFamilyGenealogyInfo")
    @ApiOperation(value = "管理员修改族谱图信息")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "userId", value = "当前用户id", dataType = "java.lang.Integer", paramType = "body", required = true), @ApiImplicitParam(name = "id", value = "修改数据id", dataType = "java.lang.Integer", paramType = "body", required = true), @ApiImplicitParam(name = "headImg", value = "头像图", dataType = "java.lang.Integer", paramType = "body"), @ApiImplicitParam(name = "isAlive", value = "是否在世（0否 1是）", dataType = "java.lang.Integer", paramType = "body"), @ApiImplicitParam(name = "parentId", value = "上级id", dataType = "java.lang.Integer", paramType = "body"), @ApiImplicitParam(name = "applyName", value = "申请人名称", dataType = "java.lang.Integer", paramType = "body"),})
    public JsonResult upFamilyGenealogyInfo(@RequestBody @ApiIgnore FamilyGenealogy familyGenealogy) {
        return this.familyGenealogyService.upFamilyGenealogyInfo(familyGenealogy);
    }

    @GetMapping("/upFamilyGenealogyChart")
    @ApiOperation(value = "根据家族id更新人员的谱信息")
    @ApiImplicitParam(name = "familyId", value = "家族id", dataType = "java.lang.Integer", paramType = "query", required = true)
    public JsonResult upFamilyGenealogyChart(Integer familyId) {
        return this.familyGenealogyService.upFamilyGenealogyChart(familyId);
    }

    @PutMapping("/upFamilyGenealogyPid")
    @ApiOperation(value = "根据id修改parentId、代数、家谱名、是否在世、家族关系、性别、familyId、音频、配偶、排行、生日、忌日、头像")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "当前id", dataType = "java.lang.Integer", paramType = "body", required = true),
            @ApiImplicitParam(name = "generation", value = "代数", dataType = "java.lang.Integer", paramType = "body", required = true),
            @ApiImplicitParam(name = "genealogyName", value = "家谱名", dataType = "java.lang.String", paramType = "body", required = true),
            @ApiImplicitParam(name = "parentId", value = "父级id", dataType = "java.lang.Integer", paramType = "body", required = true),
            @ApiImplicitParam(name = "isAlive", value = "是否在世,0否1是", dataType = "java.lang.Integer", paramType = "body", required = true),
            @ApiImplicitParam(name = "relation", value = "和家族关系1直亲2婚姻3表亲", dataType = "java.lang.Integer", paramType = "body", required = true),
            @ApiImplicitParam(name = "sex", value = "性别1男2女", dataType = "java.lang.Integer", paramType = "body", required = true),
            @ApiImplicitParam(name = "familyId", value = "家族id", dataType = "java.lang.Integer", paramType = "body", required = true),
            @ApiImplicitParam(name = "audio", value = "音频", dataType = "java.lang.String", paramType = "body", required = true),
            @ApiImplicitParam(name = "headImg", value = "头像", dataType = "java.lang.String", paramType = "body", required = true),
            @ApiImplicitParam(name = "spouse", value = "配偶", dataType = "java.lang.String", paramType = "body", required = false),
            @ApiImplicitParam(name = "ranking", value = "排行", dataType = "java.lang.Integer", paramType = "body", required = false),
            @ApiImplicitParam(name = "birthday", value = "生日", dataType = "java.lang.String", paramType = "body", required = false),
            @ApiImplicitParam(name = "mourningDay", value = "忌日", dataType = "java.lang.String", paramType = "body", required = false),
    })
    public JsonResult upFamilyGenealogyPid(@RequestBody @ApiIgnore FamilyGenealogy familyGenealogy) {
        System.out.println(familyGenealogy);
        return this.familyGenealogyService.upFamilyGenealogyPid(familyGenealogy);
    }


    @GetMapping("/getFamilyGenealogyListNotClaim")
    @ApiOperation(value = "根据家族id，代数，名字查询")
    public JsonResult getFamilyGenealogyListNotClaim(Integer familyId, Integer generation, String genealogyName, Integer limit, Integer page) {
        return this.familyGenealogyService.getFamilyGenealogyListNotClaim(familyId, generation, genealogyName, limit, page);
    }

    @GetMapping("/getFamilyGenealogyList")
    @ApiOperation(value = "根据家族id，代数，名字查询")
    public JsonResult getFamilyGenealogyList(Integer familyId, Integer generation, String genealogyName) {
        return this.familyGenealogyService.getFamilyGenealogyList(familyId, generation, genealogyName);
    }

    @GetMapping("/getFamilyGenealogyListPaging")
    @ApiOperation(value = "根据家族id，代数，名字查询")
    public JsonResult getFamilyGenealogyListPaging(Integer familyId, Integer generation, String genealogyName, Integer limit, Integer page) {
        return this.familyGenealogyService.getFamilyGenealogyListPaging(familyId, generation, genealogyName, limit, page);
    }


    @GetMapping("/getFamilyGenealogyListPagingGap")
    @ApiOperation(value = "根据家族id，代数，名字查询代差")
    public JsonResult getFamilyGenealogyListPagingGap(Integer familyId, Integer generation, String genealogyName, Integer userId, Integer limit, Integer page, Integer generationNum) {
        return this.familyGenealogyService.getFamilyGenealogyListPagingGap(familyId, generation, genealogyName, userId, limit, page, generationNum);
    }


    @PostMapping("/sendMailbox")
    @ApiOperation(value = "发送族谱邮箱")
    public JsonResult sendMailbox(@RequestBody sendMailboxVo sendMailboxVo) {
        return familyGenealogyService.deriveExcle(sendMailboxVo.getFamilyId(), sendMailboxVo.getMailbox(), sendMailboxVo.getUserId());
    }


    @PostMapping("/createConnect")
    @ApiOperation(value = "购买下载族谱链接")
    public JsonResult createConnect(@RequestBody sendMailboxVo sendMailboxVo) {
        return familyGenealogyService.createConnect(sendMailboxVo.getFamilyId(), sendMailboxVo.getUserId());
    }

    @PostMapping("/viewFamilyTreePay")
    @ApiOperation(value = "购买下载族谱链接")
    public JsonResult viewFamilyTreePay(@RequestBody sendMailboxVo sendMailboxVo) {
        return familyGenealogyService.viewFamilyTreePay(sendMailboxVo.getFamilyId(), sendMailboxVo.getUserId());
    }

    /**
     * 导出家谱图Excle
     */
    @GetMapping("/deriveExcle")
    @ApiOperation(value = "导出家谱图Excle")
    @ApiImplicitParam(name = "familyId", value = "家族id", dataType = "java.lang.Integer", paramType = "query", required = true)
    public void deriveExcle(Integer familyId, HttpServletResponse response) {
        LambdaQueryWrapper<FamilyGenealogy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyGenealogy::getFamilyId, familyId);
        wrapper.orderByAsc(FamilyGenealogy::getGeneration); // 按照 "generation" 列升序排序

        Family family = this.familyService.getById(familyId);
        if (Objects.isNull(family)) {
//            return ResponseUtil.fail("未查询到家族信息");
            return;
        }

        List<FamilyGenealogy> genealogyList = this.familyGenealogyService.list(wrapper);
        if (CollectionUtils.isEmpty(genealogyList)) {
//            return ResponseUtil.fail("该家族暂时没有家谱图");
            return;
        }
        String exportTime = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
        for (FamilyGenealogy fg : genealogyList) {
//            fg.setTitle(family.getName());
            fg.setIsAliveDesc(null != fg.getIsAlive() && fg.getIsAlive() == 1 ? "在世" : "去世");
//            fg.setExportTime(exportTime);
        }
        // 写法1 JDK8+
        // since: 3.0.0-beta1

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = now.format(formatter);

        String fileName = "火谱-" + formattedDateTime;
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        try {
            EasyExcelSheetWriteHandler writeHandler = new EasyExcelSheetWriteHandler("");
            JSONObject obj = new JSONObject();
            obj.put("family", family.getName());
            obj.put("exportTime", exportTime);
            EasyExcelCellWriteHandler easyExcelTitleHandler = new EasyExcelCellWriteHandler(null, obj);
            ExcelUtil.export(response, fileName, "模板", genealogyList, FamilyGenealogy.class, writeHandler, easyExcelTitleHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //    @ApiOperation(value = "导入族谱")
    @PostMapping("/import")
    public JsonResult importUser(@RequestPart("file") MultipartFile file, @RequestPart("familyId") String familyId) throws Exception {
        return familyGenealogyService.importUser(file, Integer.parseInt(familyId));
    }


    @ApiOperation(value = "根据名字模糊查询")
    @GetMapping(value = "/selectByName")
    public JsonResult selectByName(FamilyGenealogyInsertReqVo familyGenealogyInsertReqVo) {

        return familyGenealogyService.selectByName(familyGenealogyInsertReqVo);
    }

    /**
     * 查询族谱图树
     *
     * @param familyId 当前查询id
     * @return
     */
    @GetMapping("/getFamilyGenealogyTreeByFamilyId")
    @ApiOperation(value = "查询族谱图树")
    @ApiImplicitParam(name = "familyId", value = "当前查询id", dataType = "java.lang.Integer", paramType = "query", required = true)
    public JsonResult getFamilyGenealogyTreeByFamilyId(Integer familyId, Integer parentId, @RequestParam(defaultValue = "5", required = false) Integer level) {

        if (ObjectUtil.isNull(familyId)) {
            return ResponseUtil.fail("familyId不能为空");
        }
        return this.familyGenealogyService.getFamilyGenealogyTreeByFamilyId(familyId, parentId, level);
    }

    /**
     * 查询同代异性列表
     *
     * @param familyId   家族ID
     * @param generation 代
     * @param sex        性别
     * @return
     */
    @GetMapping("/getSpouseList")
    @ApiOperation(value = "查询同代异性列表")
    public JsonResult getFamilyGenealogyById(Integer familyId, Integer generation, Integer sex) {
        List<FamilyGenealogy> familyGenealogies = Lists.newArrayList();
        if (ObjectUtil.isNotNull(familyId) && ObjectUtil.isNotNull(generation) && ObjectUtil.isNotNull(sex)) {
            QueryWrapper<FamilyGenealogy> familyGenealogySelect = new QueryWrapper<FamilyGenealogy>();
            familyGenealogySelect.eq("family_id", familyId);
            familyGenealogySelect.eq("generation", generation);
            familyGenealogySelect.eq("sex", sex == 1 ? 2 : 1);
            familyGenealogies = familyGenealogyService.list(familyGenealogySelect);
        }
        return ResponseUtil.ok("查询成功", familyGenealogies);
    }

    @ApiOperation(value = "根据id修改族简历")
    @RequestMapping(value = "/updateResumeById", method = RequestMethod.PUT)
    public JsonResult updateResumeById(@RequestBody FamilyGenealogyIntroduceReqVo familyGenealogyUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(familyGenealogyUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        return familyGenealogyService.updateResumeById(familyGenealogyUpdateReqVo);
    }


    @ApiOperation(value = "新增")
    @RequestMapping(value = "/applyInsert", method = RequestMethod.PUT)
    public JsonResult applyInsert(@RequestBody FamilyGenealogyEditApplyReqVo applyReqVo) throws Exception {

        if (ObjectUtil.isNull(applyReqVo.getFamilyGenealogyId())) {
            return ResponseUtil.fail("参数错误！");
        }
        //查询是否存在待审核的申请，存在则直接返回 不允许申请
        QueryWrapper<FamilyGenealogyEditApply> applyWrapper = new QueryWrapper<FamilyGenealogyEditApply>();
        applyWrapper.eq("family_genealogy_id", applyReqVo.getFamilyGenealogyId());
        applyWrapper.eq("type", applyReqVo.getType());
        applyWrapper.eq("status", 0);//待审核
        long closeCnt = familyGenealogyEditApplyService.count(applyWrapper);
        if (closeCnt > 0) {
            return ResponseUtil.fail("不能重复申请！");
        }

        FamilyGenealogyEditApply apply = new FamilyGenealogyEditApply();
        apply.setApplyTime(new Date());
        apply.setFamilyGenealogyId(applyReqVo.getFamilyGenealogyId());
        apply.setStatus(0);
        apply.setReason(applyReqVo.getReason());
        apply.setType(applyReqVo.getType());
        familyGenealogyEditApplyService.save(apply);
        if (applyReqVo.getType() > 1) {
            parsePuApplyMessage(applyReqVo.getFamilyGenealogyId(), applyReqVo.getUserId(), 1);
        } else {
            parseMessage(applyReqVo.getFamilyGenealogyId(), applyReqVo.getUserId(), 1);
        }

        return ResponseUtil.ok("申请成功");
    }

    /**
     * 消息加减  type +-
     *
     * @param id
     * @param userId
     * @param type
     */
    private void parseMessage(Integer id, Integer userId, Integer type) {
        FamilyGenealogy familyGenealogy = familyGenealogyService.getById(id);

        QueryWrapper<FamilyMessage> familymessage = new QueryWrapper<FamilyMessage>();
        familymessage.eq("family_id", familyGenealogy.getFamilyId());
        familymessage.eq("user_id", userId);
        FamilyMessage familydata = familyMessageService.getOne(familymessage);
        if (familydata == null) {
            FamilyMessage familyMessage = new FamilyMessage();
            familyMessage.setEditapplyMessage(1);
            familyMessage.setFamilyId(familyGenealogy.getFamilyId());
            familyMessage.setUserId(userId);
            familyMessageService.save(familyMessage);
        } else {
            FamilyMessage familyMessage = new FamilyMessage();
            familyMessage.setId(familydata.getId());
            familyMessage.setEditapplyMessage(familydata.getEditapplyMessage() > 0 ? (familydata.getEditapplyMessage() + type) : type);
            familyMessage.setFamilyId(familyGenealogy.getFamilyId());
            familyMessage.setUserId(userId);
            familyMessageService.updateById(familyMessage);
        }
    }

    /**
     * 消息加减  type +-
     *
     * @param id
     * @param userId
     * @param type
     */
    private void parsePuApplyMessage(Integer id, Integer userId, Integer type) {
        FamilyGenealogy familyGenealogy = familyGenealogyService.getById(id);

        QueryWrapper<FamilyMessage> familymessage = new QueryWrapper<FamilyMessage>();
        familymessage.eq("family_id", familyGenealogy.getFamilyId());
        familymessage.eq("user_id", userId);
        FamilyMessage familydata = familyMessageService.getOne(familymessage);
        if (familydata == null) {
            FamilyMessage familyMessage = new FamilyMessage();
            familyMessage.setPuapplyMessage(1);
            familyMessage.setFamilyId(familyGenealogy.getFamilyId());
            familyMessage.setUserId(userId);
            familyMessageService.save(familyMessage);
        } else {
            FamilyMessage familyMessage = new FamilyMessage();
            familyMessage.setId(familydata.getId());
            familyMessage.setPuapplyMessage(familydata.getPuapplyMessage() > 0 ? familydata.getPuapplyMessage() + type : 0);
            familyMessage.setFamilyId(familyGenealogy.getFamilyId());
            familyMessage.setUserId(userId);
            familyMessageService.updateById(familyMessage);
        }
    }

    @ApiOperation(value = "审核")
    @RequestMapping(value = "/applyAudit", method = RequestMethod.PUT)
    public JsonResult applyAudit(@RequestBody AuditApplyReqVo param) throws Exception {
        FamilyGenealogyEditApply apply = familyGenealogyEditApplyService.getById(param.getId());
        apply.setAuditTime(new Date());
        apply.setStatus(param.getStatus());
        apply.setAuditReason(param.getAuditReason());
        familyGenealogyEditApplyService.updateById(apply);
        if (apply.getType() > 1) {
            parsePuApplyMessage(apply.getFamilyGenealogyId(), param.getUserId(), -1);
        } else {
            parseMessage(apply.getFamilyGenealogyId(), param.getUserId(), -1);
        }
        return ResponseUtil.ok("申请成功");
    }

    @ApiOperation(value = "查询列表")
    @RequestMapping(value = "/applyList", method = RequestMethod.GET)
    public JsonResult applyList() throws Exception {
        LambdaQueryWrapper<FamilyGenealogyEditApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyGenealogyEditApply::getStatus, 0);
        List<FamilyGenealogyEditApply> applyList = familyGenealogyEditApplyService.list(wrapper);
        if (CollectionUtils.isNotEmpty(applyList)) {
            for (FamilyGenealogyEditApply apply : applyList) {
                if (null != apply.getFamilyGenealogyId()) {
                    FamilyGenealogy genealogy = familyGenealogyService.getById(apply.getFamilyGenealogyId());
                    apply.setFamilyGenealogyName(null != genealogy ? genealogy.getGenealogyName() : "");
                }
            }
        }
        return ResponseUtil.ok("成功", applyList);
    }
}

