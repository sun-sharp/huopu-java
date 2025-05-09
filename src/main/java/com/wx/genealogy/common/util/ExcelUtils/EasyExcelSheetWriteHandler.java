package com.wx.genealogy.common.util.ExcelUtils;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @description:
 * @author: ljl
 * @date: 2024-08-20 19:25
 **/

public class EasyExcelSheetWriteHandler implements SheetWriteHandler {

    private String title;

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    public EasyExcelSheetWriteHandler() {
        super();
    }

    public EasyExcelSheetWriteHandler(String title) {
        this.title = title;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = workbook.getSheetAt(0);
        System.out.println(title);
        Row row1 = sheet.createRow(0);
        row1.setHeight((short) 800);
        Cell cell = row1.createCell(0);
        //设置标题
        cell.setCellValue(title);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) 400);
        font.setFontName("宋体");
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, 3));
    }
}
