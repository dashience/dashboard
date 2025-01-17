/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.visumbu.vb.bean.ColumnDef;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsDataSet {

    public static List<Map<String, String>> XlsDataSet(String filename, String sheetName) throws FileNotFoundException, IOException {
        Map returnMap = new HashMap();

        try {
            //FileOutputStream fos = new FileOutputStream(outputFile);

            // Get the workbook object for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filename));
            // Get first sheet from the workbook
            HSSFSheet sheet = workbook.getSheet(sheetName);
            Cell cell;
            Row row;
            // Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            List<ColumnDef> columnDefs = new ArrayList<>();
            List<Map<String, String>> data = new ArrayList<>();
            int rowCount = 0;
            List<String> header = new ArrayList<>();
            for (Iterator<Row> iterator = rowIterator; iterator.hasNext();) {
                row = iterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellCount = 0;
                for (Iterator<Cell> iterator1 = cellIterator; iterator1.hasNext();) {
                    Cell currentCell = iterator1.next();
                    if (rowCount == 0) {
                        ColumnDef columnDef = new ColumnDef(currentCell.toString(), "string", currentCell.toString());
                        columnDefs.add(columnDef);
                        header.add(currentCell.toString());
                    } else {
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put(header.get(cellCount), currentCell.toString());
                        data.add(dataMap);
                    }
                    cellCount++;
                }
                rowCount++;
            }
            returnMap.put("columnDefs", columnDefs);
            returnMap.put("data", data);
            // System.out.println(returnMap);
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Map<Integer, String> getSheetListXls(String filename) {
        Map<Integer, String> returnMap = new HashMap<>();
        System.out.println("XLS filename " + filename);
        try {
            HSSFWorkbook workbook;
            workbook = new HSSFWorkbook(new FileInputStream(filename));

            for (int i = workbook.getNumberOfSheets() - 1; i >= 0; i--) {
                HSSFSheet tmpSheet = workbook.getSheetAt(i);
                returnMap.put(i, tmpSheet.getSheetName());
            }
        } catch (IOException ex) {
            Logger.getLogger(XlsDataSet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    public Map<Integer, String> getSheetListXlsx(String filename) {
        Map<Integer, String> returnMap = new HashMap<>();
        System.out.println("XLS filename " + filename);
        try {
            XSSFWorkbook workbook;
            workbook = new XSSFWorkbook(new FileInputStream(filename));

            for (int i = workbook.getNumberOfSheets() - 1; i >= 0; i--) {
                XSSFSheet tmpSheet = workbook.getSheetAt(i);
                returnMap.put(i, tmpSheet.getSheetName());
            }
        } catch (IOException ex) {
            Logger.getLogger(XlsDataSet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnMap;
    }

    public static List<Map<String, String>> XlsDataSet(String filename, Integer sheetNo) throws FileNotFoundException, IOException {
        Map returnMap = new HashMap();

        try {
            //FileOutputStream fos = new FileOutputStream(outputFile);

            // Get the workbook object for XLS file
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filename));
            // Get first sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(sheetNo);
            Cell cell;
            Row row;
            // Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            List<ColumnDef> columnDefs = new ArrayList<>();
            List<Map<String, String>> data = new ArrayList<>();
            int rowCount = 0;
            List<String> header = new ArrayList<>();
            for (Iterator<Row> iterator = rowIterator; iterator.hasNext();) {
                row = iterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellCount = 0;
                Map<String, String> dataMap = new HashMap<>();
                for (Iterator<Cell> iterator1 = cellIterator; iterator1.hasNext();) {
                    Cell currentCell = iterator1.next();
                    if (rowCount == 0) {
                        ColumnDef columnDef = new ColumnDef(currentCell.toString(), "string", currentCell.toString());
                        columnDefs.add(columnDef);
                        header.add(currentCell.toString());
                    } else {
                        dataMap.put(header.get(cellCount), currentCell.toString());
                    }
                    cellCount++;
                }
                if (rowCount != 0) {
                    data.add(dataMap);
                }
                rowCount++;
            }
            returnMap.put("columnDefs", columnDefs);
            returnMap.put("data", data);
            System.out.println(returnMap);
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Map<String, String>> XlsxDataSet(String filename, String sheetName) throws FileNotFoundException, IOException {
        Map returnMap = new HashMap();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filename));
            // Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheet(sheetName);
            System.out.println("Sheetname ---> " + sheetName);
            Cell cell;
            Row row;
            // Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            List<ColumnDef> columnDefs = new ArrayList<>();
            List<Map<String, String>> data = new ArrayList<>();
            int rowCount = 0;
            List<String> header = new ArrayList<>();
            for (Iterator<Row> iterator = rowIterator; iterator.hasNext();) {
                row = iterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellCount = 0;
                Map<String, String> dataMap = new HashMap<>();
                for (Iterator<Cell> iterator1 = cellIterator; iterator1.hasNext();) {
                    Cell currentCell = iterator1.next();
                    if (rowCount == 0) {
                        ColumnDef columnDef = new ColumnDef(currentCell.toString(), "string", currentCell.toString());
                        columnDefs.add(columnDef);
                        header.add(currentCell.toString());
                    } else {
                        dataMap.put(header.get(cellCount), currentCell.toString());
                    }
                    cellCount++;
                }
                if (rowCount != 0) {
                    data.add(dataMap);
                }
                rowCount++;
            }
            returnMap.put("columnDefs", columnDefs);
            returnMap.put("data", data);
            System.out.println(returnMap);
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map XlsxDataSet(String filename, Integer sheetNo) throws FileNotFoundException, IOException {
        Map returnMap = new HashMap();

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filename));
            // Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(sheetNo);
            Cell cell;
            Row row;
            // Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            List<ColumnDef> columnDefs = new ArrayList<>();
            List<Map<String, String>> data = new ArrayList<>();
            int rowCount = 0;
            List<String> header = new ArrayList<>();
            for (Iterator<Row> iterator = rowIterator; iterator.hasNext();) {
                row = iterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                int cellCount = 0;
                for (Iterator<Cell> iterator1 = cellIterator; iterator1.hasNext();) {
                    Cell currentCell = iterator1.next();
                    if (rowCount == 0) {
                        ColumnDef columnDef = new ColumnDef(currentCell.toString(), "string", currentCell.toString());
                        columnDefs.add(columnDef);
                        header.add(currentCell.toString());
                    } else {
                        Map<String, String> dataMap = new HashMap<>();
                        dataMap.put(header.get(cellCount), currentCell.toString());
                        data.add(dataMap);
                    }
                    cellCount++;
                }
                rowCount++;
            }
            returnMap.put("columnDefs", columnDefs);
            returnMap.put("data", data);
            System.out.println(returnMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnMap;
    }

    public static void main(String[] argv) {
        try {
            XlsDataSet("/tmp/test.xls", 0);
        } catch (IOException ex) {
            Logger.getLogger(XlsDataSet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
