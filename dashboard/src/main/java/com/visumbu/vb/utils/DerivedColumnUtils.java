/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.admin.service.GaService;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.model.DatasetColumns;
import static com.visumbu.vb.utils.ShuntingYard.postfix;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author deeta1
 */
public class DerivedColumnUtils {
        @Autowired
    private GaService gaService;
        @Autowired
    private UiDao uiDao;
    public Map<String, List<Map<String, Object>>> addDerivedColumnForGA(GetReportsResponse gaData, Integer dataSetId, Date startDate, Date endDate, String analyticsProfileId, String metricsList, String dimensions, String productSegments, String filter) {
        Map returnMap = new HashMap();
        HashMap<String, List<Map<String, Object>>> dataMap = (HashMap) gaService.getResponseAsMap(gaData);
        Iterator it = dataMap.entrySet().iterator();

        List column = new ArrayList<>();
        List columnData = new ArrayList<>();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getKey().equals("data")) {
                columnData.add(pair.getValue());
            } else {
                column.add(pair.getValue());
            }
        }
        List<ColumnDef> columnDefs = new ArrayList<>();
        List<Map<String, Object>> originalData = new ArrayList<>();
        System.out.println("column.size() ---> " + column.size());
//        if (column.size() > 0) {
//            columnDefs = (List<ColumnDef>) column.get(0);
//        }
        System.out.println("columnData.size() ---> " + columnData.size());

        if (columnData.size() > 0) {
            originalData = (List) columnData.get(0);
        }

        System.out.println("originalData ---> " + originalData);

        System.out.println("dataSetId ---> " + dataSetId);
        List<DatasetColumns> datasetColumnList = uiDao.getDatasetColumnsByDatasetId(dataSetId);
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
            Map<String, Object> dataPair = new HashMap();

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
                Date strtDate = null, lastDate = null;
                if (tokenArrayFunc[0].equalsIgnoreCase("yoy")) {
                    strtDate = new DateTime(startDate).minusYears(1).toDate();
                    lastDate = new DateTime(endDate).minusYears(1).toDate();
                    System.out.println("yoy ---> ");
                    System.out.println("startDate ----> " + strtDate + " endDate ----> " + lastDate);
                } else if (tokenArrayFunc[0].equalsIgnoreCase("mom")) {
                    strtDate = new DateTime(startDate).minusMonths(1).toDate();
                    lastDate = new DateTime(endDate).minusMonths(1).toDate();
                    System.out.println("mom ---> ");
                    System.out.println("startDate ----> " + strtDate + " endDate ----> " + lastDate);
                } else if (tokenArrayFunc[0].equalsIgnoreCase("wow")) {
                    strtDate = new DateTime(startDate).minusWeeks(1).toDate();
                    lastDate = new DateTime(endDate).minusWeeks(1).toDate();
                    System.out.println("wow ---> ");
                    System.out.println("startDate ----> " + strtDate + " endDate ----> " + lastDate);
                }
                GetReportsResponse gaDataOver = gaService.getGenericData(analyticsProfileId, strtDate, lastDate, null, null, metricsList, dimensions, productSegments, filter);
                HashMap<String, List<Map<String, Object>>> dataMapForOver = (HashMap) gaService.getResponseAsMap(gaDataOver);
                Iterator itYoy = dataMapForOver.entrySet().iterator();

                List columnOver = new ArrayList<>();
                List columnDataOver = new ArrayList<>();

                while (itYoy.hasNext()) {
                    Map.Entry pair = (Map.Entry) itYoy.next();
                    if (pair.getKey().equals("data")) {
                        columnDataOver.add(pair.getValue());
                    } else {
                        columnOver.add(pair.getValue());
                    }
                }
                List<ColumnDef> columnDefsOver = new ArrayList<>();
                List<Map<String, Object>> dataOver = new ArrayList<>();
                System.out.println("columnYoy.size() ---> " + columnOver.size());
                if (columnOver.size() > 0) {
                    columnDefsOver = (List<ColumnDef>) columnOver.get(0);
                }
                System.out.println("columnDataYoy.size() ---> " + columnDataOver.size());

                if (columnDataOver.size() > 0) {
                    dataOver = (List) columnDataOver.get(0);
                }
                Map<String, Object> dataPairYoy = new HashMap();
                if (columnDataOver.size() > 0) {
                    int list = 0;
                    System.out.println("dataOver.size() ---> " + dataOver.size());
                    System.out.println("originalData.size() ---> " + originalData.size());

                    if (dataOver.size() == originalData.size()) {
                        for (Iterator<Map<String, Object>> iterator = dataOver.iterator(); iterator.hasNext();) {
                            dataPairYoy = (Map<String, Object>) iterator.next();
                            Map<String, Object> dataPairMap = new HashMap<>();
                            dataPairMap = originalData.get(list);
                            System.out.println("yoy --> " + dataPairYoy.get("userType"));
                            System.out.println("current year --->" + dataPairMap.get("userType"));
                            String val = dataPairYoy.get(tokenArrayFunc[1]) + "";
                            System.out.println("valOver ---> " + val);
                            System.out.println("fieldName ---> " + fieldName);
                            dataPairMap.put(fieldName, val);
                            System.out.println("dataPairMap ---> " + dataPairMap);
                            System.out.println("originalData ---> " + originalData);
                            list++;
                        }
                    } else {
                        for (Iterator<Map<String, Object>> iterator = originalData.iterator(); iterator.hasNext();) {
                            dataPairYoy = (Map<String, Object>) iterator.next();
                            Map<String, Object> dataPairMap = new HashMap<>();
                            dataPairMap.put(fieldName, "");
                        }
                    }
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
                if (columnData.size() > 0) {
                    for (Iterator<Map<String, Object>> iterator = originalData.iterator(); iterator.hasNext();) {
                        dataPair = (Map<String, Object>) iterator.next();
                        String valueExpression = datasetColumn.getExpression();
                        System.out.println("dataPair -----> " + dataPair);
                        System.out.println("formula ----> " + valueExpression);
                        for (int j = 0; j < tokenArray.length; j++) {
                            System.out.println(tokenArray[j] + " value -----> " + dataPair.get(tokenArray[j]));
                            valueExpression = valueExpression.replaceAll(tokenArray[j], dataPair.get(tokenArray[j]) + "");
                            System.out.println("formulaaaaaaaaaa ----> " + valueExpression);
                        }
//                        InToPost theTrans = new InToPost(calculateFormula);
                        postFix = postfix(valueExpression);
//                        output = theTrans.doTrans();
                        System.out.println("final output ----> " + postFix);
                        ParsePost aParser = new ParsePost(postFix);
                        result = aParser.doParse();
                        System.out.println("result --> " + BigDecimal.valueOf(result).toPlainString());
                        resultStr = BigDecimal.valueOf(result).toPlainString();
                        dataPair.put(fieldName, resultStr);
                        System.out.println("field Name ---> " + fieldName);
                        System.out.println("resultstr ---> " + resultStr);
                    }
                    System.out.println("id ---> " + id);
                }
            }

        }
        System.out.println("originalData 2--> " + originalData);
        List<Map<String, Object>> data = new ArrayList<>(originalData);

        returnMap.put("columnDefs", columnDefs);
        returnMap.put("data", data);
        return returnMap;
    }
}
