package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.FamilyDownload;
import com.wx.genealogy.system.service.FamilyDownloadService;
import com.wx.genealogy.system.service.impl.FamilyGenealogyServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/familyDownload")
@Api(tags = "家谱图下载链接")
public class FamilyDownloadController {

    @Autowired
    FamilyDownloadService familyDownloadService;

    @Autowired
    FamilyGenealogyServiceImpl familyGenealogyService;

//    @GetMapping("/connect")
//    @ApiOperation(value = "导出家谱图")
//    public JsonResult connect( @RequestParam(name = "uid") String uid) {
//        FamilyDownload familyDownload = new FamilyDownload();
//        familyDownload.setUid(uid);
//        return familyDownloadService.createConnect(familyDownload);
//    }

    @GetMapping("/connectList")
    @ApiOperation(value = "导出家谱图链接列表")
    public JsonResult connectList( @RequestParam(name = "userId") Integer userId,
                                   @RequestParam(value = "familyId") Integer familyId) {
        FamilyDownload familyDownload = new FamilyDownload();
        familyDownload.setUserId(userId);
        familyDownload.setFamilyId(familyId);
        return familyDownloadService.connectList(familyDownload);
    }

    @GetMapping("/connect")
    @ApiOperation(value = "导出家谱图")
    public void getConnectList(HttpServletResponse response ,@RequestParam(name = "uid") String uid) {
        familyDownloadService.exportDictData(response,uid);

    }
}
