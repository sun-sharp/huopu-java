package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.DouRecord;
import com.wx.genealogy.system.service.DouRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 斗收入明细 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2022-07-12
 */
@RestController
@RequestMapping("/douRecord")
@Api(tags = "斗收入明细")
public class DouRecordController {

    @Resource
    private DouRecordService douRecordService;

    @GetMapping("/selectDouRecordList")
    @ApiOperation(value = "查询所有斗明细记录")
    public JsonResult selectDouRecordList(Integer page, Integer limit,Integer userId){
        return this.douRecordService.selectDouRecordList(page,limit,userId);
    }

    /**
     * 兑换斗
     * @return
     */
    @PostMapping("/exchangeDouRecord")
    @ApiOperation(value ="兑换斗")
    public JsonResult exchangeDouRecord(@RequestBody DouRecord douRecord){
        return this.douRecordService.exchangeDouRecord(douRecord);
    }
}

