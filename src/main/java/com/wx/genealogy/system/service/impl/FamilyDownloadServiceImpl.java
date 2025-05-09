package com.wx.genealogy.system.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.DateUtils;
import com.wx.genealogy.common.util.ExcelUtil;
import com.wx.genealogy.common.util.ExcelUtils.EasyExcelCellWriteHandler;
import com.wx.genealogy.common.util.ExcelUtils.EasyExcelSheetWriteHandler;
import com.wx.genealogy.common.util.ResponseUtil;
import com.wx.genealogy.system.entity.Family;
import com.wx.genealogy.system.entity.FamilyDownload;
import com.wx.genealogy.system.entity.FamilyGenealogy;
import com.wx.genealogy.system.mapper.FamilyDownloadDao;
import com.wx.genealogy.system.service.FamilyDownloadService;
import com.wx.genealogy.system.service.FamilyGenealogyService;
import com.wx.genealogy.system.service.FamilyService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class FamilyDownloadServiceImpl  extends ServiceImpl<FamilyDownloadDao, FamilyDownload> implements FamilyDownloadService {


    @Autowired
    FamilyDownloadDao familyDownloadDao;

    @Autowired
    FamilyService familyService;

    @Autowired
    FamilyGenealogyService familyGenealogyService;

    public JsonResult connectList(FamilyDownload familyDownload){
        QueryWrapper<FamilyDownload> queryWrapper = new QueryWrapper<FamilyDownload>();
        queryWrapper.eq("user_id", familyDownload.getUserId());
        queryWrapper.eq("family_id", familyDownload.getFamilyId());
        queryWrapper.ge("time", DateUtils.getMonthDate(new Date()));
        queryWrapper.orderByDesc("time");
        List<FamilyDownload> list = familyDownloadDao.selectList(queryWrapper);
        return ResponseUtil.ok("获取成功",list);
    }


    public JsonResult createConnect(FamilyDownload familyDownload){
        QueryWrapper<FamilyDownload> queryWrapper = new QueryWrapper<FamilyDownload>();
        queryWrapper.eq("uid", familyDownload.getUid());
        List<FamilyDownload> list = familyDownloadDao.selectList(queryWrapper);
        String generatedFilePath = "";
        if(list.size()<0){
            return ResponseUtil.ok("已失效");
        }else{
            Date date = list.get(0).getTime();
            Instant instant = date.toInstant();
            LocalDateTime createTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            // 获取当前时间
            LocalDateTime currentTime = LocalDateTime.now();
            // 计算创建时间与当前时间之间的差异（以天为单位）
            long daysDifference = ChronoUnit.DAYS.between(createTime, currentTime);
            // 检查是否超过一个月（30天）
            if (daysDifference > 30) {
                System.out.println("创建时间超过一个月");
                return ResponseUtil.fail("已失效");
            }

            int familyId = list.get(0).getFamilyId();
            deriveExcle(familyId);
        }
        return ResponseUtil.ok("获取成功");
    }


    public JsonResult deriveExcle(Integer familyId) {
        LambdaQueryWrapper<FamilyGenealogy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FamilyGenealogy::getFamilyId, familyId);

        Family family = this.familyService.getById(familyId);
        if (Objects.isNull(family)) {
            return ResponseUtil.fail("未查询到家族信息");
        }

        List<FamilyGenealogy> genealogyList = this.familyGenealogyService.list(wrapper);
        if (CollectionUtils.isEmpty(genealogyList)) {
            return ResponseUtil.fail("该家族暂时没有家谱图");
        }

        // 写法1 JDK8+
        // since: 3.0.0-beta1

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = now.format(formatter);


        String fileName = "火谱-" + family.getName()+ formattedDateTime + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可

        EasyExcel.write(fileName, FamilyGenealogy.class)
                .sheet("模板")
                .doWrite(genealogyList);

        return ResponseUtil.ok("导出成功文件地址:" + fileName);
    }


    public void exportDictData(HttpServletResponse response ,String uid) {
        //设置文件下载的请求头
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止中文乱码，但是easyexcel不会发生中文乱码！
        //String fileName = URLEncoder.encode("imedi智慧健康服务数据字典", "UTF-8");
        int familyId;
        QueryWrapper<FamilyDownload> queryWrapper = new QueryWrapper<FamilyDownload>();
        queryWrapper.eq("uid", uid);
        List<FamilyDownload> list = familyDownloadDao.selectList(queryWrapper);
        String generatedFilePath = "";
        if(list.size()<0){
        }else{
            familyId = list.get(0).getFamilyId();
            familyGenealogyService.upFamilyGenealogyChart(familyId);//更新家谱
            Date date = list.get(0).getTime();
            Instant instant = date.toInstant();
            LocalDateTime createTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
            // 获取当前时间
            LocalDateTime currentTime = LocalDateTime.now();
            // 计算创建时间与当前时间之间的差异（以天为单位）
            long daysDifference = ChronoUnit.DAYS.between(createTime, currentTime);
            // 检查是否超过一个月（30天）
            if (daysDifference > 30) {
                System.out.println("创建时间超过一个月");
            }else{

                LambdaQueryWrapper<FamilyGenealogy> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(FamilyGenealogy::getFamilyId, familyId);
                wrapper.orderByAsc(FamilyGenealogy::getGeneration);
                Family family = this.familyService.getById(familyId);
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);
                String fileName = "火谱-" +family.getName() + formattedDateTime ;
                try {
                    String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
                    encodedFileName = encodedFileName.replace(":", "%3A"); // 将冒号替换为 %3A
                    encodedFileName = encodedFileName.replace("+", "%20"); // 将+替换为%20

                    response.setHeader("Content-disposition", "attachment;filename="+ encodedFileName + ".xlsx");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //查询数据库
                //        Family family = this.familyService.getById(familyId);
                List<FamilyGenealogy> genealogyList = this.familyGenealogyService.list(wrapper);
                //调用方法执行写操作
                try {
                    for(FamilyGenealogy fg : genealogyList) {
                        fg.setIsAliveDesc(null!=fg.getIsAlive()&&fg.getIsAlive()==1?"在世":"去世");
                    }
                    String exportTime = DateUtils.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
                    EasyExcelSheetWriteHandler writeHandler = new EasyExcelSheetWriteHandler("");
                    JSONObject obj = new JSONObject();
                    obj.put("family", family.getName());
                    obj.put("exportTime", exportTime);
                    EasyExcelCellWriteHandler easyExcelTitleHandler = new EasyExcelCellWriteHandler(null, obj);
                    ExcelUtil.export(response, fileName, "模板", genealogyList, FamilyGenealogy.class, writeHandler, easyExcelTitleHandler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }

}
