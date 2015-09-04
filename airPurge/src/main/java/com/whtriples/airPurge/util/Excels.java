package com.whtriples.airPurge.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class Excels {

    public static byte[] exportExcel(String title, Map<String, String> fieldMap,
                                     Collection<?> data, String pattem) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Workbook wb = new HSSFWorkbook();
        try {

            //设置日期格式
            CellStyle dateCellStyle = null;
            if (StringUtils.isNotEmpty(pattem)) {
                CreationHelper createHelper = wb.getCreationHelper();
                dateCellStyle = wb.createCellStyle();
                dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(pattem));
            }

            //设置文档头部信息
            Sheet sheet = wb.createSheet(title);
            Row row = sheet.createRow(0);
            int cellIndex = 0;
            for (String headName : fieldMap.values()) {
                row.createCell(cellIndex++).setCellValue(headName);
            }

            //设置文档内容
            int rowIndex = 1;
            for (Object obj : data) {
                cellIndex = 0;
                row = sheet.createRow(rowIndex++);
                for (String fieldName : fieldMap.keySet()) {
                    Cell cell = row.createCell(cellIndex++);
                    Object fieldValue = Reflections.getFieldValue(obj, fieldName);
                    if (fieldValue instanceof Date) {
                        if (dateCellStyle != null) {
                            cell.setCellStyle(dateCellStyle);
                        }
                        cell.setCellValue((Date) fieldValue);
                    } else {
                        if (fieldValue != null) {
                            cell.setCellValue(fieldValue.toString());
                        }
                    }
                }
            }
            wb.write(baos);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }finally{
        	try {
				wb.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return baos.toByteArray();
    }

}
