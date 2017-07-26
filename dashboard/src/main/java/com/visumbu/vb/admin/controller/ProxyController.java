/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.AdwordsService;
import com.visumbu.vb.admin.service.BingService;
import com.visumbu.vb.admin.service.DealerService;
import com.visumbu.vb.admin.service.FacebookService;
import com.visumbu.vb.admin.service.GaService;
import com.visumbu.vb.admin.service.LinkedinService;
import com.visumbu.vb.admin.service.ReportService;
import com.visumbu.vb.admin.service.SettingsService;
import com.visumbu.vb.admin.service.TwitterService;
import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.bean.DateRange;
import com.visumbu.vb.bean.Range;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.AdwordsCriteria;
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.DataSetColumns;
import com.visumbu.vb.model.DefaultFieldProperties;
import com.visumbu.vb.model.JoinDataSet;
import com.visumbu.vb.model.JoinDataSetCondition;
import com.visumbu.vb.model.Property;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.Settings;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import com.visumbu.vb.utils.CsvDataSet;
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.PropertyReader;
import com.visumbu.vb.utils.Rest;
import com.visumbu.vb.utils.SettingsProperty;
import com.visumbu.vb.utils.ShuntingYard;
import com.visumbu.vb.utils.XlsDataSet;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import test.CustomReportDesigner;
import test.DateRangeFactory;

/**
 *
 * @author jp
 */
@Controller
@RequestMapping("proxy")
public class ProxyController {

    @Autowired
    private UiService uiService;
    @Autowired
    private UserService userService;

    @Autowired
    private DealerService dealerService;

    @Autowired
    private FacebookService facebookService;

    @Autowired
    private AdwordsService adwordsService;

    @Autowired
    private GaService gaService;

    @Autowired
    private BingService bingService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private LinkedinService linkedinService;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private TwitterService twitterService;

    PropertyReader propReader = new PropertyReader();

    private final String urlDownload = "url.download";

    final static Logger log = Logger.getLogger(ProxyController.class);

