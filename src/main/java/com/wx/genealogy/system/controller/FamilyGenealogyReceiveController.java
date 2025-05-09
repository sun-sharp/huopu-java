package com.wx.genealogy.system.controller;


import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.FamilyGenealogyReceive;
import com.wx.genealogy.system.service.FamilyGenealogyReceiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * <p>
 * 认领族谱图申请 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2023-03-03
 */
@RestController
@RequestMapping("/familyGenealogyReceive")
@Api(tags = "认领族谱图")
public class FamilyGenealogyReceiveController {

    @Resource
    private FamilyGenealogyReceiveService familyGenealogyReceiveService;

    /**
     * 认领
     *
     * @param userId            当前用户id
     * @param familyGenealogyId 认领数据id
     * @return
     */
    @ApiOperation(value = "认领")
    @GetMapping("/fetch")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userId", value = "当前用户id", dataType = "java.lang.Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "familyGenealogyId", value = "认领数据id", dataType = "java.lang.Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "familyId", value = "家族id", dataType = "java.lang.Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "phone", value = "电话", dataType = "java.lang.String", paramType = "query", required = true),
            @ApiImplicitParam(name = "generation", value = "第几代", dataType = "java.lang.Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "text", value = "申请理由", dataType = "java.lang.String", paramType = "query", required = true),
            @ApiImplicitParam(name = "relation", value = "身份", dataType = "java.lang.Integer", paramType = "query", required = true),
    })
    public JsonResult fetch(Integer userId, Integer familyGenealogyId,Integer generation,Integer familyId,String phone,String text,Integer relation) {
        return this.familyGenealogyReceiveService.fetch(userId, familyGenealogyId ,generation,familyId,phone,text,relation);
    }

    /**
     * 认领
     *
     * @param userId  当前用户id
     * @return
     */
    @ApiOperation(value = "认领按钮显示")
    @GetMapping("/claim")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userId", value = "当前用户id", dataType = "java.lang.Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "familyId", value = "家族id", dataType = "java.lang.Integer", paramType = "query", required = true)
    })
    public JsonResult claim(Integer userId,Integer familyId) {
        return this.familyGenealogyReceiveService.claim(userId , familyId);
    }

    /**
     * 根据用户id和家族id查看数据
     *
     * @param userId  当前用户id
     * @return
     */
    @ApiOperation(value = "认领按钮显示")
    @GetMapping("/getGenealogy")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userId", value = "当前用户id", dataType = "java.lang.Integer", paramType = "query", required = true),
            @ApiImplicitParam(name = "familyId", value = "家族id", dataType = "java.lang.Integer", paramType = "query", required = true)
    })
    public JsonResult getGenealogy(Integer userId,Integer familyId) {


        return this.familyGenealogyReceiveService.getGenealogy(userId , familyId);
    }



    /**
     * 查询需要审核的认领申请
     *
     * @param familyId 当前家族id
     * @return
     */
    @GetMapping("/selectAuditApply")
    @ApiOperation(value = "查询需要审核的认领申请")
    @ApiImplicitParam(name = "familyId", value = "当前家族id", dataType = "java.lang.Integer", paramType = "query", required = true)
    public JsonResult selectAuditApply(Integer familyId) {
        return this.familyGenealogyReceiveService.selectAuditApply(familyId);
    }


    /**
     * 审核族谱图认领申请
     * @param receive
     * @return
     */
    @PostMapping("/auditFamilyGenealogyReceive")
    @ApiOperation(value = "审核族谱图认领申请")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userId", value = "当前审核人用户id", dataType = "java.lang.Integer", paramType = "body", required = true),
            @ApiImplicitParam(name = "id", value = "审核数据id", dataType = "java.lang.Integer", paramType = "body", required = true),
            @ApiImplicitParam(name = "status", value = "审核状态（0待审核 1审核通过 2拒绝）", dataType = "java.lang.Integer", paramType = "body", required = true),
            @ApiImplicitParam(name = "generation", value = "第几代", dataType = "java.lang.Integer", paramType = "body", required = true),
            @ApiImplicitParam(name = "familyId", value = "家族id", dataType = "java.lang.Integer", paramType = "body", required = true),
            @ApiImplicitParam(name = "refuse", value = "拒绝原因（不必填）", dataType = "java.lang.String", paramType = "body"),

    })
    public JsonResult auditFamilyGenealogyReceive(@RequestBody @ApiIgnore FamilyGenealogyReceive receive) {
        return this.familyGenealogyReceiveService.auditFamilyGenealogyReceive(receive);
    }

    /**
     * 查询我的认领族谱图申请记录
     *
     * @param userId 登录人id
     * @return
     */
    @GetMapping("/selectMyApply")
    @ApiOperation(value = "查询我的认领族谱图申请记录")
    @ApiImplicitParam(name = "userId", value = "当前审核人用户id", dataType = "java.lang.Integer", paramType = "query", required = true)
    public JsonResult selectMyApply(Integer userId) {
        if (ObjectUtil.isNull(userId)) {
            return ResponseUtil.fail("当前审核人用户id不能为空");
        }
        FamilyGenealogyReceive receive = new FamilyGenealogyReceive();
        receive.setUserId(userId);
        return this.familyGenealogyReceiveService.selectMyApply(receive);
    }

    @GetMapping("/selectApply")
    @ApiOperation(value = "查询族谱图申请记录")
    @ApiImplicitParam(name = "familyId", value = "当前家族id", dataType = "java.lang.Integer", paramType = "query", required = true)
    public JsonResult selectApply(Integer familyId) {
        if (ObjectUtil.isNull(familyId)) {
            return ResponseUtil.fail("当前家族id不能为空");
        }
        FamilyGenealogyReceive receive = new FamilyGenealogyReceive();
        receive.setFamilyId(familyId);
        return this.familyGenealogyReceiveService.selectApply(receive);
    }




}

