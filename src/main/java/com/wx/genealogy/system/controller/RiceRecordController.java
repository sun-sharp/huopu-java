package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.RiceRecord;
import com.wx.genealogy.system.service.RiceRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 米收支明细 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-10-20
 */
@RestController
@Api(tags = "用户米收支明细接口")
@RequestMapping("/riceRecord")
public class RiceRecordController {

    @Autowired
    private RiceRecordService riceRecordService;

    @ApiOperation(value = "根据userId分页查询")
    @RequestMapping(value = "/selectRiceRecordByUserId", method = RequestMethod.GET)
    public JsonResult selectFamilyManageLog(@RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "page") Integer page,
                                            @RequestParam(value = "userId") Integer userId,@RequestParam(value = "type") Integer type) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        RiceRecord riceRecord = new RiceRecord();
        riceRecord.setUserId(userId);
        return riceRecordService.selectRiceRecord(page ,limit,type,riceRecord);
    }


    /**
     * 查询所有明细记录
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/selectRiceRecordList")
    @ApiOperation(value = "查询所有明细记录")
    public JsonResult selectRiceRecordList(Integer page,Integer limit){
    return this.riceRecordService.selectRiceRecordList(page,limit);
    }
}

