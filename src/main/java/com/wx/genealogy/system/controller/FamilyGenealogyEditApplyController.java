package com.wx.genealogy.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.FamilyGenealogyEditApply;
import com.wx.genealogy.system.service.FamilyGenealogyEditApplyService;
import com.wx.genealogy.system.vo.req.FamilyGenealogyEditApplyReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 家族成员编辑申请Controller
 *
 * @author leo
 * @date 2024-07-05
 */
@Api(tags = "家谱简历编辑申请接口")
@RestController
@RequestMapping("/familyGenealogyEditApply")
public class FamilyGenealogyEditApplyController {

    @Resource
    private FamilyGenealogyEditApplyService familyGenealogyEditApplyService;

    @ApiOperation(value = "新增")
    @RequestMapping(value = "/insert", method = RequestMethod.PUT)
    public JsonResult insert(@RequestBody FamilyGenealogyEditApplyReqVo applyReqVo) throws Exception {

        if (ObjectUtil.isNull(applyReqVo.getFamilyGenealogyId())) {
            return ResponseUtil.fail("参数错误！");
        }
        //查询是否存在待审核的申请，存在则直接返回 不允许申请
        QueryWrapper<FamilyGenealogyEditApply> applyWrapper = new QueryWrapper<FamilyGenealogyEditApply>();
        applyWrapper.eq("family_genealogy_id", applyReqVo.getFamilyGenealogyId());
        applyWrapper.eq("type", applyReqVo.getType());
        applyWrapper.eq("status", 0);//待审核
        long closeCnt = familyGenealogyEditApplyService.count(applyWrapper);
        if(closeCnt > 0) {
            return ResponseUtil.fail("不能重复申请！");
        }

        FamilyGenealogyEditApply apply = new FamilyGenealogyEditApply();
        apply.setApplyTime(new Date());
        apply.setFamilyGenealogyId(applyReqVo.getFamilyGenealogyId());
        apply.setStatus(0);
        apply.setReason(applyReqVo.getReason());
        apply.setType(applyReqVo.getType());
        familyGenealogyEditApplyService.save(apply);
        return ResponseUtil.ok("申请成功");
    }

    @ApiOperation(value = "审核")
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public JsonResult audit(@RequestParam("id") Integer id,
                                 @RequestParam("status") Integer status) throws Exception {
        FamilyGenealogyEditApply apply = familyGenealogyEditApplyService.getById(id);
        apply.setAuditTime(new Date());
        apply.setStatus(status);
        familyGenealogyEditApplyService.updateById(apply);
        return ResponseUtil.ok("申请成功");
    }

    @ApiOperation(value = "查询列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list(@RequestParam(value = "familyGenealogyId",required = false) Integer familyGenealogyId) throws Exception {
        LambdaQueryWrapper<FamilyGenealogyEditApply> wrapper = new LambdaQueryWrapper<>();
        if(null != familyGenealogyId) {
            wrapper.eq(FamilyGenealogyEditApply::getFamilyGenealogyId, familyGenealogyId);
        }
        List<FamilyGenealogyEditApply> applyList = familyGenealogyEditApplyService.list(wrapper);
        return ResponseUtil.ok("成功", applyList);
    }

}
