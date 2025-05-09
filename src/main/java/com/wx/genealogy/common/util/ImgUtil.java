package com.wx.genealogy.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class ImgUtil {
    public static final String IMAGE_PATH = new File("").getAbsolutePath();

    public static String[] insertImg(MultipartFile[] files, String path) throws IOException {
        String[] imgAddress = new String[files.length];

        String newNath = IMAGE_PATH + path;
        File TempFile = new File(newNath);
        if (TempFile.exists()) {
            if (TempFile.isDirectory()) {
                System.out.println("该文件夹存在。");
            } else {
                System.out.println("同名的文件存在，不能创建文件夹。");
            }
        } else {
            System.out.println("文件夹不存在，创建该文件夹。");
            TempFile.mkdir();
            String args = path;
            String strArray[] = args.split("/");
            int l = strArray.length;
            //一级目录a,二级目录b,三级目录c
            String[] directories = new String[l];
            for (int i = 0; i < strArray.length; i++) {
                directories[i] = strArray[i];
            }
            createFileWithMultilevelDirectory(directories, IMAGE_PATH);
        }
        for (int i = 0; i < files.length; i++) {
            InputStream is = files[i].getInputStream();

            // 获取文件后缀名
            String filename = files[i].getOriginalFilename();
            //文件前缀名
            String fileNameNow = filename.substring(0, filename.lastIndexOf("."));
            //文件后缀名
            String filename_extension = filename.substring(filename
                    .lastIndexOf(".") + 1);

            //时间戳做新的文件名，避免中文乱码-重新生成filename
            @SuppressWarnings("AlibabaAvoidNewDateGetTime") long filename1 = System.currentTimeMillis();
            String newfilename = Long.toString(filename1) + "." + filename_extension;

            //文件地址
            String address = newNath + newfilename;
            //上传到本地磁盘/服务器
            try {
                OutputStream os = new FileOutputStream(new File(newNath, newfilename));
                int len = 0;
                byte[] buffer = new byte[2048];

                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.close();
                os.flush();
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgAddress[i] = path + newfilename;
        }
        return imgAddress;
    }

    public static String saveAudio(MultipartFile file, String path) throws IOException {


        String newNath = IMAGE_PATH + path;
        File TempFile = new File(newNath);
        if (TempFile.exists()) {
            if (TempFile.isDirectory()) {
                System.out.println("该文件夹存在。");
            } else {
                System.out.println("同名的文件存在，不能创建文件夹。");
            }
        } else {
            System.out.println("文件夹不存在，创建该文件夹。");
            TempFile.mkdir();
            String args = path;
            String strArray[] = args.split("/");
            int l = strArray.length;
            //一级目录a,二级目录b,三级目录c
            String[] directories = new String[l];
            for (int i = 0; i < strArray.length; i++) {
                directories[i] = strArray[i];
            }
            createFileWithMultilevelDirectory(directories, IMAGE_PATH);
        }

            InputStream is = file.getInputStream();

            // 获取文件后缀名
            String filename = file.getOriginalFilename();
            //文件前缀名
            String fileNameNow = filename.substring(0, filename.lastIndexOf("."));
            //文件后缀名
            String filename_extension = filename.substring(filename
                    .lastIndexOf(".") + 1);

            //时间戳做新的文件名，避免中文乱码-重新生成filename
            @SuppressWarnings("AlibabaAvoidNewDateGetTime") long filename1 = System.currentTimeMillis();
            String newfilename = Long.toString(filename1) + "." + filename_extension;

            //文件地址
            String address = newNath + newfilename;
            //上传到本地磁盘/服务器
            try {
                OutputStream os = new FileOutputStream(new File(newNath, newfilename));
                int len = 0;
                byte[] buffer = new byte[2048];

                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.close();
                os.flush();
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String imgAddress = path + newfilename;





        return imgAddress;
    }

    //创建多级目录
    public static void createMultilevelDirectory(String[] directories, String rootPath) {
        if (directories.length == 0) {
        }
        File root = new File(rootPath);
        for (int i = 0; i < directories.length; i++) {
            File directory = new File(root, directories[i]);
            directory.mkdir();
            root = directory;
        }
    }

    public static void createFileWithMultilevelDirectory(String[] directories, String rootName) throws IOException {
        //调用上面的创建多级目录的方法
        createMultilevelDirectory(directories, rootName);
    }
}
