package com.wx.genealogy.system.controller;

import com.alibaba.fastjson.JSON;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.common.util.wechat.prop.WeChatProp;
import com.wx.genealogy.common.util.wechat.util.MyHttpClient;
import com.wx.genealogy.system.entity.User;
import com.wx.genealogy.system.service.UserService;
import com.wx.genealogy.system.vo.req.UserInsertReqVo;
import com.wx.genealogy.system.vo.req.UserUpdateReqVo;
import com.wx.genealogy.system.vo.req.UserFamilyIndexUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public JsonResult login(@RequestParam("code") String code) throws Exception {
        String openid = "";
        //获取用户openid
        String url = WeChatProp.WEIXIN_API_URL + "/sns/jscode2session?appid="+WeChatProp.APP_ID+"&secret="+WeChatProp.SECRET+"&js_code="+
                code+"&grant_type=authorization_code";
        Map maps = (Map) JSON.parse(MyHttpClient.client(url));
        for (Object map : maps.entrySet()) {
            if (((Map.Entry) map).getKey() == "openid") {
                openid = (String) ((Map.Entry) map).getValue();
            }
        }
        if(openid.equals("")==true)
        {
            return ResponseUtil.fail("openid获取失败");
        }

        User userFind=new User();
        userFind.setOpenid(openid);

       JsonResult result=userService.findUser(userFind);
       System.out.println(result);


        return  result;
    }


    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
    public JsonResult insertUser(@RequestBody UserInsertReqVo userInsertReqVo) throws Exception {

        if (ObjectUtil.isNull(userInsertReqVo.getOpenid())) {
            throw new MissingServletRequestParameterException("openid", "String");
        }
        if (ObjectUtil.isNull(userInsertReqVo.getAvatar())) {
            throw new MissingServletRequestParameterException("avatar", "String");
        }
        if (ObjectUtil.isNull(userInsertReqVo.getNickName())) {
            throw new MissingServletRequestParameterException("name", "String");
        }

        User userInsert=new User();
        userInsert.setOpenid(userInsertReqVo.getOpenid());
        userInsert.setAvatar(userInsertReqVo.getAvatar());
        userInsert.setNickName(userInsertReqVo.getNickName());
        userInsert.setLastLoginTime(DateUtils.getDateTime());
        return userService.insertUser(userInsert);
    }

    @ApiOperation(value = "根据id修改单个用户")
    @RequestMapping(value = "/updateUserById", method = RequestMethod.PUT)
    public JsonResult updateUserById(@RequestBody UserUpdateReqVo userUpdateReqVo) throws Exception {

        if (ObjectUtil.isNull(userUpdateReqVo.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        User userUpdate=new User();
        userUpdate.setId(userUpdateReqVo.getUserId());
        userUpdate.setAvatar(userUpdateReqVo.getAvatar());
        userUpdate.setRealName(userUpdateReqVo.getRealName());
        userUpdate.setSex(userUpdateReqVo.getSex());
        userUpdate.setSecondName(userUpdateReqVo.getSecondName());
        return userService.updateUserById(userUpdate);
    }

    @ApiOperation(value = "根据用户ID更新家庭索引")
    @RequestMapping(value = "/updateFamilyIndex", method = RequestMethod.POST)
    public JsonResult updateFamilyIndex(@RequestBody UserFamilyIndexUpdateReqVo updateReq) throws Exception {
        if (ObjectUtil.isNull(updateReq.getUserId())) {
            throw new MissingServletRequestParameterException("userId", "number");
        }

        User userUpdate = new User();
        userUpdate.setId(updateReq.getUserId());
        userUpdate.setFamily_index(updateReq.getFamilyIndex());

        return userService.updateUserById(userUpdate);
    }

    @ApiOperation(value = "根据用户id查询查询用户信息")
    @RequestMapping(value = "/findUserById", method = RequestMethod.GET)
    public JsonResult findUserById(@RequestParam(value = "userId") Integer userId) throws Exception {
        if (ObjectUtil.isNull(userId)) {
            throw new MissingServletRequestParameterException("userId", "number");
        }
        return userService.findUserById(userId);
    }

}

