/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.model.DatasetColumns;
import static com.visumbu.vb.utils.ShuntingYard.postfix;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.annotations.ColumnDefault;

public class CsvDataSet {

    public static List<Map<String,String>> CsvDataSet(String filename) throws FileNotFoundException, IOException {

        //Create the CSVFormat object
        CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        //initialize the CSVParser object
        CSVParser parser = new CSVParser(new FileReader(filename), format);
        Map<String, Integer> headerMap = parser.getHeaderMap();
        List<ColumnDef> columnDefs = new ArrayList<>();
        for (Map.Entry<String, Integer> entrySet : headerMap.entrySet()) {
            String key = entrySet.getKey();
            Integer value = entrySet.getValue();
            ColumnDef columnDef = new ColumnDef(key, "string", key);
            columnDefs.add(columnDef);

        }
        Map returnMap = new HashMap();
        returnMap.put("columnDefs", columnDefs);
        List<Map<String, String>> data = new ArrayList<>();
        System.out.println(headerMap);
        for (CSVRecord record : parser) {
            Map<String, String> dataMap = new HashMap<>();
            for (Map.Entry<String, Integer> entrySet : headerMap.entrySet()) {
                String key = entrySet.getKey();
                dataMap.put(key, record.get(key));
            }
            data.add(dataMap);
        }
        
        returnMap.put("data", data);
        //close the parser
        parser.close();
        return data;
    }
    
