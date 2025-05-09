package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.exception.ServiceException;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.FamilyManageLog;
import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.service.FamilyMessageService;
import com.wx.genealogy.system.service.FamilyUserService;
import com.wx.genealogy.system.vo.req.FamilyUserInsertReqVo;
import com.wx.genealogy.system.vo.req.FamilyUserUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 家族用户关联 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-10-09
 */
@RestController
@RequestMapping("/familyUser")
@Api(tags = "家族成员接口")
public class FamilyUserController {

    @Autowired
    private FamilyUserService familyUserService;

    @Autowired
    private FamilyMessageService familyMessageService;

    @ApiOperation(value = "申请加入家族")
    @RequestMapping(value = "/insertFamilyUser", method = RequestMethod.POST)
    public JsonResult insertFamilyUser(@RequestBody FamilyUserInsertReqVo familyUserInsertReqVo) throws Exception {
        if (ObjectUtil.isNull(familyUserInsertReqVo.getFamilyId())) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }
        if (ObjectUtil.isNull(familyUserInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }

        if (ObjectUtil.isNull(familyUserInsertReqVo.getRelation())) {
            throw new MissingServletRequestParameterException("relation", "number");
        }
        if (ObjectUtil.isNull(familyUserInsertReqVo.getSex())) {
            throw new MissingServletRequestParameterException("sex", "number");
        }
        if (ObjectUtil.isNull(familyUserInsertReqVo.getIntroduce())) {
            throw new MissingServletRequestParameterException("introduce", "String");
        }
        if (ObjectUtil.isNull(familyUserInsertReqVo.getGenealogyName())) {
            throw new MissingServletRequestParameterException("genealogyName", "String");
        }

        FamilyUser familyUserInsert=new FamilyUser();
        familyUserInsert.setFamilyId(familyUserInsertReqVo.getFamilyId());
        familyUserInsert.setGenealogyName(familyUserInsertReqVo.getGenealogyName());
        familyUserInsert.setUserId(familyUserInsertReqVo.getUserId());
        familyUserInsert.setRelation(familyUserInsertReqVo.getRelation());
        familyUserInsert.setSex(familyUserInsertReqVo.getSex());
        familyUserInsert.setIntroduce(familyUserInsertReqVo.getIntroduce());
        familyUserInsert.setLevel(3);
        familyUserInsert.setStatus(1);
        familyUserInsert.setCreateTime(DateUtils.getDateTime());
        return familyUserService.insertFamilyUser(familyUserInsert);
    }

    @GetMapping("/getFamilyUserListPaging")
    @ApiOperation(value = "根据家族id，代数，名字查询")
    public JsonResult getFamilyUserListPaging(Integer familyId,String genealogyName,Integer limit,Integer page) {
        return familyUserService.getFamilyUserListPaging(familyId,genealogyName,limit,page);
    }


    @ApiOperation(value = "删除家族成员")
        @RequestMapping(value = "/deleteFamilyUser", method = RequestMethod.POST)
    public JsonResult deleteFamilyUser(@RequestBody FamilyUser familyUser) {
        return familyUserService.deleteFamilyUser(familyUser);
    }

