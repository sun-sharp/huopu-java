package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.Family;
import com.wx.genealogy.system.entity.FamilyClean;
import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.service.FamilyCleanService;
import com.wx.genealogy.system.service.FamilyService;
import com.wx.genealogy.system.vo.req.FamilyInsertReqVo;
import com.wx.genealogy.system.vo.req.FamilyUpdateReqVo;
import com.wx.genealogy.system.vo.req.UnlockFamilyUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 家族 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/family")
@Api(tags = "家族接口")
public class FamilyController {

    @Autowired
    private FamilyService familyService;
    @Autowired
    private FamilyCleanService familyCleanService;
    @ApiOperation(value = "创建家族")
    @RequestMapping(value = "/insertFamily", method = RequestMethod.POST)
    public JsonResult insertFamily(@RequestBody FamilyInsertReqVo familyInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(familyInsertReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        if (ObjectUtil.isNull(familyInsertReqVo.getName())) {
            throw new MissingServletRequestParameterException("name", "String");
        }

        Family familyInsert=new Family();
        FamilyUser familyUserInsert = new FamilyUser();
        familyInsert.setUserId(familyInsertReqVo.getUserId());
        familyInsert.setLogo(familyInsertReqVo.getLogo());
        familyInsert.setName(familyInsertReqVo.getName());
        familyInsert.setHunname(familyInsertReqVo.getHunname());
        familyInsert.setPuname(familyInsertReqVo.getPuname());

        familyUserInsert.setUserId(familyInsertReqVo.getUserId());
//        System.out.println("第一次："+familyInsertReqVo.getPuname());
        familyUserInsert.setGenealogyName(familyInsertReqVo.getPuname());
        familyUserInsert.setRelation(familyInsertReqVo.getRelation());

        return familyService.insertFamily(familyInsert,familyUserInsert);
    }

    @ApiOperation(value = "根据id修改家族")
    @RequestMapping(value = "/updateFamilyById", method = RequestMethod.PUT)
    public JsonResult updateFamilyById(@RequestBody FamilyUpdateReqVo familyUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(familyUpdateReqVo.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        Family familyUpdate=new Family();
        ObjectUtil.copyByName(familyUpdateReqVo,familyUpdate);
        return familyService.updateFamilyById(familyUpdate);
    }

    @RequestMapping(value = "/selectFamily", method = RequestMethod.GET)
    public JsonResult selectFamily(@RequestParam(value = "limit") Integer limit,
                                   @RequestParam(value = "page") Integer page,
                                   @RequestParam(value = "name",required = false) String name) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        Family family = new Family();
        family.setName(name);
        return familyService.selectFamily(page ,limit,family);
    }


    @ApiOperation(value = "根据id删除家族")
    @RequestMapping(value = "/deleteFamilyById", method = RequestMethod.GET)
    public JsonResult deleteFamilyById(@RequestParam(value = "id") Integer id,@RequestParam(value = "userId") Integer userId) throws Exception {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        return familyService.deleteFamilyById(id,userId);
    }


    @ApiOperation(value = "解锁家族")
    @RequestMapping(value = "/unlockFamilyById", method = RequestMethod.POST)
    public JsonResult unlockFamilyById(@RequestBody UnlockFamilyUpdateReqVo unlockFamilyUpdateReqVoc) throws Exception {
        if (ObjectUtil.isNull(unlockFamilyUpdateReqVoc.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        FamilyClean familyclean=new FamilyClean();
        familyclean.setUserId(unlockFamilyUpdateReqVoc.getUserId());
        familyclean.setFamilyId(unlockFamilyUpdateReqVoc.getFamilyId());
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(f.parse(f.format(new Date())).getTime() + 24 * 3600 * 1000 * unlockFamilyUpdateReqVoc.getMultiple());
        familyclean.setCleanTime(d);

        return familyCleanService.updateFamilyUnlockById(familyclean,unlockFamilyUpdateReqVoc.getMi(),unlockFamilyUpdateReqVoc.getMultiple());
    }

    @ApiOperation(value = "家族名称模糊分页查询")
    @RequestMapping(value = "/selectFamilyByName", method = RequestMethod.GET)
    public JsonResult selectFamilyByName(@RequestParam(value = "limit") Integer limit,
                                 @RequestParam(value = "page") Integer page,
                                 @RequestParam(value = "name") String name) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(name)) {
            throw new MissingServletRequestParameterException("name", "String");
        }
        return familyService.selectFamilyByName(page ,limit,name);
    }

    @ApiOperation(value = "根据id和userId查询家族信息和用户与当前家族的关联性")
    @RequestMapping(value = "/findFamilyByIdAndUserId", method = RequestMethod.GET)
    public JsonResult findFamilyByIdAndUserId(@RequestParam(value = "id") Integer id,
                                              @RequestParam(value = "userId", required = false) Integer userId,
                                              @RequestParam(value = "authorId", required = false) Integer authorId,
                                              @RequestParam(value = "familyGenealogyId", required = false) Integer familyGenealogyId) throws Exception {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id", "number");
        }
//        if (ObjectUtil.isNull(userId) && ObjectUtil.isNull(familyGenealogyId)) {
//            throw new MissingServletRequestParameterException("userId", "number");
//        }
        return familyService.findFamilyByIdAndUserId(id,userId, authorId, familyGenealogyId);
    }
}