    @RequestMapping(value = "getData", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getGenericData(HttpServletRequest request, HttpServletResponse response) {

        Map returnMap = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        String joinDataSetIdStr = request.getParameter("joinDataSetId");
        String dataSourceId = request.getParameter("dataSourceId");
        String dataSourceType = request.getParameter("dataSourceType");
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String[]> entrySet : parameterMap.entrySet()) {
            String key = entrySet.getKey();
            String[] value = entrySet.getValue();
            if (value != null) {
                valueMap.put(key, Arrays.asList(value));
            }
        }
        String fieldsOnly = request.getParameter("fieldsOnly");

        String dataSetId = request.getParameter("dataSetId");
        String widgetIdStr = request.getParameter("widgetId");
        String userIdStr = request.getParameter("userId");

        Integer dataSetIdInt = null;
        Integer widgetIdInt = null;
        Integer userIdInt = null;
        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined") && !widgetIdStr.equalsIgnoreCase("null")) {
            widgetIdInt = Integer.parseInt(widgetIdStr);
        }
        if (userIdStr != null) {
            try {
                userIdInt = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {

            }
        }
        if (dataSetId != null) {
            try {
                dataSetIdInt = Integer.parseInt(dataSetId);
//                DataSet dataSet = uiService.getDataSetById(dataSetIdInt);
//                if (dataSet.getJoinDataSetId() != null) {
//                    joinDataSetIdStr = dataSet.getJoinDataSetId().getId() + "";
//                }
            } catch (NumberFormatException e) {

            }
        }
//
//        if (joinDataSetIdStr != null && !joinDataSetIdStr.isEmpty() && !joinDataSetIdStr.equalsIgnoreCase("null") && (dataSourceType == null || dataSourceType.isEmpty() || dataSourceType.equalsIgnoreCase("null"))) {
//            try {
//                Integer joinDataSetIdInt = Integer.parseInt(joinDataSetIdStr);
//                returnMap = getJoinData(valueMap, request, response, joinDataSetIdInt);
//            } catch (NumberFormatException e) {
//
//            }
//        } else {
        returnMap = getData(valueMap, request, response);
//        }
        returnMap.put("columnDefs", getColumnDefObject((List<Map<String, Object>>) returnMap.get("data")));

        updateDataSetColumnId((List) returnMap.get("columnDefs"), userIdInt, dataSetIdInt, widgetIdInt);

        List<Map<String, Object>> data = (List<Map<String, Object>>) returnMap.get("data");

        List<DataSetColumns> dataSetColumnList = null;
        if (widgetIdInt == null) {
            dataSetColumnList = uiService.getDataSetColumnsByDataSetId(dataSetIdInt, userIdInt);
        } else {
            dataSetColumnList = uiService.getDataSetColumns(dataSetIdInt, widgetIdInt); // DataSetColumnsByDataSetId(dataSetIdInt, userIdInt);
        }
        if (dataSetColumnList.size() > 0) {
            List<Map<String, Object>> dataWithDerivedFunctions = addDerivedColumnsFunction(dataSetColumnList, data, valueMap, request, response);
            List<Map<String, Object>> dataWithDerivedColumns = addDerivedColumnsExpr(dataSetColumnList, dataWithDerivedFunctions);
            System.out.println(dataSetColumnList);
            System.out.println("DATA INSIDE DERIVED COLUMN");
            System.out.println(dataWithDerivedColumns);
            returnMap.put("data", dataWithDerivedColumns);
        }
        returnMap.put("columnDefs", getColumnDefObject((List<Map<String, Object>>) returnMap.get("data")));
        System.out.println("returnMap 1234 ----> " + returnMap);
        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined") && !widgetIdStr.equalsIgnoreCase("null")) {
            String queryFilter = null;
            widgetIdInt = Integer.parseInt(widgetIdStr);
            TabWidget tabWidget = uiService.getWidgetByIdAndDataSetId(widgetIdInt, dataSetIdInt);
            if (tabWidget != null) {
                queryFilter = tabWidget.getQueryFilter();
            }
            List<Map<String, Object>> originalData = (List<Map<String, Object>>) returnMap.get("data");
            List<Map<String, Object>> returnDataMap = ShuntingYard.applyExpression(originalData, queryFilter);
            returnMap.put("data", returnDataMap);
        }
        Map dataMap = new HashMap<>();
        dataMap.put("columnDefs", returnMap.get("columnDefs"));
//        if (dataSetIdInt != null) {
//            dataMap.put("columnDefs", updateDataSetColumnId((List) returnMap.get("columnDefs"), userIdInt, dataSetIdInt, widgetIdInt));
//        }
        if (fieldsOnly != null) {
            // return dataMap;
        }
        dataMap.put("data", returnMap.get("data"));
        // dataMap.put("columnDefs", getColumnDefObject((List<Map<String, Object>>) dataMap.get("data")));
        return dataMap;
    }

    private List<ColumnDef> updateDataSetColumnId(List<ColumnDef> columnDefObject, Integer userId, Integer dataSetId, Integer widgetId) {
        List<ColumnDef> columnDef = new ArrayList<>();
        for (Iterator<ColumnDef> iterator = columnDefObject.iterator(); iterator.hasNext();) {
            ColumnDef column = iterator.next();
            DataSetColumns dataSetColumn = uiService.getDataSetColumn(column.getFieldName(), column, userId, dataSetId, widgetId);
            if (dataSetColumn != null) {
                column.setId(dataSetColumn.getId());
                column.setExpression(dataSetColumn.getExpression());
                column.setDisplayFormat(dataSetColumn.getDisplayFormat());
                column.setUserId(dataSetColumn.getUserId());
                column.setWidgetId(dataSetColumn.getWidgetId());
            }
            columnDef.add(column);
        }
        List<DataSetColumns> dataSetColumns = uiService.getDataSetColumns(dataSetId, widgetId);
        for (Iterator<DataSetColumns> iterator = dataSetColumns.iterator(); iterator.hasNext();) {
            DataSetColumns dataSetColumn = iterator.next();
            if (checkIsDerivedExpr(dataSetColumn) || checkIsDerivedFunction(dataSetColumn)) {
                continue;
            }
            boolean exist = false;
            for (Iterator<ColumnDef> iterator1 = columnDefObject.iterator(); iterator1.hasNext();) {
                ColumnDef column = iterator1.next();
                if (column.getFieldName().equalsIgnoreCase(dataSetColumn.getFieldName())) {
                    exist = true;
                }
            }
            if (!exist) {
                if (dataSetColumn.getExpression() == null && dataSetColumn.getFunctionName() == null) {
                    uiService.deleteDataSetColumns(dataSetColumn.getId());
                }
            }
        }

        System.out.println("columnDef ---> " + columnDef);
        return columnDef;
    }

    public String getFromMultiValueMap(MultiValueMap valueMap, String key) {
        List<String> dataSourceTypeList = (List<String>) valueMap.get(key);
        if (dataSourceTypeList != null && !dataSourceTypeList.isEmpty()) {
            return dataSourceTypeList.get(0);
        }
        return null;
    }

    public MultiValueMap<String, String> getRequest(DataSet dataSet, MultiValueMap valueMap) {
        MultiValueMap<String, String> joinValueMap = new LinkedMultiValueMap<>();
        if (dataSet.getReportName() != null) {
            joinValueMap.put("dataSetReportName", Arrays.asList(dataSet.getReportName()));
        }
        joinValueMap.put("dataSetId", Arrays.asList(dataSet.getId() + ""));
        if (dataSet.getJoinDataSetId() != null) {
            joinValueMap.put("joinDataSetId", Arrays.asList(dataSet.getJoinDataSetId().getId() + ""));
        } else {
            joinValueMap.put("joinDataSetId", null);
        }
        joinValueMap.put("timeSegment", Arrays.asList(dataSet.getTimeSegment()));
        joinValueMap.put("filter", Arrays.asList(dataSet.getNetworkType()));
        joinValueMap.put("url", Arrays.asList(dataSet.getUrl()));
        joinValueMap.put("query", Arrays.asList(dataSet.getQuery()));
        joinValueMap.put("productSegment", Arrays.asList(dataSet.getProductSegment()));
        DataSource dataSource = dataSet.getDataSourceId();
        if (dataSource != null) {
            joinValueMap.put("connectionUrl", Arrays.asList(dataSource.getConnectionString()));
            joinValueMap.put("dataSourceId", Arrays.asList(dataSource.getId() + ""));
            joinValueMap.put("driver", Arrays.asList(dataSource.getDataSourceType()));
            joinValueMap.put("dataSourceType", Arrays.asList(dataSource.getDataSourceType()));
            joinValueMap.put("username", Arrays.asList(dataSource.getUserName()));
            joinValueMap.put("password", Arrays.asList(dataSource.getPassword()));
        } else {
            joinValueMap.put("connectionUrl", null);
            joinValueMap.put("dataSourceId", null);
            joinValueMap.put("driver", null);
            joinValueMap.put("dataSourceType", null);
            joinValueMap.put("username", null);
            joinValueMap.put("password", null);
        }
        joinValueMap.put("startDate", Arrays.asList(getFromMultiValueMap(valueMap, "startDate")));
        joinValueMap.put("accountId", Arrays.asList(getFromMultiValueMap(valueMap, "accountId")));
        joinValueMap.put("locationId", Arrays.asList(getFromMultiValueMap(valueMap, "locationId")));
        joinValueMap.put("endDate", Arrays.asList(getFromMultiValueMap(valueMap, "endDate")));
        return joinValueMap;
    }

    public List<Map<String, Object>> joinData(List dataSet1, List dataSet2, String joinType, List<String> mappings) {
        List<Map<String, Object>> joinData = new ArrayList<>();
        if (joinType.equalsIgnoreCase("inner")) {
            joinData = innerJoin(dataSet1, dataSet2, mappings);
        } else if (joinType.equalsIgnoreCase("left")) {
            joinData = leftJoin(dataSet1, dataSet2, mappings);
        } else if (joinType.equalsIgnoreCase("right")) {
            joinData = rightJoin(dataSet1, dataSet2, mappings);
        } else if (joinType.equalsIgnoreCase("union")) {
            joinData = union(dataSet1, dataSet2, mappings);
        }
        return joinData;
    }

    public Map getJoinDataSet(MultiValueMap valueMap, HttpServletRequest request, HttpServletResponse response, Integer dataSetId) {
        DataSet dataSet = null;
        DataSet dataSetIdFirst = null;
        DataSet dataSetIdSecond = null;
        JoinDataSet joinDataSet = null;
        if (dataSetId == null) {
            String joinDataSetId = getFromMultiValueMap(valueMap, "joinDataSetId");
            System.out.println(joinDataSetId);
            if (!isNullOrEmpty(joinDataSetId)) {
                joinDataSet = uiService.getJoinDataSetById(Integer.parseInt(joinDataSetId));
                System.out.println(joinDataSet);
            }
        } else {
            dataSet = uiService.getDataSetById(dataSetId);
            System.out.println(dataSet);
            if (!dataSet.getDataSourceId().getDataSourceType().equalsIgnoreCase("join")) {
                return getData(valueMap, request, response);
            }
            joinDataSet = dataSet.getJoinDataSetId();
        }
        if (joinDataSet != null) {
            dataSetIdFirst = joinDataSet.getDataSetIdFirst();
            dataSetIdSecond = joinDataSet.getDataSetIdSecond();
            String operationType = joinDataSet.getOperationType();
            return getJoinDataSet(joinDataSet.getId(), dataSetIdFirst, dataSetIdSecond, operationType, valueMap, request, response);
        }
        return null;
    }

    public Map getJoinDataSet(Integer joinDataSetId, DataSet dataSetIdFirst, DataSet dataSetIdSecond, String operationType, MultiValueMap valueMap, HttpServletRequest request, HttpServletResponse response) {
        String userIdStr = getFromMultiValueMap(valueMap, "userId");

        Integer userIdInt = null;
        if (userIdStr != null) {
            try {
                userIdInt = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {

            }
        }
        MultiValueMap<String, String> request1 = getRequest(dataSetIdFirst, valueMap);
        MultiValueMap<String, String> request2 = getRequest(dataSetIdSecond, valueMap);
        Map dataMap1 = getJoinDataSet(request1, request, response, dataSetIdFirst.getId());
        Map dataMap2 = getJoinDataSet(request2, request, response, dataSetIdSecond.getId());
        List<Map<String, Object>> dataList1 = (List<Map<String, Object>>) dataMap1.get("data");
        dataList1 = addDerivedColumnsToDataSet(dataSetIdFirst.getId(), userIdInt, dataList1, request1, request, response);

        List<Map<String, Object>> dataList2 = (List<Map<String, Object>>) dataMap2.get("data");
        dataList2 = addDerivedColumnsToDataSet(dataSetIdSecond.getId(), userIdInt, dataList2, request2, request, response);

        Integer secondDataSetAppender = dataSetIdSecond.getId();
        if (!operationType.equalsIgnoreCase("union")) {
            Set<String> columnSet = dataList1.get(0).keySet();
            for (Iterator<Map<String, Object>> iterator = dataList2.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                try {
                    dataMap.entrySet().forEach((entry) -> {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        for (String columnStr : columnSet) {
                            if (key.equalsIgnoreCase(columnStr)) {
                                dataMap.remove(key);
                                dataMap.put(key + secondDataSetAppender, value);
                                System.out.println("dataMap ---> " + dataMap);
                                break;
                            }
                        }
                    });
                } catch (ConcurrentModificationException e) {
                }
            }
        }

        List<JoinDataSetCondition> joinDatasetConditionList = uiService.getJoinDataSetConditionById(joinDataSetId);
        List<String> mappings = new ArrayList<>();

        Map<String, Object> dataSetTwoMap = dataList2.get(0);
        for (Iterator<JoinDataSetCondition> iterator = joinDatasetConditionList.iterator(); iterator.hasNext();) {
            JoinDataSetCondition joinDataSetCondition = iterator.next();
            String concatCondition = null;
            if (dataSetTwoMap.get(joinDataSetCondition.getConditionFieldSecond()) != null) {
                concatCondition = "" + joinDataSetCondition.getConditionFieldFirst() + "," + joinDataSetCondition.getConditionFieldSecond();
            } else {
                concatCondition = "" + joinDataSetCondition.getConditionFieldFirst() + "," + joinDataSetCondition.getConditionFieldSecond() + secondDataSetAppender;
            }
            if (joinDataSetCondition.getColumnName() != null) {
                concatCondition += "," + joinDataSetCondition.getColumnName();
            }
            mappings.add(concatCondition);
        }
        System.out.println("MAPPINGS " + mappings);
        List<Map<String, Object>> joinData = joinData(dataList1, dataList2, operationType, mappings);
        Map returnMap = new HashMap();
        returnMap.put("data", joinData);
        System.out.println("JOINED DATA" + joinData);
        returnMap.put("columnDefs", getColumnDefObject(joinData));
        return returnMap;
    }

    public List<Map<String, Object>> addDerivedColumnsToDataSet(Integer dataSetId, Integer userId, List<Map<String, Object>> data, MultiValueMap request, HttpServletRequest httpRequest, HttpServletResponse response) {
        List<DataSetColumns> dataSetColumnList = uiService.getDataSetColumnsByDataSetId(dataSetId, userId);
        if (dataSetColumnList.size() > 0) {
            List<Map<String, Object>> dataWithDerivedFunctions = addDerivedColumnsFunction(dataSetColumnList, data, request, httpRequest, response);
            List<Map<String, Object>> dataWithDerivedColumns = addDerivedColumnsExpr(dataSetColumnList, dataWithDerivedFunctions);
            return dataWithDerivedColumns;
        }
        return data;
    }

    public Map getJoinData(MultiValueMap valueMap, HttpServletRequest request, HttpServletResponse response, Integer joinDataSetIdInt) {
        DataSet dataSetOne = null;
        DataSet dataSetTwo = null;
        String operationType = null;
        List<String> mappings = new ArrayList<>();
        Map joinDataSetOneMap = new HashMap<>();
        Map joinDataSetTwoMap = new HashMap<>();

        String userIdStr = getFromMultiValueMap(valueMap, "userId");

        Integer userIdInt = null;
        if (userIdStr != null) {
            try {
                userIdInt = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {

            }
        }

        List<JoinDataSetCondition> joinDatasetConditionList = uiService.getJoinDataSetConditionById(joinDataSetIdInt);
        for (Iterator<JoinDataSetCondition> iterator = joinDatasetConditionList.iterator(); iterator.hasNext();) {
            JoinDataSetCondition joinDataSetCondition = iterator.next();
            JoinDataSet joinDataSet = joinDataSetCondition.getJoinDataSetId();
            dataSetOne = joinDataSet.getDataSetIdFirst();
            dataSetTwo = joinDataSet.getDataSetIdSecond();
            operationType = joinDataSet.getOperationType();
            System.out.println("Join data set");
            System.out.println(joinDataSet);
        }
        System.out.println("Data Set one ===> ");
        System.out.println(dataSetOne);
        MultiValueMap joinValueMapOne = getRequest(dataSetOne, valueMap);

        String joinDataSetIdStr1 = getFromMultiValueMap(joinValueMapOne, "joinDataSetId");
        String dataSourceId1 = getFromMultiValueMap(joinValueMapOne, "dataSourceId");

        if (joinDataSetIdStr1 != null && !joinDataSetIdStr1.isEmpty() && !joinDataSetIdStr1.equalsIgnoreCase("null") && (dataSourceId1 == null || dataSourceId1.isEmpty() || dataSourceId1.equalsIgnoreCase("null"))) {
            try {
                Integer joinDataSetIdNum = Integer.parseInt(joinDataSetIdStr1);
                System.out.println("SECOND LEVEL JOIN: " + joinDataSetIdNum);
                joinDataSetOneMap = getJoinData(joinValueMapOne, request, response, joinDataSetIdNum);
            } catch (NumberFormatException e) {

            }
        } else {
            joinDataSetOneMap = getData(joinValueMapOne, request, response);
        }
        List<Map<String, Object>> dataSetOneList = (List<Map<String, Object>>) joinDataSetOneMap.get("data");
        String dataSetIdOneStr = getFromMultiValueMap(joinValueMapOne, "dataSetId");
        if (dataSetIdOneStr != null && !dataSetIdOneStr.isEmpty()) {
            Integer dataSetIdInt = Integer.parseInt(dataSetIdOneStr);
            List<DataSetColumns> dataSetColumnList = uiService.getDataSetColumnsByDataSetId(dataSetIdInt, userIdInt);
            if (dataSetColumnList.size() > 0) {
                List<Map<String, Object>> dataWithDerivedFunctions = addDerivedColumnsFunction(dataSetColumnList, dataSetOneList, joinValueMapOne, request, response);
                List<Map<String, Object>> dataWithDerivedColumns = addDerivedColumnsExpr(dataSetColumnList, dataWithDerivedFunctions);
                joinDataSetOneMap.put("data", dataWithDerivedColumns);
            }
        }

        Set<String> columnSet = dataSetOneList.get(0).keySet();

        MultiValueMap joinValueMapTwo = getRequest(dataSetTwo, valueMap);
        String joinDataSetIdStr2 = getFromMultiValueMap(joinValueMapTwo, "joinDataSetId");
        String dataSourceId2 = getFromMultiValueMap(joinValueMapTwo, "dataSourceId");
        if (joinDataSetIdStr2 != null && !joinDataSetIdStr2.isEmpty() && !joinDataSetIdStr2.equalsIgnoreCase("null") && (dataSourceId2 == null || dataSourceId2.isEmpty() || dataSourceId2.equalsIgnoreCase("null"))) {
            try {
                Integer joinDataSetIdNum = Integer.parseInt(joinDataSetIdStr2);
                joinDataSetTwoMap = getJoinData(joinValueMapTwo, request, response, joinDataSetIdNum);
            } catch (NumberFormatException e) {

            }
        } else {
            joinDataSetTwoMap = getData(joinValueMapTwo, request, response);
        }
        List<Map<String, Object>> dataSetTwoList = (List<Map<String, Object>>) joinDataSetTwoMap.get("data");
        String dataSetIdTwoStr = getFromMultiValueMap(joinValueMapTwo, "dataSetId");
        if (dataSetIdTwoStr != null && !dataSetIdTwoStr.isEmpty()) {

            Integer dataSetIdInt = Integer.parseInt(dataSetIdTwoStr);

            List<DataSetColumns> dataSetColumnList = uiService.getDataSetColumnsByDataSetId(dataSetIdInt, userIdInt);
            if (dataSetColumnList.size() > 0) {
                List<Map<String, Object>> dataWithDerivedFunctions = addDerivedColumnsFunction(dataSetColumnList, dataSetTwoList, joinValueMapTwo, request, response);
                List<Map<String, Object>> dataWithDerivedColumns = addDerivedColumnsExpr(dataSetColumnList, dataWithDerivedFunctions);
                joinDataSetTwoMap.put("data", dataWithDerivedColumns);
            }
        }
        Integer secondDataSetAppender = dataSetTwo.getId(); //new Long(new Date().getTime() / 100000).intValue();
        if (!operationType.equalsIgnoreCase("union")) {
            for (Iterator<Map<String, Object>> iterator = dataSetTwoList.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                try {
                    dataMap.entrySet().forEach((entry) -> {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        for (String columnStr : columnSet) {
                            if (key.equalsIgnoreCase(columnStr)) {
                                dataMap.remove(key);
                                dataMap.put(key + secondDataSetAppender, value);
                                System.out.println("dataMap ---> " + dataMap);
                                break;
                            }
                        }
                    });
                } catch (ConcurrentModificationException e) {
                }
            }
        }
        Map<String, Object> dataSetTwoMap = dataSetTwoList.get(0);
        for (Iterator<JoinDataSetCondition> iterator = joinDatasetConditionList.iterator(); iterator.hasNext();) {
            JoinDataSetCondition joinDataSetCondition = iterator.next();
            String concatCondition = null;
            if (dataSetTwoMap.get(joinDataSetCondition.getConditionFieldSecond()) != null) {
                concatCondition = "" + joinDataSetCondition.getConditionFieldFirst() + "," + joinDataSetCondition.getConditionFieldSecond();
            } else {
                concatCondition = "" + joinDataSetCondition.getConditionFieldFirst() + "," + joinDataSetCondition.getConditionFieldSecond() + secondDataSetAppender;
            }
            if (joinDataSetCondition.getColumnName() != null) {
                concatCondition += "," + joinDataSetCondition.getColumnName();
            }
            mappings.add(concatCondition);
        }
        List<Map<String, Object>> joinData = new ArrayList<>();
        if (operationType.equalsIgnoreCase("inner")) {
            joinData = innerJoin(dataSetOneList, dataSetTwoList, mappings);
        } else if (operationType.equalsIgnoreCase("left")) {
            joinData = leftJoin(dataSetOneList, dataSetTwoList, mappings);
        } else if (operationType.equalsIgnoreCase("right")) {
            joinData = rightJoin(dataSetOneList, dataSetTwoList, mappings);
        } else if (operationType.equalsIgnoreCase("union")) {
            joinData = union(dataSetOneList, dataSetTwoList, mappings);
        }

        List<Map<String, Object>> data = joinData;
        List<ColumnDef> columnDefs = getColumnDefObject(data);
        Map returnMap = new HashMap<>();
        returnMap.put("columnDefs", columnDefs);
        returnMap.put("data", data);
        return returnMap;
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty() || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("undefined");
    }

    public Map getData(MultiValueMap request, HttpServletRequest httpRequest, HttpServletResponse response) {
        Map returnMap = new HashMap<>();

        String dataSourceType = getFromMultiValueMap(request, "dataSourceType");
        String dataSetId = getFromMultiValueMap(request, "dataSetId");
        Integer dataSetIdInt = null;
        if (dataSetId != null) {
            try {
                dataSetIdInt = Integer.parseInt(dataSetId);
            } catch (Exception e) {

            }
            if (dataSetIdInt != null) {
                DataSet dataSet = uiService.readDataSet(dataSetIdInt);
                dataSourceType = (dataSourceType == null || dataSourceType.isEmpty()) ? dataSet.getDataSourceId().getDataSourceType() : dataSourceType;
            }
        }
        String joinDataSetId = getFromMultiValueMap(request, "joinDataSetId");
        request.put("joinDataSetId", Arrays.asList(joinDataSetId));
        if (isNullOrEmpty(dataSourceType)) {
            dataSourceType = "join";
        }

        if (dataSourceType.equalsIgnoreCase("facebook") || dataSourceType.equalsIgnoreCase("instagram")) {
            returnMap = (Map) getFbData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("csv")) {
            returnMap = (Map) getCsvData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("adwords")) {
            returnMap = (Map) getAdwordsData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("analytics")) {
            returnMap = (Map) getAnalyticsData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("bing")) {
            List<Map<String, Object>> dataList = getBingData(request, httpRequest, response);
            returnMap.put("data", dataList);
            returnMap.put("columnDefs", getColumnDefObject(dataList));
        } else if (dataSourceType.equalsIgnoreCase("sql")) {
            returnMap = getSqlData(request, httpRequest, response);
            List<Map<String, Object>> data = (List<Map<String, Object>>) returnMap.get("data");
            returnMap.put("columnDefs", getColumnDefObject(data));
        } else if (dataSourceType.equalsIgnoreCase("https")) {
            getHttpsData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("xls")) {
            returnMap = (Map) getXlsData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("pinterest")) {
            returnMap = (Map) getPinterestData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("linkedin")) {
            returnMap = (Map) getLinkedInData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("twitter")) {
            List<Map<String, Object>> dataList = getTwitterData(request, response);
            returnMap.put("data", dataList);
            returnMap.put("columnDefs", getColumnDefObject(dataList));
        } else if (dataSourceType.equalsIgnoreCase("join")) {
            System.out.println("Join Data Set");
            returnMap = getJoinDataSet(request, httpRequest, response, dataSetIdInt);
        }
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) returnMap.get("data");
        List<ColumnDef> columnDefs = (List<ColumnDef>) returnMap.get("columnDefs");
        System.out.println("Column Def For Data Format");
        System.out.println(returnMap);
        returnMap.put("data", formatData(dataList, columnDefs));
        return returnMap;
    }

    public static List<Map<String, Object>> formatData(final List<Map<String, Object>> dataSet, List<ColumnDef> columnDef) {
        boolean formatRequired = false;
        for (Iterator<ColumnDef> iterator1 = columnDef.iterator(); iterator1.hasNext();) {
            ColumnDef column = iterator1.next();
            if (column.getDataFormat() != null) {
                formatRequired = true;
            }
        }
        if (formatRequired == false) {
            return dataSet;
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = dataSet.iterator(); iterator.hasNext();) {
            Map<String, Object> data = iterator.next();
            for (Iterator<ColumnDef> iterator1 = columnDef.iterator(); iterator1.hasNext();) {
                ColumnDef column = iterator1.next();
                if (column.getDataFormat() != null) {
                    if (column.getDataFormat().equalsIgnoreCase(",")) {
                        String value = data.get(column.getFieldName()) + "";
                        data.put(column.getFieldName(), value.replaceAll(",", ""));
                    }
                    if (column.getDataFormat().equalsIgnoreCase("$")) {
                        String value = data.get(column.getFieldName()) + "";
                        data.put(column.getFieldName(), value.replaceAll(",", ""));
                    } else if (column.getDataFormat().equalsIgnoreCase("%")) {
                        String value = data.get(column.getFieldName()) + "";
                        data.put(column.getFieldName(), value.replaceAll("%", ""));
                    } else if (column.getFieldType() != null && column.getFieldType().equalsIgnoreCase("date") && column.getDataFormat() != null && !column.getDataFormat().isEmpty()) {
                        String value = data.get(column.getFieldName()) + "";
                        Date toDate = DateUtils.toDate(value, column.getDataFormat());
                        data.put(column.getFieldName(), DateUtils.dateToString(toDate, "MM/dd/yyyy"));
                        System.out.println("VALUE =============> " + value);
                    }
                }
            }
            dataList.add(data);
        }
        return dataList;
    }

    public static List<Map<String, Object>> leftJoin(final List<Map<String, Object>> dataSet1, final List<Map<String, Object>> dataSet2, final List<String> mappings) {
        Set<String> columnSet = dataSet2.get(0).keySet();
        List<Map<String, Object>> returnList = new ArrayList<>();
        dataSet1.forEach(map -> {
            Stream<Map<String, Object>> filters = dataSet2.stream().filter(e -> {
                boolean isValid = true;
                for (Iterator<String> iterator = mappings.iterator(); iterator.hasNext();) {
                    String mapping = iterator.next();
                    String[] mappingArray = mapping.split(",");
                    if (!(map.get(mappingArray[0]) + "").equalsIgnoreCase(e.get(mappingArray[1]) + "")) {
                        return false;
                    }
                }
                return isValid;
            });
            if (filters == null) {
                Map<String, Object> dataMap = new HashMap<>(map);
                for (Iterator<String> iterator = columnSet.iterator(); iterator.hasNext();) {
                    String column = iterator.next();
                    dataMap.putIfAbsent(column, "");
                }
                returnList.add(dataMap);
            } else {
                boolean matched = false;
                for (Iterator<Map<String, Object>> iterator = filters.iterator(); iterator.hasNext();) {
                    Map<String, Object> filter = iterator.next();
                    Map<String, Object> dataMap = new HashMap<>(map);
                    dataMap.putAll(filter);
                    returnList.add(dataMap);
                    matched = true;
                }
                if (matched == false) {
                    Map<String, Object> dataMap = new HashMap<>(map);
                    for (Iterator<String> iterator = columnSet.iterator(); iterator.hasNext();) {
                        String column = iterator.next();
                        dataMap.putIfAbsent(column, null);
                    }
                    returnList.add(dataMap);
                }
            }
        });
        return returnList;
    }

    public static List<Map<String, Object>> rightJoin(final List<Map<String, Object>> dataSet1, final List<Map<String, Object>> dataSet2, final List<String> mappings) {
        Set<String> columnSet = dataSet1.get(0).keySet();
        List<Map<String, Object>> returnList = new ArrayList<>();
        dataSet2.forEach(map -> {
            Stream<Map<String, Object>> filters = dataSet1.stream().filter(e -> {
                boolean isValid = true;
                for (Iterator<String> iterator = mappings.iterator(); iterator.hasNext();) {
                    String condition = iterator.next();
                    String[] mappingArray = condition.split(",");
                    if (!(map.get(mappingArray[1]) + "").equalsIgnoreCase(e.get(mappingArray[0]) + "")) {
                        return false;
                    }
                }
                return isValid;
            });
            if (filters == null) {
                Map<String, Object> dataMap = new HashMap<>(map);
                for (Iterator<String> iterator = columnSet.iterator(); iterator.hasNext();) {
                    String column = iterator.next();
                    dataMap.putIfAbsent(column, "");
                }
                returnList.add(dataMap);
            } else {
                boolean matched = false;
                for (Iterator<Map<String, Object>> iterator = filters.iterator(); iterator.hasNext();) {
                    Map<String, Object> filter = iterator.next();
                    Map<String, Object> dataMap = new HashMap<>(map);
                    dataMap.putAll(filter);
                    returnList.add(dataMap);
                    matched = true;
                }
                if (matched == false) {
                    Map<String, Object> dataMap = new HashMap<>(map);
                    for (Iterator<String> iterator = columnSet.iterator(); iterator.hasNext();) {
                        String column = iterator.next();
                        dataMap.putIfAbsent(column, null);
                    }
                    returnList.add(dataMap);
                }
            }
        });
        return returnList;
    }

    public static List<Map<String, Object>> innerJoin(List<Map<String, Object>> dataSet1, List<Map<String, Object>> dataSet2, List<String> mappings) {
        List<Map<String, Object>> returnList = new ArrayList<>();
        dataSet1.forEach(map -> {
            Stream<Map<String, Object>> filters = dataSet2.stream().filter(e -> {
                boolean isValid = true;
                for (Iterator<String> iterator = mappings.iterator(); iterator.hasNext();) {
                    String mapping = iterator.next();
                    String[] mappingArray = mapping.split(",");
                    if (!(map.get(mappingArray[0]) + "").equalsIgnoreCase(e.get(mappingArray[1]) + "")) {
                        return false;
                    }
                }
                return isValid;
            });
            for (Iterator<Map<String, Object>> iterator = filters.iterator(); iterator.hasNext();) {
                Map<String, Object> filter = iterator.next();
                Map<String, Object> dataMap = new HashMap<>(map);
                dataMap.putAll(filter);
                returnList.add(dataMap);
            }
        });

//    dataSet1.forEach(map -> map.put(TypeId.AMOUNT, 
//        dataSet2.stream()
//            .filter(e -> e.player_id == (int)map.get(TypeId.PLAYER) && 
//                         e.platform_id == (int)map.get(TypeId.PLATFORM))
//            .findFirst().map(e -> e.amount).orElse(null)
//        ));
        return returnList;
    }

    public static List<Map<String, Object>> union(List<Map<String, Object>> dataSet1, List<Map<String, Object>> dataSet2, List<String> mappings) {

        List<Map<String, Object>> returnList = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = dataSet1.iterator(); iterator.hasNext();) {
            Map<String, Object> dataSetMap = iterator.next();
            Map<String, Object> newDataMap = new HashMap<>();
            for (Iterator<String> iterator1 = mappings.iterator(); iterator1.hasNext();) {
                String mapping = iterator1.next();
                String[] mappingArray = mapping.split(",");
                String dsColumnName = mappingArray[0];
                String columnName = mappingArray[2];
                newDataMap.put(columnName, dataSetMap.get(dsColumnName));
            }
            returnList.add(newDataMap);
        }

        for (Iterator<Map<String, Object>> iterator = dataSet2.iterator(); iterator.hasNext();) {
            Map<String, Object> dataSetMap = iterator.next();
            Map<String, Object> newDataMap = new HashMap<>();
            for (Iterator<String> iterator1 = mappings.iterator(); iterator1.hasNext();) {
                String mapping = iterator1.next();
                String[] mappingArray = mapping.split(",");
                String dsColumnName = mappingArray[1];
                String columnName = mappingArray[2];
                newDataMap.put(columnName, dataSetMap.get(dsColumnName));
            }
            returnList.add(newDataMap);
        }
        return returnList;
    }

    public Map<String, Date> getCustomDate(String dateRangeName, Integer lastNdays, Integer lastNweeks, Integer lastNmonths, Integer lastNyears, Date endDate) {
        Map returnDateMap = new HashMap<>();
        Range dateRangeSelect = null;
        DateRange dateRange = null;
        if (lastNdays != null) {
            dateRangeSelect = Range.DAY;
        } else if (lastNweeks != null) {
            dateRangeSelect = Range.WEEK;
        } else if (lastNmonths != null) {
            dateRangeSelect = Range.MONTH;
        } else if (lastNyears != null) {
            dateRangeSelect = Range.YEAR;
        }

        if (dateRangeSelect.equals(Range.DAY)) {
            dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNdays, endDate);
        } else if (dateRangeSelect.equals(Range.WEEK)) {
            dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNweeks, endDate);
        } else if (dateRangeSelect.equals(Range.MONTH)) {
            dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNmonths, endDate);
        } else if (dateRangeSelect.equals(Range.YEAR)) {
            dateRange = DateRangeFactory.getRange(dateRangeSelect, lastNyears, endDate);
        }

        if (dateRange != null) {
            returnDateMap.put("startDate", dateRange.getStartDate());
            returnDateMap.put("endDate", dateRange.getEndDate());
        }
        return returnDateMap;
    }

    public DateRange getDateRange(String functionName, String dateRangeName, String customStartDate, String customEndDate, Date startDate, Date endDate) {
        DateRange dateRange = new DateRange();
        if (functionName.equalsIgnoreCase("yoy")) {
            dateRange.setStartDate(new DateTime(startDate).minusYears(1).toDate());
            dateRange.setEndDate(new DateTime(endDate).minusYears(1).toDate());
        } else if (functionName.equalsIgnoreCase("mom")) {
            dateRange.setStartDate(new DateTime(startDate).minusMonths(1).toDate());
            dateRange.setEndDate(new DateTime(endDate).minusMonths(1).toDate());
        } else if (functionName.equalsIgnoreCase("wow")) {
            dateRange.setStartDate(new DateTime(startDate).minusWeeks(1).toDate());
            dateRange.setEndDate(new DateTime(endDate).minusWeeks(1).toDate());
        } else if (functionName.equalsIgnoreCase("custom")) {
            dateRange.setStartDate(DateUtils.getStartDate(customStartDate));
            dateRange.setEndDate(DateUtils.getEndDate(customEndDate));
        }
        return dateRange;
    }

    public List<Map<String, Object>> addDerivedColumnsFunction(List<DataSetColumns> dataSetColumns, List<Map<String, Object>> data, MultiValueMap request, HttpServletRequest httpRequest, HttpServletResponse response) {
        // Supported Functions : yoy, mom, wow, custom
        String format = "yyyy-MM-dd";
        Date startDate = DateUtils.getStartDate(getFromMultiValueMap(request, "startDate"));
        Date endDate = DateUtils.getEndDate(getFromMultiValueMap(request, "endDate"));
        String cachedRange = DateUtils.dateToString(startDate, format) + " To " + DateUtils.dateToString(endDate, format);
        Map<String, List> cachedData = new HashMap<>();
        cachedData.put(cachedRange, data);
        Map<String, List> derivedColumnData = new HashMap<>();
        for (Iterator<DataSetColumns> iterator = dataSetColumns.iterator(); iterator.hasNext();) {
            DataSetColumns dataSetColumn = iterator.next();
            boolean isDerivedColumn = checkIsDerivedFunction(dataSetColumn);
            if (isDerivedColumn) {
                String functionName = dataSetColumn.getFunctionName();
                String dateRangeName = dataSetColumn.getDateRangeName();
                Integer lastNdays = dataSetColumn.getLastNdays();
                Integer lastNweeks = dataSetColumn.getLastNweeks();
                Integer lastNmonths = dataSetColumn.getLastNmonths();
                Integer lastNyears = dataSetColumn.getLastNyears();
                String customStartDate = dataSetColumn.getCustomStartDate();
                String customEndDate = dataSetColumn.getCustomEndDate();
                if (dateRangeName != null && !dateRangeName.isEmpty()) {
                    if (!dateRangeName.equalsIgnoreCase("custom")) {
                        Map<String, Date> dateMap = getCustomDate(dateRangeName, lastNdays, lastNweeks, lastNmonths, lastNyears, endDate);
                        customStartDate = DateUtils.dateToString(dateMap.get("startDate"), "MM/dd/yyyy");
                        customEndDate = DateUtils.dateToString(dateMap.get("endDate"), "MM/dd/yyyy");
                    }
                }

                DateRange dateRange = getDateRange(functionName, dateRangeName, customStartDate, customEndDate, startDate, endDate);
                String cachedRangeForFunction = DateUtils.dateToString(dateRange.getStartDate(), format) + " To " + DateUtils.dateToString(dateRange.getEndDate(), format);
                if (cachedData.get(cachedRangeForFunction) == null) {
                    List<String> startDateValue = new ArrayList();
                    startDateValue.add(DateUtils.dateToString(dateRange.getStartDate(), "MM/dd/yyyy"));
                    request.put("startDate", startDateValue);
                    List<String> endDateValue = new ArrayList();
                    endDateValue.add(DateUtils.dateToString(dateRange.getEndDate(), "MM/dd/yyyy"));
                    request.put("endDate", endDateValue);
                    Map dataMapForFunction = getData(request, httpRequest, response);
                    List<Map<String, Object>> dataForFunction = (List<Map<String, Object>>) dataMapForFunction.get("data");
                    cachedData.put(cachedRangeForFunction, dataForFunction);
                } else {

                }
                derivedColumnData.put(dataSetColumn.getFieldName(), cachedData.get(cachedRangeForFunction));
            } else {

            }
        }
        List<Map<String, Object>> returnData = new ArrayList<>();
        System.out.println("dataaaaa ------------> " + data);
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            Map<String, Object> returnDataMap = dataMap;
            for (Iterator<DataSetColumns> iterator1 = dataSetColumns.iterator(); iterator1.hasNext();) {
                DataSetColumns dataSetColumn = iterator1.next();
                boolean isDerivedColumn = checkIsDerivedFunction(dataSetColumn);
                if (isDerivedColumn) {
                    String functionName = dataSetColumn.getFunctionName();
                    String dateRangeName = dataSetColumn.getDateRangeName();
                    Integer lastNdays = dataSetColumn.getLastNdays();
                    Integer lastNweeks = dataSetColumn.getLastNweeks();
                    Integer lastNmonths = dataSetColumn.getLastNmonths();
                    Integer lastNyears = dataSetColumn.getLastNyears();
                    String customStartDate = dataSetColumn.getCustomStartDate();
                    String customEndDate = dataSetColumn.getCustomEndDate();
                    if (dateRangeName != null && !dateRangeName.isEmpty()) {
                        if (!dateRangeName.equalsIgnoreCase("custom")) {
                            Map<String, Date> dateMap = getCustomDate(dateRangeName, lastNdays, lastNweeks, lastNmonths, lastNyears, endDate);
                            customStartDate = DateUtils.dateToString(dateMap.get("startDate"), "MM/dd/yyyy");
                            customEndDate = DateUtils.dateToString(dateMap.get("endDate"), "MM/dd/yyyy");
                            //System.out.println("customStartDate ---> " + customStartDate);
                            //System.out.println("customEndDate ---> " + customEndDate);
                        }
                    }
                    DateRange dateRange = getDateRange(functionName, dateRangeName, customStartDate, customEndDate, startDate, endDate);
                    String cachedRangeForFunction = DateUtils.dateToString(dateRange.getStartDate(), format) + " To " + DateUtils.dateToString(dateRange.getEndDate(), format);
                    Object derivedFunctionValue = getDataForDerivedFunctionColumn(cachedData.get(cachedRangeForFunction), dataMap.get(dataSetColumn.getBaseField()), dataSetColumn);
                    returnDataMap.put(dataSetColumn.getFieldName(), derivedFunctionValue);
                } else {
                    returnDataMap.put(dataSetColumn.getFieldName(), dataMap.get(dataSetColumn.getFieldName()));
                }
            }
            returnData.add(returnDataMap);
        }
        return returnData;
    }

    public Object getDataForDerivedFunctionColumn(List<Map<String, Object>> data, Object baseFieldValue, DataSetColumns dataSetColumn) {
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            if ((mapData.get(dataSetColumn.getBaseField()) + "").equalsIgnoreCase(baseFieldValue + "")) {
                return mapData.get(dataSetColumn.getColumnName());
            }
        }
        return null;
    }

    public static List<Map<String, Object>> addDerivedColumnsExpr(List<DataSetColumns> dataSetColumns, List<Map<String, Object>> data) {
        List<Map<String, Object>> returnData = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            returnData.add(addDerivedColumnsExpr(dataSetColumns, dataMap));
        }
        return returnData;
    }

    public static Map<String, Object> addDerivedColumnsExpr(List<DataSetColumns> dataSetColumns, Map<String, Object> data) {
        Map<String, Object> returnMap = data;
        for (Iterator<DataSetColumns> iterator = dataSetColumns.iterator(); iterator.hasNext();) {
            DataSetColumns dataSetColumn = iterator.next();
            boolean isDerivedColumn = checkIsDerivedExpr(dataSetColumn);
            if (isDerivedColumn) {
                if (dataSetColumn.getExpression() != null) {
                    String expressionValue = executeExpression(dataSetColumn, data);
                    System.out.println("OUTPUT FROM EXPRESSION " + expressionValue);
                    if ((expressionValue.startsWith("'") && expressionValue.endsWith("'"))) {
                        Object expValue = expressionValue.substring(1, expressionValue.length() - 1);
                        returnMap.put(dataSetColumn.getFieldName(), expValue);
                    } else {
                        Object expValue = expressionValue;
                        returnMap.put(dataSetColumn.getFieldName(), expValue);
                    }
                }
            } else {
                returnMap.put(dataSetColumn.getFieldName(), data.get(dataSetColumn.getFieldName()));
            }

        }
        return returnMap;
    }

    private static String executeExpression(DataSetColumns dataSetColumn, Map<String, Object> data) {
        String postFixRule = ShuntingYard.postfix(dataSetColumn.getExpression());
        return ShuntingYard.executeExpression(data, postFixRule);
    }

    private static boolean checkIsDerivedExpr(DataSetColumns dataSetColumn) {
        if (dataSetColumn.getExpression() != null && !dataSetColumn.getExpression().isEmpty()) {
            return true;
        }
        return false;
    }

    private static boolean checkIsDerivedFunction(DataSetColumns dataSetColumn) {
        if (dataSetColumn.getFunctionName() != null && !dataSetColumn.getFunctionName().isEmpty()) {
            return true;
        }
        return false;
    }

    Map getCsvData(MultiValueMap<String, String> request, HttpServletResponse response) {
        try {
            String connectionString = getFromMultiValueMap(request, "connectionUrl");
            String dataSetId = getFromMultiValueMap(request, "dataSetId");
            Integer dataSetIdInt = null;
            if (dataSetId != null) {
                try {
                    dataSetIdInt = Integer.parseInt(dataSetId);
                } catch (Exception e) {

                }
            }
            if (connectionString == null) {

                DataSet dataSet = null;

                if (dataSetIdInt != null) {
                    dataSet = uiService.readDataSet(dataSetIdInt);
                }
                if (dataSet != null) {
                    connectionString = (connectionString == null || connectionString.isEmpty()) ? dataSet.getDataSourceId().getConnectionString() : connectionString;
                }
            }
            List<Map<String, Object>> dataSet = CsvDataSet.CsvDataSet(connectionString);

            Map returnMap = new HashMap<>();
            returnMap.put("data", dataSet);
            returnMap.put("columnDefs", getColumnDefObject(dataSet));
            return returnMap;
        } catch (IOException ex) {

        }
        return null;
    }

    Map getPinterestData(MultiValueMap<String, String> request, HttpServletResponse response) {

        String reportName = getFromMultiValueMap(request, "dataSetReportName");
        String dataSetId = getFromMultiValueMap(request, "dataSetId");

        String accountIdStr = getFromMultiValueMap(request, "accountId");
        System.out.println("Pinterest Account ID -->" + accountIdStr);
        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
//        System.out.println(account);
//        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
//        String accessToken = getAccountId(accountProperty, "pinterestAccessToken");
//        

        //get the acces token from settings
        List<Settings> pinterestAccessToken = settingsService.getProperty("pinterestAccessToken");
        System.out.println("***************************");
        System.out.println(pinterestAccessToken);
        String accessToken = SettingsProperty.getSettingsProperty(pinterestAccessToken, "pinterestAccessToken");

        System.out.println("Pinterst access token--->" + accessToken);
        if (accessToken == null) {
            System.out.println("pinterest accesstoken data not found now...");
            Map pinterestData = new HashMap();
            List<ColumnDef> columnDefs = new ArrayList();
            List<Map<String, String>> returnData = new ArrayList<>();
            pinterestData.put("columnDefs", columnDefs);
            pinterestData.put("data", returnData);
            return pinterestData;
        } else {
            Integer dataSetIdInt = null;
            DataSet dataSet = null;
            if (dataSetId != null) {
                try {
                    dataSetIdInt = Integer.parseInt(dataSetId);
                } catch (Exception e) {

                }
                if (dataSetIdInt != null) {
                    dataSet = uiService.readDataSet(dataSetIdInt);
                }
                if (dataSet != null) {
                    reportName = dataSet.getReportName();
                }
            }
            if (reportName.equalsIgnoreCase("getTopBoards")) {
                try {
                    String fbUrl = "https://api.pinterest.com/v1/me/boards/?access_token=" + accessToken
                            + "&fields=id%2Cname%2Curl%2Ccounts%2Ccreated_at%2Ccreator%2Cdescription%2Creason";
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&PINTEREST&&&&&&&&");
                    System.out.println(fbUrl);

                    String data = Rest.getData(fbUrl);
                    JSONParser parser = new JSONParser();
                    Object jsonObj = parser.parse(data);
                    JSONObject json = (JSONObject) jsonObj;
                    Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
                    Map returnMap = new HashMap<>();
                    List<Map<String, Object>> fbData = (List<Map<String, Object>>) jsonToMap.get("data");
                    List<Map<String, String>> returnData = new ArrayList<>();
                    for (Iterator<Map<String, Object>> iterator = fbData.iterator(); iterator.hasNext();) {
                        Map<String, Object> fbDataMap = iterator.next();
                        Map<String, String> returnDataMap = new HashMap<>();
                        returnDataMap.put("name", fbDataMap.get("name") + "");
                        returnDataMap.put("description", fbDataMap.get("description") + "");
                        returnDataMap.put("pins_counts", ((Map) fbDataMap.get("counts")).get("pins") + "");
                        returnData.add(returnDataMap);

                    }

                    Map pinterestData = new HashMap();
                    List<ColumnDef> columnDefs = getColumnDef(returnData);
                    returnMap.put("columnDefs", columnDefs);

                    returnMap.put("data", returnData);
                    System.out.println("************* Controller &********************");
                    System.out.println(returnMap);

                    return returnMap;
                } catch (ParseException ex) {
                    java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
                }
//            return null;
            }
            if (reportName.equalsIgnoreCase("getTopPins")) {
                try {
                    String fbUrl = "https://api.pinterest.com/v1/me/pins/?"
                            + "access_token=" + accessToken
                            + "&fields=id%2Clink%2Cnote%2Curl%2Cattribution%2Cboard%2Ccolor%2Ccounts%2Ccreated_at%2Ccreator%2Coriginal_link%2Cmetadata%2Cmedia";
                    String data = Rest.getData(fbUrl);
                    JSONParser parser = new JSONParser();
                    Object jsonObj = parser.parse(data);
                    JSONObject json = (JSONObject) jsonObj;
                    Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
                    Map returnMap = new HashMap<>();
                    List<Map<String, Object>> fbData = (List<Map<String, Object>>) jsonToMap.get("data");
                    List<Map<String, String>> returnData = new ArrayList<>();
                    for (Iterator<Map<String, Object>> iterator = fbData.iterator(); iterator.hasNext();) {
                        Map<String, Object> fbDataMap = iterator.next();
                        Map<String, String> returnDataMap = new HashMap<>();
                        returnDataMap.put("note", fbDataMap.get("note") + "");
                        returnDataMap.put("url", fbDataMap.get("url") + "");
                        returnDataMap.put("created_at", fbDataMap.get("created_at") + "");
                        returnData.add(returnDataMap);
                    }

                    Map pinterestData = new HashMap();
                    List<ColumnDef> columnDefs = getColumnDef(returnData);
                    returnMap.put("columnDefs", columnDefs);
                    returnMap.put("data", returnData);
                    System.out.println("************* Controller &********************");
                    System.out.println(returnMap);
                    return returnMap;
                } catch (ParseException ex) {
                    java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
                }
//            return null;
            }
            if (reportName.equalsIgnoreCase("getOrganicData")) {
                try {
                    String fbUrl = "https://api.pinterest.com/v1/me/?access_token=" + accessToken
                            + "&fields=first_name%2Cid%2Clast_name%2Curl%2Ccounts";
                    String data = Rest.getData(fbUrl);
                    JSONParser parser = new JSONParser();
//                Object jsonObj = parser.parse(data);
//                JSONObject json = (JSONObject) jsonObj;
//                Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
                    Map returnMap = new HashMap<>();
//                List fbData = (List<Map<String,Object>>) jsonToMap.get("data");

                    //////////////////////////
                    JSONObject jsonArray = (JSONObject) parser.parse(data);

                    Map<String, Object> myData = (Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) jsonArray).get("data")).get("counts");
                    List<Map<String, Object>> pinterestData = new ArrayList<>();

                    Map<String, Object> myMapData = new HashMap<>();
                    for (Map.Entry<String, Object> entry : myData.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        myMapData.put(key, value + "");
                    }
                    pinterestData.add(myMapData);

                    List<ColumnDef> columnDefObject = getColumnDefObject(pinterestData);

                    /////////////////////////////////////////////////////////
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    System.out.println(pinterestData);
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

//                fbData.lastIndexOf(jsonObj);
//                String likesCount = fbData.size() + "";
//                Map<String, String> boardsSize = new HashMap<>();
//                boardsSize.put("total_pin_likes", likesCount);
//                List<Map<String, String>> listData = new ArrayList<>();
//                listData.add(boardsSize);
//
                    returnMap.put("columnDefs", columnDefObject);

                    returnMap.put("data", pinterestData);
                    return returnMap;
                } catch (ParseException ex) {
                    java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
                }
//            return null;
            }
        }
        return null;

    }

    @RequestMapping(value = "getSheets", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map<Integer, String> getXlsSheets(HttpServletRequest request, HttpServletResponse response) {
        String dataSourceId = request.getParameter("dataSourceId");
        Integer dataSourceIdInt = Integer.parseInt(dataSourceId);
        DataSource dataSource = uiService.getDataSourceById(dataSourceIdInt);
        XlsDataSet xlsDs = new XlsDataSet();
        if (dataSource.getConnectionString().endsWith("xls")) {
            return xlsDs.getSheetListXls(dataSource.getConnectionString());
        } else if (dataSource.getConnectionString().endsWith("xlsx")) {
            return xlsDs.getSheetListXlsx(dataSource.getConnectionString());
        }
        return null;
    }

    public Map getXlsData(MultiValueMap request, HttpServletResponse response) {
        try {
            String dataSetId = getFromMultiValueMap(request, "dataSetId");
            String dataSetReportName = getFromMultiValueMap(request, "dataSetReportName");
            String connectionUrl = getFromMultiValueMap(request, "connectionUrl");
            Integer dataSetIdInt = null;
            DataSet dataSet = null;
            if (dataSetId != null) {
                try {
                    dataSetIdInt = Integer.parseInt(dataSetId);
                } catch (Exception e) {

                }
                if (dataSetIdInt != null) {
                    dataSet = uiService.readDataSet(dataSetIdInt);
                }
                if (dataSet != null) {
                    dataSetReportName = (dataSetReportName == null || dataSetReportName.isEmpty()) ? dataSet.getReportName() : dataSetReportName;
                    connectionUrl = dataSet.getDataSourceId().getConnectionString();
                }
            }
            String accountIdStr = getFromMultiValueMap(request, "accountId");
            Date startDate = DateUtils.getStartDate(getFromMultiValueMap(request, "startDate"));
            Date endDate = DateUtils.getEndDate(getFromMultiValueMap(request, "endDate"));
            String fieldsOnly = getFromMultiValueMap(request, "fieldsOnly");

            String widgetIdStr = getFromMultiValueMap(request, "widgetId");
            if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
                Integer widgetId = Integer.parseInt(widgetIdStr);
                TabWidget widget = uiService.getWidgetById(widgetId);
                startDate = DateUtils.getStartDate(widget.getCustomStartDate());
                endDate = DateUtils.getEndDate(widget.getCustomEndDate());
                if (!widget.getDateRangeName().equalsIgnoreCase("custom") && !widget.getDateRangeName().equalsIgnoreCase("select date duration")) {
                    Map dateRange = getCustomDate(widget.getDateRangeName(), widget.getLastNdays(), widget.getLastNweeks(), widget.getLastNmonths(), widget.getLastNyears(), endDate);
                    startDate = DateUtils.getStartDate(dateRange.get("startDate") + "");
                    endDate = DateUtils.getEndDate(dateRange.get("endDate") + "");
                }
            }

            Integer accountId = Integer.parseInt(accountIdStr);
            Account account = userService.getAccountId(accountId);
            if (connectionUrl.endsWith("xlsx")) {
                List<Map<String, String>> xlsxDataSet = XlsDataSet.XlsxDataSet(connectionUrl, dataSetReportName);
                Map returnMap = new HashMap();
                returnMap.put("data", xlsxDataSet);
                returnMap.put("columnDefs", getColumnDef(xlsxDataSet));
                return returnMap;
            } else if (connectionUrl.endsWith("xls")) {
                List<Map<String, String>> xlsDataSet = XlsDataSet.XlsDataSet(connectionUrl, dataSetReportName);
                Map returnMap = new HashMap();
                returnMap.put("data", xlsDataSet);
                returnMap.put("columnDefs", getColumnDef(xlsDataSet));
                return returnMap;
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void getHttpsData(final MultiValueMap<String, String> request, HttpServletResponse response) {
        String url = getFromMultiValueMap(request, "url");
        String dataSetId = getFromMultiValueMap(request, "dataSetId");
        Integer dataSetIdInt = null;
        DataSet dataSet = null;
        if (dataSetId != null) {
            try {
                dataSetIdInt = Integer.parseInt(dataSetId);
            } catch (Exception e) {

            }
            if (dataSetIdInt != null) {
                dataSet = uiService.readDataSet(dataSetIdInt);
            }
            if (dataSet != null) {
                if (url == null) {
                    url = (url == null || url.isEmpty()) ? dataSet.getUrl() : url;
                }
            }
        }
        String accountIdStr = getFromMultiValueMap(request, "accountId");
        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, List<String>> entrySet : request.entrySet()) {
            String key = entrySet.getKey();
            List<String> value = entrySet.getValue();
            valueMap.put(key, value);
        }
        for (Iterator<Property> iterator = accountProperty.iterator(); iterator.hasNext();) {
            Property property = iterator.next();
            List<String> valueList = new ArrayList();
            valueList.add(property.getPropertyValue());
            valueMap.put(property.getPropertyName(), valueList);
        }

        String data = Rest.getData(url, valueMap);
        try {
            response.getOutputStream().write(data.getBytes());
        } catch (IOException ex) {
        }
    }

    private Object getAnalyticsData(MultiValueMap request, HttpServletResponse response) {
        System.out.println("Analytics");
        String dataSetId = getFromMultiValueMap(request, "dataSetId");
        String dataSetReportName = getFromMultiValueMap(request, "dataSetReportName");
        String timeSegment = getFromMultiValueMap(request, "timeSegment");
        if (timeSegment != null && (timeSegment.isEmpty() || timeSegment.equalsIgnoreCase("undefined") || timeSegment.equalsIgnoreCase("null"))) {
            System.out.println("In ifffff timeSegment...");
            timeSegment = null;
        }
        String productSegment = getFromMultiValueMap(request, "productSegment");
        if (productSegment != null && (productSegment.isEmpty() || productSegment.equalsIgnoreCase("undefined") || productSegment.equalsIgnoreCase("null"))) {
            System.out.println("In ifffff productSegment...");
            productSegment = null;
        }
        Integer dataSetIdInt = null;
        DataSet dataSet = null;

        if (dataSetId != null) {
            try {
                dataSetIdInt = Integer.parseInt(dataSetId);
            } catch (Exception e) {

            }
            if (dataSetIdInt != null) {
                dataSet = uiService.readDataSet(dataSetIdInt);
            }
            if (dataSet != null) {
                System.out.println("productSegment ---> " + productSegment);
                System.out.println("productSegment dataset---> " + dataSet.getProductSegment());

                dataSetReportName = (dataSetReportName == null || dataSetReportName.isEmpty()) ? dataSet.getReportName() : dataSetReportName;
                timeSegment = (timeSegment == null || timeSegment.isEmpty()) ? dataSet.getTimeSegment() : timeSegment;
                productSegment = (productSegment == null || productSegment.isEmpty()) ? dataSet.getProductSegment() : productSegment;
            }
        }
        System.out.println("timeSegment ---> " + timeSegment);
        System.out.println("productSegment2 ---> " + productSegment);

        String accountIdStr = getFromMultiValueMap(request, "accountId");
        String fieldsOnly = getFromMultiValueMap(request, "fieldsOnly");

        String widgetIdStr = getFromMultiValueMap(request, "widgetId");
        Date startDate = DateUtils.getStartDate(getFromMultiValueMap(request, "startDate"));
        Date endDate = DateUtils.getEndDate(getFromMultiValueMap(request, "endDate"));

        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
            Integer widgetId = Integer.parseInt(widgetIdStr);
            TabWidget widget = uiService.getWidgetById(widgetId);

            String widgetProductSegment = widget.getProductSegment();
            String widgetTimeSegment = widget.getTimeSegment();
            String widgetNetworkType = widget.getNetworkType();
            if (widgetProductSegment == null || widgetProductSegment.isEmpty() || widgetProductSegment.equalsIgnoreCase("null") || widgetProductSegment.equalsIgnoreCase("none")) {
                productSegment = null;
            } else if (widgetProductSegment != null || !widgetProductSegment.isEmpty() || !widgetProductSegment.equalsIgnoreCase("null") || !widgetProductSegment.equalsIgnoreCase("none") || !widgetProductSegment.equalsIgnoreCase("undefined")) {
                productSegment = productSegment == null ? widgetProductSegment : productSegment;
            }
            if (widgetTimeSegment == null || widgetTimeSegment.isEmpty() || widgetTimeSegment.equalsIgnoreCase("null") || widgetTimeSegment.equalsIgnoreCase("none")) {
                timeSegment = null;
            } else if (widgetTimeSegment != null || !widgetTimeSegment.isEmpty() || !widgetProductSegment.equalsIgnoreCase("null") || !widgetTimeSegment.equalsIgnoreCase("none") || !widgetTimeSegment.equalsIgnoreCase("undefined")) {
                timeSegment = timeSegment == null ? widgetTimeSegment : timeSegment;
            }
            if (widget.getDateRangeName() != null && !widget.getDateRangeName().isEmpty()) {
                if (widget.getDateRangeName().equalsIgnoreCase("custom")) {
                    startDate = DateUtils.getStartDate(widget.getCustomStartDate());
                    endDate = DateUtils.getEndDate(widget.getCustomEndDate());
                } else if (!widget.getDateRangeName().equalsIgnoreCase("custom") && !widget.getDateRangeName().equalsIgnoreCase("select date duration") && !widget.getDateRangeName().equalsIgnoreCase("none")) {
                    Map<String, Date> dateRange = getCustomDate(widget.getDateRangeName(), widget.getLastNdays(), widget.getLastNweeks(), widget.getLastNmonths(), widget.getLastNyears(), endDate);
                    startDate = dateRange.get("startDate");
                    endDate = dateRange.get("endDate");
                }
            }
        }
        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        String gaAccountId = getAccountId(accountProperty, "gaAccountId");
        String gaProfileId = getAccountId(accountProperty, "gaProfileId");

        Map dataMap = gaService.getGaReport(dataSetReportName, gaProfileId, startDate, endDate, timeSegment, productSegment, dataSetIdInt);
        List<Map<String, Object>> data = (List<Map<String, Object>>) dataMap.get("data");
        List<ColumnDef> columnDefs = getColumnDefObject(data);
        Map returnMap = new HashMap<>();
        returnMap.put("columnDefs", columnDefs);
        returnMap.put("data", data);
        return returnMap;
    }

    private Object getAdwordsData(MultiValueMap request, HttpServletResponse response) {
        String dataSetId = getFromMultiValueMap(request, "dataSetId");
        String dataSetReportName = getFromMultiValueMap(request, "dataSetReportName");
        String timeSegment = getFromMultiValueMap(request, "timeSegment");
        String filter = getFromMultiValueMap(request, "filter");
        String productSegment = getFromMultiValueMap(request, "productSegment");
        Integer dataSetIdInt = null;
        DataSet dataSet = null;

        System.out.println("Adwords Data0 ===> " + timeSegment + " --- " + productSegment + " --- " + filter);
        if (dataSetId != null) {
            try {
                dataSetIdInt = Integer.parseInt(dataSetId);
            } catch (Exception e) {

            }
            if (dataSetIdInt != null) {
                dataSet = uiService.readDataSet(dataSetIdInt);
            }

            if (dataSet != null) {
                dataSetReportName = (dataSetReportName == null || dataSetReportName.isEmpty()) ? dataSet.getReportName() : dataSetReportName;
                timeSegment = (timeSegment == null || timeSegment.isEmpty()) ? dataSet.getTimeSegment() : timeSegment;
                productSegment = (productSegment == null || productSegment.isEmpty()) ? dataSet.getProductSegment() : productSegment;
                filter = (filter == null || filter.isEmpty()) ? dataSet.getNetworkType() : filter;
                System.out.println("Adwords Data1 ===> " + timeSegment + " --- " + productSegment + " --- " + filter);
            }
        }
        System.out.println("Adwords Data2 ===> " + timeSegment + " --- " + productSegment + " --- " + filter);
        if (timeSegment != null && (timeSegment.isEmpty() || timeSegment.equalsIgnoreCase("undefined") || timeSegment.equalsIgnoreCase("null") || timeSegment.equalsIgnoreCase("none"))) {
            timeSegment = null;
        }
        System.out.println("Adwords Data3 ===> " + timeSegment + " --- " + productSegment + " --- " + filter);
        if (productSegment != null && (productSegment.isEmpty() || productSegment.equalsIgnoreCase("undefined") || productSegment.equalsIgnoreCase("null") || productSegment.equalsIgnoreCase("none"))) {
            productSegment = null;
        }
        System.out.println("Adwords Data4 ===> " + timeSegment + " --- " + productSegment + " --- " + filter);
        if (filter != null && (filter.isEmpty() || filter.equalsIgnoreCase("undefined") || filter.equalsIgnoreCase("null") || filter.equalsIgnoreCase("none"))) {
            filter = null;
        }
        System.out.println("Adwords Data5 ===> " + timeSegment + " --- " + productSegment + " --- " + filter);
        String accountIdStr = getFromMultiValueMap(request, "accountId");
        Date startDate = DateUtils.getStartDate(getFromMultiValueMap(request, "startDate"));
        Date endDate = DateUtils.getEndDate(getFromMultiValueMap(request, "endDate"));
        String fieldsOnly = getFromMultiValueMap(request, "fieldsOnly");
        String widgetIdStr = getFromMultiValueMap(request, "widgetId");
        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
            Integer widgetId = Integer.parseInt(widgetIdStr);
            TabWidget widget = uiService.getWidgetById(widgetId);

            String widgetProductSegment = widget.getProductSegment();
            String widgetTimeSegment = widget.getTimeSegment();
            String widgetNetworkType = widget.getNetworkType();

            if (widgetNetworkType != null && !widgetNetworkType.isEmpty() && !widgetNetworkType.equalsIgnoreCase("none") && !widgetNetworkType.equalsIgnoreCase("undefined")) {
                filter = widgetNetworkType;
            }
            if (widgetProductSegment != null && !widgetProductSegment.isEmpty() && !widgetProductSegment.equalsIgnoreCase("none") && !widgetProductSegment.equalsIgnoreCase("undefined")) {
                productSegment = widgetProductSegment;
            }
            if (widgetTimeSegment != null && !widgetTimeSegment.isEmpty() && !widgetTimeSegment.equalsIgnoreCase("none") && !widgetTimeSegment.equalsIgnoreCase("undefined")) {
                timeSegment = widgetTimeSegment;
            }
            if (widget.getDateRangeName() != null && !widget.getDateRangeName().isEmpty()) {
                if (widget.getDateRangeName().equalsIgnoreCase("custom")) {
                    startDate = DateUtils.getStartDate(widget.getCustomStartDate());
                    endDate = DateUtils.getEndDate(widget.getCustomEndDate());
                } else if (!widget.getDateRangeName().equalsIgnoreCase("custom") && !widget.getDateRangeName().equalsIgnoreCase("select date duration") && !widget.getDateRangeName().equalsIgnoreCase("none")) {
                    Map<String, Date> dateRange = getCustomDate(widget.getDateRangeName(), widget.getLastNdays(), widget.getLastNweeks(), widget.getLastNmonths(), widget.getLastNyears(), endDate);
                    startDate = dateRange.get("startDate");
                    endDate = dateRange.get("endDate");
                }
            }
        }
        System.out.println("Adwords Data5 ===> " + timeSegment + " --- " + productSegment + " --- " + filter);

        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        String adwordsAccountId = getAccountId(accountProperty, "adwordsAccountId");
        List<Map<String, Object>> data = adwordsService.getAdwordsReport(dataSetReportName, startDate, endDate, adwordsAccountId, timeSegment, productSegment, filter);
        System.out.println("Adwords Data ===> " + data);
        if (data == null) {
            List<ColumnDef> columnDefs = new ArrayList<>();
            Map returnMap = new HashMap();
            returnMap.put("columnDefs", columnDefs);
            returnMap.put("data", data);
            return returnMap;
        }
        if (dataSetReportName.equalsIgnoreCase("geoPerformance")) {
            for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                Object cityCriteria = dataMap.get("city");
                Object regionCriteria = dataMap.get("region");
                Object countryCriteria = dataMap.get("countryTerritory");
                try {
                    if (cityCriteria != null) {
                        Integer criteriaId = Integer.parseInt(cityCriteria + "");
                        AdwordsCriteria criteria = uiService.getAdwordsCriteria(criteriaId);
                        if (criteria != null) {
                            dataMap.put("cityName", criteria.getCriteriaName());
                        }
                    }
                } catch (NumberFormatException e) {

                }
                try {
                    if (regionCriteria != null) {
                        Integer criteriaId = Integer.parseInt(regionCriteria + "");
                        AdwordsCriteria criteria = uiService.getAdwordsCriteria(criteriaId);
                        if (criteria != null) {
                            dataMap.put("regionName", criteria.getCriteriaName());
                        }
                    }
                } catch (NumberFormatException e) {

                }
                try {
                    if (countryCriteria != null) {
                        Integer criteriaId = Integer.parseInt(countryCriteria + "");
                        AdwordsCriteria criteria = uiService.getAdwordsCriteria(criteriaId);
                        if (criteria != null) {
                            dataMap.put("countryName", criteria.getCriteriaName());
                        }
                    }
                } catch (NumberFormatException e) {

                }

            }
        }
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            List<String> costFields = Arrays.asList(new String[]{"avgCPC", "cost", "costConv"});
            for (Iterator<String> iterator1 = costFields.iterator(); iterator1.hasNext();) {
                String costField = iterator1.next();
                Object cost = dataMap.get(costField);
                if (cost != null) {
                    dataMap.put(costField, covertAdwordsCost(cost));
                }
            }
        }
        Map returnMap = new HashMap();
        if (data == null) {
            return null;
        }

        List<ColumnDef> columnDefs = getColumnDefObject(data);
        returnMap.put("columnDefs", columnDefs);
