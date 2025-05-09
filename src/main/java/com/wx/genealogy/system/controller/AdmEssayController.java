package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.annotation.MyLog;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.Essay;
import com.wx.genealogy.system.service.EssayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AdmEssayController
 * @Author weixin
 * @Data 2021/10/26 10:27
 * @Description
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admEssay")
@Api(tags = "帖子接口（管理员）")
public class AdmEssayController {

    @Autowired
    private EssayService essayService;

    @ApiOperation(value = "分页查看帖子")
    @PreAuthorize("hasAuthority('essay:get/essay')")
    @RequestMapping(value = "/selectEssay", method = RequestMethod.GET)
    public JsonResult selectEssay(@RequestParam(value = "limit") Integer limit,
                                  @RequestParam(value = "page") Integer page,
                                  @RequestParam(value = "sort",required = false) Integer sort) throws Exception {
        if (ObjectUtil.isNull(page)) {
            throw new MissingServletRequestParameterException("page", "number");
        }
        if (ObjectUtil.isNull(limit)) {
            throw new MissingServletRequestParameterException("limit", "number");
        }
        Essay essay = new Essay();
        return essayService.selectEssay(page ,limit,essay,sort);
    }

    @MyLog(value = "管理员删除帖子")
    @ApiOperation("根据id删除单个功能")
    @PreAuthorize("hasAuthority('essay:delete/essay')")
    @RequestMapping(value="/deleteEssayById",method= RequestMethod.DELETE)
    public JsonResult deleteEssayById(@RequestParam("id")Integer id) throws Exception
    {
        if (ObjectUtil.isNull(id)) {
            throw new MissingServletRequestParameterException("id", "number");
        }
        return essayService.deleteEssayById(id);
    }
}
