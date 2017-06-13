/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

//import static com.visumbu.vb.admin.controller.EnliventController.processFollowings;
import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.admin.service.AdwordsService;
import com.visumbu.vb.admin.service.BingService;
import com.visumbu.vb.admin.service.DealerService;
import com.visumbu.vb.admin.service.FacebookService;
import com.visumbu.vb.admin.service.GaService;
import com.visumbu.vb.admin.service.LinkedinService;
import com.visumbu.vb.admin.service.ReportService;
import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.bean.DateRange;
import com.visumbu.vb.bean.Range;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.AdwordsCriteria;
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.DatasetColumns;
import com.visumbu.vb.model.DefaultFieldProperties;
import com.visumbu.vb.model.JoinDataSet;
import com.visumbu.vb.model.JoinDataSetCondition;
import com.visumbu.vb.model.Property;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.utils.CsvDataSet;
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.JsonSimpleUtils;
import com.visumbu.vb.utils.PropertyReader;
import com.visumbu.vb.utils.Rest;
import com.visumbu.vb.utils.ShuntingYard;
import com.visumbu.vb.utils.XlsDataSet;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    private UiDao uiDao;

    PropertyReader propReader = new PropertyReader();

    private final String urlDownload = "url.download";

    final static Logger log = Logger.getLogger(ProxyController.class);

    @RequestMapping(value = "getData", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object getGenericData(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Calling of getGenericData function in ProxyController class");

        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String[]> entrySet : parameterMap.entrySet()) {
            String key = entrySet.getKey();
            String[] value = entrySet.getValue();
            valueMap.put(key, Arrays.asList(value));
        }

        String joinDataSetIdStr = getFromMultiValueMap(valueMap, "joinDataSetId");
        Map returnMap = new HashMap<>();
        if (joinDataSetIdStr != null) {
            returnMap = getCombinedData(valueMap, request, response, joinDataSetIdStr);
        } else {
            returnMap = getData(valueMap, request, response);
        }
        String fieldsOnly = request.getParameter("fieldsOnly");

        String dataSetId = request.getParameter("dataSetId");
        Integer dataSetIdInt = null;
        if (dataSetId != null) {
            try {
                dataSetIdInt = Integer.parseInt(dataSetId);
            } catch (Exception e) {

            }
        }

        List<Map<String, Object>> data = (List<Map<String, Object>>) returnMap.get("data");

        List<DatasetColumns> datasetColumnList = uiDao.getDatasetColumnsByDatasetId(dataSetIdInt);

        if (datasetColumnList.size() > 0) {
            List<Map<String, Object>> dataWithDerivedFunctions = addDerivedColumnsFunction(datasetColumnList, data, valueMap, request, response);
            List<Map<String, Object>> dataWithDerivedColumns = addDerivedColumnsExpr(datasetColumnList, dataWithDerivedFunctions);
            returnMap.put("data", dataWithDerivedColumns);
        }
        String widgetIdStr = request.getParameter("widgetId");

        if (widgetIdStr != null && !widgetIdStr.isEmpty()) {
            String queryFilter = null;
            Integer widgetId = Integer.parseInt(widgetIdStr);
            TabWidget tabWidget = uiService.getWidgetByIdAndDataSetId(widgetId, dataSetIdInt);
            if (tabWidget != null) {
                queryFilter = tabWidget.getQueryFilter();
            }
            List<Map<String, Object>> originalData = (List<Map<String, Object>>) returnMap.get("data");
            List<Map<String, Object>> returnDataMap = ShuntingYard.applyExpression(originalData, queryFilter);
            returnMap.put("data", returnDataMap);
        }
        returnMap.put("columnDefs", getColumnDefObject((List<Map<String, Object>>) returnMap.get("data")));
        //System.out.println("returnMap final ---> " + returnMap);
        Map dataMap = new HashMap<>();
        dataMap.put("columnDefs", returnMap.get("columnDefs"));
        if (fieldsOnly != null) {
            return dataMap;
        }
        dataMap.put("data", returnMap.get("data"));
        return dataMap;
    }

    public String getFromMultiValueMap(MultiValueMap valueMap, String key) {
        List<String> dataSourceTypeList = (List<String>) valueMap.get(key);
        if (dataSourceTypeList != null && !dataSourceTypeList.isEmpty()) {
            return dataSourceTypeList.get(0);
        }
        return null;
    }

    public MultiValueMap<String, String> getRequest(DataSet dataSet, MultiValueMap valueMap) {
        MultiValueMap<String, String> combinedValueMap = new LinkedMultiValueMap<>();
        combinedValueMap.put("dataSetReportName", Arrays.asList(dataSet.getReportName()));
        combinedValueMap.put("dataSetId", Arrays.asList(dataSet.getId() + ""));
        combinedValueMap.put("timeSegment", Arrays.asList(dataSet.getTimeSegment()));
        combinedValueMap.put("filter", Arrays.asList(dataSet.getNetworkType()));
        combinedValueMap.put("url", Arrays.asList(dataSet.getUrl()));
        combinedValueMap.put("query", Arrays.asList(dataSet.getQuery()));
        combinedValueMap.put("productSegment", Arrays.asList(dataSet.getProductSegment()));
        DataSource dataSource = dataSet.getDataSourceId();
        combinedValueMap.put("connectionUrl", Arrays.asList(dataSource.getConnectionString()));
        combinedValueMap.put("dataSourceId", Arrays.asList(dataSource.getId() + ""));
        combinedValueMap.put("driver", Arrays.asList(dataSource.getDataSourceType()));
        combinedValueMap.put("dataSourceType", Arrays.asList(dataSource.getDataSourceType()));
        combinedValueMap.put("username", Arrays.asList(dataSource.getUserName()));
        combinedValueMap.put("password", Arrays.asList(dataSource.getPassword()));
        combinedValueMap.put("startDate", Arrays.asList(getFromMultiValueMap(valueMap, "startDate")));
        combinedValueMap.put("accountId", Arrays.asList(getFromMultiValueMap(valueMap, "accountId")));
        combinedValueMap.put("locationId", Arrays.asList(getFromMultiValueMap(valueMap, "locationId")));
        combinedValueMap.put("endDate", Arrays.asList(getFromMultiValueMap(valueMap, "endDate")));
        return combinedValueMap;
    }

    public Map getCombinedData(MultiValueMap valueMap, HttpServletRequest request, HttpServletResponse response, String joinDataSetIdStr) {
        DataSet dataSetOne = null;
        DataSet dataSetTwo = null;
        String operationType = null;
        Integer joinDataSetIdInt = null;

        joinDataSetIdInt = Integer.parseInt(joinDataSetIdStr);
        List<String> conditions = new ArrayList<>();

        List<JoinDataSetCondition> joinDatasetConditionList = uiDao.getJoinDataSetConditionById(joinDataSetIdInt);
        for (Iterator<JoinDataSetCondition> iterator = joinDatasetConditionList.iterator(); iterator.hasNext();) {
            JoinDataSetCondition joinDataSetCondition = iterator.next();
            JoinDataSet joinDataSet = joinDataSetCondition.getJoinDataSetId();
            String concatCondition = "" + joinDataSetCondition.getConditionFieldFirst() + "," + joinDataSetCondition.getConditionFieldSecond() + "2";
            System.out.println("Concat Condition ----> " + concatCondition);
            conditions.add(concatCondition);
            dataSetOne = joinDataSet.getDataSetIdFirst();
            dataSetTwo = joinDataSet.getDataSetIdSecond();
            operationType = joinDataSet.getOperationType();
        }

        MultiValueMap combinedValueMapOne = getRequest(dataSetOne, valueMap);
        Map joinValueMapOne = getData(combinedValueMapOne, request, response);

        List<Map<String, Object>> dataSetOneList = (List<Map<String, Object>>) joinValueMapOne.get("data");
        Set<String> columnSet = dataSetOneList.get(0).keySet();

        MultiValueMap joinValueMapTwo = getRequest(dataSetTwo, valueMap);
        Map joinDataSetTwoMap = getData(joinValueMapTwo, request, response);

        List<Map<String, Object>> dataSetTwoList = (List<Map<String, Object>>) joinDataSetTwoMap.get("data");
        for (Iterator<Map<String, Object>> iterator = dataSetTwoList.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            try {
                dataMap.entrySet().forEach((entry) -> {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    for (String columnStr : columnSet) {
                        if (key.equalsIgnoreCase(columnStr)) {
                            dataMap.remove(key);
                            dataMap.put(key + "2", value);
                            break;
                        }
                    }
                });
            } catch (ConcurrentModificationException e) {
            }
        }
//        System.out.println("dataSetTwoList ---> " + dataSetTwoList);

        List<Map<String, Object>> combinedData = new ArrayList<>();
        if (operationType.equalsIgnoreCase("inner")) {
            System.out.println("Innnnnnnnnnnnnerrrrrrrrr");
            combinedData = innerJoin(dataSetOneList, dataSetTwoList, conditions);
        } else if (operationType.equalsIgnoreCase("left")) {
            System.out.println("LeeEEeeeeeeeeeeeeft");
            combinedData = leftJoin(dataSetOneList, dataSetTwoList, conditions);
        } else if (operationType.equalsIgnoreCase("right")) {
            System.out.println("rightttttttttttttttttt");
            combinedData = rightJoin(dataSetOneList, dataSetTwoList, conditions);
        }
        System.out.println("combinedData ---> " + combinedData);

        List<Map<String, Object>> data = combinedData;
        List<ColumnDef> columnDefs = getColumnDefObject(data);
        Map returnMap = new HashMap<>();
        returnMap.put("columnDefs", columnDefs);
        returnMap.put("data", data);
        return returnMap;
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
                dataSourceType = dataSet.getDataSourceId().getDataSourceType();
            }
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
        } else if (dataSourceType.equalsIgnoreCase("https")) {
            getHttpsData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("xls")) {
            returnMap = (Map) getXlsData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("pinterest")) {
            returnMap = (Map) getPinterestData(request, response);
        } else if (dataSourceType.equalsIgnoreCase("linkedin")) {
            returnMap = (Map) getLinkedInData(request, response);
        }

        //System.out.println("return map ---> " + returnMap);
        return returnMap;
    }

    public static List<Map<String, Object>> leftJoin(final List<Map<String, Object>> col1, final List<Map<String, Object>> col2, final List<String> conditions) {
        Set<String> columnSet = col2.get(0).keySet();
        System.out.println("columnSet ---> " + columnSet);
        List<Map<String, Object>> returnList = new ArrayList<>();
        col1.forEach(map -> {
            Stream<Map<String, Object>> filters = col2.stream().filter(e -> {
                boolean isValid = true;
                for (Iterator<String> iterator = conditions.iterator(); iterator.hasNext();) {
                    String condition = iterator.next();
                    String[] fields = condition.split(",");
                    System.out.println("field[0] ---> " + fields[0]);
                    System.out.println("field[1] ---> " + fields[1]);
                    if (!(map.get(fields[0]) + "").equalsIgnoreCase(e.get(fields[1]) + "")) {
                        System.out.println("return false");
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

    public static List<Map<String, Object>> rightJoin(final List<Map<String, Object>> col1, final List<Map<String, Object>> col2, final List<String> conditions) {
        Set<String> columnSet = col1.get(0).keySet();
        System.out.println("columnSet ---> " + columnSet);
        List<Map<String, Object>> returnList = new ArrayList<>();
        col2.forEach(map -> {
            Stream<Map<String, Object>> filters = col1.stream().filter(e -> {
                boolean isValid = true;
                for (Iterator<String> iterator = conditions.iterator(); iterator.hasNext();) {
                    String condition = iterator.next();
                    String[] fields = condition.split(",");
                    System.out.println("field[0] ---> " + fields[0]);
                    System.out.println("field[1] ---> " + fields[1]);
                    if (!(map.get(fields[1]) + "").equalsIgnoreCase(e.get(fields[0]) + "")) {
                        System.out.println("return false");
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

    public static List<Map<String, Object>> innerJoin(List<Map<String, Object>> col1, List<Map<String, Object>> col2, List<String> conditions) {
        List<Map<String, Object>> returnList = new ArrayList<>();
        col1.forEach(map -> {
            Stream<Map<String, Object>> filters = col2.stream().filter(e -> {
                boolean isValid = true;
                for (Iterator<String> iterator = conditions.iterator(); iterator.hasNext();) {
                    String condition = iterator.next();
                    String[] fields = condition.split(",");
                    if (!(map.get(fields[0]) + "").equalsIgnoreCase(e.get(fields[1]) + "")) {
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

//        
//    col1.forEach(map -> map.put(TypeId.AMOUNT, 
//        col2.stream()
//            .filter(e -> e.player_id == (int)map.get(TypeId.PLAYER) && 
//                         e.platform_id == (int)map.get(TypeId.PLATFORM))
//            .findFirst().map(e -> e.amount).orElse(null)
//        ));
        return returnList;
    }

    public Map<String, Date> getCustomDate(String dateRangeName, Integer lastNdays, Integer lastNweeks, Integer lastNmonths, Integer lastNyears, Date endDate) {
        //System.out.println("Date Range Name --> " + dateRangeName);

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

        //System.out.println("dateRangeSelect ---> " + dateRangeSelect);
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

    public List<Map<String, Object>> addDerivedColumnsFunction(List<DatasetColumns> datasetColumns, List<Map<String, Object>> data, MultiValueMap request, HttpServletRequest httpRequest, HttpServletResponse response) {
        // Supported Functions : yoy, mom, wow, custom
        String format = "yyyy-MM-dd";
        Date startDate = DateUtils.getStartDate(getFromMultiValueMap(request, "startDate"));
        Date endDate = DateUtils.getEndDate(getFromMultiValueMap(request, "endDate"));
        String cachedRange = DateUtils.dateToString(startDate, format) + " To " + DateUtils.dateToString(endDate, format);
        Map<String, List> cachedData = new HashMap<>();
        cachedData.put(cachedRange, data);
        Map<String, List> derivedColumnData = new HashMap<>();
        for (Iterator<DatasetColumns> iterator = datasetColumns.iterator(); iterator.hasNext();) {
            DatasetColumns datasetColumn = iterator.next();
            boolean isDerivedColumn = checkIsDerivedFunction(datasetColumn);
            if (isDerivedColumn) {
                String functionName = datasetColumn.getFunctionName();
                String dateRangeName = datasetColumn.getDateRangeName();
                Integer lastNdays = datasetColumn.getLastNdays();
                Integer lastNweeks = datasetColumn.getLastNweeks();
                Integer lastNmonths = datasetColumn.getLastNmonths();
                Integer lastNyears = datasetColumn.getLastNyears();
                String customStartDate = datasetColumn.getCustomStartDate();
                String customEndDate = datasetColumn.getCustomEndDate();
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
                derivedColumnData.put(datasetColumn.getFieldName(), cachedData.get(cachedRangeForFunction));
            } else {

            }
        }
        List<Map<String, Object>> returnData = new ArrayList<>();

        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            Map<String, Object> returnDataMap = new HashMap<>();
            for (Iterator<DatasetColumns> iterator1 = datasetColumns.iterator(); iterator1.hasNext();) {
                DatasetColumns datasetColumn = iterator1.next();
                boolean isDerivedColumn = checkIsDerivedFunction(datasetColumn);
                if (isDerivedColumn) {
                    String functionName = datasetColumn.getFunctionName();
                    String dateRangeName = datasetColumn.getDateRangeName();
                    Integer lastNdays = datasetColumn.getLastNdays();
                    Integer lastNweeks = datasetColumn.getLastNweeks();
                    Integer lastNmonths = datasetColumn.getLastNmonths();
                    Integer lastNyears = datasetColumn.getLastNyears();
                    String customStartDate = datasetColumn.getCustomStartDate();
                    String customEndDate = datasetColumn.getCustomEndDate();
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
                    Object derivedFunctionValue = getDataForDerivedFunctionColumn(cachedData.get(cachedRangeForFunction), dataMap.get(datasetColumn.getBaseField()), datasetColumn);
                    returnDataMap.put(datasetColumn.getFieldName(), derivedFunctionValue);
                } else {
                    returnDataMap.put(datasetColumn.getFieldName(), dataMap.get(datasetColumn.getFieldName()));
                }
            }
            //System.out.println(returnDataMap);
            returnData.add(returnDataMap);
        }

        return returnData;
    }

    public Object getDataForDerivedFunctionColumn(List<Map<String, Object>> data, Object baseFieldValue, DatasetColumns datasetColumn) {
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> mapData = iterator.next();
            System.out.println("MapData ---->" + mapData);
            if ((mapData.get(datasetColumn.getBaseField()) + "").equalsIgnoreCase(baseFieldValue + "")) {
                System.out.println("Matching MapData ---->" + mapData);
                return mapData.get(datasetColumn.getColumnName());
            }
        }
        return null;
    }

    public static List<Map<String, Object>> addDerivedColumnsExpr(List<DatasetColumns> datasetColumns, List<Map<String, Object>> data) {
        List<Map<String, Object>> returnData = new ArrayList<>();
        for (Iterator<Map<String, Object>> iterator = data.iterator(); iterator.hasNext();) {
            Map<String, Object> dataMap = iterator.next();
            returnData.add(addDerivedColumnsExpr(datasetColumns, dataMap));
        }
        return returnData;
    }

    public static Map<String, Object> addDerivedColumnsExpr(List<DatasetColumns> datasetColumns, Map<String, Object> data) {
        Map<String, Object> returnMap = new HashMap<>();
        for (Iterator<DatasetColumns> iterator = datasetColumns.iterator(); iterator.hasNext();) {
            DatasetColumns datasetColumn = iterator.next();
            boolean isDerivedColumn = checkIsDerivedExpr(datasetColumn);
            if (isDerivedColumn) {
                if (datasetColumn.getExpression() != null) {
                    String expressionValue = executeExpression(datasetColumn, data);
                    // //System.out.println("OUTPUT FROM EXPRESSION " + expressionValue);
                    if ((expressionValue.startsWith("'") && expressionValue.endsWith("'"))) {
                        Object expValue = expressionValue.substring(1, expressionValue.length() - 1);
                        returnMap.put(datasetColumn.getFieldName(), expValue);
                    } else {
                        Object expValue = expressionValue;
                        returnMap.put(datasetColumn.getFieldName(), expValue);
                    }
                }
            } else {
                returnMap.put(datasetColumn.getFieldName(), data.get(datasetColumn.getFieldName()));
            }

        }
        return returnMap;
    }

    private static String executeExpression(DatasetColumns datasetColumn, Map<String, Object> data) {
        String postFixRule = ShuntingYard.postfix(datasetColumn.getExpression());
        return ShuntingYard.executeExpression(data, postFixRule);
    }

    private static boolean checkIsDerivedExpr(DatasetColumns datasetColumn) {
        if (datasetColumn.getExpression() != null && !datasetColumn.getExpression().isEmpty()) {
            return true;
        }
        return false;
    }

    private static boolean checkIsDerivedFunction(DatasetColumns datasetColumn) {
        if (datasetColumn.getFunctionName() != null && !datasetColumn.getFunctionName().isEmpty()) {
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
        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        String accessToken = getAccountId(accountProperty, "pinterestAccessToken");

        //System.out.println("Pinterst access token--->" + accessToken);
        if (accessToken == null) {
            //System.out.println("pinterest accesstoken data not found now...");
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
                    //System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&PINTEREST&&&&&&&&");
                    //System.out.println(fbUrl);

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
                    //System.out.println("************* Controller &********************");
                    //System.out.println(returnMap);

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
                    //System.out.println("************* Controller &********************");
                    //System.out.println(returnMap);
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
                    List<Map<String, Object>> twitterData = new ArrayList<>();

                    Map<String, Object> myMapData = new HashMap<>();
                    for (Map.Entry<String, Object> entry : myData.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        myMapData.put(key, value + "");
                    }
                    twitterData.add(myMapData);

                    List<ColumnDef> columnDefObject = getColumnDefObject(twitterData);

                    /////////////////////////////////////////////////////////
                    //System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    //System.out.println(twitterData);
                    //System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//                fbData.lastIndexOf(jsonObj);
//                String likesCount = fbData.size() + "";
//                Map<String, String> boardsSize = new HashMap<>();
//                boardsSize.put("total_pin_likes", likesCount);
//                List<Map<String, String>> listData = new ArrayList<>();
//                listData.add(boardsSize);
//
                    returnMap.put("columnDefs", columnDefObject);

                    returnMap.put("data", twitterData);
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
                    url = dataSet.getUrl();
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
        String dataSetId = getFromMultiValueMap(request, "dataSetId");
        String dataSetReportName = getFromMultiValueMap(request, "dataSetReportName");
        String timeSegment = getFromMultiValueMap(request, "timeSegment");
        if (timeSegment != null && (timeSegment.isEmpty() || timeSegment.equalsIgnoreCase("undefined") || timeSegment.equalsIgnoreCase("null") || timeSegment.equalsIgnoreCase("none"))) {
            timeSegment = null;
        }
        String productSegment = getFromMultiValueMap(request, "productSegment");
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
                dataSetReportName = (dataSetReportName == null || dataSetReportName.isEmpty()) ? dataSet.getReportName() : dataSetReportName;
                timeSegment = (timeSegment == null || timeSegment.isEmpty()) ? dataSet.getTimeSegment() : timeSegment;
                productSegment = (productSegment == null || productSegment.isEmpty()) ? dataSet.getProductSegment() : productSegment;
            }
        }
        String accountIdStr = getFromMultiValueMap(request, "accountId");
        String fieldsOnly = getFromMultiValueMap(request, "fieldsOnly");

        String widgetIdStr = getFromMultiValueMap(request, "widgetId");
        //System.out.println("widgetID ---> " + widgetIdStr);
        Date startDate = DateUtils.getStartDate(getFromMultiValueMap(request, "startDate"));
        //System.out.println("startDate 1 ----> " + startDate);
        Date endDate = DateUtils.getEndDate(getFromMultiValueMap(request, "endDate"));
        //System.out.println("endDate 1 ----> " + endDate);

        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
            Integer widgetId = Integer.parseInt(widgetIdStr);
            TabWidget widget = uiService.getWidgetById(widgetId);
            //System.out.println("Widget title --->" + widget.getWidgetTitle());
            //System.out.println("Date Range Name ---> " + widget.getDateRangeName());
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
        //System.out.println("SSSSSSSStartDAaaaaaaaaaaaTe ---> " + startDate);
        //System.out.println("EEEEEEEEndDAaaaaaaaaaaaTe ---> " + endDate);

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
                timeSegment = (timeSegment == null) ? dataSet.getTimeSegment() : timeSegment;
                productSegment = (productSegment == null) ? dataSet.getProductSegment() : productSegment;
                filter = (filter == null) ? dataSet.getNetworkType() : filter;
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
        String accountIdStr = getFromMultiValueMap(request, "accountId");
        Date startDate = DateUtils.getStartDate(getFromMultiValueMap(request, "startDate"));
        // //System.out.println("startDate 1 ----> " + startDate);

        Date endDate = DateUtils.getEndDate(getFromMultiValueMap(request, "endDate"));
        // //System.out.println("endDate 1 ----> " + endDate);
        String fieldsOnly = getFromMultiValueMap(request, "fieldsOnly");
        String widgetIdStr = getFromMultiValueMap(request, "widgetId");
        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
            Integer widgetId = Integer.parseInt(widgetIdStr);
            TabWidget widget = uiService.getWidgetById(widgetId);
            //System.out.println("Widget title --->" + widget.getWidgetTitle());
            //System.out.println("Date Range Name ---> " + widget.getDateRangeName());
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
                        // //System.out.println("CITY CRITERIA CLASS  " + cityCriteria.getClass());
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
        // //System.out.println(data);
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
        // //System.out.println("Report Name" + request.getParameter("reportName"));
        // //System.out.println("Time Segment" + request.getParameter("timeSegment"));
        // //System.out.println("Product Segment" + request.getParameter("productSegment"));
        // //System.out.println("filter " + request.getParameter("filter"));

        List<Map<String, Object>> data = adwordsService.getAdwordsReport(request.getParameter("reportName"), DateUtils.get30DaysBack(), new Date(), "827-719-8225", request.getParameter("timeSegment"), request.getParameter("productSegment"), request.getParameter("filter"));
        // //System.out.println(data);
        Map returnMap = new HashMap();
        String fieldsOnly = request.getParameter("fieldsOnly");
        List<ColumnDef> columnDefs = getColumnDefObject(data);
        returnMap.put("columnDefs", columnDefs);
        returnMap.put("data", data);
        // //System.out.println(returnMap);
        return returnMap;
    }

    @RequestMapping(value = "testGa", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object testGa(HttpServletRequest request, HttpServletResponse response) {
        // //System.out.println("Report Name" + request.getParameter("reportName"));
        // //System.out.println("Time Segment" + request.getParameter("timeSegment"));
        // //System.out.println("Product Segment" + request.getParameter("productSegment"));
        // //System.out.println("filter " + request.getParameter("filter"));
        return gaService.getGaReport(request.getParameter("reportName"), "112725239", DateUtils.get30DaysBack(), new Date(), request.getParameter("timeSegment"), request.getParameter("productSegment"), null);
    }

    //linkedin 
    Map getLinkedInData(MultiValueMap request, HttpServletResponse response) {

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
                dataSetReportName = (dataSetReportName == null) ? dataSet.getReportName() : dataSetReportName;
                timeSegment = (timeSegment == null) ? dataSet.getTimeSegment() : timeSegment;
            }
        }
        String accountIdStr = getFromMultiValueMap(request, "accountId");
        //System.out.println("Linkedin Account ID-->" + accountIdStr);
        Date startDate = DateUtils.getStartDate(getFromMultiValueMap(request, "startDate"));
        //System.out.println("startDate 1 ----> " + startDate);

        Date endDate = DateUtils.getEndDate(getFromMultiValueMap(request, "endDate"));
        //System.out.println("endDate 1 ----> " + endDate);
        String widgetIdStr = getFromMultiValueMap(request, "widgetId");

        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
            Integer widgetId = Integer.parseInt(widgetIdStr);
            TabWidget widget = uiService.getWidgetById(widgetId);
            //System.out.println("Widget title --->" + widget.getWidgetTitle());
            //System.out.println("Date Range Name ---> " + widget.getDateRangeName());
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

        String accessToken = "AQVrr3w94F9NPdypSkVL_mY1hpRBlbg0DjsAymBxVnIvKw91gdapkEZt-hIUdzC34AZfgShbH17iWw0ef8VtT7gSKQsQ8mtPt2d9w_soy5FnKJaZgSHiT-Ug9MnzmB3fjlR2_tc6OoGmgeaMEuAHV3Yvnb-gzRg2TC4Aez2pUNR9jiv5WWM";

        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        String linkedinAccountId = getAccountId(accountProperty, "linkedinAccountId");
        try {
            Long linkedInaccountId = Long.parseLong(linkedinAccountId);

            List<Map<String, Object>> data = linkedinService.get(accessToken, linkedInaccountId, dataSetReportName,
                    startDate, endDate, timeSegment, productSegment);
            log.debug(data);
            //System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            //System.out.println(data);
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
        // //System.out.println("Calling of getFbData function in ProxyController class");
        String dataSetId = getFromMultiValueMap(request, "dataSetId");
        String dataSetReportName = getFromMultiValueMap(request, "dataSetReportName");
        String timeSegment = getFromMultiValueMap(request, "timeSegment");
        String productSegment = getFromMultiValueMap(request, "productSegment");
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
                dataSetReportName = (dataSetReportName == null || dataSetReportName.isEmpty()) ? dataSet.getReportName() : dataSetReportName;
                timeSegment = (timeSegment == null || timeSegment.isEmpty()) ? dataSet.getTimeSegment() : timeSegment;
                productSegment = (productSegment == null || productSegment.isEmpty()) ? dataSet.getProductSegment() : productSegment;
            }
        }
        String accountIdStr = getFromMultiValueMap(request, "accountId");
        Date startDate = DateUtils.getStartDate(getFromMultiValueMap(request, "startDate"));
        // //System.out.println("startDate 1 ----> " + startDate);

        Date endDate = DateUtils.getEndDate(getFromMultiValueMap(request, "endDate"));
        // //System.out.println("endDate 1 ----> " + endDate);
        String fieldsOnly = getFromMultiValueMap(request, "fieldsOnly");
        String widgetIdStr = getFromMultiValueMap(request, "widgetId");
        if (widgetIdStr != null && !widgetIdStr.isEmpty() && !widgetIdStr.equalsIgnoreCase("undefined")) {
            Integer widgetId = Integer.parseInt(widgetIdStr);
            TabWidget widget = uiService.getWidgetById(widgetId);
            //System.out.println("Widget title --->" + widget.getWidgetTitle());
            //System.out.println("Date Range Name ---> " + widget.getDateRangeName());
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
        String facebookAccountId = getAccountId(accountProperty, "facebookAccountId");
        String facebookOrganicAccountId = getAccountId(accountProperty, "facebookOrganicAccountId");
        Long facebookAccountIdInt = Long.parseLong(facebookAccountId);
        Long facebookOrganicAccountIdInt = null;
        if (facebookOrganicAccountId != null) {
            facebookOrganicAccountIdInt = Long.parseLong(facebookOrganicAccountId);
        }
        String accessToken = "EAAUAycrj0GsBAM3EgwLcQjz5zywESZBpHN76cERZCaxEZC9ZAzMjRzRxIznWM3u8s4DBwUvhMaQAGglDOIa9tSV7ZCVf9ZBajV9aA6khaCRmEZAQhIHUInBVYZBZAT5nycwniZCozuLcjhTm0eW5tAUxIugmvxszsivmh5ZClzuMZApZBJxd0RZBIDk1r0";
        List<Map<String, Object>> data = facebookService.get(accessToken, dataSetReportName, facebookAccountIdInt,
                facebookOrganicAccountIdInt, startDate, endDate, timeSegment, productSegment);
        // //System.out.println("FbData list ----> " + data);
//        Date startDate = DateUtils.getSixMonthsBack(new Date()); // 1348734005171064L
//        Date endDate = new Date();
//        List<Map<String, String>> data = facebookService.get(accessToken, "accountPerformance", 1348731135171351L, startDate, endDate, "daily");
        Map returnMap = new HashMap();
        List<ColumnDef> columnDefs = getColumnDefObject(data);
        returnMap.put("columnDefs", columnDefs);
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
                    dataSetReportName = (dataSetReportName == null || dataSetReportName.isEmpty()) ? dataSet.getReportName() : dataSetReportName;
                    timeSegment = (timeSegment == null || timeSegment.isEmpty()) ? dataSet.getTimeSegment() : timeSegment;
                    productSegment = (productSegment == null || productSegment.isEmpty()) ? dataSet.getProductSegment() : productSegment;
                }
            }
            valueMap.put("timeSegment", Arrays.asList(timeSegment));
            valueMap.put("productSegment", Arrays.asList(productSegment));
            valueMap.put("dataSetReportName", Arrays.asList(dataSetReportName));

//            //System.out.println("My dataSetReportName -->"+dataSetReportName);
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
            //System.out.println("url: " + url);
            log.debug("valuemap: " + valueMap);
            //System.out.println("valuemap: " + valueMap);
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

//    private List<Map<String, Object>> getBingData(MultiValueMap<String, String> valueMap, HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String accountIdStr = getFromMultiValueMap(valueMap, "accountId");
//            Integer accountId = Integer.parseInt(accountIdStr);
//            Account account = userService.getAccountId(accountId);
//            List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
//
//            for (Iterator<Property> iterator = accountProperty.iterator(); iterator.hasNext();) {
//                Property property = iterator.next();
//                List<String> valueList = new ArrayList();
//                valueList.add(property.getPropertyValue());
//                valueMap.put(property.getPropertyName(), valueList);
//            }
//            String dataSetId = getFromMultiValueMap(valueMap, "dataSetId");
//            String dataSetReportName = getFromMultiValueMap(valueMap, "dataSetReportName");
//            String timeSegment = getFromMultiValueMap(valueMap, "timeSegment");
//            String productSegment = getFromMultiValueMap(valueMap, "productSegment");
//            if (timeSegment == null) {
//                timeSegment = "daily";
//            }
//            if (productSegment == null) {
//                productSegment = "none";
//            }
//            Integer dataSetIdInt = null;
//            DataSet dataSet = null;
//            if (dataSetId != null) {
//                try {
//                    dataSetIdInt = Integer.parseInt(dataSetId);
//                } catch (Exception e) {
//
//                }
//                if (dataSetIdInt != null) {
//                    dataSet = uiService.readDataSet(dataSetIdInt);
//                }
//                if (dataSet != null) {
//                    dataSetReportName = (dataSetReportName == null || dataSetReportName.isEmpty()) ? dataSet.getReportName() : dataSetReportName;
//                    timeSegment = (timeSegment == null || timeSegment.isEmpty()) ? dataSet.getTimeSegment() : timeSegment;
//                    productSegment = (productSegment == null  || productSegment.isEmpty()) ? dataSet.getProductSegment() : productSegment;
//                }
//            }
//            valueMap.put("timeSegment", Arrays.asList(timeSegment));
//            valueMap.put("productSegment", Arrays.asList(productSegment));
//            valueMap.put("dataSetReportName", Arrays.asList(dataSetReportName));
//
////            System.out.println("My dataSetReportName -->"+dataSetReportName);
//            String url = "../dbApi/admin/bing/getData";
//            Integer port = 80;
//            if (request != null) {
//                port = request.getServerPort();
//            }
//
//            String localUrl = "http://localhost/";
//            if (request != null) {
//                localUrl = request.getScheme() + "://" + request.getServerName() + ":" + port + "/";
//            }
//            log.debug("UR:" + url);
//            if (url.startsWith("../")) {
//                url = url.replaceAll("\\.\\./", localUrl);
//            }
//            log.debug("url: " + url);
//            System.out.println("url: " + url);
//            log.debug("valuemap: " + valueMap);
//            System.out.println("valuemap: " + valueMap);
//            String data = Rest.getData(url, valueMap);
//            JSONParser parser = new JSONParser();
//            Object jsonObj = parser.parse(data);
//            List dataList = JsonSimpleUtils.toList((JSONArray) jsonObj);
//            return dataList;
//        } catch (ParseException ex) {
//            java.util.logging.Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }

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
                    columnDefs.add(new ColumnDef(key, fieldProperties.getDataType() == null ? "string" : fieldProperties.getDataType(), fieldProperties.getDisplayName(), fieldProperties.getAgregationFunction(), fieldProperties.getDisplayFormat()));
                } else {
                    Object value = entrySet.getValue();
                    String valueString = value + "";
                    valueString = valueString.replaceAll("^\"|\"$", "");
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

        //System.out.println("startDate 1 -->" + startDate1);
        //System.out.println("endDate 1 -->" + endDate1);
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
        // //System.out.println("Start Function of download");
        String dealerId = request.getParameter("dealerId");
        String exportType = request.getParameter("exportType");
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);

        Date startDate1 = DateUtils.getStartDate(request.getParameter("startDate"));
        // //System.out.println("startDate 1 ----> " + startDate1);
        Date endDate1 = DateUtils.getEndDate(request.getParameter("endDate"));
        // //System.out.println("endDate 1 ----> " + endDate1);

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
            // //System.out.println("key ---> " + key + " value ---> " + value);
            valueMap.put(key, Arrays.asList(value));
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entrySet : parameterMap.entrySet()) {
            String key = entrySet.getKey();
            String[] value = entrySet.getValue();
            // //System.out.println("key ---> " + key + " value ---> " + value);
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
                // //System.out.println("startDate  ----> " + startDate);
                Date endDate = DateUtils.getEndDate(request.getParameter("endDate"));
                // //System.out.println("endDate  ----> " + endDate);
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
                // //System.out.println("tabWidget Id---> " + tabWidget.getId());
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
                // //System.out.println("valueMap ---> " + valueMap);
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }

}
