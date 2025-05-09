package com.wx.genealogy.common.util;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片压缩工具类
 *
 * @author lnj
 * createTime 2018-10-19 15:31
 **/
public class ImageUtil {

    // 图片默认缩放比率
    private static final double DEFAULT_SCALE = 0.6d;

    // 图片默认大小 1M
    private static final long DEFAULT_SIZE = 1024000;

    // 缩略图后缀
    private static final String SUFFIX = "-thumbnail";

    public static final String IMAGE_PATH = new File("").getAbsolutePath();


    /**
     * 生成缩略图到指定的目录
     *
     * @param files 要生成缩略图的文件列表
     * @throws IOException
     */
    public static String[] generateThumbnail2Directory(String[] files) throws IOException {
        return generateThumbnail2Directory(DEFAULT_SCALE, files);
    }

    /**
     * 生成缩略图到指定的目录
     *
     * @param scale    图片缩放率
     * @param files    要生成缩略图的文件列表
     * @throws IOException
     */
    public static String[] generateThumbnail2Directory(double scale, String[] files) throws IOException {
        for (int i = 0; i < files.length; i++) {
            File originFile = new File(IMAGE_PATH+files[i]);
            BufferedImage read = ImageIO.read(originFile);
            long fileSize = originFile.length();
            if(fileSize > DEFAULT_SIZE) {//图片大于1M时 缩放 0.6
                String newName = files[i].replace(".", "-thumbnail.");
                File file = new File(IMAGE_PATH + newName);
                file.createNewFile();
                Thumbnails.of(read)
                        // 图片缩放率，不能和size()一起使用
                        .scale(scale)
                        // 缩略图保存目录,该目录需存在，否则报错
                        .toFile(new File(IMAGE_PATH + newName));
                files[i] = newName;
            }
        }
        return files;
    }

    /**
     * 文件追加后缀
     *
     * @param fileName 原文件名
     * @param suffix   文件后缀
     * @return
     */
    public static String appendSuffix(String fileName, String suffix) {
        String newFileName = "";

        int indexOfDot = fileName.lastIndexOf('.');

        if (indexOfDot != -1) {
            newFileName = fileName.substring(0, indexOfDot);
            newFileName += suffix;
            newFileName += fileName.substring(indexOfDot);
        } else {
            newFileName = fileName + suffix;
        }

        return newFileName;
    }

    public static String getFileExtention(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension;
    }
}
