package com.wx.genealogy.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ExcelUtil;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.FamilyUser;
import com.wx.genealogy.system.service.FamilyUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @ClassName AdmFamilyUserController
 * @Author weixin
 * @Data 2021/10/28 10:01
 * @Description
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admFamilyUser")
@Api(tags = "家族成员接口(管理员)")
public class AdmFamilyUserController {

    @Autowired
    private FamilyUserService familyUserService;

    @ApiOperation(value = "根据家族id分页查询当前家族下的所有成员")
//    @PreAuthorize("hasAuthority('family:get/family')")
    @RequestMapping(value = "/selectFamilyUserByFamilyId", method = RequestMethod.GET)
    public JsonResult selectFamilyUserByFamilyId(@RequestParam(value = "limit") Integer limit,
                                                 @RequestParam(value = "page") Integer page,
                                                 @RequestParam(value = "familyId") Integer familyId,
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
        familyUserSelect.setRelation(relation);
        familyUserSelect.setSex(sex);
        familyUserSelect.setStatus(2);
        return familyUserService.selectFamilyUserByFamilyId(page ,limit,1,familyUserSelect);
    }


}
