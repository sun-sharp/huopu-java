package com.wx.genealogy.common.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements Converter<LocalDate> {
    @Override
    public Class supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if(cellData.getType().equals(CellDataTypeEnum.NUMBER)){
            LocalDate localDate = LocalDate.of(1900, 1, 1);
            //execl有些奇怪的bug, 导致日期数差2
            localDate = localDate.plusDays(cellData.getNumberValue().longValue()-2);
            return localDate;
        }else if(cellData.getType().equals(CellDataTypeEnum.STRING)) {
            return LocalDate.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        }else{
            return null;
        }
    }

    @Override
    public CellData convertToExcelData(LocalDate value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new CellData<>(value.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
    }
}