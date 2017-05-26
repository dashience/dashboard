/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

//import static com.visumbu.vb.admin.controller.EnliventController.processFollowings;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.admin.service.AdwordsService;
import com.visumbu.vb.admin.service.BingService;
import com.visumbu.vb.admin.service.DealerService;
import com.visumbu.vb.admin.service.FacebookService;
import com.visumbu.vb.admin.service.GaService;
import com.visumbu.vb.admin.service.ReportService;
import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.AdwordsCriteria;
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.DatasetColumns;
import com.visumbu.vb.model.Dealer;
import com.visumbu.vb.model.DefaultFieldProperties;
import com.visumbu.vb.model.Property;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.utils.ApiUtils;
import com.visumbu.vb.utils.CsvDataSet;
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.ParsePost;
import com.visumbu.vb.utils.PropertyReader;
import com.visumbu.vb.utils.Rest;
import com.visumbu.vb.utils.ShuntingYard;
import static com.visumbu.vb.utils.ShuntingYard.postfix;
import com.visumbu.vb.utils.XlsDataSet;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
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
    private UiDao uiDao;

    PropertyReader propReader = new PropertyReader();

    private final String urlDownload = "url.download";

    final static Logger log = Logger.getLogger(ProxyController.class);

    @RequestMapping(value = "getData", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getGenericData(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Calling of getGenericData function in ProxyController class");
        Map returnMap = new HashMap<>();
        String dataSourceType = request.getParameter("dataSourceType");
        String dataSetId = request.getParameter("dataSetId");
        Integer dataSetIdInt = null;
        if (dataSetId != null) {
            try {
                dataSetIdInt = Integer.parseInt(dataSetId);
            } catch (Exception e) {

            }
            if (dataSetIdInt != null) {
                DataSet dataSet = uiService.readDataSet(dataSetIdInt);
                dataSourceType = dataSet.getDataSourceId().getDataSourceType();
            }
        }
        System.out.println(dataSourceType);

        if (dataSourceType.equalsIgnoreCase("facebook") || dataSourceType.equalsIgnoreCase("instagram")) {
            returnMap = (Map) getFbData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("csv")) {
            returnMap = (Map) getCsvData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("adwords")) {
            returnMap = (Map) getAdwordsData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("analytics")) {
            returnMap = (Map) getAnalyticsData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("bing")) {
            returnMap = (Map) getBingData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("https")) {
            getHttpsData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("xls")) {
            returnMap = (Map) getXlsData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("pinterest")) {
            returnMap = (Map) getPinterestData(request, response);
        }

        String widgetIdStr = request.getParameter("widgetId");

        System.out.println("WIDGET ID " + widgetIdStr);
        if (widgetIdStr != null && !widgetIdStr.isEmpty()) {
            String queryFilter;
            Integer widgetId = Integer.parseInt(widgetIdStr);
            TabWidget tabWidget = uiService.getWidgetByIdAndDataSetId(widgetId, dataSetIdInt);
            if (tabWidget == null) {
                queryFilter = null;
                List<Map<String, Object>> data = (List<Map<String, Object>>) returnMap.get("data");
                List<Map<String, Object>> returnDataMap = ShuntingYard.applyExpression(data, queryFilter);
                returnMap.put("data", returnDataMap);
            } else {
                queryFilter = tabWidget.getQueryFilter();
                List<Map<String, Object>> data = (List<Map<String, Object>>) returnMap.get("data");
                List<Map<String, Object>> returnDataMap = ShuntingYard.applyExpression(data, queryFilter);
                returnMap.put("data", returnDataMap);
            }
        }
        return returnMap;
    }

    @RequestMapping(value = "getCsvData", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map getCsvData(HttpServletRequest request, HttpServletResponse response) {
        try {
            String connectionString = request.getParameter("connectionUrl");
            List<DatasetColumns> datasetColumnList = new ArrayList<>();
            String dataSetId = request.getParameter("dataSetId");
            Integer dataSetIdInt = null;
            if (dataSetId != null) {
                try {
                    dataSetIdInt = Integer.parseInt(dataSetId);
                } catch (Exception e) {

                }
                if (dataSetIdInt != null) {
                    datasetColumnList = uiDao.getDatasetColumnsByDatasetId(dataSetIdInt);
                }
            }
            if (connectionString == null) {

                DataSet dataSet = null;

                if (dataSetIdInt != null) {
                    dataSet = uiService.readDataSet(dataSetIdInt);
                }
                if (dataSet != null) {
                    connectionString = dataSet.getDataSourceId().getConnectionString();
                }
            }
            List<Map<String, Object>> dataSet = CsvDataSet.CsvDataSet(connectionString);

            System.out.println("datasetColumnList0 ----> " + datasetColumnList);

            if (datasetColumnList.size() > 0) {
                Map<String, List<Map<String, Object>>> csvData = addDerivedColumn(dataSet, null, datasetColumnList, null, null, null, null, null, null, null, null, null, null, null, null);

                Map returnCsvMap = new HashMap();
                Iterator it = csvData.entrySet().iterator();

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
                List<Map<String, Object>> columnsData = (List<Map<String, Object>>) columnData.get(0);
                List<ColumnDef> columns = (List<ColumnDef>) column.get(0);
                System.out.println("columnData ---> " + columnData);
                System.out.println("column ---> " + column);

                List<ColumnDef> columnDef = getColumnDefForDerivedColumnObject(columnsData, columns);
                returnCsvMap.put("columnDefs", columnDef);

                returnCsvMap.put("data", columnsData);
                return returnCsvMap;
            }
            Map returnMap = new HashMap<>();
            returnMap.put("data", dataSet);
            returnMap.put("columnDefs", getColumnDefObject(dataSet));
            return returnMap;
        } catch (IOException ex) {

        }
        return null;
    }

    @RequestMapping(value = "pinterest", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map getPinterestData(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("getPinterestData function");
        String reportName = request.getParameter("dataSetReportName");
        String dataSetId = request.getParameter("dataSetId");

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
                String fbUrl = "https://api.pinterest.com/v1/me/boards/?access_token=AZ3tcCqL10kF4AhAKjY4YHzUBwZJFLtfDUst59xD--hbPkA-ZQAAAAA&fields=id%2Cname%2Curl%2Ccounts%2Ccreated_at%2Ccreator%2Cdescription%2Creason";
                String data = Rest.getData(fbUrl);
                JSONParser parser = new JSONParser();
                if (data == null) {
                    return null;
                }
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
                List<DatasetColumns> datasetColumnList = uiDao.getDatasetColumnsByDatasetId(dataSetIdInt);
                System.out.println("datasetColumnList0 ----> " + datasetColumnList);
                if (datasetColumnList.size() > 0) {
                    List<Map<String, Object>> returnDataObj = new ArrayList<>();
                    for (Iterator<Map<String, Object>> iterator = fbData.iterator(); iterator.hasNext();) {
                        Map<String, Object> fbDataMap = iterator.next();
                        Map<String, Object> returnDataMap = new HashMap<>();
                        returnDataMap.put("name", fbDataMap.get("name") + "");
                        returnDataMap.put("description", fbDataMap.get("description") + "");
                        returnDataMap.put("pins_counts", ((Map) fbDataMap.get("counts")).get("pins") + "");
                        returnDataObj.add(returnDataMap);
                    }
                    Map<String, List<Map<String, Object>>> pinInterestData = addDerivedColumn(returnDataObj, null, datasetColumnList, null, null, null, null, null, null, null, null, null, null, null, null);
                    Map returnPinMap = new HashMap();
                    Iterator it = pinInterestData.entrySet().iterator();

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
                    List<Map<String, Object>> columnsData = (List<Map<String, Object>>) columnData.get(0);
                    List<ColumnDef> columns = (List<ColumnDef>) column.get(0);
                    System.out.println("columnData ---> " + columnData);
                    System.out.println("column ---> " + column);

                    List<ColumnDef> columnDefs = getColumnDefForDerivedColumnObject(columnsData, columns);
                    returnPinMap.put("columnDefs", columnDefs);

                    returnPinMap.put("data", columnsData);
                    return returnPinMap;
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
//                String fbUrl = "https://api.pinterest.com/v1/me/pins/?access_token=AZ3tcCqL10kF4AhAKjY4YHzUBwZJFLtfDUst59xD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl";
                String fbUrl = "https://api.pinterest.com/v1/me/pins/?access_token=AZb-_MWyppZRUUDgHauO9_3lCjwRFLtkrsSCIPVD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl%2Cattribution%2Cboard%2Ccolor%2Ccounts%2Ccreated_at%2Ccreator%2Coriginal_link%2Cmetadata%2Cmedia";
                String data = Rest.getData(fbUrl);
                if (data == null) {
                    return null;
                }
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

                List<DatasetColumns> datasetColumnList = uiDao.getDatasetColumnsByDatasetId(dataSetIdInt);
                System.out.println("datasetColumnList0 ----> " + datasetColumnList);
                if (datasetColumnList.size() > 0) {
                    List<Map<String, Object>> returnDataObj = new ArrayList<>();
                    for (Iterator<Map<String, Object>> iterator = fbData.iterator(); iterator.hasNext();) {
                        Map<String, Object> fbDataMap = iterator.next();
                        Map<String, Object> returnDataMap = new HashMap<>();
                        returnDataMap.put("note", fbDataMap.get("note") + "");
                        returnDataMap.put("url", fbDataMap.get("url") + "");
                        returnDataMap.put("created_at", fbDataMap.get("created_at") + "");
                        returnDataObj.add(returnDataMap);
                    }
                    Map<String, List<Map<String, Object>>> pinInterestData = addDerivedColumn(returnDataObj, null, datasetColumnList, null, null, null, null, null, null, null, null, null, null, null, null);
                    Map returnPinMap = new HashMap();
                    Iterator it = pinInterestData.entrySet().iterator();

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
                    List<Map<String, Object>> columnsData = (List<Map<String, Object>>) columnData.get(0);
                    List<ColumnDef> columns = (List<ColumnDef>) column.get(0);
                    System.out.println("columnData ---> " + columnData);
                    System.out.println("column ---> " + column);

                    List<ColumnDef> columnDefs = getColumnDefForDerivedColumnObject(columnsData, columns);
                    returnPinMap.put("columnDefs", columnDefs);

                    returnPinMap.put("data", columnsData);
                    return returnPinMap;
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
//                String fbUrl = "https://api.pinterest.com/v1/me/likes/?access_token=AS94T9w2BZ8g5z1i47YGkp7c6U88FLtkVt0cCntD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl%2Cattribution%2Cboard%2Ccolor%2Ccounts%2Ccreated_at%2Coriginal_link%2Cmetadata%2Cimage%2Cmedia%2Ccreator";
                String fbUrl = "https://api.pinterest.com/v1/me/?access_token=AZb-_MWyppZRUUDgHauO9_3lCjwRFLtkrsSCIPVD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Ccounts";
                String data = Rest.getData(fbUrl);
                JSONParser parser = new JSONParser();
//                Object jsonObj = parser.parse(data);
//                JSONObject json = (JSONObject) jsonObj;
//                Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
                Map returnMap = new HashMap<>();
//                List fbData = (List<Map<String,Object>>) jsonToMap.get("data");
                if (data == null) {
                    return null;
                }
                //////////////////////////
                JSONObject jsonArray = (JSONObject) parser.parse(data);

                Map<String, Object> myData = (Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) jsonArray).get("data")).get("counts");
                List<Map<String, Object>> twitterData = new ArrayList<>();

                Map<String, Object> myMapData = new HashMap<>();
                for (Map.Entry<String, Object> entry : myData.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    myMapData.put(key, value + "");
                }
                twitterData.add(myMapData);

                List<DatasetColumns> datasetColumnList = uiDao.getDatasetColumnsByDatasetId(dataSetIdInt);
                System.out.println("datasetColumnList0 ----> " + datasetColumnList);
                if (datasetColumnList.size() > 0) {

                    Map<String, List<Map<String, Object>>> pinInterestData = addDerivedColumn(twitterData, null, datasetColumnList, null, null, null, null, null, null, null, null, null, null, null, null);
                    Map returnPinMap = new HashMap();
                    Iterator it = pinInterestData.entrySet().iterator();

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
                    List<Map<String, Object>> columnsData = (List<Map<String, Object>>) columnData.get(0);
                    List<ColumnDef> columns = (List<ColumnDef>) column.get(0);
                    System.out.println("columnData ---> " + columnData);
                    System.out.println("column ---> " + column);

                    List<ColumnDef> columnDefs = getColumnDefForDerivedColumnObject(columnsData, columns);
                    returnPinMap.put("columnDefs", columnDefs);

                    returnPinMap.put("data", columnsData);
                    return returnPinMap;
                }
                List<ColumnDef> columnDefObject = getColumnDefObject(twitterData);

                /////////////////////////////////////////////////////////
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                System.out.println(twitterData);
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

//                fbData.lastIndexOf(jsonObj);
//                String likesCount = fbData.size() + "";
//                Map<String, String> boardsSize = new HashMap<>();
//                boardsSize.put("total_pin_likes", likesCount);
//                List<Map<String, String>> listData = new ArrayList<>();
//                listData.add(boardsSize);
//
                returnMap.put("columnDefs", columnDefObject);

                returnMap.put("data", twitterData);

//                List<DatasetColumns> datasetColumnList = uiDao.getDatasetColumnsByDatasetId(dataSetIdInt);
//                System.out.println("datasetColumnList0 ----> " + datasetColumnList);
//                if (datasetColumnList.size() > 0) {
//                    System.out.println("datasetColumnList1 ---> " + datasetColumnList);
//                    return data;
//                }
                return returnMap;
            } catch (ParseException ex) {
                java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
            }
//            return null;
        }

//        if (reportName.equalsIgnoreCase("getFollowingsCount")) {
//            ArrayList<String> followingsApiUrls = new ArrayList<>();
//
//            followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername");
//            followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername&cursor=Pz9Nakl5TXpveU56azVPVGMwTXpreU5ERXpOVGczTXpBNk9USXlNek0zTURVMU9UWTJNelk1TXpnek1WOUZ8ZDdiZWVlOWQ5NDZlMmE4MjgwZjcyZTAxY2YyM2NiZDVmOGE5MjllMWIwMWZjY2MxYThlNjAzMjg4Yzk1MjhiMg%3D%3D");
//            followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername&cursor=Pz9Nakl5TXpveE9EY3pNakU0TURNeE5Ua3pOelV6TURnNk9USXlNek0zTURVM01USTNOVE13TmpNeE5GOUZ8MTMzODE2NzlmMmYwNDMwYTc5NzU4MDg5YTE1OTU3Nzc4YTYzODFlNjFmY2YzN2ZkYzQyMzJkMDUwMzM5MWQ2MA%3D%3D");
//            followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername&cursor=Pz9Nakl5TXpveU5EVXlNelV5TnprMk1UYzFPVEF6TnpjNk9USXlNek0zTURVNE1UZzNOekUyT1RVM05WOUZ8MmU0YzRmZWYwYmZhM2JlZTRmZGM2MjM0NzViNWMzMTg5NDJjZmQ4YjljNGZhYjc1ZWIxN2QzMWQyZmY4ZmU2NA%3D%3D");
//            followingsApiUrls.add("https://api.pinterest.com/v1/me/followers/?access_token=AXCeGz6mwYDUI1eKrMbJ4PFKErp9FLtlVR4iV_hD--hbPkA-ZQAAAAA&fields=first_name%2Cid%2Clast_name%2Curl%2Caccount_type%2Cbio%2Ccounts%2Cimage%2Ccreated_at%2Cusername&cursor=Pz9Nakl5TkRveU5UZzJNRFV6TkRFd01URXpOVEUwTVRRNk9USXlNek0zTURVMU16WXhPVFk0TVRVNU4xOUp8YWQxZjViZjlmNTQ2YTg2YzI3NGU0MmQ0Nzg5ODVjMmVmNTY2MDRlZDZjZDZhMzAzNzE5MTU5YjQ1NWVkZjc5NQ%3D%3D");
//
////            String fbUrl = "https://api.pinterest.com/v1/me/likes/?access_token=AS94T9w2BZ8g5z1i47YGkp7c6U88FLtkVt0cCntD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl%2Cattribution%2Cboard%2Ccolor%2Ccounts%2Ccreated_at%2Coriginal_link%2Cmetadata%2Cimage%2Cmedia%2Ccreator";
//            int maxCount = 0;
//            for (int i = 0; i < followingsApiUrls.size(); i++) {
//                try {
//                    int getFollowingsCount = processFollowings(followingsApiUrls.get(i));
//                    maxCount = maxCount + getFollowingsCount;
//                } catch (ParseException ex) {
//                    java.util.logging.Logger.getLogger(EnliventController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            Map returnMap = new HashMap<>();
//            String pinterestFollowersCount = maxCount + "";
//            Map<String, String> followersSize = new HashMap<>();
//            followersSize.put("followings_count", pinterestFollowersCount);
//            List<Map<String, String>> listData = new ArrayList<>();
//            listData.add(followersSize);
//
//            List<ColumnDef> columnDefs = getColumnDef(listData);
//            returnMap.put("columnDefs", columnDefs);
//
//            returnMap.put("data", listData);
//            return returnMap;
//        }
//
//        if (reportName.equalsIgnoreCase("getTotalBoards")) {
//            try {
//                String fbUrl = "https://api.pinterest.com/v1/me/boards/?access_token=AZ3tcCqL10kF4AhAKjY4YHzUBwZJFLtfDUst59xD--hbPkA-ZQAAAAA&fields=id%2Cname%2Curl%2Ccounts%2Ccreated_at%2Ccreator%2Cdescription%2Creason";
//                String data = Rest.getData(fbUrl);
//                JSONParser parser = new JSONParser();
//                Object jsonObj = parser.parse(data);
//                JSONObject json = (JSONObject) jsonObj;
//                Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
//                Map returnMap = new HashMap<>();
//                List fbData = (List<Map>) jsonToMap.get("data");
//                fbData.lastIndexOf(jsonObj);
//                String boardsCount = fbData.size() + "";
//                Map<String, String> boardsSize = new HashMap<>();
//                boardsSize.put("total_boards", boardsCount);
//                List<Map<String, String>> listData = new ArrayList<>();
//                listData.add(boardsSize);
//
//                List<ColumnDef> columnDefs = getColumnDef(listData);
//                returnMap.put("columnDefs", columnDefs);
//
//                returnMap.put("data", listData);
//                return returnMap;
//            } catch (ParseException ex) {
//                java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
//            }
////            return null;
//        }
//        if (reportName.equalsIgnoreCase("getTotalPins")) {
//            try {
//                String fbUrl = "https://api.pinterest.com/v1/me/pins/?access_token=AZ3tcCqL10kF4AhAKjY4YHzUBwZJFLtfDUst59xD--hbPkA-ZQAAAAA&fields=id%2Clink%2Cnote%2Curl";
//                String data = Rest.getData(fbUrl);
//                JSONParser parser = new JSONParser();
//                Object jsonObj = parser.parse(data);
//                JSONObject json = (JSONObject) jsonObj;
//                Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
//                Map returnMap = new HashMap<>();
//                List fbData = (List<Map>) jsonToMap.get("data");
//                fbData.lastIndexOf(jsonObj);
//                String pinsCount = fbData.size() + "";
//                Map<String, String> pinsSize = new HashMap<>();
//                pinsSize.put("total_pins", pinsCount);
//                List<Map<String, String>> listData = new ArrayList<>();
//                listData.add(pinsSize);
//
//                List<ColumnDef> columnDefs = getColumnDef(listData);
//                returnMap.put("columnDefs", columnDefs);
//
//                returnMap.put("data", listData);
//                return returnMap;
//            } catch (ParseException ex) {
//                java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
//            }
////            return null;
//        }
        return null;

    }

//    public static int processFollowings(String fbUrl) throws ParseException {
//
//        String data = Rest.getData(fbUrl);
//        JSONParser parser = new JSONParser();
//        Object jsonObj = parser.parse(data);
//        JSONObject json = (JSONObject) jsonObj;
//        Map<String, Object> jsonToMap = JsonSimpleUtils.jsonToMap(json);
//        List fbData = (List<Map>) jsonToMap.get("data");
////        fbData.lastIndexOf(jsonObj);
//        int followingsCount = fbData.size();
//        System.out.println("-------------------------------------------");
//        System.out.println(followingsCount);
//        System.out.println("--------------------------------------");
//        return followingsCount;
//    }
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

    public Map getXlsData(HttpServletRequest request, HttpServletResponse response) {
        try {
            String dataSetId = request.getParameter("dataSetId");
            String dataSetReportName = request.getParameter("dataSetReportName");
            String connectionUrl = request.getParameter("connectionUrl");
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
                    connectionUrl = dataSet.getDataSourceId().getConnectionString();
                }
            }
            String accountIdStr = request.getParameter("accountId");
            Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
            Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
            String fieldsOnly = request.getParameter("fieldsOnly");

            String widgetIdStr = request.getParameter("widgetId");
            if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
                Integer widgetId = Integer.parseInt(widgetIdStr);
                System.out.println("widgetId ----> " + widgetId);
                TabWidget widget = uiService.getWidgetById(widgetId);

                String start = widget.getCustomStartDate();
                String end = widget.getCustomEndDate();

                if (start != null) {
                    startDate = DateUtils.getStartDate(start);
                }
                if (end != null) {
                    endDate = DateUtils.getEndDate(end);
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

    public void getHttpsData(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getParameter("url");
        String dataSetId = request.getParameter("dataSetId");
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
                    url = dataSet.getUrl();
                }
            }
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        String accountIdStr = request.getParameter("accountId");
        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String[]> entrySet : parameterMap.entrySet()) {
            String key = entrySet.getKey();
            String[] value = entrySet.getValue();
            valueMap.put(key, Arrays.asList(value));
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

    private Object getBingData(HttpServletRequest request, HttpServletResponse response) {
        String dataSetId = request.getParameter("dataSetId");
        String dataSetReportName = request.getParameter("dataSetReportName");
        String timeSegment = request.getParameter("timeSegment");
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
        String accountIdStr = request.getParameter("accountId");
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        System.out.println("startDate 1 ----> " + startDate);
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        System.out.println("endDate 1 ----> " + endDate);
        String fieldsOnly = request.getParameter("fieldsOnly");

        String widgetIdStr = request.getParameter("widgetId");
        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
            Integer widgetId = Integer.parseInt(widgetIdStr);
            System.out.println("widgetId ----> " + widgetId);
            TabWidget widget = uiService.getWidgetById(widgetId);

            String start = widget.getCustomStartDate();
            String end = widget.getCustomEndDate();

            if (start != null) {
                startDate = DateUtils.getStartDate(start);
                System.out.println("startDate 2----> " + startDate);
            }
            if (end != null) {
                endDate = DateUtils.getEndDate(end);
                System.out.println("endDate 2----> " + endDate);
            }
        }

        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        String bingAccountId = getAccountId(accountProperty, "bingAccountId");
        Long bingAccountIdLong = Long.parseLong(bingAccountId);
        Map returnMap = new HashMap();
//        List<Map<String, String>> data = bingService.get(dataSetReportName, bingAccountIdLong, startDate, endDate, timeSegment);
//        System.out.println(data);
//        List<ColumnDef> columnDefs = getColumnDef(data);
//        returnMap.put("columnDefs", columnDefs);
//        if (fieldsOnly != null) {
//            return returnMap;
//        }
//        returnMap.put("data", data);
        return returnMap;
    }

    private Object getAnalyticsData(HttpServletRequest request, HttpServletResponse response) {
        String dataSetId = request.getParameter("dataSetId");
        String dataSetReportName = request.getParameter("dataSetReportName");
        String timeSegment = request.getParameter("timeSegment");
        if (timeSegment != null && (timeSegment.isEmpty() || timeSegment.equalsIgnoreCase("undefined") || timeSegment.equalsIgnoreCase("null") || timeSegment.equalsIgnoreCase("none"))) {
            timeSegment = null;
        }
        String productSegment = request.getParameter("productSegment");
        if (productSegment != null && (productSegment.isEmpty() || productSegment.equalsIgnoreCase("undefined") || productSegment.equalsIgnoreCase("null") || productSegment.equalsIgnoreCase("none"))) {
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
                dataSetReportName = dataSet.getReportName();
                timeSegment = dataSet.getTimeSegment();
                productSegment = dataSet.getProductSegment();
            }
        }
        String accountIdStr = request.getParameter("accountId");
        String fieldsOnly = request.getParameter("fieldsOnly");

        String widgetIdStr = request.getParameter("widgetId");
        System.out.println("widgetID ---> " + widgetIdStr);
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        System.out.println("startDate 1 ----> " + startDate);
        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        System.out.println("endDate 1 ----> " + endDate);
        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
            Integer widgetId = Integer.parseInt(widgetIdStr);
            System.out.println("widgetId ----> " + widgetId);
            TabWidget widget = uiService.getWidgetById(widgetId);

            String start = widget.getCustomStartDate();
            String end = widget.getCustomEndDate();

            if (start != null) {
                startDate = DateUtils.getStartDate(start);
                System.out.println("startDate 2 ----> " + startDate);
            }
            if (end != null) {
                endDate = DateUtils.getEndDate(end);
                System.out.println("endDate 2 ----> " + endDate);
            }
        }

        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        String gaAccountId = getAccountId(accountProperty, "gaAccountId");
        String gaProfileId = getAccountId(accountProperty, "gaProfileId");
        System.out.println("Report Name " + dataSetReportName);
        System.out.println("datasetId ---->" + dataSetId);
        System.out.println("datasetIdInt ---->" + dataSetIdInt);
        Map<String, List<Map<String, Object>>> analyticsData = gaService.getGaReport(dataSetReportName, gaProfileId, startDate, endDate, timeSegment, productSegment, dataSetIdInt);
        List<DatasetColumns> datasetColumnList = uiDao.getDatasetColumnsByDatasetId(dataSetIdInt);

        System.out.println("datasetColumnList0 ----> " + datasetColumnList);
        if (datasetColumnList.size() > 0) {
            System.out.println("datasetColumnList1 ---> " + datasetColumnList);
            Map<String, List<Map<String, Object>>> data = addDerivedColumn(null, analyticsData, datasetColumnList, dataSetReportName, startDate, endDate, gaProfileId, null, null, null, null, timeSegment, productSegment, null, dataSetIdInt);
            return data;
        }

        return analyticsData;
    }

    private Object getAdwordsData(HttpServletRequest request, HttpServletResponse response) {
        String dataSetId = request.getParameter("dataSetId");
        String dataSetReportName = request.getParameter("dataSetReportName");
        String timeSegment = request.getParameter("timeSegment");
        String filter = request.getParameter("filter");
        String productSegment = request.getParameter("productSegment");
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
                productSegment = dataSet.getProductSegment();
                filter = dataSet.getNetworkType();
            }
        }
        if (timeSegment != null && (timeSegment.isEmpty() || timeSegment.equalsIgnoreCase("undefined") || timeSegment.equalsIgnoreCase("null") || timeSegment.equalsIgnoreCase("none"))) {
            timeSegment = null;
        }
        if (productSegment != null && (productSegment.isEmpty() || productSegment.equalsIgnoreCase("undefined") || productSegment.equalsIgnoreCase("null") || productSegment.equalsIgnoreCase("none"))) {
            productSegment = null;
        }
        if (filter != null && (filter.isEmpty() || filter.equalsIgnoreCase("undefined") || filter.equalsIgnoreCase("null") || filter.equalsIgnoreCase("none"))) {
            filter = null;
        }
        String accountIdStr = request.getParameter("accountId");
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        System.out.println("startDate 1 ----> " + startDate);

        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        System.out.println("endDate 1 ----> " + endDate);
        String fieldsOnly = request.getParameter("fieldsOnly");
        String widgetIdStr = request.getParameter("widgetId");
        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
            Integer widgetId = Integer.parseInt(widgetIdStr);
            System.out.println("widgetId ----> " + widgetId);
            TabWidget widget = uiService.getWidgetById(widgetId);

            String start = widget.getCustomStartDate();
            String end = widget.getCustomEndDate();

            if (start != null) {
                startDate = DateUtils.getStartDate(start);
                System.out.println("startDate 2 ----> " + startDate);
            }
            if (end != null) {
                endDate = DateUtils.getEndDate(end);
                System.out.println("endDate 2 ----> " + endDate);
            }
        }

        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        String adwordsAccountId = getAccountId(accountProperty, "adwordsAccountId");
        List<Map<String, Object>> data = adwordsService.getAdwordsReport(dataSetReportName, startDate, endDate, adwordsAccountId, timeSegment, productSegment, filter);
        if (dataSetReportName.equalsIgnoreCase("geoPerformance")) {
            for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
                Map<String, Object> dataMap = iterator.next();
                Object cityCriteria = dataMap.get("city");
                Object regionCriteria = dataMap.get("region");
                Object countryCriteria = dataMap.get("countryTerritory");
                try {
                    if (cityCriteria != null) {
                        System.out.println("CITY CRITERIA CLASS  " + cityCriteria.getClass());
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
        System.out.println(data);
        Map returnMap = new HashMap();
        if (data == null) {
            return null;
        }
        List<DatasetColumns> datasetColumnList = uiDao.getDatasetColumnsByDatasetId(dataSetIdInt);

        System.out.println("datasetColumnList0 ----> " + datasetColumnList);
        if (datasetColumnList.size() > 0) {
            System.out.println("datasetColumnList1 ---> " + datasetColumnList);
            Map<String, List<Map<String, Object>>> adwordsData = addDerivedColumn(data, null, datasetColumnList, dataSetReportName, startDate, endDate, null, adwordsAccountId, null, null, null, timeSegment, productSegment, filter, null);
            Map returnMapAdwords = new HashMap();
            Iterator it = adwordsData.entrySet().iterator();

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
            List<Map<String, Object>> columnsData = (List<Map<String, Object>>) columnData.get(0);
            List<ColumnDef> columns = (List<ColumnDef>) column.get(0);
            System.out.println("columnData ---> " + columnData);
            System.out.println("column ---> " + column);

            List<ColumnDef> columnDefs = getColumnDefForDerivedColumnObject(columnsData, columns);
            returnMapAdwords.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMapAdwords;
            }
            returnMapAdwords.put("data", columnsData);
            return returnMapAdwords;
        }
        List<ColumnDef> columnDefs = getColumnDefObject(data);
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        returnMap.put("data", data);
        return returnMap;
    }

    public Map<String, List<Map<String, Object>>> addDerivedColumn(List<Map<String, Object>> dataList, Map<String, List<Map<String, Object>>> analyticsData, List<DatasetColumns> datasetColumnList, String dataSetReportName, Date startDate, Date endDate, String gaProfileId, String adwordsAccountId, String accessToken, Long facebookAccountIdInt, Long facebookOrganicAccountIdInt, String timeSegment, String productSegment, String filter, Integer dataSetIdInt) {
        Map returnMap = new HashMap();

        List<ColumnDef> columnDefs = new ArrayList<>();
        List<Map<String, Object>> originalData = new ArrayList<>();
        if (dataList != null) {
            originalData = dataList;
        }
        if (analyticsData != null) {
            Iterator it = analyticsData.entrySet().iterator();

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
            List<Map<String, Object>> columnsData = (List<Map<String, Object>>) columnData.get(0);
            List<ColumnDef> columns = (List<ColumnDef>) column.get(0);
            originalData = columnsData;
        }

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
                if (tokenArrayFunc[0].equalsIgnoreCase("yoy") && startDate != null && endDate != null) {
                    strtDate = new DateTime(startDate).minusYears(1).toDate();
                    lastDate = new DateTime(endDate).minusYears(1).toDate();
                    System.out.println("yoy ---> ");
                    System.out.println("startDate ----> " + strtDate + " endDate ----> " + lastDate);
                } else if (tokenArrayFunc[0].equalsIgnoreCase("mom") && startDate != null && endDate != null) {
                    strtDate = new DateTime(startDate).minusMonths(1).toDate();
                    lastDate = new DateTime(endDate).minusMonths(1).toDate();
                    System.out.println("mom ---> ");
                    System.out.println("startDate ----> " + strtDate + " endDate ----> " + lastDate);
                } else if (tokenArrayFunc[0].equalsIgnoreCase("wow") && startDate != null && endDate != null) {
                    strtDate = new DateTime(startDate).minusWeeks(1).toDate();
                    lastDate = new DateTime(endDate).minusWeeks(1).toDate();
                    System.out.println("wow ---> ");
                    System.out.println("startDate ----> " + strtDate + " endDate ----> " + lastDate);
                }
                System.out.println("addDerivedColumnforDA function");
                List<Map<String, Object>> dataOver = new ArrayList<>();
                if (adwordsAccountId != null) {
                    System.out.println("adwords");
                    dataOver = adwordsService.getAdwordsReport(dataSetReportName, strtDate, lastDate, adwordsAccountId, timeSegment, productSegment, filter);
                } else if (facebookAccountIdInt != null && facebookOrganicAccountIdInt != null) {
                    System.out.println("facebook");
                    dataOver = facebookService.get(accessToken, dataSetReportName, facebookAccountIdInt,
                            facebookOrganicAccountIdInt, strtDate, lastDate, timeSegment, productSegment);
                } else if (gaProfileId != null) {
                    System.out.println("analytics");
                    Map<String, List<Map<String, Object>>> analyticsDataOver = gaService.getGaReport(dataSetReportName, gaProfileId, strtDate, lastDate, timeSegment, productSegment, dataSetIdInt);
                    Iterator it = analyticsDataOver.entrySet().iterator();

                    List columnOver = new ArrayList<>();
                    List columnDataOver = new ArrayList<>();

                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        if (pair.getKey().equals("data")) {
                            columnDataOver.add(pair.getValue());
                        } else {
                            columnOver.add(pair.getValue());
                        }
                    }
                    List<Map<String, Object>> columnsDataOver = (List<Map<String, Object>>) columnDataOver.get(0);
                    List<ColumnDef> columnsOver = (List<ColumnDef>) columnOver.get(0);
                    dataOver = columnsDataOver;
                }
                Map<String, Object> dataPairYoy = new HashMap();
                if (dataOver.size() > 0) {
                    int list = 0;
                    System.out.println("dataOver.size() ---> " + dataOver.size());
                    System.out.println("originalData.size() ---> " + originalData.size());
                    System.out.println("dataOver List ---> " + dataOver);
                    if (dataOver.size() == originalData.size()) {
                        for (Iterator<Map<String, Object>> iterator = dataOver.iterator(); iterator.hasNext();) {
                            dataPairYoy = (Map<String, Object>) iterator.next();
                            Map<String, Object> dataPairMap = new HashMap<>();
                            dataPairMap = originalData.get(list);
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
                if (dataOver.size() == 0 && strtDate == null && lastDate == null) {
                    for (Iterator<Map<String, Object>> iterator = originalData.iterator(); iterator.hasNext();) {
                        dataPairYoy = (Map<String, Object>) iterator.next();
                        Map<String, Object> dataPairMap = new HashMap<>();
                        dataPairYoy.put(fieldName, dataPairYoy.get(tokenArrayFunc[1]) + "");
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
                if (originalData.size() > 0) {
                    for (Iterator<Map<String, Object>> iterator = originalData.iterator(); iterator.hasNext();) {
                        dataPair = (Map<String, Object>) iterator.next();
                        int flag = 0;
                        String valueExpression = datasetColumn.getExpression();
                        String operatorExpression = datasetColumn.getExpression();
                        System.out.println("dataPair -----> " + dataPair);
                        System.out.println("formula ----> " + valueExpression);
                        for (int j = 0; j < tokenArray.length; j++) {
                            System.out.println(tokenArray[j] + " value -----> " + dataPair.get(tokenArray[j]));
                            try {
                                double value = Double.parseDouble(dataPair.get(tokenArray[j]) + "");
                                valueExpression = valueExpression.replaceAll(tokenArray[j], value + "");
                                System.out.println("formulaaaaaaaaaa ---> " + valueExpression);
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
        List<Map<String, Object>> data = new ArrayList<>(originalData);

        returnMap.put("columnDefs", columnDefs);
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
        System.out.println("Report Name" + request.getParameter("reportName"));
        System.out.println("Time Segment" + request.getParameter("timeSegment"));
        System.out.println("Product Segment" + request.getParameter("productSegment"));
        System.out.println("filter " + request.getParameter("filter"));

        List<Map<String, Object>> data = adwordsService.getAdwordsReport(request.getParameter("reportName"), DateUtils.get30DaysBack(), new Date(), "827-719-8225", request.getParameter("timeSegment"), request.getParameter("productSegment"), request.getParameter("filter"));
        System.out.println(data);
        Map returnMap = new HashMap();
        String fieldsOnly = request.getParameter("fieldsOnly");
        List<ColumnDef> columnDefs = getColumnDefObject(data);
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        returnMap.put("data", data);
        System.out.println(returnMap);
        return returnMap;
    }

    @RequestMapping(value = "testGa", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object testGa(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Report Name" + request.getParameter("reportName"));
        System.out.println("Time Segment" + request.getParameter("timeSegment"));
        System.out.println("Product Segment" + request.getParameter("productSegment"));
        System.out.println("filter " + request.getParameter("filter"));
        return gaService.getGaReport(request.getParameter("reportName"), "112725239", DateUtils.get30DaysBack(), new Date(), request.getParameter("timeSegment"), request.getParameter("productSegment"), null);
    }

    @RequestMapping(value = "getFbData", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getFbData(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Calling of getFbData function in ProxyController class");
        String dataSetId = request.getParameter("dataSetId");
        String dataSetReportName = request.getParameter("dataSetReportName");
        String timeSegment = request.getParameter("timeSegment");
        String productSegment = request.getParameter("productSegment");
        if (timeSegment == null) {
            timeSegment = "daily";
        }
        if (productSegment == null) {
            productSegment = "none";
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
                productSegment = dataSet.getProductSegment();
            }
        }
        String accountIdStr = request.getParameter("accountId");
        Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
        System.out.println("startDate 1 ----> " + startDate);

        Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
        System.out.println("endDate 1 ----> " + endDate);
        String fieldsOnly = request.getParameter("fieldsOnly");
        String widgetIdStr = request.getParameter("widgetId");
        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
            Integer widgetId = Integer.parseInt(widgetIdStr);
            System.out.println("widgetId ----> " + widgetId);
            TabWidget widget = uiService.getWidgetById(widgetId);

            String start = widget.getCustomStartDate();
            String end = widget.getCustomEndDate();

            if (start != null) {
                startDate = DateUtils.getStartDate(start);
                System.out.println("startDate 2 ----> " + startDate);
            }
            if (end != null) {
                endDate = DateUtils.getEndDate(end);
                System.out.println("endDate 2 ----> " + endDate);
            }
        }
        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        String facebookAccountId = getAccountId(accountProperty, "facebookAccountId");
        String facebookOrganicAccountId = getAccountId(accountProperty, "facebookOrganicAccountId");
        Long facebookAccountIdInt = Long.parseLong(facebookAccountId);
        Long facebookOrganicAccountIdInt = null;
        if (facebookOrganicAccountId != null) {
            facebookOrganicAccountIdInt = Long.parseLong(facebookOrganicAccountId);
        }
        String accessToken = "EAAUAycrj0GsBAM3EgwLcQjz5zywESZBpHN76cERZCaxEZC9ZAzMjRzRxIznWM3u8s4DBwUvhMaQAGglDOIa9tSV7ZCVf9ZBajV9aA6khaCRmEZAQhIHUInBVYZBZAT5nycwniZCozuLcjhTm0eW5tAUxIugmvxszsivmh5ZClzuMZApZBJxd0RZBIDk1r0";
        log.debug("Report Name ---- " + dataSetReportName);
        log.debug("Account Id ---- " + facebookAccountIdInt);
        log.debug("Time segment ---- " + timeSegment);
        log.debug("Start Date ---- " + startDate);
        List<DatasetColumns> datasetColumnList = uiDao.getDatasetColumnsByDatasetId(dataSetIdInt);
        System.out.println("datasetColumnList0 ----> " + datasetColumnList);
        List<Map<String, Object>> data = facebookService.get(accessToken, dataSetReportName, facebookAccountIdInt,
                facebookOrganicAccountIdInt, startDate, endDate, timeSegment, productSegment);
        if (datasetColumnList.size() > 0) {
            Map<String, List<Map<String, Object>>> fbData = addDerivedColumn(data, null, datasetColumnList, dataSetReportName, startDate, endDate, null, null, accessToken, facebookAccountIdInt, facebookOrganicAccountIdInt, timeSegment, productSegment, null, null);
            Map returnMap = new HashMap();
            Iterator it = fbData.entrySet().iterator();

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
            List<Map<String, Object>> columnsData = (List<Map<String, Object>>) columnData.get(0);
            List<ColumnDef> columns = (List<ColumnDef>) column.get(0);
            System.out.println("columnData ---> " + columnData);
            System.out.println("column ---> " + column);

            List<ColumnDef> columnDefs = getColumnDefForDerivedColumnObject(columnsData, columns);
            returnMap.put("columnDefs", columnDefs);
            if (fieldsOnly != null) {
                return returnMap;
            }
            returnMap.put("data", columnsData);
            return returnMap;
        }

        System.out.println("FbData list ----> " + data);
//        Date startDate = DateUtils.getSixMonthsBack(new Date()); // 1348734005171064L
//        Date endDate = new Date();
//        List<Map<String, String>> data = facebookService.get(accessToken, "accountPerformance", 1348731135171351L, startDate, endDate, "daily");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = getColumnDefObject(data);
        returnMap.put("columnDefs", columnDefs);
        if (fieldsOnly != null) {
            return returnMap;
        }
        returnMap.put("data", data);
        return returnMap;
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

    private List<ColumnDef> getColumnDefObject(List<Map<String, Object>> data) {
        log.debug("Calling of getColumnDef function in ProxyController class");
        List<ColumnDef> columnDefs = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            for (Map.Entry<String, Object> entrySet : mapData.entrySet()) {
                String key = entrySet.getKey();
                DefaultFieldProperties fieldProperties = uiService.getDefaultFieldProperties(key);
                if (fieldProperties != null) {
                    columnDefs.add(new ColumnDef(key, fieldProperties.getDataType() == null ? "string" : fieldProperties.getDataType(), fieldProperties.getDisplayName(), fieldProperties.getAgregationFunction(), fieldProperties.getDisplayFormat()));
                } else {
                    Object value = entrySet.getValue();
                    String valueString = value + "";
                    System.out.println(value.getClass());
                    if (NumberUtils.isNumber(valueString)) {
                        columnDefs.add(new ColumnDef(key, "number", key));
                    } else if (DateUtils.convertToDate(valueString) != null) {
                        columnDefs.add(new ColumnDef(key, "date", key));
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

    private List<ColumnDef> getColumnDefForDerivedColumn(List<Map<String, String>> data, List<ColumnDef> column) {

        List<ColumnDef> columnDefs = new ArrayList<>();
        if (data == null) {
            return null;
        }
        for (Iterator<Map<String, String>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, String> mapData = iterator.next();
            for (Iterator<ColumnDef> iterate = column.iterator(); iterate.hasNext();) {
                ColumnDef columns = iterate.next();
                for (Map.Entry<String, String> entrySet : mapData.entrySet()) {
                    String key = entrySet.getKey();
                    String value = entrySet.getValue();
                    if (key.equalsIgnoreCase(columns.getFieldName())) {
                        if (NumberUtils.isNumber(value) && columns.getType() == null) {
                            columnDefs.add(new ColumnDef(columns.getId(), key, "number", key, columns.getAgregationFunction(), columns.getDisplayFormat(), columns.getStatus(), columns.getExpression(), columns.getFunctionName()));
                        } else if (DateUtils.convertToDate(value) != null && columns.getType() == null) {
                            columnDefs.add(new ColumnDef(columns.getId(), key, "date", key, columns.getAgregationFunction(), columns.getDisplayFormat(), columns.getStatus(), columns.getExpression(), columns.getFunctionName()));
                        } else {
                            columnDefs.add(new ColumnDef(columns.getId(), key, columns.getType() == null ? "string" : columns.getType(), key, columns.getAgregationFunction(), columns.getDisplayFormat(), columns.getStatus(), columns.getExpression(), columns.getFunctionName()));
                        }
                    }
                    // columnDefs.add(new ColumnDef(key, "string", key));
                }
            }
            return columnDefs;
        }
        return columnDefs;
    }

    private List<ColumnDef> getColumnDefForDerivedColumnObject(List<Map<String, Object>> data, List<ColumnDef> column) {

        List<ColumnDef> columnDefs = new ArrayList<>();
        if (data == null) {
            return null;
        }
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            for (Iterator<ColumnDef> iterate = column.iterator(); iterate.hasNext();) {
                ColumnDef columns = iterate.next();
                for (Map.Entry<String, Object> entrySet : mapData.entrySet()) {
                    String key = entrySet.getKey();
                    Object objValue = entrySet.getValue();
                    String value = objValue + "";
                    if (key.equalsIgnoreCase(columns.getFieldName())) {
                        if (NumberUtils.isNumber(value) && columns.getType() == null) {
                            columnDefs.add(new ColumnDef(columns.getId(), key, "number", key, columns.getAgregationFunction(), columns.getDisplayFormat(), columns.getStatus(), columns.getExpression(), columns.getFunctionName()));
                        } else if (DateUtils.convertToDate(value) != null && columns.getType() == null) {
                            columnDefs.add(new ColumnDef(columns.getId(), key, "date", key, columns.getAgregationFunction(), columns.getDisplayFormat(), columns.getStatus(), columns.getExpression(), columns.getFunctionName()));
                        } else {
                            columnDefs.add(new ColumnDef(columns.getId(), key, columns.getType() == null ? "string" : columns.getType(), key, columns.getAgregationFunction(), columns.getDisplayFormat(), columns.getStatus(), columns.getExpression(), columns.getFunctionName()));
                        }
                    }
                    // columnDefs.add(new ColumnDef(key, "string", key));
                }
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

            List<TabWidget> tabWidgets = uiService.getTabWidget(tabId);
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
                        System.out.println("DS TYPE ==>  CSV");
//                        url = "../admin/csv/getData";
                        url = "../dashboard/admin/csv/getData";
                        valueMap.put("connectionUrl", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getConnectionString(), "UTF-8")));
                        valueMap.put("driver", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getSqlDriver(), "UTF-8")));
                    } else if (tabWidget.getDataSourceId().getDataSourceType().equalsIgnoreCase("facebook")) {
                        url = "../dashboard/admin/proxy/getData?";
//                    url = "../admin/proxy/getData?";
                    }
                    valueMap.put("dataSetId", Arrays.asList("" + tabWidget.getDataSetId().getId()));
//                valueMap.put("location", Arrays.asList(URLEncoder.encode(request.getParameter("location"), "UTF-8")));
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
        System.out.println("startDate 1 ----> " + startDate1);
        Date endDate1 = DateUtils.getEndDate(request.getParameter("endDate"));
        System.out.println("endDate 1 ----> " + endDate1);
//        String widgetIdStr = request.getParameter("widgetId");
//        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
//            Integer widgetId = Integer.parseInt(widgetIdStr);
//            System.out.println("widgetId ----> " + widgetId);
//            TabWidget widget = uiService.getWidgetById(widgetId);
//
//            String start = widget.getCustomStartDate();
//            String end = widget.getCustomEndDate();
//
//            if (start != null) {
//                startDate = DateUtils.getStartDate(start);
//                System.out.println("startDate 2 ----> " + startDate);
//            }
//            if (end != null) {
//                endDate = DateUtils.getEndDate(end);
//                System.out.println("endDate 2 ----> " + endDate);
//            }
//        }
        String start_date = month_date.format(startDate1);
        String end_date = month_date.format(endDate1);
        String selectDate;

        System.out.println("startDate ----> " + start_date);
        System.out.println("endDate ----> " + end_date);

        if (start_date.equalsIgnoreCase(end_date)) {
            selectDate = start_date;
        } else {
            selectDate = start_date.concat(" - " + end_date);
        }
        System.out.println("selectDate ---> " + selectDate);
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
                System.out.println("startDate  ----> " + startDate);
                Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
                System.out.println("endDate  ----> " + endDate);
//                String url = "../dashboard/admin/proxy/getData?"; // tabWidget.getDirectUrl();
                String url = propReader.readUrl(urlDownload) + "";
//                String url = "../admin/proxy/getData?"; // tabWidget.getDirectUrl();
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
//                    System.out.println("DS TYPE ==>  CSV");
////                    url = "../admin/csv/getData";
//                    url = "../dashboard/admin/csv/getData";
//                    valueMap.put("connectionUrl", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getConnectionString(), "UTF-8")));
////                    valueMap.put("driver", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getSqlDriver(), "UTF-8")));
//                } else if (tabWidget.getDataSourceId().getDataSourceType().equalsIgnoreCase("facebook")) {
//                    url = "../dashboard/admin/proxy/getData?";
////                    url = "../admin/proxy/getData?";
//                }
                valueMap.put("widgetId", Arrays.asList("" + tabWidget.getId()));
                valueMap.put("dataSetId", Arrays.asList("" + tabWidget.getDataSetId().getId()));

//                valueMap.put("connectionUrl", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getConnectionString(), "UTF-8")));
//                valueMap.put("driver", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getSqlDriver(), "UTF-8")));
//                valueMap.put("location", Arrays.asList(URLEncoder.encode(request.getParameter("location"), "UTF-8")));
                valueMap.put("accountId", Arrays.asList(URLEncoder.encode(request.getParameter("accountId"), "UTF-8")));

                String start = tabWidget.getCustomStartDate();
                String end = tabWidget.getCustomEndDate();

                if (start != null) {
                    startDate = DateUtils.getStartDate(start);
                    System.out.println("startDate 2 ----> " + startDate);
                }
                if (end != null) {
                    endDate = DateUtils.getEndDate(end);
                    System.out.println("endDate 2 ----> " + endDate);
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
        System.out.println("Start Function of download");
        String dealerId = request.getParameter("dealerId");
        String exportType = request.getParameter("exportType");
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);

        Date startDate1 = DateUtils.getStartDate(request.getParameter("startDate"));
        System.out.println("startDate 1 ----> " + startDate1);
        Date endDate1 = DateUtils.getEndDate(request.getParameter("endDate"));
        System.out.println("endDate 1 ----> " + endDate1);

//        String widgetIdStr = request.getParameter("widgetId");
//        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
//            Integer widgetId = Integer.parseInt(widgetIdStr);
//            System.out.println("widgetId ----> " + widgetId);
//            TabWidget widget = uiService.getWidgetById(widgetId);
//
//            String start = widget.getCustomStartDate();
//            String end = widget.getCustomEndDate();
//
//            if (start != null) {
//                startDate = DateUtils.getStartDate(start);
//                System.out.println("startDate 2 ----> " + startDate);
//            }
//            if (end != null) {
//                endDate = DateUtils.getEndDate(end);
//                System.out.println("endDate 2 ----> " + endDate);
//            }
//        }
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
            System.out.println("key ---> " + key + " value ---> " + value);
            valueMap.put(key, Arrays.asList(value));
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entrySet : parameterMap.entrySet()) {
            String key = entrySet.getKey();
            String[] value = entrySet.getValue();
            System.out.println("key ---> " + key + " value ---> " + value);
            valueMap.put(key, Arrays.asList(value));
        }

        List<TabWidget> tabWidgets = uiService.getTabWidget(tabId);
        String account = null;
        String product = "Analytics";
        for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
            TabWidget tabWidget = iterator.next();
            try {
                if (tabWidget.getDataSourceId() == null) {
                    continue;
                }
                Date startDate = DateUtils.getStartDate(request.getParameter("startDate"));
                System.out.println("startDate  ----> " + startDate);
                Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
                System.out.println("endDate  ----> " + endDate);
//                String url = "../dashboard/admin/proxy/getData?";
                String url = propReader.readUrl(urlDownload) + "";
                System.out.println("url ---> " + url);
//                String url = "../admin/proxy/getData?";
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
//                    System.out.println("DS TYPE ==>  CSV");
////                    url = "../admin/csv/getData";
//                    url = "../dashboard/admin/csv/getData";
//                    valueMap.put("connectionUrl", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getConnectionString(), "UTF-8")));
////                    valueMap.put("driver", Arrays.asList(URLEncoder.encode(tabWidget.getDataSourceId().getSqlDriver(), "UTF-8")));
//                } else if (tabWidget.getDataSourceId().getDataSourceType().equalsIgnoreCase("facebook")) {
////                    url = "../admin/proxy/getData?";
//                    url = "../dashboard/admin/proxy/getData?";
//
//                }
                valueMap.put("widgetId", Arrays.asList("" + tabWidget.getId()));
                System.out.println("tabWidget Id---> " + tabWidget.getId());
                valueMap.put("dataSetId", Arrays.asList("" + tabWidget.getDataSetId().getId()));
                valueMap.put("accountId", Arrays.asList(URLEncoder.encode(request.getParameter("accountId"), "UTF-8")));

                String start = tabWidget.getCustomStartDate();
                String end = tabWidget.getCustomEndDate();
                System.out.println("start ---> " + start);
                System.out.println("end ---> " + end);

                if (start != null) {
                    startDate = DateUtils.getStartDate(start);
                    System.out.println("startDate 2 ----> " + startDate);
                }
                if (end != null) {
                    endDate = DateUtils.getEndDate(end);
                    System.out.println("endDate 2 ----> " + endDate);
                }
                System.out.println("startDate ---> " + startDate);
                System.out.println("endDate ---> " + endDate);

                valueMap.put("startDate", Arrays.asList("" + URLEncoder.encode(DateUtils.dateToString(startDate, "MM/dd/yyyy"), "UTF-8")));
                valueMap.put("endDate", Arrays.asList("" + URLEncoder.encode(DateUtils.dateToString(endDate, "MM/dd/yyyy"), "UTF-8")));
                System.out.println("valueMap ---> " + valueMap);
                Integer port = request.getServerPort();

                int account_id = Integer.parseInt(request.getParameter("accountId"));
                account = userService.getAccountName(account_id);

//                int product_id = Integer.parseInt(request.getParameter("productId"));
//                System.out.println("product_id :" + product_id);
//                product = userService.getProductName(product_id);
//
//                System.out.println("product name :" + product);
                System.out.println("account name :" + account);

                String localUrl = request.getScheme() + "://" + request.getServerName() + ":" + port + "/";
                log.debug("URL:" + url);
                if (url.startsWith("../")) {
                    url = url.replaceAll("\\.\\./", localUrl);
                }
                System.out.println("url: " + url);
                System.out.println("valuemap: " + valueMap);
                String data = Rest.getData(url, valueMap);
                System.out.println("Data -----> : " + data);
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }

}
