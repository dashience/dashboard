/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.model.WidgetColumn;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author arul
 */
public class DataExporter {

    public void exportToXls(List<WidgetColumn> columnDef, Integer maxRecord, List<Map<String, Object>> data, OutputStream out) {
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Employee Data");

        //This data needs to be written (Object[])
        int rownum = 0;
        int cellnum = 0;
        Row rowh = sheet.createRow(rownum++);
        for (Iterator<WidgetColumn> iterator = columnDef.iterator(); iterator.hasNext();) {
            WidgetColumn columnDefObj = iterator.next();
            String fieldName = columnDefObj.getDisplayName();
            Cell cell = rowh.createCell(cellnum++);
            cell.setCellValue(fieldName);
        }

        if (data != null) {
            for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                int count = rownum++;
                if (maxRecord != null) {
                    if (maxRecord >= count) {
                        Row row = sheet.createRow(count);
                        cellnum = 0;
                        for (Iterator<WidgetColumn> iterator1 = columnDef.iterator(); iterator1.hasNext();) {
                            WidgetColumn columnDefObj = iterator1.next();
                            String fieldName = columnDefObj.getFieldName();
                            Cell cell = row.createCell(cellnum++);
                            cell.setCellValue(dataMap.get(fieldName) + "");
                        }
                    }
                } else {
                    Row row = sheet.createRow(count);
                    cellnum = 0;
                    for (Iterator<WidgetColumn> iterator1 = columnDef.iterator(); iterator1.hasNext();) {
                        WidgetColumn columnDefObj = iterator1.next();
                        String fieldName = columnDefObj.getFieldName();
                        Cell cell = row.createCell(cellnum++);
                        cell.setCellValue(dataMap.get(fieldName) + "");
                    }
                }
            }
        }

        try {
            //Write the workbook in file system
            String fileName = "/tmp/" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + ".xlsx";
            // OutputStream out = new FileOutputStream(new File(fileName));
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
