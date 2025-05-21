package com.wx.genealogy.system.controller;

import com.wx.genealogy.common.domin.JsonResult;
import com.wx.genealogy.common.util.ImageUtil;
import com.wx.genealogy.common.util.ImgUtil;
import com.wx.genealogy.system.service.FamilyGenealogyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName FileController
 * @Author hangyi
 * @Data 2020/6/29 16:36
 * @Description
 * @Version 1.0
 **/
@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * 家族logo
     **/
    public static String familyLogoPath = "/img/familylogo/";

    /**
     * 用户头像
     **/
    public static String userAvatarPath = "/img/useravatar/";

    /**
     * 文章配图
     **/
    public static String essayImgPath = "/img/essayimg/";

    /**
     * 任务完成时截图证明
     */
    public static  String taskResultsImgPath="/img/taskResultsImg/";

    /**
     * 家族语音
     */
    public static String audioPath = "/img/audio/";

    public static final String IMAGE_PATH = new File("").getAbsolutePath();

    @Autowired
    private FamilyGenealogyService familyGenealogyService;


    @ApiOperation(value = "家族logo上传")
    @RequestMapping(value = "/uploadFamilyLogo", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public JsonResult uploadFamilyLogo(@RequestParam("files") MultipartFile[] files) throws IOException {
        JsonResult jsonResult = new JsonResult(200);
        String[] address = ImgUtil.insertImg(files, familyLogoPath);
       address = ImageUtil.generateThumbnail2Directory(address);
        jsonResult.setData(address);
        return jsonResult;
    }

    @ApiOperation(value = "用户头像上传")
    @RequestMapping(value = "/uploadUserAvatar", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public JsonResult uploadUserAvatar(@RequestParam("files") MultipartFile[] files) throws IOException {
        JsonResult jsonResult = new JsonResult(200);
        String[] address = ImgUtil.insertImg(files, userAvatarPath);
        address = ImageUtil.generateThumbnail2Directory(address);
        jsonResult.setData(address);
        return jsonResult;
    }

    @ApiOperation(value = "家族导入上传")
        @RequestMapping(value = "/importFamilyExcel", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
        public JsonResult importFamilyExcel(@RequestPart("file") MultipartFile file, @RequestPart("familyId") String familyId) throws Exception {

        return  familyGenealogyService.importUser(file,Integer.parseInt(familyId));
    }

    @ApiOperation(value = "文章配图上传")
    @RequestMapping(value = "/uploadEssayImg", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public JsonResult uploadEssayImg(@RequestParam("files") MultipartFile[] files) throws IOException {
        JsonResult jsonResult = new JsonResult(200);
        String[] address = ImgUtil.insertImg(files, essayImgPath);
        address = ImageUtil.generateThumbnail2Directory(address);
        jsonResult.setData(address);
        return jsonResult;
    }

    @ApiOperation(value = "任务完成证明图")
    @RequestMapping(value = "/uploadTaskResultsImg", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public JsonResult uploadTaskResultsImg(@RequestParam("files") MultipartFile[] files) throws IOException {
        JsonResult jsonResult = new JsonResult(200);
        String[] address = ImgUtil.insertImg(files, taskResultsImgPath);
        address = ImageUtil.generateThumbnail2Directory(address);
        jsonResult.setData(address);
        return jsonResult;
    }


    @ApiOperation(value = "家族语音上传")
    @PutMapping("/upFamilyGenealogyPid")
    @RequestMapping(value = "/uploadAudio", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public String uploadAudio(@RequestParam("file") MultipartFile file) throws IOException {
        String address = ImgUtil.saveAudio(file, audioPath);
//        return "https://www.ohopu.com/api"+ address;
//        return "http://127.0.0.1:8089"+address;
        return "http://192.168.0.102:8089"+address;
    }

    private String generateUniqueFileName() {
        // 根据需要生成唯一的文件名，可以使用UUID或其他方式
        // 这里简单示范，使用当前时间戳
        return System.currentTimeMillis() + "";
    }

    private void saveFile(MultipartFile file, String filePath) throws IOException {
        // 确保目标目录存在
        File directory = new File(audioPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // 将文件保存到指定路径
        file.transferTo(new File(filePath));
    }
}
