package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.DouDeliverLog;
import com.wx.genealogy.system.service.DouDeliverLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 斗投递记录Controller
 *
 * @author leo
 * @date 2024-07-06
 */
@RestController
@RequestMapping("/douDeliverLog")
public class DouDeliverLogController {

    @Resource
    private DouDeliverLogService douDeliverLogService;

    @ApiOperation(value = "管理员分页查询用户")
    @PreAuthorize("hasAuthority('user:get/user')")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public JsonResult page(@RequestParam("page") Integer page,
                                 @RequestParam("limit") Integer limit,
                                 @RequestParam(value = "userName",required = false) String userName) throws Exception {
        DouDeliverLog deliverLog = new DouDeliverLog();
        deliverLog.setUserName(userName);
        return douDeliverLogService.page(page,limit,deliverLog);
    }

}
