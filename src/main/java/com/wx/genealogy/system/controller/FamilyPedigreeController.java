package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.FamilyPedigree;
import com.wx.genealogy.system.service.FamilyPedigreeService;
import com.wx.genealogy.system.vo.req.FamilyPedigreeRecursionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/familyPedigree")
@Api(tags = "族谱")
public class FamilyPedigreeController {
    @Autowired
    private FamilyPedigreeService familyPedigreeService;

    @GetMapping("/get")
    public String updateFamilyMessage()  {
        return "调用成功";
    }

    @ApiOperation(value = "获取每一代人数")
    @GetMapping("/getGeneration")
    public JsonResult getGeneration(Long id)  {


        return ResponseUtil.ok("获取成功",familyPedigreeService.getGeneration(id));
    }


//    @ApiOperation(value = "导入族谱")
    @PostMapping("/import")
    public JsonResult importUser(@RequestPart("file") MultipartFile file,@RequestPart("familyUser") String  jsonString) throws Exception {

        return familyPedigreeService.importFamilyPedigreeByUser(file,jsonString);

    }


//    @ApiOperation(value = "新增族谱")
    @PostMapping("/add")
    public JsonResult add(@RequestBody FamilyPedigree familyPedigree) {

        return  ResponseUtil.ok("修改成功",familyPedigreeService.insertFamilyPedigree(familyPedigree));
    }

    @PostMapping("/getRecursion")
    public JsonResult getFamilyPedigreeRecursion(@RequestBody FamilyPedigree familyPedigree) {

        return  ResponseUtil.ok("修改成功",familyPedigreeService.getFamilyPedigreeRecursion(familyPedigree.getFamilyId()));
    }

//    public List<FamilyPedigreeRecursionVo> treeFamilyPedigreeRecursionVo() {
@GetMapping("/getTree")
    public JsonResult treeFamilyPedigreeRecursionVo() {
         return  ResponseUtil.ok("获取成功",familyPedigreeService.treeFamilyPedigreeRecursionVo());
    }

}