    public Map<String, List<Map<String, String>>> addDerivedColumnForCsv(List<Map<String, String>> data, List<DatasetColumns> datasetColumnList) {
        List<Map<String, String>> originalData = new ArrayList<>(data);
        List<ColumnDef> columnDefs = new ArrayList<>();

        Map returnMap = new HashMap<>();

        System.out.println("originalData ---> " + originalData);
        String fieldName;
        String displayName;
        String fieldType;
        String expression;
        String displayFormat;
        String formula;
        String status;
        String functionName;
        Integer id;

        System.out.println("datasetColumnList --> " + datasetColumnList);
        for (Iterator<DatasetColumns> datasetColumns = datasetColumnList.iterator(); datasetColumns.hasNext();) {
            DatasetColumns datasetColumn = datasetColumns.next();
            System.out.println("datasetcolumn.getExpression() ----> " + datasetColumn.getExpression());
            id = datasetColumn.getId();
            fieldName = datasetColumn.getFieldName();
            displayName = datasetColumn.getDisplayName();
            fieldType = datasetColumn.getFieldType();
            displayFormat = datasetColumn.getDisplayFormat();
            System.out.println("displayFormat ---> " + displayFormat);
            formula = datasetColumn.getExpression();
            status = datasetColumn.getStatus();
            functionName = datasetColumn.getFunctionName();
            Map<String, String> dataPair = new HashMap();

            columnDefs.add(new ColumnDef(id, fieldName, fieldType, displayName, null, displayFormat, status, formula, functionName));
            if (functionName != null && !functionName.trim().isEmpty()) {
                String funcName = functionName;

                funcName = funcName.replaceAll("\\s+", "");
                System.out.println("funcName ---> " + funcName);
                StringTokenizer tokenizer = new StringTokenizer(funcName, "()");
                String[] tokenArrayFunc = new String[tokenizer.countTokens()];
                int i = -1;
                while (tokenizer.hasMoreTokens()) {
                    String token = (String) tokenizer.nextToken().trim();
                    if (!token.equalsIgnoreCase("\\s+")) {
                        System.out.println("token name 1---> " + token);
                        tokenArrayFunc[++i] = token;
                    }
                }
//                Date strtDate = null, lastDate = null;
//                if (tokenArrayFunc[0].equalsIgnoreCase("yoy")) {
//                    strtDate = new DateTime(startDate).minusYears(1).toDate();
//                    lastDate = new DateTime(endDate).minusYears(1).toDate();
//                    System.out.println("yoy ---> ");
//                    System.out.println("startDate ----> " + strtDate + " endDate ----> " + lastDate);
//                } else if (tokenArrayFunc[0].equalsIgnoreCase("mom")) {
//                    strtDate = new DateTime(startDate).minusMonths(1).toDate();
//                    lastDate = new DateTime(endDate).minusMonths(1).toDate();
//                    System.out.println("mom ---> ");
//                    System.out.println("startDate ----> " + strtDate + " endDate ----> " + lastDate);
//                } else if (tokenArrayFunc[0].equalsIgnoreCase("wow")) {
//                    strtDate = new DateTime(startDate).minusWeeks(1).toDate();
//                    lastDate = new DateTime(endDate).minusWeeks(1).toDate();
//                    System.out.println("wow ---> ");
//                    System.out.println("startDate ----> " + strtDate + " endDate ----> " + lastDate);
//                }

                Map<String, String> dataPairYoy = new HashMap();

                for (Iterator<Map<String, String>> iterator = originalData.iterator(); iterator.hasNext();) {
                    dataPairYoy = (Map<String, String>) iterator.next();
                    Map<String, Object> dataPairMap = new HashMap<>();
                    dataPairYoy.put(fieldName, dataPairYoy.get(tokenArrayFunc[1]) + "");
                }

            } else if (formula != null && !formula.trim().isEmpty()) {
                expression = formula;
                expression = expression.replaceAll("\\s+", "");
                StringTokenizer tokenizer = new StringTokenizer(expression, "([+*/-()1234567890])");
                String[] tokenArray = new String[tokenizer.countTokens()];
                int i = -1;
                while (tokenizer.hasMoreTokens()) {
                    String token = (String) tokenizer.nextToken().trim();
                    if (!token.equalsIgnoreCase("\\s+")) {
                        System.out.println("token name 1---> " + token);
                        tokenArray[++i] = token;
                    }
                }

                String postFix;

                double result = 0;
                String resultStr = null;
                System.out.println("originaldata ---> " + originalData);
                if (data.size() > 0) {
                    for (Iterator<Map<String, String>> iterator = originalData.iterator(); iterator.hasNext();) {
                        dataPair = (Map<String, String>) iterator.next();
                        int flag = 0;
                        String valueExpression = datasetColumn.getExpression();
                        String operatorExpression = datasetColumn.getExpression();
                        System.out.println("dataPair -----> " + dataPair);
                        System.out.println("formula ----> " + valueExpression);
                        for (int j = 0; j < tokenArray.length; j++) {
                            System.out.println(tokenArray[j] + " value -----> " + dataPair.get(tokenArray[j]));
                            try {
                                double value = Double.parseDouble(dataPair.get(tokenArray[j]));
                                valueExpression = valueExpression.replaceAll(tokenArray[j], value + "");
                                System.out.println("formulaaaaaaaaaa ----> " + valueExpression);
                            } catch (NumberFormatException e) {
                                valueExpression = valueExpression.replaceAll(tokenArray[j], dataPair.get(tokenArray[j]) + "");
                                operatorExpression = operatorExpression.replaceAll(tokenArray[j], "");
                                System.out.println("formulaaaaaaaaaa 1----> " + valueExpression);
                                System.out.println("operator ----> " + operatorExpression);
                                System.out.println(e);
                                flag++;
                            }
                        }
                        if (flag == 0) {
//                        InToPost theTrans = new InToPost(calculateFormula);
                            postFix = postfix(valueExpression);
//                        output = theTrans.doTrans();
                            System.out.println("final output ----> " + postFix);
                            ParsePost aParser = new ParsePost(postFix);
                            result = aParser.doParse();
                            System.out.println("result --> " + BigDecimal.valueOf(result).toPlainString());
                            resultStr = BigDecimal.valueOf(result).toPlainString();
                            dataPair.put(fieldName, resultStr);
                        } else {
                            if (operatorExpression.contains("-")) {
                                dataPair.put(fieldName, "");
                            } else if (operatorExpression.contains("*")) {
                                dataPair.put(fieldName, "");
                            } else if (operatorExpression.contains("/")) {
                                dataPair.put(fieldName, "");
                            } else if (operatorExpression.contains("(")) {
                                dataPair.put(fieldName, "");
                            } else if (operatorExpression.contains(")")) {
                                dataPair.put(fieldName, "");
                            } else {
                                valueExpression = valueExpression.replaceAll("\\+", "");
                                dataPair.put(fieldName, valueExpression);
                            }
                        }
                        System.out.println("field Name ---> " + fieldName);
                        System.out.println("resultstr ---> " + resultStr);
                    }
                    System.out.println("id ---> " + id);
                }
            }

        }
        System.out.println("originalData 2--> " + originalData);
        List<Map<String, String>> fbData = new ArrayList<>(originalData);
        returnMap.put("columnDefs", columnDefs);
        returnMap.put("data", fbData);
        return returnMap;
    }

    public static void main(String[] argv) {
        try {
            System.out.println(CsvDataSet("/tmp/employees.csv"));
        } catch (IOException ex) {
            Logger.getLogger(CsvDataSet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
