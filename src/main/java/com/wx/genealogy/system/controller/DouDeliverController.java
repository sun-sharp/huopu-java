package com.wx.genealogy.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wx.genealogy.common.annotation.MyLog;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ExcelUtil;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.DouDeliver;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.service.DouDeliverService;
import com.wx.genealogy.system.vo.req.DouDeliverReqVo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 斗投递Controller
 *
 * @author leo
 * @date 2024-07-05
 */
@RestController
@RequestMapping("/douDeliver")
public class DouDeliverController {

    @Resource
    private DouDeliverService douDeliverService;

    @MyLog(value = "批量投递接口")
    @ApiOperation(value = "批量投递接口")
    @PreAuthorize("hasAuthority('user:update/user')")
    @RequestMapping(value = "/updateUserByIds", method = RequestMethod.PUT)
    public JsonResult updateUserByIds(@RequestBody DouDeliverReqVo deliver) throws Exception {

        if (ObjectUtil.isNull(deliver.getIds()) || deliver.getIds().size() == 0) {
            return ResponseUtil.fail("请选择用户！");
        }
        if(null == deliver.getValidYear() || deliver.getValidYear() > 50 || deliver.getValidYear() < 1) {
            return ResponseUtil.fail("有效期为1-50之间的整数！");
        }
        return douDeliverService.updateUserByIds(deliver);
    }

    @ApiOperation(value = "斗投递列表")
    @PreAuthorize("hasAuthority('user:get/user')")
    @RequestMapping(value = "/selectUser", method = RequestMethod.GET)
    public JsonResult selectUser(@RequestParam("page") Integer page,
                                 @RequestParam("limit") Integer limit,
                                 @RequestParam(value = "nickName",required = false) String nickName,
                                 @RequestParam(value = "sex",required = false) Integer sex,
                                 @RequestParam(value = "realName",required = false) String realName) throws Exception {
        User user = new User();
        user.setNickName(nickName);
        user.setRealName(realName);
        user.setSex(sex);
        return douDeliverService.selectUser(page,limit,user);
    }

    @ApiOperation(value = "斗投递记录列表")
    @PreAuthorize("hasAuthority('user:get/user')")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public JsonResult page(@RequestParam("page") Integer page,
                           @RequestParam("limit") Integer limit,
                           @RequestParam(value = "userName",required = false) String userName) throws Exception {
        DouDeliver deliver = new DouDeliver();
        deliver.setUserName(userName);
        return douDeliverService.page(page,limit,deliver);
    }

    @ApiOperation(value = "小程序查看斗清单")
    @RequestMapping(value = "/getDeliverListByUserId", method = RequestMethod.GET)
    public JsonResult getDeliverListByUserId(@RequestParam(value = "userId",required = true) Integer userId) throws Exception {
        return douDeliverService.list(userId);
    }

    /**
     * 导出投递记录Excel
     */
    @PostMapping("/exportExcel")
    @ApiOperation(value = "导出投递记录Excel")
    public void exportExcel(@RequestBody List<Long> ids, HttpServletResponse response) {

        if (ObjectUtil.isNull(ids) || ids.size() == 0) {
//            return ResponseUtil.fail("请选择要导出的记录！");
            return;
        }
        LambdaQueryWrapper<DouDeliver> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(DouDeliver::getId, ids);
        wrapper.orderByDesc(DouDeliver::getId);

        List<DouDeliver> douDeliverList = douDeliverService.list(wrapper);
        if (CollectionUtils.isEmpty(douDeliverList)) {
//            return ResponseUtil.fail("该家族暂时没有家谱图");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = now.format(formatter);

        String fileName = "投递记录-" + formattedDateTime;
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        try {
            ExcelUtil.export(response, fileName, "模板", douDeliverList, DouDeliver.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
