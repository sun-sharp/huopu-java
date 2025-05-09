package com.wx.genealogy.common.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.wx.genealogy.common.util.ExcelUtils.EasyExcelCellWriteHandler;
import com.wx.genealogy.common.util.ExcelUtils.EasyExcelSheetWriteHandler;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author: leo
 * @Description: Excel文件导出工具类
 * @Date: 2024-07-09
 */
public class ExcelUtil {

    private static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "must-revalidate");
            response.setDateHeader("Expires", 0L);
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "utf-8") + ".xlsx");
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @SneakyThrows
    public static <T> void export(HttpServletResponse response, String fileName, String sheetName, List<T> dataList, Class<T> clazz) throws IOException {
        setResponseHeader(response,fileName);
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        EasyExcel.write(response.getOutputStream(), clazz)
                .registerWriteHandler(horizontalCellStyleStrategy)
                .sheet(sheetName).doWrite(dataList);
    }

    @SneakyThrows
    public static <T> void export(HttpServletResponse response, String fileName, String sheetName, List<T> dataList, Class<T> clazz, EasyExcelSheetWriteHandler writeHandler, EasyExcelCellWriteHandler easyExcelTitleHandler) throws IOException {
        setResponseHeader(response,fileName);
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        EasyExcel.write(response.getOutputStream(), clazz)
//                .registerWriteHandler(horizontalCellStyleStrategy)
                .registerWriteHandler(writeHandler)
                .registerWriteHandler(easyExcelTitleHandler)
                .sheet(sheetName).doWrite(dataList);
    }

}
