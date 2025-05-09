package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.*;
import com.wx.genealogy.system.service.TombstoneService;
import com.wx.genealogy.system.vo.req.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tombstone")
@Api(tags = "家族祠堂")
public class TombstoneController {

    @Autowired
    private TombstoneService tombstoneService;
    @ApiOperation(value = "查找家族祠堂")
    @RequestMapping(value = "/selectTomstoneByfamilyId", method = RequestMethod.GET)
    public JsonResult selectTomstoneByfamilyId(@RequestParam(value = "limit") Integer limit,@RequestParam(value = "page") Integer page, @RequestParam(value = "familyId") Integer familyId) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(familyId)) {
            throw new MissingServletRequestParameterException("familyId", "number");
        }


        return tombstoneService.selectTomstoneByfamilyId(page ,limit,familyId);
    }


    @ApiOperation(value = "添加祠堂")
    @PostMapping(value = "/insertTomstone")
    public JsonResult insertTomstone(@RequestBody TomstoneInsertReqVo tomstoneInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(tomstoneInsertReqVo.getUserName())) {
            throw new MissingServletRequestParameterException("userName", "");
        }

        if (ObjectUtil.isNull(tomstoneInsertReqVo.getFamilyId())){
            throw new MissingServletRequestParameterException("familyId", "number");
        }
        Tombstone tombstone = new Tombstone();
        tombstone.setUserName(tomstoneInsertReqVo.getUserName());
        tombstone.setFamilyId(tomstoneInsertReqVo.getFamilyId());
        tombstone.setPicture(tomstoneInsertReqVo.getPicture());
        tombstone.setContent(tomstoneInsertReqVo.getContent());
        return tombstoneService.insertTomstone(tombstone);
    }
    @ApiOperation(value = "添加贴文")
    @RequestMapping(value = "/insertTomstoneessay", method = RequestMethod.POST)
    public JsonResult insertTomstoneessay(@RequestBody TomstoneEssayReqVo tomstoneEssayReqVo) throws Exception {

        if (ObjectUtil.isNull(tomstoneEssayReqVo.getTombstoneId())){
            throw new MissingServletRequestParameterException("tombstoneId", "number");
        }
        TombstoneEssay tombstone = new TombstoneEssay();
        tombstone.setEssayId(tomstoneEssayReqVo.getEssayId());
        tombstone.setTombstoneId(tomstoneEssayReqVo.getTombstoneId());
        tombstone.setUserId(tomstoneEssayReqVo.getUserId());
        return tombstoneService.insertTomstoneessay(tombstone);
    }

    @ApiOperation(value = "得到贴文")
    @RequestMapping(value = "/getTomstoneessay", method = RequestMethod.POST)
    public JsonResult getTomstoneessay(@RequestBody TomstoneEssayReqVo tomstoneEssayReqVo) throws Exception {

        if (ObjectUtil.isNull(tomstoneEssayReqVo.getTombstoneId())){
            throw new MissingServletRequestParameterException("tombstoneId", "number");
        }
        TombstoneEssay tombstone = new TombstoneEssay();
        tombstone.setTombstoneId(tomstoneEssayReqVo.getTombstoneId());
        tombstone.setUserId(tomstoneEssayReqVo.getUserId());
        return tombstoneService.getTomstoneessay(tombstone);
    }
    @ApiOperation(value = "得到贡品类型")
    @RequestMapping(value = "/getTomstonegift", method = RequestMethod.GET)
    public JsonResult getTomstonegift(@RequestParam(value = "tombstoneId") Integer tombstoneId) throws Exception {
        if (ObjectUtil.isNull(tombstoneId)){
            throw new MissingServletRequestParameterException("tombstoneId", "number");
        }

        return tombstoneService.getTomstonegift(tombstoneId);
    }


    @ApiOperation(value = "得到贡品人员")
    @RequestMapping(value = "/getTomstonegiftuser", method = RequestMethod.GET)
    public JsonResult getTomstonegiftuser(@RequestParam(value = "tombstoneId") Integer tombstoneId,@RequestParam(value = "giftId") Integer giftId) throws Exception {
        if (ObjectUtil.isNull(tombstoneId)){
            throw new MissingServletRequestParameterException("tombstoneId", "number");
        }

        return tombstoneService.getTomstonegiftuser(tombstoneId,giftId);
    }


    @ApiOperation(value = "祠堂家族详细内容")
    @RequestMapping(value = "/findTomstoneByfamilyId", method = RequestMethod.GET)
    public JsonResult findTomstoneByfamilyId( @RequestParam(value = "id") Integer id) throws Exception {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        return tombstoneService.findTomstoneByfamilyId(id);
    }



    @ApiOperation(value = "添加留言")
    @RequestMapping(value = "/insertTomstoneMessage", method = RequestMethod.POST)
    public JsonResult insertTomstoneMessage(@RequestBody TomstoneInsertMessage tomstoneInsertMessage) throws Exception {

        if (ObjectUtil.isNull(tomstoneInsertMessage.getTombstoneId())) {
            throw new MissingServletRequestParameterException("tombstoneId", "number");
        }

        if (ObjectUtil.isNull(tomstoneInsertMessage.getUserId())){
            throw new MissingServletRequestParameterException("userid", "number");
        }
        TombstoneMessage tombstone = new TombstoneMessage();
        tombstone.setTombstoneId(tomstoneInsertMessage.getTombstoneId());
        tombstone.setUserId(tomstoneInsertMessage.getUserId());
        tombstone.setMessage(tomstoneInsertMessage.getMessage());
        return tombstoneService.insertTomstoneMessage(tombstone);
    }



    @ApiOperation(value = "祠堂扫墓")
    @RequestMapping(value = "/insertTomstoneSweep", method = RequestMethod.POST)
    public JsonResult insertTomstoneSweep(@RequestBody TomstoneInsertSweep tomstoneInsertSweep) throws Exception {

        if (ObjectUtil.isNull(tomstoneInsertSweep.getTombstoneId())) {
            throw new MissingServletRequestParameterException("tombstoneId", "number");
        }

        if (ObjectUtil.isNull(tomstoneInsertSweep.getUserId())){
            throw new MissingServletRequestParameterException("userid", "number");
        }
        TombstoneSweep tombstone = new TombstoneSweep();
        tombstone.setTombstoneId(tomstoneInsertSweep.getTombstoneId());
        tombstone.setUserId(tomstoneInsertSweep.getUserId());
        return tombstoneService.insertTomstoneSweep(tombstone);
    }

    @ApiOperation(value = "祠堂献花")
    @RequestMapping(value = "/insertTomstoneFlower", method = RequestMethod.POST)
    public JsonResult insertTomstoneFlower(@RequestBody TomstoneInsertSweep tomstoneInsertSweep) throws Exception {

        if (ObjectUtil.isNull(tomstoneInsertSweep.getTombstoneId())) {
            throw new MissingServletRequestParameterException("tombstoneId", "number");
        }

        if (ObjectUtil.isNull(tomstoneInsertSweep.getUserId())){
            throw new MissingServletRequestParameterException("userid", "number");
        }
        TombstoneFlower tombstone = new TombstoneFlower();
        tombstone.setTombstoneId(tomstoneInsertSweep.getTombstoneId());
        tombstone.setUserId(tomstoneInsertSweep.getUserId());
        return tombstoneService.insertTomstoneFlower(tombstone);
    }



    @ApiOperation(value = "祠堂献祭")
    @RequestMapping(value = "/insertTomstonegiftuser", method = RequestMethod.POST)
    public JsonResult insertTomstonegiftuser(@RequestBody TomstoneInsertGiftReqVo  tomstoneInsertGiftReqVo) throws Exception {

        if (ObjectUtil.isNull(tomstoneInsertGiftReqVo.getGiftId())) {
            throw new MissingServletRequestParameterException("giftId", "number");
        }

        if (ObjectUtil.isNull(tomstoneInsertGiftReqVo.getUserId())){
            throw new MissingServletRequestParameterException("userid", "number");
        }
        TombstoneUsergift tombstone = new TombstoneUsergift();
        tombstone.setTombstone(tomstoneInsertGiftReqVo.getTombstoneId());
        tombstone.setUserid(tomstoneInsertGiftReqVo.getUserId());
        tombstone.setGiftid(tomstoneInsertGiftReqVo.getGiftId());
        return tombstoneService.insertTomstonegiftuser(tombstone);
    }
    @ApiOperation(value = "添加祭品")
    @RequestMapping(value = "/insertTomstoneGifttype", method = RequestMethod.POST)
    public JsonResult insertTomstoneGifttype(@RequestBody TombstoneInsertGifttypeReqVo tombstoneInsertGifttypeReqVo) throws Exception {


        TombstoneGift tombstone = new TombstoneGift();
        tombstone.setName(tombstoneInsertGifttypeReqVo.getName());
        tombstone.setMessage(tombstoneInsertGifttypeReqVo.getMessage());
        tombstone.setPicture(tombstoneInsertGifttypeReqVo.getPicture());
        tombstone.setTombstoneId(tombstoneInsertGifttypeReqVo.getTombstoneId());
        return tombstoneService.insertTomstoneGifttype(tombstone);
    }





}