//        if (fieldsOnly != null) {
//            return returnMap;
//        }
        returnMap.put("data", data);
        return returnMap;
    }

    private Double covertAdwordsCost(Object costData) {
        if (costData == null) {
            return null;
        }
        String costDataStr = costData + "";
        Double cost = Double.parseDouble(costDataStr);
        if (cost > 0) {
            return cost / 1000000;
        }
        return 0D;
    }

    @RequestMapping(value = "testAdwords", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object testAdwords(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> data = adwordsService.getAdwordsReport(request.getParameter("reportName"), DateUtils.get30DaysBack(), new Date(), "827-719-8225", request.getParameter("timeSegment"), request.getParameter("productSegment"), request.getParameter("filter"));
        Map returnMap = new HashMap();
        String fieldsOnly = request.getParameter("fieldsOnly");
        List<ColumnDef> columnDefs = getColumnDefObject(data);
        returnMap.put("columnDefs", columnDefs);
        returnMap.put("data", data);
        return returnMap;
    }

    @RequestMapping(value = "testGa", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object testGa(HttpServletRequest request, HttpServletResponse response) {
        return gaService.getGaReport(request.getParameter("reportName"), "112725239", DateUtils.get30DaysBack(), new Date(), request.getParameter("timeSegment"), request.getParameter("productSegment"), null);
    }

    //linkedin 
    Map getLinkedInData(MultiValueMap request, HttpServletResponse response) {

        String dataSetId = getFromMultiValueMap(request, "dataSetId");
        String dataSetReportName = getFromMultiValueMap(request, "dataSetReportName");
        String timeSegment = getFromMultiValueMap(request, "timeSegment");
        String productSegment = getFromMultiValueMap(request, "productSegment");

//        if (timeSegment == null) {
//            timeSegment = "daily";
//        }
        Integer dataSetIdInt = null;
        DataSet dataSet = null;
        if (dataSetId != null) {
            try {
                dataSetIdInt = Integer.parseInt(dataSetId);
            } catch (Exception e) {

            }
            if (dataSetIdInt != null) {
                dataSet = uiService.readDataSet(dataSetIdInt);
            }
            if (dataSet != null) {
                dataSetReportName = (dataSetReportName == null) ? dataSet.getReportName() : dataSetReportName;
                timeSegment = (timeSegment == null || timeSegment.isEmpty()) ? dataSet.getTimeSegment() : timeSegment;
            }
        }
        String accountIdStr = getFromMultiValueMap(request, "accountId");
        Date startDate = DateUtils.getStartDate(getFromMultiValueMap(request, "startDate"));
        Date endDate = DateUtils.getEndDate(getFromMultiValueMap(request, "endDate"));
        String widgetIdStr = getFromMultiValueMap(request, "widgetId");

        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
            Integer widgetId = Integer.parseInt(widgetIdStr);
            TabWidget widget = uiService.getWidgetById(widgetId);
            if (widget.getDateRangeName() != null && !widget.getDateRangeName().isEmpty()) {
                if (widget.getDateRangeName().equalsIgnoreCase("custom")) {
                    startDate = DateUtils.getStartDate(widget.getCustomStartDate());
                    endDate = DateUtils.getEndDate(widget.getCustomEndDate());
                } else if (!widget.getDateRangeName().equalsIgnoreCase("custom") && !widget.getDateRangeName().equalsIgnoreCase("select date duration") && !widget.getDateRangeName().equalsIgnoreCase("none")) {
                    Map<String, Date> dateRange = getCustomDate(widget.getDateRangeName(), widget.getLastNdays(), widget.getLastNweeks(), widget.getLastNmonths(), widget.getLastNyears(), endDate);
                    startDate = dateRange.get("startDate");
                    endDate = dateRange.get("endDate");
                }
            }
        }

//        String accessToken = "AQVrr3w94F9NPdypSkVL_mY1hpRBlbg0DjsAymBxVnIvKw91gdapkEZt-hIUdzC34AZfgShbH17iWw0ef8VtT7gSKQsQ8mtPt2d9w_soy5FnKJaZgSHiT-Ug9MnzmB3fjlR2_tc6OoGmgeaMEuAHV3Yvnb-gzRg2TC4Aez2pUNR9jiv5WWM";
        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        String linkedinAccountId = getAccountId(accountProperty, "linkedinAccountId");
        try {
            Long linkedInaccountId = Long.parseLong(linkedinAccountId);

            List<Map<String, Object>> data = linkedinService.get(linkedInaccountId, dataSetReportName,
                    startDate, endDate, timeSegment, productSegment);
            log.debug(data);
            Map returnMap = new HashMap();
            List<ColumnDef> columnDefs = getColumnDefObject(data);
            returnMap.put("columnDefs", columnDefs);

            returnMap.put("data", data);
            return returnMap;
        } catch (NumberFormatException ex) {
            Map linkedInData = new HashMap();
            List<ColumnDef> columnDefs = new ArrayList();
            List<Map<String, String>> returnData = new ArrayList<>();
            linkedInData.put("columnDefs", columnDefs);
            linkedInData.put("data", returnData);
            return linkedInData;
        }

    }

    Object getFbData(final MultiValueMap<String, String> request, HttpServletResponse response) {
        String dataSetId = getFromMultiValueMap(request, "dataSetId");
        String dataSetReportName = getFromMultiValueMap(request, "dataSetReportName");
        String timeSegment = getFromMultiValueMap(request, "timeSegment");
        String productSegment = getFromMultiValueMap(request, "productSegment");
//        if (timeSegment == null) {
//            timeSegment = "daily";
//        }
//        if (productSegment == null) {
//            productSegment = "none";
//        }
        Integer dataSetIdInt = null;
        DataSet dataSet = null;
        if (dataSetId != null) {
            try {
                dataSetIdInt = Integer.parseInt(dataSetId);
            } catch (Exception e) {

            }
            if (dataSetIdInt != null) {
                dataSet = uiService.readDataSet(dataSetIdInt);
            }
            if (dataSet != null) {
                dataSetReportName = (dataSetReportName == null || dataSetReportName.isEmpty()) ? dataSet.getReportName() : dataSetReportName;
                timeSegment = (timeSegment == null || timeSegment.isEmpty()) ? dataSet.getTimeSegment() : timeSegment;
                productSegment = (productSegment == null || productSegment.isEmpty()) ? dataSet.getProductSegment() : productSegment;
            }
        }
        String accountIdStr = getFromMultiValueMap(request, "accountId");
        Date startDate = DateUtils.getStartDate(getFromMultiValueMap(request, "startDate"));
        Date endDate = DateUtils.getEndDate(getFromMultiValueMap(request, "endDate"));
        String fieldsOnly = getFromMultiValueMap(request, "fieldsOnly");
        String widgetIdStr = getFromMultiValueMap(request, "widgetId");
        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
            Integer widgetId = Integer.parseInt(widgetIdStr);
            TabWidget widget = uiService.getWidgetById(widgetId);
            if (widget.getDateRangeName() != null && !widget.getDateRangeName().isEmpty()) {
                if (widget.getDateRangeName().equalsIgnoreCase("custom")) {
                    startDate = DateUtils.getStartDate(widget.getCustomStartDate());
                    endDate = DateUtils.getEndDate(widget.getCustomEndDate());
                } else if (!widget.getDateRangeName().equalsIgnoreCase("custom") && !widget.getDateRangeName().equalsIgnoreCase("select date duration") && !widget.getDateRangeName().equalsIgnoreCase("none")) {
                    Map<String, Date> dateRange = getCustomDate(widget.getDateRangeName(), widget.getLastNdays(), widget.getLastNweeks(), widget.getLastNmonths(), widget.getLastNyears(), endDate);
                    startDate = dateRange.get("startDate");
                    endDate = dateRange.get("endDate");
                }
            }
        }
        Integer accountId = null;
        try {
            accountId = Integer.parseInt(accountIdStr);
        } catch (NumberFormatException e) {
            List<ColumnDef> columnDefs = new ArrayList<>();
            List<Map<String, Object>> data = new ArrayList<>();
            Map returnMap = new HashMap();
            returnMap.put("columnDefs", columnDefs);
            returnMap.put("data", data);
            return returnMap;
        }
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        String facebookAccountId = getAccountId(accountProperty, "facebookAccountId");
        String facebookOrganicAccountId = getAccountId(accountProperty, "facebookOrganicAccountId");
        Long facebookAccountIdInt = Long.parseLong(facebookAccountId);
        Long facebookOrganicAccountIdInt = null;
        if (facebookOrganicAccountId != null) {
            facebookOrganicAccountIdInt = Long.parseLong(facebookOrganicAccountId);
        }

        //code to get access token from settings
//        List<Settings> facebookAccessToken = settingsService.getProperty("facebookAccessToken");
//        String accessToken =SettingsProperty.getSettingsProperty(facebookAccessToken, "facebookAccessToken");
//        String accessToken = "EAAUAycrj0GsBAM3EgwLcQjz5zywESZBpHN76cERZCaxEZC9ZAzMjRzRxIznWM3u8s4DBwUvhMaQAGglDOIa9tSV7ZCVf9ZBajV9aA6khaCRmEZAQhIHUInBVYZBZAT5nycwniZCozuLcjhTm0eW5tAUxIugmvxszsivmh5ZClzuMZApZBJxd0RZBIDk1r0";
        log.debug("Report Name ---- " + dataSetReportName);
        log.debug("Account Id ---- " + facebookAccountIdInt);
        log.debug("Time segment ---- " + timeSegment);
        log.debug("Start Date ---- " + startDate);
        List<Map<String, Object>> data = facebookService.get(dataSetReportName, facebookAccountIdInt,
                facebookOrganicAccountIdInt, startDate, endDate, timeSegment, productSegment);
//        Date startDate = DateUtils.getSixMonthsBack(new Date()); // 1348734005171064L
//        Date endDate = new Date();
//        List<Map<String, String>> data = facebookService.get(accessToken, "accountPerformance", 1348731135171351L, startDate, endDate, "daily");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = getColumnDefObject(data);
        returnMap.put("columnDefs", columnDefs);
        returnMap.put("data", data);
        return returnMap;
    }

    List<Map<String, Object>> getTwitterData(MultiValueMap<String, String> request, HttpServletResponse response) {
        String dataSetId = getFromMultiValueMap(request, "dataSetId");
        String dataSetReportName = getFromMultiValueMap(request, "dataSetReportName");
        String timeSegment = getFromMultiValueMap(request, "timeSegment");
        String productSegment = getFromMultiValueMap(request, "productSegment");
        if (timeSegment == null) {
            timeSegment = "daily";
        }

        Integer dataSetIdInt = null;
        DataSet dataSet = null;
        if (dataSetId != null) {
            try {
                dataSetIdInt = Integer.parseInt(dataSetId);
            } catch (Exception e) {

            }
            if (dataSetIdInt != null) {
                dataSet = uiService.readDataSet(dataSetIdInt);
            }
            if (dataSet != null) {
                dataSetReportName = dataSet.getReportName();
                timeSegment = dataSet.getTimeSegment();
            }
        }
        String accountIdStr = getFromMultiValueMap(request, "accountId");
        Date startDate = DateUtils.getStartDate(getFromMultiValueMap(request, "startDate"));

        Date endDate = DateUtils.getEndDate(getFromMultiValueMap(request, "endDate"));
        String fieldsOnly = getFromMultiValueMap(request, "fieldsOnly");

        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        String twitterAccountId = getAccountId(accountProperty, "twitterAccountId");
        String twitterScreenName = getAccountId(accountProperty, "twitterScreenName");
        String twitterOauthToken = getAccountId(accountProperty, "twitterOauthToken");
        String twitterOauthSignature = getAccountId(accountProperty, "twitterOauthSignature");
        String twitterOauthNonce = getAccountId(accountProperty, "twitterOauthNonce");
        String twitterOauthConsumerKey = getAccountId(accountProperty, "twitterConsumerKey");

        try {
            Long twitterOganicAccountId = Long.parseLong(twitterAccountId);
            List<Map<String, Object>> twitterReport = twitterService.get(dataSetReportName, twitterAccountId, twitterScreenName,
                    twitterOauthToken, twitterOauthSignature, twitterOauthNonce, twitterOauthConsumerKey, startDate, endDate, timeSegment, productSegment);
            return twitterReport;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String getAccountId(List<Property> accountProperty, String propertyName) {
        String propertyAccountId = null;
        for (Iterator<Property> iterator = accountProperty.iterator(); iterator.hasNext();) {
            Property property = iterator.next();
            if (property.getPropertyName().equalsIgnoreCase(propertyName)) {
                propertyAccountId = property.getPropertyValue();
            }
        }
        return propertyAccountId;
    }

    private List<Map<String, Object>> getBingData(MultiValueMap<String, String> valueMap, HttpServletRequest request, HttpServletResponse response) {
        try {
            String accountIdStr = getFromMultiValueMap(valueMap, "accountId");
            Integer accountId = Integer.parseInt(accountIdStr);
            Account account = userService.getAccountId(accountId);
            List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());

            for (Iterator<Property> iterator = accountProperty.iterator(); iterator.hasNext();) {
                Property property = iterator.next();
                List<String> valueList = new ArrayList();
                valueList.add(property.getPropertyValue());
                valueMap.put(property.getPropertyName(), valueList);
            }
            String dataSetId = getFromMultiValueMap(valueMap, "dataSetId");
            String dataSetReportName = getFromMultiValueMap(valueMap, "dataSetReportName");
            String timeSegment = getFromMultiValueMap(valueMap, "timeSegment");
            String productSegment = getFromMultiValueMap(valueMap, "productSegment");
//            if (timeSegment == null) {
//                timeSegment = "daily";
//            }
//            if (productSegment == null) {
//                productSegment = "none";
//            }
            Integer dataSetIdInt = null;
            DataSet dataSet = null;
            if (dataSetId != null) {
                try {
                    dataSetIdInt = Integer.parseInt(dataSetId);
                } catch (Exception e) {

                }
                if (dataSetIdInt != null) {
                    dataSet = uiService.readDataSet(dataSetIdInt);
                }
                if (dataSet != null) {
                    dataSetReportName = (dataSetReportName == null || dataSetReportName.isEmpty()) ? dataSet.getReportName() : dataSetReportName;
                    timeSegment = (timeSegment == null || timeSegment.isEmpty()) ? dataSet.getTimeSegment() : timeSegment;
                    productSegment = (productSegment == null || productSegment.isEmpty()) ? dataSet.getProductSegment() : productSegment;
                }
            }
            valueMap.put("timeSegment", Arrays.asList(timeSegment));
            valueMap.put("productSegment", Arrays.asList(productSegment));
            valueMap.put("dataSetReportName", Arrays.asList(dataSetReportName));

            String url = "../dbApi/admin/bing/getData";
            Integer port = 80;
            if (request != null) {
                port = request.getServerPort();
            }

            String localUrl = "http://localhost/";
            if (request != null) {
                localUrl = request.getScheme() + "://" + request.getServerName() + ":" + port + "/";
            }
            log.debug("UR:" + url);
            if (url.startsWith("../")) {
                url = url.replaceAll("\\.\\./", localUrl);
            }
            log.debug("url: " + url);
            log.debug("valuemap: " + valueMap);
            String data = Rest.getData(url, valueMap);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            List dataList = JsonSimpleUtils.toList((JSONArray) jsonObj);
            return dataList;
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Map getSqlData(MultiValueMap<String, String> valueMap, HttpServletRequest request, HttpServletResponse response) {
        try {
            String accountIdStr = getFromMultiValueMap(valueMap, "accountId");
            Integer accountId = Integer.parseInt(accountIdStr);
            Account account = userService.getAccountId(accountId);
            List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());

            for (Iterator<Property> iterator = accountProperty.iterator(); iterator.hasNext();) {
                Property property = iterator.next();
                List<String> valueList = new ArrayList();
                valueList.add(property.getPropertyValue());
                valueMap.put(property.getPropertyName(), valueList);
            }
            String dataSetId = getFromMultiValueMap(valueMap, "dataSetId");
            String dataSetReportName = getFromMultiValueMap(valueMap, "dataSetReportName");
            String timeSegment = getFromMultiValueMap(valueMap, "timeSegment");
            String productSegment = getFromMultiValueMap(valueMap, "productSegment");
            String joinDataSetId = getFromMultiValueMap(valueMap, "joinDataSetId");
            valueMap.put("joinDataSetId", Arrays.asList(joinDataSetId));

//            if (timeSegment == null) {
//                timeSegment = "daily";
//            }
//            if (productSegment == null) {
//                productSegment = "none";
//            }
            Integer dataSetIdInt = null;
            DataSet dataSet = null;
            if (dataSetId != null) {
                try {
                    dataSetIdInt = Integer.parseInt(dataSetId);
                } catch (Exception e) {

                }
                if (dataSetIdInt != null) {
                    dataSet = uiService.readDataSet(dataSetIdInt);
                }
                if (dataSet != null) {
                    dataSetReportName = (dataSetReportName == null || dataSetReportName.isEmpty()) ? dataSet.getReportName() : dataSetReportName;
                    timeSegment = (timeSegment == null || timeSegment.isEmpty()) ? dataSet.getTimeSegment() : timeSegment;
                    productSegment = (productSegment == null || productSegment.isEmpty()) ? dataSet.getProductSegment() : productSegment;
                }
            }
            valueMap.put("timeSegment", Arrays.asList(timeSegment == null ? "" : timeSegment));
            valueMap.put("productSegment", Arrays.asList(productSegment == null ? "" : productSegment));
            valueMap.put("dataSetReportName", Arrays.asList(dataSetReportName == null ? "" : dataSetReportName));

            String url = "../dbApi/admin/dataSet/getData";
            Integer port = 80;
            if (request != null) {
                port = request.getServerPort();
            }

            String localUrl = "http://localhost/";
            if (request != null) {
                localUrl = request.getScheme() + "://" + request.getServerName() + ":" + port + "/";
            }
            log.debug("UR:" + url);
            if (url.startsWith("../")) {
                url = url.replaceAll("\\.\\./", localUrl);
            }
            log.debug("url: " + url);
            log.debug("valuemap: " + valueMap);

            String query = getFromMultiValueMap(valueMap, "query");

            String dashboardFilter = getFromMultiValueMap(valueMap, "dashboardFilter");

            if (isNullOrEmpty(dashboardFilter)) {
                dashboardFilter = "";
            }

            dashboardFilter = getQueryFilter(dashboardFilter);

            if (!isNullOrEmpty(dashboardFilter.trim())) {
                if (query != null) {
                    if (query.indexOf("where") > 0) {
                        query += " " + dashboardFilter;
                    } else {
                        query += " where " + dashboardFilter;
                    }
                }
            }

            System.out.println("Query ===> " + query);

            try {
                query = URLEncoder.encode(query, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
            }
            valueMap.put("query", Arrays.asList(query));
            try {
                dashboardFilter = URLEncoder.encode(dashboardFilter, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
            }
            valueMap.put("dashboardFilter", Arrays.asList(dashboardFilter));

            String data = Rest.getData(url, valueMap);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(data);
            Map returnData = JsonSimpleUtils.toMap((JSONObject) jsonObj);
            return returnData;
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<ColumnDef> getColumnDefObject(List<Map<String, Object>> data) {
        log.debug("Calling of getColumnDef function in ProxyController class");
        List<ColumnDef> columnDefs = new ArrayList<>();
        if (data == null) {
            return null;
        }
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            for (Map.Entry<String, Object> entrySet : mapData.entrySet()) {
                String key = entrySet.getKey();
                DefaultFieldProperties fieldProperties = uiService.getDefaultFieldProperties(key);
                if (fieldProperties != null) {
                    ColumnDef columnDef = new ColumnDef(key, fieldProperties.getDataType() == null ? "string" : fieldProperties.getDataType(), fieldProperties.getDisplayName(), fieldProperties.getAgregationFunction(), fieldProperties.getDisplayFormat());
                    if (fieldProperties.getDataType() != null && fieldProperties.getDataType().equalsIgnoreCase("date")) {
                        columnDef.setDataFormat(fieldProperties.getDataFormat());
                    }
                    if (fieldProperties.getDataFormat() != null) {
                        columnDef.setDataFormat(fieldProperties.getDataFormat());
                    }
                    System.out.println("DAta Format ===> " + fieldProperties.getDataFormat());
                    columnDefs.add(columnDef);
                } else {
                    Object value = entrySet.getValue();
                    String valueString = value + "";
                    valueString = valueString.replaceAll("^\"|\"$", "");
                    if (NumberUtils.isNumber(valueString)) {
                        columnDefs.add(new ColumnDef(key, "number", key));
                    } else if (DateUtils.convertToDate(valueString) != null) {
                        columnDefs.add(new ColumnDef(key, "date", key));
                    } else if (valueString.indexOf("%") > 0) {
                        ColumnDef columnDef = new ColumnDef(key, "number", key);
                        columnDef.setDataFormat("%");
                        columnDefs.add(columnDef);
                    } else {
                        columnDefs.add(new ColumnDef(key, "string", key));
                    }
                }
            }
            return columnDefs;
        }
        return columnDefs;
    }

    private List<ColumnDef> getColumnDef(List<Map<String, String>> data) {

        List<ColumnDef> columnDefs = new ArrayList<>();
        if (data == null) {
            return null;
        }
        for (Iterator<Map<String, String>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, String> mapData = iterator.next();
            for (Map.Entry<String, String> entrySet : mapData.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                value = value.replaceAll("^\"|\"$", "");
                if (NumberUtils.isNumber(value)) {
                    columnDefs.add(new ColumnDef(key, "number", key));
                } else if (DateUtils.convertToDate(value) != null) {
                    columnDefs.add(new ColumnDef(key, "date", key));
                } else {
                    columnDefs.add(new ColumnDef(key, "string", key));
                }
                // columnDefs.add(new ColumnDef(key, "string", key));
            }
            return columnDefs;
        }
        return columnDefs;
    }

    @RequestMapping(value = "getJson", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getJson(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Calling of getJson function in ProxyController class");
        String url = request.getParameter("url");
        String query = request.getParameter("query");
        log.debug("QUERY FROM BROWSER " + query);
        String dealerId = request.getParameter("dealerId");
        Map<String, String> dealerAccountDetails = dealerService.getDealerAccountDetails(dealerId);
        Integer port = request.getServerPort();
        String localUrl = request.getScheme() + "://" + request.getServerName() + ":" + port + "/";

        if (url.startsWith("../")) {
            url = url.replaceAll("\\.\\./", localUrl);
        }

        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entrySet : dealerAccountDetails.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            valueMap.put(key, Arrays.asList(value));
        }
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> entrySet : parameterMap.entrySet()) {
                String key = entrySet.getKey();
                String[] value = entrySet.getValue();
                for (int i = 0; i < value.length; i++) {
                    value[i] = URLEncoder.encode(value[i], "UTF-8");
                }
                valueMap.put(key, Arrays.asList(value));
            }
            String data = Rest.getData(url, valueMap);
            return data;
        } catch (Exception ex) {
            log.error("Exception in getJson Function: " + ex);
        }
        return null;
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public @ResponseBody
    void get(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Calling of get function in ProxyController class");
        String url = request.getParameter("url");
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entrySet : parameterMap.entrySet()) {
            try {
                String key = entrySet.getKey();
                String[] value = entrySet.getValue();
                MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
                valueMap.put(key, Arrays.asList(value));
                String data = Rest.getData(url, valueMap);
                response.getOutputStream().write(data.getBytes());
            } catch (IOException ex) {
                log.error("IOException in get Function :" + ex);
            }
        }
    }

    @RequestMapping(value = "testXls/{tabId}", method = RequestMethod.GET)
    public @ResponseBody
    void xlsDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        log.debug("Calling of xlsDownload function in ProxyController class");
        OutputStream out = null;
        try {
            String dealerId = request.getParameter("dealerId");
            Map<String, String> dealerAccountDetails = dealerService.getDealerAccountDetails(dealerId);
            MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
            for (Map.Entry<String, String> entrySet : dealerAccountDetails.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                valueMap.put(key, Arrays.asList(value));
            }
            Map<String, String[]> parameterMap = request.getParameterMap();
            for (Map.Entry<String, String[]> entrySet : parameterMap.entrySet()) {
                String key = entrySet.getKey();
                String[] value = entrySet.getValue();
                valueMap.put(key, Arrays.asList(value));
            }
            Integer accountId = Integer.parseInt(dealerId);
            List<TabWidget> tabWidgets = uiService.getTabWidget(tabId, accountId);
            for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
                TabWidget tabWidget = iterator.next();
                try {
                    if (tabWidget.getDataSourceId() == null) {
                        continue;
                    }
                    String url = "../dashboard/admin/proxy/getData?";
//                    String url = "../admin/proxy/getData?";
                    log.debug("TYPE => " + tabWidget.getDataSourceId().getDataSourceType());
                    if (tabWidget.getDataSourceId().getDataSourceType().equalsIgnoreCase("sql")) {
                        url = "../dbApi/admin/dataSet/getData";
                        valueMap.put("username", Arrays.asList(tabWidget.getDataSourceId().getUserName()));
                        valueMap.put("password", Arrays.asList(tabWidget.getDataSourceId().getPassword()));
                        valueMap.put("query", Arrays.asList(URLEncoder.encode(tabWidget.getDataSetId().getQuery(), "UTF-8")));
                        valueMap.put("connectionUrl", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getConnectionString(), "UTF-8")));
                        valueMap.put("driver", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getSqlDriver(), "UTF-8")));
                    } else if (tabWidget.getDataSourceId().getDataSourceType().equalsIgnoreCase("csv")) {
                        url = "../dashboard/admin/csv/getData";
                        valueMap.put("connectionUrl", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getConnectionString(), "UTF-8")));
                        valueMap.put("driver", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getSqlDriver(), "UTF-8")));
                    } else if (tabWidget.getDataSourceId().getDataSourceType().equalsIgnoreCase("facebook")) {
                        url = "../dashboard/admin/proxy/getData?";
                    }
                    valueMap.put("dataSetId", Arrays.asList("" + tabWidget.getDataSetId().getId()));
                    valueMap.put("accountId", Arrays.asList(URLEncoder.encode(request.getParameter("accountId"), "UTF-8")));

                    Integer port = request.getServerPort();

                    String localUrl = request.getScheme() + "://" + request.getServerName() + ":" + port + "/";
                    log.debug("UR:" + url);
                    if (url.startsWith("../")) {
                        url = url.replaceAll("\\.\\./", localUrl);
                    }
                    log.debug("url: " + url);
                    log.debug("valuemap: " + valueMap);
                    String data = Rest.getData(url, valueMap);
                    JSONParser parser = new JSONParser();
                    Object jsonObj = parser.parse(data);
                    Map<String, Object> responseMap = JsonSimpleUtils.toMap((JSONObject) jsonObj);
                    List dataList = (List) responseMap.get("data");
                    tabWidget.setData(dataList);

                } catch (ParseException ex) {
                    log.error("Parse Exception in xlsDownload Function : " + ex);
                } catch (UnsupportedEncodingException ex) {
                    log.error("UnsupportedEncodingException in xlsDownload Function: " + ex);
                }
            }
            out = response.getOutputStream();

            CustomReportDesigner crd = new CustomReportDesigner();
            // crd.dynamicXlsDownload(tabWidgets, out);
        } catch (IOException ex) {
            log.error("IOException in xlsDownload Function: " + ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                log.error("Finally catches IOException in xlsDownload Function: " + ex);
            }
        }
    }

    @RequestMapping(value = "downloadReport/{reportId}", method = RequestMethod.GET)
    public @ResponseBody
    void downloadReport(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
        log.debug("Start Function of downloadReport");
        String dealerId = request.getParameter("dealerId");
        String exportType = request.getParameter("exportType");
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);

        Date startDate1 = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate1 = DateUtils.getEndDate(request.getParameter("endDate"));

        String start_date = month_date.format(startDate1);
        String end_date = month_date.format(endDate1);
        String selectDate;

        if (start_date.equalsIgnoreCase(end_date)) {
            selectDate = start_date;
        } else {
            selectDate = start_date.concat(" - " + end_date);
        }
        log.debug("EXport type ==> " + exportType);
        if (exportType == null || exportType.isEmpty()) {
            exportType = "pdf";
        }
        log.debug(" ===> " + exportType);
        Map<String, String> dealerAccountDetails = dealerService.getDealerAccountDetails(dealerId);
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entrySet : dealerAccountDetails.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            valueMap.put(key, Arrays.asList(value));
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entrySet : parameterMap.entrySet()) {
            String key = entrySet.getKey();
            String[] value = entrySet.getValue();
            valueMap.put(key, Arrays.asList(value));
        }

        //List<TabWidget> tabWidgets = uiService.getTabWidget(tabId);
        List<TabWidget> tabWidgets = new ArrayList<>();
        Report report = reportService.getReportById(reportId);
        String account = null;
        String product = "Dashience Report";
        List<ReportWidget> reportWidgets = reportService.getReportWidget(reportId);
        for (Iterator<ReportWidget> iterator = reportWidgets.iterator(); iterator.hasNext();) {
            ReportWidget reportWidget = iterator.next();
            TabWidget widget = reportWidget.getWidgetId();
            tabWidgets.add(widget);
        }

        for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
            TabWidget tabWidget = iterator.next();
            try {
                if (tabWidget.getDataSourceId() == null) {
                    continue;
                }
                Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
                Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));

                String url = propReader.readUrl(urlDownload) + "";
                log.debug("TYPE => " + tabWidget.getDataSourceId().getDataSourceType());
                if (tabWidget.getDataSourceId().getDataSourceType().equalsIgnoreCase("sql")) {
                    url = "../dbApi/admin/dataSet/getData";
                    valueMap.put("username", Arrays.asList(tabWidget.getDataSourceId().getUserName()));
                    valueMap.put("password", Arrays.asList(tabWidget.getDataSourceId().getPassword()));
                    valueMap.put("query", Arrays.asList(URLEncoder.encode(tabWidget.getDataSetId().getQuery(), "UTF-8")));
                    valueMap.put("connectionUrl", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getConnectionString(), "UTF-8")));
                    valueMap.put("driver", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getSqlDriver(), "UTF-8")));
                }