    @ApiOperation(value = "新增家族成员（免申请）")
    @RequestMapping(value = "/insertFamilyUserNoApply", method = RequestMethod.POST)
    public JsonResult insertFamilyUserNoApply(@RequestBody FamilyUserInsertReqVo familyUserInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(familyUserInsertReqVo.getFamilyId())) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }
        if (ObjectUtil.isNull(familyUserInsertReqVo.getGeneration())) {
            throw new MissingServletRequestParameterException("genealogy", "number");
        }
        if (ObjectUtil.isNull(familyUserInsertReqVo.getGenealogyName())) {
            throw new MissingServletRequestParameterException("genealogyName", "String");
        }
        if (ObjectUtil.isNull(familyUserInsertReqVo.getRelation())) {
            throw new MissingServletRequestParameterException("relation", "number");
        }
        if (ObjectUtil.isNull(familyUserInsertReqVo.getSex())) {
            throw new MissingServletRequestParameterException("sex", "number");
        }

        FamilyUser familyUserInsert=new FamilyUser();
        familyUserInsert.setFamilyId(familyUserInsertReqVo.getFamilyId());
        familyUserInsert.setUserId(familyUserInsertReqVo.getUserId());
        familyUserInsert.setGeneration(familyUserInsertReqVo.getGeneration());
        familyUserInsert.setGenealogyName(familyUserInsertReqVo.getGenealogyName());
        familyUserInsert.setRelation(familyUserInsertReqVo.getRelation());
        familyUserInsert.setSex(familyUserInsertReqVo.getSex());
        familyUserInsert.setIntroduce(familyUserInsertReqVo.getIntroduce());
        familyUserInsert.setLevel(3);
        familyUserInsert.setStatus(2);
        familyUserInsert.setCreateTime(DateUtils.getDateTime());
        return familyUserService.insertFamilyUserNoApply(familyUserInsert);
    }

    @ApiOperation(value = "根据id修改家族成员")
    @RequestMapping(value = "/updateFamilyUserById", method = RequestMethod.PUT)
    public JsonResult updateFamilyUserById(@RequestBody FamilyUserUpdateReqVo familyUserUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(familyUserUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        FamilyUser familyUserUpdate=new FamilyUser();
        ObjectUtil.copyByName(familyUserUpdateReqVo,familyUserUpdate);
        FamilyManageLog familyManageLog = new FamilyManageLog();
        if(familyUserUpdateReqVo.getFamilyManageLogInsertReqVo()!=null&&familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getRecord()!=null&&familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getRecord()==1){
            familyManageLog.setFamilyId(familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getFamilyId());
            familyManageLog.setFamilyUserId(familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getFamilyUserId());
            familyManageLog.setUserId(familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getUserId());
            familyManageLog.setAction(familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getAction());
            familyManageLog.setContent(familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getContent());
            familyManageLog.setCreateTime(DateUtils.getDateTime());
        }

        return familyUserService.updateFamilyUserById(familyUserUpdate,familyManageLog);
    }

    @ApiOperation(value = "根据id审批家族成员")
    @RequestMapping(value = "/examineFamilyUserById", method = RequestMethod.PUT)
    public JsonResult examineFamilyUserById(@RequestBody FamilyUserUpdateReqVo familyUserUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(familyUserUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        FamilyUser familyUserUpdate=new FamilyUser();
        ObjectUtil.copyByName(familyUserUpdateReqVo,familyUserUpdate);
        FamilyManageLog familyManageLog = new FamilyManageLog();
        if(familyUserUpdateReqVo.getFamilyManageLogInsertReqVo()!=null&&familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getRecord()!=null&&familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getRecord()==1){
            familyManageLog.setFamilyId(familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getFamilyId());
            familyManageLog.setFamilyUserId(familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getFamilyUserId());
            familyManageLog.setUserId(familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getUserId());
            familyManageLog.setAction(familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getAction());
            familyManageLog.setContent(familyUserUpdateReqVo.getFamilyManageLogInsertReqVo().getContent());
            familyManageLog.setCreateTime(DateUtils.getDateTime());
        }
        return familyUserService.updateStatusById(familyUserUpdate,familyManageLog);
    }




    @ApiOperation(value = "申请入谱")
    @RequestMapping(value = "/cepstrumFamily", method = RequestMethod.GET)
    public JsonResult cepstrumFamily(@RequestParam(value = "userId") Integer userId,
                                      @RequestParam(value = "familyId") Integer familyId
    ){
        return familyUserService.cepstrumFamily(userId,familyId);
    }


    @ApiOperation(value = "查看入谱申请")
    @RequestMapping(value = "/selectCepstrumFamily", method = RequestMethod.GET)
    public JsonResult selectCepstrumFamily(@RequestParam(value = "userId") Integer userId,
                                     @RequestParam(value = "familyId") Integer familyId
    ){
        return familyUserService.selectCepstrumFamily(familyId);
    }





    @ApiOperation(value = "修改完成状态")
    @RequestMapping(value = "/updateFamilyupdates", method = RequestMethod.GET)
    public JsonResult updateFamilyupdates(@RequestParam(value = "id" ) Integer id,
                                          @RequestParam(value = "generation",required = false) Integer generation,
                                          @RequestParam(value = "genealogyName",required = false) String genealogyName,
                                          @RequestParam(value = "level",required = false) Integer level,
                                          @RequestParam(value = "joins",required = false) Integer joins,
                                      @RequestParam(value = "status",required = false) Integer status,
                                          @RequestParam(value = "familyId",required = false) Integer familyId,
                                      @RequestParam(value = "userId",required = false) Integer userId) throws Exception {

        return familyUserService.updateFamilyupdates(id,status,userId,familyId,generation,genealogyName,level,joins);
    }


    @ApiOperation(value = "修改完成状态")
    @RequestMapping(value = "/updateFamilydai", method = RequestMethod.GET)
    public JsonResult updateFamilydai(
                                          @RequestParam(value = "generation",required = false) Integer generation,
                                          @RequestParam(value = "genealogyName",required = false) String genealogyName,
                                          @RequestParam(value = "familyIdUserId",required = false) Integer familyIdUserId,
                                          @RequestParam(value = "relation",required = false) Integer relation,
                                          @RequestParam(value = "status",required = false) Integer status,
                                          @RequestParam(value = "familyId",required = false) Integer familyId,
                                          @RequestParam(value = "userId",required = false) Integer userId) throws Exception {
        return familyUserService.updateFamilyUp(userId,familyIdUserId,generation,genealogyName,relation,familyId);
    }


    @ApiOperation(value = "查询家族申请")
    @GetMapping(value = "/selectStatusByFamilyId")
    @ApiImplicitParam(name = "familyId", value = "当前家族id", dataType = "java.lang.Integer", paramType = "query", required = true)
    public JsonResult select(Integer familyId) {
        System.out.println("******");
        System.out.println(familyId);
        if (ObjectUtil.isNull(familyId)) {
            throw new ServiceException("家族id不能为空！");
        }
        return familyUserService.selectStatusByFamilyId(familyId);
    }

    @ApiOperation(value = "根据用户id分页查询已加入的家族")
    @RequestMapping(value = "/selectFamilyUserAndFamily", method = RequestMethod.GET)
    public JsonResult selectFamilyUserAndFamily(@RequestParam(value = "limit") Integer limit,
                                                @RequestParam(value = "page") Integer page,
                                                @RequestParam(value = "genealogyName" ,required = false) String genealogyName,
                                                @RequestParam(value = "userId") Integer userId


    ) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        FamilyUser familyUserSelect=new FamilyUser();
        familyUserSelect.setUserId(userId);
        familyUserSelect.setStatus(2);
        if (!ObjectUtil.isNull(genealogyName)) {
            familyUserSelect.setGenealogyName(genealogyName);
        }


        return familyUserService.selectFamilyUserAndFamily(page ,limit,familyUserSelect);
    }

    @ApiOperation(value = "根据用户id分页查询已加入的家族")
    @RequestMapping(value = "/selectFamilyUserAndFamilys", method = RequestMethod.GET)
    public JsonResult selectFamilyUserAndFamilys(@RequestParam(value = "limit") Integer limit,
                                                @RequestParam(value = "page") Integer page,
                                                @RequestParam(value = "userId") Integer userId,
                                                @RequestParam(value = "updatestatus",required = false) Integer updatestatus

    ) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        FamilyUser familyUserSelect=new FamilyUser();
        familyUserSelect.setUserId(userId);
        familyUserSelect.setStatus(2);
        familyUserSelect.setUpdatestatus(updatestatus);

        return familyUserService.selectFamilyUserAndFamily(page ,limit,familyUserSelect);
    }


    @ApiOperation(value = "根据家族id分页查询当前家族下的所有申请记录")
    @RequestMapping(value = "/selectApplyByFamilyId", method = RequestMethod.GET)
    public JsonResult selectApplyByFamilyId(@RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "page") Integer page,
                                            @RequestParam(value = "sort") Integer sort,
                                            @RequestParam(value = "familyId") Integer familyId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(sort)) {
            throw new MissingServletRequestParameterException("sort", "number");
        }
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }
        FamilyUser familyUserSelect=new FamilyUser();
        familyUserSelect.setFamilyId(familyId);
        return familyUserService.selectFamilyUserByFamilyId(page ,limit,sort,familyUserSelect);
    }

    @ApiOperation(value = "根据家族id分页查询当前家族下的所有成员")
    @RequestMapping(value = "/selectFamilyUserByFamilyId", method = RequestMethod.GET)
    public JsonResult selectFamilyUserByFamilyId(@RequestParam(value = "limit") Integer limit,
                                                 @RequestParam(value = "page") Integer page,
                                                 @RequestParam(value = "familyId") Integer familyId,
                                                 @RequestParam(value="generation",required=false) Integer generation,
                                                 @RequestParam(value="relation",required=false) Integer relation,
                                                 @RequestParam(value="sex",required=false) Integer sex) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }
        FamilyUser familyUserSelect=new FamilyUser();
        familyUserSelect.setFamilyId(familyId);
        familyUserSelect.setGeneration(generation);
        familyUserSelect.setRelation(relation);
        familyUserSelect.setSex(sex);
        familyUserSelect.setStatus(2);
        return familyUserService.selectFamilyUserByFamilyId(page ,limit,1,familyUserSelect);
    }

    @ApiOperation(value = "根据家族id分页查询当前家族下的所有管理员")
    @RequestMapping(value = "/selectManagerByFamilyId", method = RequestMethod.GET)
    public JsonResult selectManagerByFamilyId(@RequestParam(value = "limit") Integer limit,
                                                 @RequestParam(value = "page") Integer page,
                                                 @RequestParam(value = "familyId") Integer familyId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }
        FamilyUser familyUserSelect=new FamilyUser();
        familyUserSelect.setFamilyId(familyId);
        familyUserSelect.setStatus(2);
        return familyUserService.selectManagerByFamilyId(page ,limit,familyUserSelect);
    }


    @ApiOperation(value = "跟进家族id，代数，关系，查找成员")
    @RequestMapping(value = "/selectManagerByFamilyIdNo", method = RequestMethod.GET)
    public JsonResult selectManagerByFamilyIdNo(
            @RequestParam(value = "familyId") Integer familyId
    ) throws Exception {
        System.out.println(familyId);
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }

        FamilyUser familyUser = new FamilyUser();
        familyUser.setFamilyId(familyId);
        return familyUserService.selectManagerByFamilyIdNo(familyId);
    }


    @ApiOperation(value = "根据id批量修改会员为管理员")
    @RequestMapping(value = "/updateFamilyUserByIdList", method = RequestMethod.PUT)
    public JsonResult updateFamilyUserByIdList(@RequestBody List<FamilyUserUpdateReqVo> familyUserUpdateReqVoList) throws Exception {
        if(familyUserUpdateReqVoList==null||familyUserUpdateReqVoList.size()==0){
            return ResponseUtil.fail("参数为空");
        }
        FamilyUser familyUser = new FamilyUser();
        familyUser.setLevel(2);
        familyUser.setManageTime(DateUtils.getDateTime());
        List<Integer> idList = new ArrayList<>();
        List<FamilyManageLog> familyManageLogList = new ArrayList<>();
        for (int i=0;i<familyUserUpdateReqVoList.size();i++){
            idList.add(familyUserUpdateReqVoList.get(i).getId());

            FamilyManageLog familyManageLog = new FamilyManageLog();
            familyManageLog.setFamilyId(familyUserUpdateReqVoList.get(i).getFamilyManageLogInsertReqVo().getFamilyId());
            familyManageLog.setFamilyUserId(familyUserUpdateReqVoList.get(i).getFamilyManageLogInsertReqVo().getFamilyUserId());
            familyManageLog.setUserId(familyUserUpdateReqVoList.get(i).getFamilyManageLogInsertReqVo().getUserId());
            familyManageLog.setAction(familyUserUpdateReqVoList.get(i).getFamilyManageLogInsertReqVo().getAction());
            familyManageLog.setContent(familyUserUpdateReqVoList.get(i).getFamilyManageLogInsertReqVo().getContent());
            familyManageLog.setCreateTime(DateUtils.getDateTime());
            familyManageLogList.add(familyManageLog);
        }
        return familyUserService.updateFamilyUserByIdList(idList,familyUser,familyManageLogList);
    }



    @ApiOperation(value = "跟进家族id，代数，关系，查找成员")
    @RequestMapping(value = "/selectFamilyUserBygenertion", method = RequestMethod.GET)
    public JsonResult selectFamilyUserBygenertion(
                                              @RequestParam(value = "genealogy") Integer genealogy,
                                              @RequestParam(value = "familyId") Integer familyId,
                                              @RequestParam(value = "familyUserId") Integer familyUserId


    ) throws Exception {
        if (ObjectUtil.isNull(genealogy)) {
            throw new MissingServletRequestParameterException("genealogy", "number");
        }
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }

        return familyUserService.selectFamilyUserBygenertion(genealogy ,familyId,familyUserId);
    }


    @ApiOperation(value = "判断某用户是否在家族里面")
    @RequestMapping(value = "/selectFamilyUserByuserid", method = RequestMethod.GET)
    public JsonResult selectFamilyUserByuserid(
            @RequestParam(value = "familyId") Integer familyId,
            @RequestParam(value = "userId") Integer userId

    ) throws Exception {
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }

        return familyUserService.selectFamilyUserByuserid(familyId ,userId);
    }




    @ApiOperation(value = "添加")
    @RequestMapping(value = "/insertFamilyUserBygenertion", method = RequestMethod.GET)
    public JsonResult insertFamilyUserBygenertion(
            @RequestParam(value = "family_userid") Integer family_userid,

            @RequestParam(value = "number") Integer number,
            @RequestParam(value = "familyId") Integer familyId,

            @RequestParam(value = "genealogy",required = false,defaultValue="") String genealogy,
            @RequestParam(value = "genealogy_two",required = false,defaultValue="") String genealogy_two
//            @RequestParam(value = "sex") Integer sex,
//            @RequestParam(value = "sex1") Integer sex1



            ) throws Exception {
        if (ObjectUtil.isNull(genealogy)) {
            throw new MissingServletRequestParameterException("genealogy", "number");
        }
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }

        return familyUserService.insertFamilyUserBygenertion(family_userid,number,familyId,genealogy ,genealogy_two);
    }




}

