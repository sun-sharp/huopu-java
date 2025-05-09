package com.wx.genealogy.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wx.genealogy.common.annotation.MyLog;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ExcelUtil;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @ClassName AdmUserController
 * @Author weixin
 * @Data 2021/10/26 10:09
 * @Description
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admUser")
@Api(tags = "用户接口（管理员）")
public class AdmUserController {

    @Autowired
    private UserService userService;

    @MyLog(value = "管理员修改用户")
    @ApiOperation(value = "根据id修改单个用户(管理员)")
    @PreAuthorize("hasAuthority('user:update/user')")
    @RequestMapping(value = "/updateUserById", method = RequestMethod.PUT)
    public JsonResult updateUserById(@RequestBody User user) throws Exception {

        if (ObjectUtil.isNull(user.getId())) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        return userService.updateUserById(user);
    }

    @ApiOperation(value = "管理员分页查询用户")
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
        return userService.selectUser(page,limit,user);
    }

    /**
     * 导出投递记录Excel
     */
    @PostMapping("/exportExcel")
    @ApiOperation(value = "导出投递记录Excel")
    public void exportExcel(@RequestBody List<Long> ids, HttpServletResponse response) {

        if (ObjectUtil.isNull(ids) || ids.size() == 0) {
//            return ResponseUtil.fail("请选择要导出的记录！");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if(null != ids && ids.size() > 0) {
            wrapper.in(User::getId, ids);
        }
        wrapper.orderByDesc(User::getId);

        List<User> userList = userService.list(wrapper);
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = now.format(formatter);

        String fileName = "用户记录-" + formattedDateTime;
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        try {
            for(User user : userList) {

            }
            ExcelUtil.export(response, fileName, "模板", userList, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
