package com.wx.genealogy.system.service;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.system.entity.FamilyDownload;

import javax.servlet.http.HttpServletResponse;

public interface FamilyDownloadService {
    public JsonResult createConnect(FamilyDownload familyDownload);

    public JsonResult connectList(FamilyDownload familyDownload);

    public  void exportDictData(HttpServletResponse response ,String uid);
}
