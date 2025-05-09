package com.wx.genealogy.common.util.ExcelUtils;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * @description:
 * @author: ljl
 * @date: 2024-08-20 19:49
 **/

public class EasyExcelCellWriteHandler implements CellWriteHandler {
    /**
     * 错误信息处理时正则表达式的格式
     */
    private final String EXCEL_ERROR_REG = "^(.*)(\\(错误:)(.*)(\\))$";
    /**
     * 操作列
     */
    private final List<Integer> columnIndex;

    private JSONObject headTitle;

    PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}");

    public EasyExcelCellWriteHandler(List<Integer> columnIndex, Short colorIndex, HashMap<Integer, String> annotationsMap, HashMap<Integer, String[]> dropDownMap, String time, String month, String year, JSONObject sheetTitle) {
        this.columnIndex = columnIndex;
        this.headTitle = headTitle;
    }

    public EasyExcelCellWriteHandler(List<Integer> columnIndex, JSONObject headTitle) {
        this.columnIndex = columnIndex;
        this.headTitle = headTitle;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
        // 动态设置表头字段
        if (!ObjectUtils.isEmpty(head)) {
            List<String> headNameList = head.getHeadNameList();
            if (CollectionUtils.isNotEmpty(headNameList)) {

                ArrayList<Properties> propertiesList = new ArrayList<>();

                for (String key : headTitle.keySet()) {
                    Properties properties = new Properties();
                    properties.setProperty(key, headTitle.getString(key));
                    propertiesList.add(properties);
                }
                for (int i = 0; i < headNameList.size(); i++) {
                    for (Properties properties : propertiesList) {
                        //表头中如果有${}设置的单元格，则可以自定义赋值。 根据构造方法传入的jsonObject对象
                        headNameList.set(i, placeholderHelper.replacePlaceholders(headNameList.get(i), properties));
                    }
                }
            }
        }
    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
//        System.out.println("第 " + cell.getRowIndex() + "行；" + "第 " + cell.getColumnIndex() + "列");
//        if (!isHead && cell.getStringCellValue().contains("个")) {
        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        Font cellFont = workbook.createFont();
//        cellFont.setBold(true);
        cellStyle.setFont(cellFont);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        if (!isHead && cell.getColumnIndex() == 3) {
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
        } else {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
        }
        cell.setCellStyle(cellStyle);
    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }
}
