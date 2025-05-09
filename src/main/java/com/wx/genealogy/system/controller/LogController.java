package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.annotation.MyLog;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.service.SysLogService;
import com.wx.genealogy.system.vo.req.GetLogReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName LogController
 * @Author hangyi
 * @Data 2020/7/21 12:02
 * @Description
 * @Version 1.0
 **/
@Api(tags = "日志管理接口")
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private SysLogService sysLogService;

    @ApiOperation("管理员查询日志")
    @PreAuthorize("hasAuthority('log:get/log')")
    @RequestMapping(value = "/sysUserGetLog", method = RequestMethod.POST)
    public JsonResult sysUserGetLog(@RequestBody GetLogReqVo getLogReqVo) throws Exception {
        return sysLogService.sysUserGetLog(getLogReqVo.getPage(), getLogReqVo.getLimit(), getLogReqVo.getStartTime() ,getLogReqVo.getEndTime());
    }

    @MyLog(value = "管理员删除日志")
    @ApiOperation("管理员删除日志")
    @PreAuthorize("hasAuthority('log:delete/log')")
    @RequestMapping(value = "/sysUserDeleteLog{id}", method = RequestMethod.DELETE)
    public JsonResult userDeleteLog(@PathVariable Integer id) throws Exception {
        return sysLogService.deleteLog(id);
    }


}