//                else if (tabWidget.getDataSourceId().getDataSourceType().equalsIgnoreCase("csv")) {
//                    url = "../dashboard/admin/csv/getData";
//                    valueMap.put("connectionUrl", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getConnectionString(), "UTF-8")));
//                } else if (tabWidget.getDataSourceId().getDataSourceType().equalsIgnoreCase("facebook")) {
//                    url = "../dashboard/admin/proxy/getData?";
//                }

                valueMap.put("widgetId", Arrays.asList("" + tabWidget.getId()));
                valueMap.put("dataSetId", Arrays.asList("" + tabWidget.getDataSetId().getId()));
                valueMap.put("accountId", Arrays.asList(URLEncoder.encode(request.getParameter("accountId"), "UTF-8")));

                if (tabWidget.getDateRangeName() != null && !tabWidget.getDateRangeName().isEmpty()) {
                    if (tabWidget.getDateRangeName().equalsIgnoreCase("custom")) {
                        startDate = DateUtils.getEndDate(tabWidget.getCustomStartDate());
                        endDate = DateUtils.getEndDate(tabWidget.getCustomEndDate());
                    } else if (!tabWidget.getDateRangeName().equalsIgnoreCase("custom") && !tabWidget.getDateRangeName().equalsIgnoreCase("select date duration") && !tabWidget.getDateRangeName().equalsIgnoreCase("none")) {
                        Map<String, Date> dateRange = getCustomDate(tabWidget.getDateRangeName(), tabWidget.getLastNdays(), tabWidget.getLastNweeks(), tabWidget.getLastNmonths(), tabWidget.getLastNyears(), endDate);
                        startDate = dateRange.get("startDate");
                        endDate = dateRange.get("endDate");
                    }
                }

                valueMap.put("startDate", Arrays.asList("" + URLEncoder.encode(DateUtils.dateToString(startDate, "MM/dd/yyyy"), "UTF-8")));
                valueMap.put("endDate", Arrays.asList("" + URLEncoder.encode(DateUtils.dateToString(endDate, "MM/dd/yyyy"), "UTF-8")));

                Integer port = request.getServerPort();

                int id = Integer.parseInt(request.getParameter("accountId"));
                account = userService.getAccountName(id);

                String localUrl = request.getScheme() + "://" + request.getServerName() + ":" + port + "/";
                log.debug("UR:" + url);
                if (url.startsWith("../")) {
                    url = url.replaceAll("\\.\\./", localUrl);
                }
                log.debug("url: " + url);
                log.debug("valuemap: " + valueMap);
                String data = Rest.getData(url, valueMap);
                JSONParser parser = new JSONParser();
                Object jsonObj = parser.parse(data);
                Map<String, Object> responseMap = JsonSimpleUtils.toMap((JSONObject) jsonObj);
                List dataList = (List) responseMap.get("data");
                tabWidget.setData(dataList);

            } catch (ParseException ex) {
                log.error("ParseException in downloadReport Function: " + ex);
            } catch (UnsupportedEncodingException ex) {
                log.error("UnsupportedEncodingException in downloadReport Function: " + ex);
            }
        }
        try {
            if (exportType.equalsIgnoreCase("pdf")) {
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-disposition", "attachment; filename=richanalytics.pdf");
                OutputStream out = response.getOutputStream();
                CustomReportDesigner crd = new CustomReportDesigner();
                crd.dynamicPdfTable(tabWidgets, account, product, selectDate, out);

            } else if (exportType.equalsIgnoreCase("ppt")) {
                response.setContentType("application/vnd.ms-powerpoint");
                response.setHeader("Content-disposition", "attachment; filename=richanalytics.pptx");
                OutputStream out = response.getOutputStream();
                CustomReportDesigner crd = new CustomReportDesigner();
                crd.dynamicPptTable(tabWidgets, out);
            }
        } catch (IOException ex) {
            log.error("IOException in downloadReport Function: " + ex);
        }
        log.debug("End Function of downloadReport");
    }

    @RequestMapping(value = "download/{tabId}", method = RequestMethod.GET)
    public @ResponseBody
    void download(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        String dealerId = request.getParameter("dealerId");
        String exportType = request.getParameter("exportType");
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);

        Date startDate1 = DateUtils.getStartDate(request.getParameter("startDate"));
        Date endDate1 = DateUtils.getEndDate(request.getParameter("endDate"));

        String start_date = month_date.format(startDate1);
        String end_date = month_date.format(endDate1);
        String selectDate;

        if (start_date.equalsIgnoreCase(end_date)) {
            selectDate = start_date;
        } else {
            selectDate = start_date.concat(" - " + end_date);
        }
        log.debug("Export type ==> " + exportType);
        if (exportType == null || exportType.isEmpty()) {
            exportType = "pdf";
        }
        log.debug(" ===> " + exportType);
        Map<String, String> dealerAccountDetails = dealerService.getDealerAccountDetails(dealerId);
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entrySet : dealerAccountDetails.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            valueMap.put(key, Arrays.asList(value));
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entrySet : parameterMap.entrySet()) {
            String key = entrySet.getKey();
            String[] value = entrySet.getValue();
            valueMap.put(key, Arrays.asList(value));
        }
        Integer accountId = Integer.parseInt(dealerId);
        List<TabWidget> tabWidgets = uiService.getTabWidget(tabId, accountId);
        String account = null;
        String product = "Analytics";
        for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
            TabWidget tabWidget = iterator.next();
            try {
                if (tabWidget.getDataSourceId() == null) {
                    continue;
                }
                Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
                Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
                String url = propReader.readUrl(urlDownload) + "";

                log.debug("TYPE => " + tabWidget.getDataSourceId().getDataSourceType());
                if (tabWidget.getDataSourceId().getDataSourceType().equalsIgnoreCase("sql")) {
                    url = "../dbApi/admin/dataSet/getData";
                    valueMap.put("username", Arrays.asList(tabWidget.getDataSourceId().getUserName()));
                    valueMap.put("password", Arrays.asList(tabWidget.getDataSourceId().getPassword()));
                    valueMap.put("query", Arrays.asList(URLEncoder.encode(tabWidget.getDataSetId().getQuery(), "UTF-8")));
                    valueMap.put("connectionUrl", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getConnectionString(), "UTF-8")));
                    valueMap.put("driver", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getSqlDriver(), "UTF-8")));
                }

                valueMap.put("widgetId", Arrays.asList("" + tabWidget.getId()));
                valueMap.put("dataSetId", Arrays.asList("" + tabWidget.getDataSetId().getId()));
                valueMap.put("accountId", Arrays.asList(URLEncoder.encode(request.getParameter("accountId"), "UTF-8")));

                if (tabWidget.getDateRangeName() != null && !tabWidget.getDateRangeName().isEmpty()) {
                    if (tabWidget.getDateRangeName().equalsIgnoreCase("custom")) {
                        startDate = DateUtils.getEndDate(tabWidget.getCustomStartDate());
                        endDate = DateUtils.getEndDate(tabWidget.getCustomEndDate());
                    } else if (!tabWidget.getDateRangeName().equalsIgnoreCase("custom") && !tabWidget.getDateRangeName().equalsIgnoreCase("select date duration") && !tabWidget.getDateRangeName().equalsIgnoreCase("none")) {
                        Map<String, Date> dateRange = getCustomDate(tabWidget.getDateRangeName(), tabWidget.getLastNdays(), tabWidget.getLastNweeks(), tabWidget.getLastNmonths(), tabWidget.getLastNyears(), endDate);
                        startDate = dateRange.get("startDate");
                        endDate = dateRange.get("endDate");
                    }
                }

                valueMap.put("startDate", Arrays.asList("" + URLEncoder.encode(DateUtils.dateToString(startDate, "MM/dd/yyyy"), "UTF-8")));
                valueMap.put("endDate", Arrays.asList("" + URLEncoder.encode(DateUtils.dateToString(endDate, "MM/dd/yyyy"), "UTF-8")));
                Integer port = request.getServerPort();

                int account_id = Integer.parseInt(request.getParameter("accountId"));
                account = userService.getAccountName(account_id);

                String localUrl = request.getScheme() + "://" + request.getServerName() + ":" + port + "/";
                log.debug("URL:" + url);
                if (url.startsWith("../")) {
                    url = url.replaceAll("\\.\\./", localUrl);
                }

                String data = Rest.getData(url, valueMap);
                JSONParser parser = new JSONParser();
                Object jsonObj = parser.parse(data);
                Map<String, Object> responseMap = JsonSimpleUtils.toMap((JSONObject) jsonObj);
                List dataList = (List) responseMap.get("data");
                tabWidget.setData(dataList);
            } catch (ParseException ex) {
                log.error("ParseException in download function: " + ex);
            } catch (UnsupportedEncodingException ex) {
                log.error("UnsupportedEncodingException in download function: " + ex);
            }
        }
        try {
            if (exportType.equalsIgnoreCase("pdf")) {
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-disposition", "attachment; filename=richanalytics.pdf");
                OutputStream out = response.getOutputStream();
                CustomReportDesigner crd = new CustomReportDesigner();
                crd.dynamicPdfTable(tabWidgets, account, product, selectDate, out);
            } else if (exportType.equalsIgnoreCase("ppt")) {
                response.setContentType("application/vnd.ms-powerpoint");
                response.setHeader("Content-disposition", "attachment; filename=richanalytics.pptx");
                OutputStream out = response.getOutputStream();
                CustomReportDesigner crd = new CustomReportDesigner();
                crd.dynamicPptTable(tabWidgets, out);
            }
        } catch (IOException ex) {
            log.error("IOException in download Function: " + ex);
        }
        log.debug("End Function of download");
    }

    public static void main(String argv[]) {
        String url = "../api/admin/paid/clicksImpressionsGraph";
        String localUrl = "Test";
        if (url.startsWith("../")) {
            url = url.replaceAll("\\.\\./", localUrl);
        }
        log.debug(url);
    }

    private static String getQueryFilter(String jsonDynamicFilter) {
        try {
            if (jsonDynamicFilter == null || jsonDynamicFilter.isEmpty()) {
                return "";
            }
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(jsonDynamicFilter);
            JSONObject json = (JSONObject) jsonObj;
            Map<String, Object> jsonToMap = (Map<String, Object>) JsonSimpleUtils.jsonToMap(json);
            List<String> queryString = new ArrayList<>();
            for (Map.Entry<String, Object> entry : jsonToMap.entrySet()) {
                String key = entry.getKey();
                List<String> value = (List<String>) entry.getValue();
                List<String> innerQuery = new ArrayList<>();
                for (Iterator<String> iterator = value.iterator(); iterator.hasNext();) {
                    String valueString = iterator.next();
                    innerQuery.add(key + " = " + "'" + valueString + "'");
                }
                String join = String.join(" OR ", innerQuery);
                // String output = key + " in " + join;
                queryString.add(" ( " + join + " ) ");
            }
            return String.join(" AND ", queryString);

        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }

}
