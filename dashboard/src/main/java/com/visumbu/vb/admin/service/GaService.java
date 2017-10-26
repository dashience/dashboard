/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.OrderBy;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.utils.DateUtils;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analytics.model.Goal;
import com.google.api.services.analytics.model.Goals;
import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.bean.GaReport;
import com.visumbu.vb.model.DefaultFieldProperties;
import com.visumbu.vb.utils.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Varghees
 */
@Service("gaService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GaService {

    @Autowired
    private UiDao uiDao;
    // private static final String CLIENT_SECRET_JSON_RESOURCE = "F:\\GaToken\\client_secret_384381056232-sqrgb2u8j26gbkqi6dis682ojapsf85a.apps.googleusercontent.com.json";
    // Replace with your view ID.
    private static final String VIEW_ID = "82176546";
    // The directory where the user's credentials will be stored.
    private static final File DATA_STORE_DIR = new File("/tmp/");

    private static final String APPLICATION_NAME = "Hello Analytics Reporting";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static NetHttpTransport httpTransport;
    private static FileDataStoreFactory dataStoreFactory;
    private static AnalyticsReporting analyticsReporting = initializeAnalyticsReporting();

    private static Analytics initializeAnalytics() throws Exception {
        // Initializes an authorized analytics service object.

        // Construct a GoogleCredential object with the service account email
        // and p12 file downloaded from the developer console.
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

        GoogleAuthorizationCodeFlow flow1 = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, "162577857765-uanp79mjictf7bkla9gotj5dhk4nr0ka.apps.googleusercontent.com", "xXIHWHPBQ9B9KpkFs_1tmniu",
                Collections.singleton(AnalyticsScopes.ANALYTICS_READONLY)).setDataStoreFactory(
                dataStoreFactory).build();
        // Authorize.
        Credential credential = new AuthorizationCodeInstalledApp(flow1,
                new LocalServerReceiver()).authorize("user");

        // Construct the Analytics service object.
        return new Analytics.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Initializes an authorized Analytics Reporting service object.
     *
     * @return The analytics reporting service object.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private static AnalyticsReporting initializeAnalyticsReporting() {

        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

            // Load client secrets.
            /*GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
             new InputStreamReader(HelloAnalytics.class
             .getResourceAsStream(CLIENT_SECRET_JSON_RESOURCE)));
             */
            // Set up authorization code flow for all authorization scopes.
            /*GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
             AnalyticsReportingScopes.all()).setDataStoreFactory(dataStoreFactory)
             .build();
             */
            GoogleAuthorizationCodeFlow flow1 = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, JSON_FACTORY, "384381056232-sqrgb2u8j26gbkqi6dis682ojapsf85a.apps.googleusercontent.com", "1nJygCmZKdFCOykaGmbjBpKy",
                    Collections.singleton(AnalyticsScopes.ANALYTICS_READONLY)).setDataStoreFactory(
                    dataStoreFactory).build();
            // Authorize.
            Credential credential = new AuthorizationCodeInstalledApp(flow1,
                    new LocalServerReceiver()).authorize("user");
            // Construct the Analytics Reporting service object.
            return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(GaService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Map<String, List<Map<String, Object>>> getGaReport(String reportName, String analyticsProfileId, Date startDate, Date endDate, String reqDimensions, String reqProductSegments, Integer dataSetId, String filter) {
        Map<String, GaReport> gaReports = ApiUtils.getAllGaReports();
        GaReport gaReport = gaReports.get(reportName);
        String metricsList = gaReport.getFields();
        String productSegments = reqProductSegments == null ? null : reqProductSegments;
        if (productSegments == null || productSegments.trim().isEmpty() || productSegments.trim().equalsIgnoreCase("none")) {
            productSegments = null;
        }
        String dimensions = reqDimensions;
        if (dimensions == null || dimensions.trim().isEmpty() || dimensions.trim().equalsIgnoreCase("none")) {
            dimensions = gaReport.getDefaultDimension();
        }
        if (dimensions == null || dimensions.trim().isEmpty() || dimensions.trim().equalsIgnoreCase("none")) {
            dimensions = null;
        }
//        String filter = gaReport.getDefaultFilter();
        String gaFilter = "ga:region==" + filter;
        if(filter.equalsIgnoreCase("all")) {
            gaFilter = null;
        }
        System.out.println("Metric List " + metricsList + " Product Segments " + productSegments + " dimensions " + dimensions + " Filter " + gaFilter);
        GetReportsResponse gaData = getGenericData(analyticsProfileId, startDate, endDate, null, null, metricsList, dimensions, productSegments, gaFilter);
        return getResponseAsMap(gaData);
    }

    public GetReportsResponse getGenericData(String viewId, Date startDate1, Date endDate1, Date startDate2, Date endDate2, String metrics, String dimentions, String productSegments, String filter) {
        System.out.println(viewId);
        try {
            List<DateRange> dateRangeList = new ArrayList<>();
            DateRange dateRange = new DateRange();
            dateRange.setStartDate(DateUtils.getGaStartDate(startDate1));
            dateRange.setEndDate(DateUtils.getGaEndDate(endDate1));
            dateRangeList.add(dateRange);

            if (startDate2 != null) {
                DateRange dateRange1 = new DateRange();
                dateRange1.setStartDate(DateUtils.getGaStartDate(startDate2));
                if (endDate2 != null) {
                    dateRange1.setEndDate(DateUtils.getGaEndDate(endDate2));
                } else {
                    dateRange1.setEndDate(DateUtils.getGaEndDate(startDate1));
                }
                dateRangeList.add(dateRange1);
            }

            String[] metricsArray = metrics.split(";");
            List<Metric> metricList = new ArrayList<>();
            for (int i = 0; i < metricsArray.length; i++) {
                String metricStr = metricsArray[i];
                String[] nameAliasArray = metricStr.split(",");
                if (nameAliasArray[0].isEmpty() || nameAliasArray[0].equalsIgnoreCase("none")) {
                    continue;
                }
                if (nameAliasArray.length >= 2) {
                    Metric metric = new Metric()
                            .setExpression(nameAliasArray[0])
                            .setAlias(nameAliasArray[1]);
                    metricList.add(metric);
                } else if (nameAliasArray.length >= 1) {
                    Metric metric = new Metric()
                            .setExpression(nameAliasArray[0]);
                    metricList.add(metric);
                }
            }

            List<Dimension> dimensionList = new ArrayList<>();
            if (dimentions != null) {
                String[] dimensionArray = dimentions.split(";");
                for (int i = 0; i < dimensionArray.length; i++) {
                    String dimensionStr = dimensionArray[i];
                    if (dimensionStr.isEmpty() || dimensionStr.equalsIgnoreCase("none")) {
                        continue;
                    }
                    Dimension dimension = new Dimension()
                            .setName(dimensionStr);
                    dimensionList.add(dimension);

                }
            }
            if (productSegments != null) {
                String[] productSegmentArray = productSegments.split(";");
                for (int i = 0; i < productSegmentArray.length; i++) {
                    String productSegment = productSegmentArray[i];
                    String[] nameAliasArray = productSegment.split(",");
                    if (nameAliasArray[0].isEmpty() || nameAliasArray[0].equalsIgnoreCase("none")) {
                        continue;
                    }
                    if (nameAliasArray.length >= 2) {
                        Dimension dimension = new Dimension()
                                .setName(nameAliasArray[0]);
                        dimensionList.add(dimension);
                    } else if (nameAliasArray.length >= 1) {
                        Dimension dimension = new Dimension()
                                .setName(nameAliasArray[0]);
                        dimensionList.add(dimension);
                    }
                }
            }
            ReportRequest request = new ReportRequest()
                    .setViewId(viewId)
                    .setDateRanges(dateRangeList)
                    //.setDimensions(dimensionList)
                    //.setFiltersExpression(filter)
                    .setMetrics(metricList)
                    .setPageSize(100000);
            if (filter != null) {
                request.setFiltersExpression(filter);
            }
            if (dimensionList != null && !dimensionList.isEmpty()) {
                request.setDimensions(dimensionList);
            }
            ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
            requests.add(request);
            // Create the GetReportsRequest object.
            GetReportsRequest getReport = new GetReportsRequest()
                    .setReportRequests(requests);

            // Call the batchGet method.
            System.out.println("get Report ---> " + getReport);
            GetReportsResponse response = analyticsReporting.reports().batchGet(getReport).execute();

            // Return the response.
            return response;
        } catch (IOException ex) {
            Logger.getLogger(GaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public GetReportsResponse getGenericData(String viewId, Date startDate1, Date endDate1, Date startDate2, Date endDate2,
            String metrics, String dimentions, String filter, String orderBy, Integer maxResults) {
        try {
            List<DateRange> dateRangeList = new ArrayList<>();
            DateRange dateRange = new DateRange();
            dateRange.setStartDate(DateUtils.getGaStartDate(startDate1));
            dateRange.setEndDate(DateUtils.getGaEndDate(endDate1));
            dateRangeList.add(dateRange);

            if (startDate2 != null) {
                DateRange dateRange1 = new DateRange();
                dateRange1.setStartDate(DateUtils.getGaStartDate(startDate2));
                if (endDate2 != null) {
                    dateRange1.setEndDate(DateUtils.getGaEndDate(endDate2));
                } else {
                    dateRange1.setEndDate(DateUtils.getGaEndDate(startDate1));
                }
                dateRangeList.add(dateRange1);
            }
            String[] metricsArray = metrics.split(";");
            List<Metric> metricList = new ArrayList<>();
            for (int i = 0; i < metricsArray.length; i++) {
                String metricStr = metricsArray[i];
                String[] nameAliasArray = metricStr.split(",");
                if (nameAliasArray.length >= 2) {
                    Metric metric = new Metric()
                            .setExpression(nameAliasArray[0])
                            .setAlias(nameAliasArray[1]);
                    metricList.add(metric);
                } else if (nameAliasArray.length >= 1) {
                    Metric metric = new Metric()
                            .setExpression(nameAliasArray[0]);
                    metricList.add(metric);
                }
            }

            String[] dimensionArray = dimentions.split(";");
            List<Dimension> dimensionList = new ArrayList<>();
            for (int i = 0; i < dimensionArray.length; i++) {
                String dimensionStr = dimensionArray[i];
                Dimension dimension = new Dimension()
                        .setName(dimensionStr);
                dimensionList.add(dimension);

            }
            List<OrderBy> orderByList = new ArrayList<>();
            String[] orderByArr = orderBy.split(";");
            for (int i = 0; i < orderByArr.length; i++) {
                String orderByStr = orderByArr[i];
                OrderBy orderByVal = new OrderBy();
                orderByVal.setFieldName(orderByStr);
                orderByVal.setSortOrder("DESCENDING");
                orderByList.add(orderByVal);
            }

            ReportRequest request = new ReportRequest()
                    .setViewId(viewId)
                    .setDateRanges(dateRangeList)
                    .setDimensions(dimensionList)
                    .setPageSize(maxResults)
                    .setFiltersExpression(filter)
                    .setOrderBys(orderByList)
                    .setMetrics(metricList);
            ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
            requests.add(request);
            // Create the GetReportsRequest object.
            GetReportsRequest getReport = new GetReportsRequest()
                    .setReportRequests(requests);

            // Call the batchGet method.
            GetReportsResponse response = analyticsReporting.reports().batchGet(getReport).execute();

            // Return the response.
            return response;
        } catch (IOException ex) {
            Logger.getLogger(GaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Map<String, List<Map<String, Object>>> getResponseAsMap(GetReportsResponse response) {
        Map returnMap = new HashMap();

        for (Report report : response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();
            List<ColumnDef> columnDefs = new ArrayList<>();
            if (dimensionHeaders != null) {
                for (int i = 0; i < dimensionHeaders.size(); i++) {
                    String key = dimensionHeaders.get(i);
                    key = key.replaceAll("ga:", "");
                    DefaultFieldProperties fieldProperties = uiDao.getDefaultFieldProperties(key);
                    if (fieldProperties != null) {
                        columnDefs.add(new ColumnDef(key, fieldProperties.getDataType() == null ? "string" : fieldProperties.getDataType(), fieldProperties.getDisplayName(), fieldProperties.getAgregationFunction(), fieldProperties.getDisplayFormat()));
                    } else {
                        columnDefs.add(new ColumnDef(key, "string", key));
                    }
                }
            }
            for (int i = 0; i < metricHeaders.size(); i++) {
                String key = metricHeaders.get(i).getName();
                key = key.replaceAll("ga:", "");
                String type = metricHeaders.get(i).getType();
                DefaultFieldProperties fieldProperties = uiDao.getDefaultFieldProperties(key);
                if (fieldProperties != null) {
                    columnDefs.add(new ColumnDef(key, fieldProperties.getDataType() == null ? "string" : fieldProperties.getDataType(), fieldProperties.getDisplayName(), fieldProperties.getAgregationFunction(), fieldProperties.getDisplayFormat()));
                } else {
                    columnDefs.add(new ColumnDef(key, "string", key));
                }
            }
            returnMap.put("columnDefs", columnDefs);
            if (rows == null) {
                System.out.println("No data found for ");
                return new HashMap();
            }
            List<Map<String, Object>> data = new ArrayList<>();
            for (ReportRow row : rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();
                Map<String, Object> dataMap = new HashMap();
                if (dimensionHeaders != null) {
                    for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                        System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                        String key = dimensionHeaders.get(i);
                        key = key.replaceAll("ga:", "");
                        dataMap.put(key, dimensions.get(i));
                    }
                }
                for (int j = 0; j < metrics.size(); j++) {
                    System.out.print("Date Range (" + j + "): ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        String key = metricHeaders.get(k).getName();
                        key = key.replaceAll("ga:", "");
                        dataMap.put(key, values.getValues().get(k));
                        System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
                    }
                }
                data.add(dataMap);
            }
            returnMap.put("data", data);
        }
        return returnMap;
    }

    /**
     * Parses and prints the Analytics Reporting API V4 response.
     *
     * @param response the Analytics Reporting API V4 response.
     */
    private static void printResponse(GetReportsResponse response) {

        for (Report report : response.getReports()) {
            ColumnHeader header = report.getColumnHeader();
            List<String> dimensionHeaders = header.getDimensions();
            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            List<ReportRow> rows = report.getData().getRows();

            if (rows == null) {
                System.out.println("No data found for ");
                return;
            }

            for (ReportRow row : rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();
                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                    System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                }

                for (int j = 0; j < metrics.size(); j++) {
                    System.out.print("Date Range (" + j + "): ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
                    }
                }
            }
        }
    }

    /**
     * Query the Analytics Reporting API V4. Constructs a request for the
     * sessions for the past seven days. Returns the API response.
     *
     * @param service
     * @return GetReportResponse
     * @throws IOException
     */
    private static GetReportsResponse getReport(AnalyticsReporting service) throws IOException {
        // Create the DateRange object.
        List<DateRange> dateRangeList = new ArrayList<>();
// The start date for the query in the format `YYYY-MM-DD`.
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("7DaysAgo");
        dateRange.setEndDate("today");

        dateRangeList.add(dateRange);
        DateRange dateRange1 = new DateRange();
        dateRange1.setStartDate("14DaysAgo");
        dateRange1.setEndDate("7DaysAgo");
        dateRangeList.add(dateRange1);
        // Create the Metrics object.
        Metric sessions = new Metric()
                .setExpression("ga:sessions")
                .setAlias("sessions");

        //Create the Dimensions object.
        Dimension browser = new Dimension()
                .setName("ga:browser");

        // Create the ReportRequest object.
        ReportRequest request = new ReportRequest()
                .setViewId(VIEW_ID)
                .setDateRanges(dateRangeList)
                .setDimensions(Arrays.asList(browser))
                .setMetrics(Arrays.asList(sessions));

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = service.reports().batchGet(getReport).execute();

        // Return the response.
        return response;
    }

    public static String getGaGoals(String accountId, String viewId) {
        String returnStr = "";
        try {
            Analytics analytics = initializeAnalytics();
            try {
                Goals goals = analytics.management().goals().list(accountId, "~all", "~all").execute();
                for (Goal goal : goals.getItems()) {
                    if (goal.getProfileId().equalsIgnoreCase(viewId) && goal.getActive()) {
                        if (goal.getName().equalsIgnoreCase("Directions Page View") || goal.getName().equalsIgnoreCase("Directions Page Views")) {
                            returnStr += "ga:goal" + goal.getId() + "Completions,directionsPageView;";
                        } else if (goal.getName().equalsIgnoreCase("Inventory Page View") || goal.getName().equalsIgnoreCase("Inventory Page Views")) {
                            returnStr += "ga:goal" + goal.getId() + "Completions,inventoryPageViews;";
                        } else if (goal.getName().equalsIgnoreCase("Lead Submission") || goal.getName().equalsIgnoreCase("Lead Submission - General")) {
                            returnStr += "ga:goal" + goal.getId() + "Completions,leadSubmission;";
                        } else if (goal.getName().equalsIgnoreCase("Specials Page Views") || goal.getName().equalsIgnoreCase("Specials Page View")) {
                            returnStr += "ga:goal" + goal.getId() + "Completions,specialsPageView;";
                        } else if (goal.getName().equalsIgnoreCase("Time on Site > 2 Min") || goal.getName().equalsIgnoreCase("Time on Site > 2 Minutes")) {
                            returnStr += "ga:goal" + goal.getId() + "Completions,timeOnSiteGt2Mins;";
                        } else if (goal.getName().equalsIgnoreCase("VDP Views")) {
                            returnStr += "ga:goal" + goal.getId() + "Completions,vdpViews;";
                        }
                    }
                }

            } catch (GoogleJsonResponseException e) {
                System.err.println("There was a service error: "
                        + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
            }

        } catch (Exception ex) {
            Logger.getLogger(GaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnStr;
    }

    public static void main(String[] args) {
        try {
            getGaGoals("43651400", "79919517");
            AnalyticsReporting service = initializeAnalyticsReporting();
            GetReportsResponse response = getReport(service);
            printResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
