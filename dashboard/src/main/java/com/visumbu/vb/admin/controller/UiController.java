/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.dao.bean.DataSourceBean;
import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.bean.TabWidgetBean;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.UserAccount;
import com.visumbu.vb.model.UserPermission;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

// linked in api imports
import com.visumbu.vb.admin.service.FacebookService;
import com.visumbu.vb.bean.DatasetColumnBean;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.DatasetColumns;

import com.visumbu.vb.model.Currency;
import com.visumbu.vb.model.Timezone;
import com.visumbu.vb.model.WidgetTag;

import com.visumbu.vb.utils.Rest;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 *
 * @author netphenix
 */
@Controller
@RequestMapping("ui")
public class UiController extends BaseController {

    @Autowired
    private UiService uiService;

    @Autowired
    private UserService userService;

    @Autowired
    private FacebookService facebookService;

    private Rest rest;

    @RequestMapping(value = "product", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getProduct(HttpServletRequest request, HttpServletResponse response) {
        return uiService.getProduct();
    }

    @RequestMapping(value = "product/{dealerId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDealerProduct(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dealerId) {
        return uiService.getDealerProduct(dealerId);
    }

    @RequestMapping(value = "dashboard", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDashboards(HttpServletRequest request, HttpServletResponse response) {
        VbUser user = userService.findByUsername(getUser(request));
        if (user == null) {
            return null;
        }
        return uiService.getDashboards(user);
    }

//    @RequestMapping(value = "dbTabs/{dashboardId}", method = RequestMethod.POST, produces = "application/json")
//    public @ResponseBody
//    DashboardTabs createDashboardTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId, @RequestBody DashboardTabs dashboardTabs) {
//        dashboardTabs.setDashboardId(uiService.getDashboardById(dashboardId));
//        return uiService.createDashboardTabs(dashboardTabs);
//    }
    @RequestMapping(value = "dbTabs/{agencyProductId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DashboardTabs createAgencyProductTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer agencyProductId, @RequestBody DashboardTabs dashboardTabs) {
        dashboardTabs.setAgencyProductId(uiService.getAgencyProductById(agencyProductId));
        return uiService.createDashboardTabs(dashboardTabs);
    }

    @RequestMapping(value = "dbTabs/{agencyProductId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DashboardTabs updateAgencyProductTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer agencyProductId, @RequestBody DashboardTabs dashboardTab) {
        return uiService.updateTab(dashboardTab);
    }

//    @RequestMapping(value = "dbTabs/{dashboardId}", method = RequestMethod.PUT, produces = "application/json")
//    public @ResponseBody
//    DashboardTabs updateTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId, @RequestBody DashboardTabs dashboardTab) {
//        return uiService.updateTab(dashboardTab);
//    }
    @RequestMapping(value = "dbTabUpdateOrder/{agencyProductId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object updateAgencyProductTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer agencyProductId) {
        String tabOrder = request.getParameter("tabOrder");
        uiService.updateAgencyProductTab(agencyProductId, tabOrder);
        return null;
    }

    @RequestMapping(value = "dbTabs/{agencyProductId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getAgencyProductTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer agencyProductId) {
        return uiService.getAgencyProductTab(agencyProductId);
    }

//    @RequestMapping(value = "dbTabs/{dashboardId}", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    List getDashboardTabs(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dashboardId) {
//        VbUser user = userService.findByUsername(getUser(request));
//        if (user == null) {
//            return null;
//        }
//        return uiService.getDashboardTabsByProductDashboard(dashboardId, user.getId());
//    }
    @RequestMapping(value = "dbTab/{tabId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DashboardTabs deleteDashboardTab(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        return uiService.deleteDashboardTab(tabId);
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    TabWidget createTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId, @RequestBody TabWidgetBean tabWidget) {
        return uiService.saveTabWidget(tabId, tabWidget);
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    TabWidget updateTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId, @RequestBody TabWidgetBean tabWidget) {
        System.out.println("save tab widget");
        return uiService.saveTabWidget(tabId, tabWidget);
        //return null; //uiService.createTabWidget(tabId, tabWidget);
    }

    @RequestMapping(value = "dbWidgetDuplicate/{widgetId}/{tabId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    TabWidget getWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId, @PathVariable Integer tabId) {
        return uiService.getWidget(widgetId, tabId);
    }

    @RequestMapping(value = "dbDuplicateTag/{widgetId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<WidgetTag> getTagWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId) {
        return uiService.getTagWidget(widgetId);
    }

    @RequestMapping(value = "dbWidgetUpdateOrder/{tabId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Object updateWidgetUpdateOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        String widgetOrder = request.getParameter("widgetOrder");
        uiService.updateWidgetUpdateOrder(tabId, widgetOrder);
        return null;
    }

    @RequestMapping(value = "dbWidget/{tabId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer tabId) {
        return uiService.getTabWidget(tabId);
    }

    @RequestMapping(value = "reportWidgetByWidgetId/{widgetId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReportWidgetByWidgetId(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId) {
        return uiService.getReportWidgetByWidgetId(widgetId);
    }

    @RequestMapping(value = "dbWidget/{widgetId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    TabWidget deleteTabWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId) {
        return uiService.deleteTabWidget(widgetId);
    }

    @RequestMapping(value = "widgetColumn/{widgetId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    WidgetColumn addWidgetColumn(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId, @RequestBody WidgetColumn widgetColumn) {
        return uiService.addWidgetColumn(widgetId, widgetColumn);
    }

//    @RequestMapping(value = "widgetColumn/{widgetId}", method = RequestMethod.POST, produces = "application/json")
//    public @ResponseBody
//    WidgetColumn getWidgetColumn(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId, @RequestBody WidgetColumn widgetColumn) {
//        return uiService.getWidgetColumn(widgetId);
//    }
    @RequestMapping(value = "widgetColumn/{widgetId}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    WidgetColumn update(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer widgetId, @RequestBody WidgetColumn widgetColumn) {
        return uiService.updateWidgetColumn(widgetId, widgetColumn);
    }

    @RequestMapping(value = "widgetColumn/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    WidgetColumn deleteWidgetColumn(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return uiService.deleteWidgetColumn(id);
    }

//    @RequestMapping(value = "report", method = RequestMethod.POST, produces = "application/json")
//    public @ResponseBody
//    Report addReport(HttpServletRequest request, HttpServletResponse response, @RequestBody Report report) {
//        VbUser user = userService.findByUsername(getUser(request));
//        report.setAgencyId(user.getAgencyId());
//        return uiService.addReport(report);
//    }
//
//    @RequestMapping(value = "report", method = RequestMethod.PUT, produces = "application/json")
//    public @ResponseBody
//    Report updateReport(HttpServletRequest request, HttpServletResponse response, @RequestBody Report report) {
//        VbUser user = userService.findByUsername(getUser(request));
//        report.setAgencyId(user.getAgencyId());
//        return uiService.updateReport(report);
//    }
//    @RequestMapping(value = "dbReportUpdateOrder/{reportId}", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    Object updateReportUpdateOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
//        String widgetOrder = request.getParameter("widgetOrder");
//        uiService.updateReportOrder(reportId, widgetOrder);
//        return null;
//    }
//
//    @RequestMapping(value = "report/{reportId}", method = RequestMethod.DELETE, produces = "application/json")
//    public @ResponseBody
//    Report deleteReport(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
//        return uiService.deleteReport(reportId);
//    }
//
//    @RequestMapping(value = "report", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    List getReport(HttpServletRequest request, HttpServletResponse response) {
//        VbUser user = userService.findByUsername(getUser(request));
//        if (user == null) {
//            return null;
//        }
//        return uiService.getAgencyReport(user);
//    }
//    @RequestMapping(value = "report/{reportId}", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    Report getReportById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
//        return uiService.getReportById(reportId);
//    }
//    @RequestMapping(value = "report/{reportId}", method = RequestMethod.DELETE, produces = "application/json")
//    public @ResponseBody
//    Report deleteReport(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
//        return uiService.deleteReport(reportId);
//    }
//
//    @RequestMapping(value = "reportWidget", method = RequestMethod.POST, produces = "application/json")
//    public @ResponseBody
//    ReportWidget createReportWidget(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportWidget reportWidget) {
//        return uiService.createReportWidget(reportWidget);
//    }
////
//
//    @RequestMapping(value = "reportWidget", method = RequestMethod.PUT, produces = "application/json")
//    public @ResponseBody
//    ReportWidget updateReportWidget(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportWidget reportWidget) {
//        return uiService.updateReportWidget(reportWidget);
//    }
//
    @RequestMapping(value = "reportWidget", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getReportWidget(HttpServletRequest request, HttpServletResponse response) {
        return uiService.getReportWidget();
    }
//    

//    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    List getReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
//        return uiService.getReportWidget(reportId);
//    }
//
//    @RequestMapping(value = "reportWidget/{reportId}", method = RequestMethod.DELETE, produces = "application/json")
//    public @ResponseBody
//    ReportWidget deleteReportWidget(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer reportId) {
//        return uiService.deleteReportWidget(reportId);
//    }
    @RequestMapping(value = "dataSource", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DataSource create(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSourceBean dataSource) {
        VbUser user = userService.findByUsername(getUser(request));
        dataSource.setUserId(user);
        dataSource.setAgencyId(user.getAgencyId());
        return uiService.saveDataSource(dataSource);
    }

    @RequestMapping(value = "fileUpload", method = RequestMethod.POST)
    public @ResponseBody
    Object continueFileUpload(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest mRequest;
        String returnFilename = null;
        try {
            mRequest = (MultipartHttpServletRequest) request;
            mRequest.getParameterMap();

            Iterator<String> itr = mRequest.getFileNames();
            while (itr.hasNext()) {
                MultipartFile mFile = mRequest.getFile(itr.next());
                String fileName = mFile.getOriginalFilename();
                System.out.println(fileName);
                returnFilename = "/opt/datasources/" + RandomStringUtils.randomAlphanumeric(32).toUpperCase() + new Date().getTime() + "-" + fileName;
                java.nio.file.Path path = Paths.get(returnFilename);
                Files.deleteIfExists(path);
                InputStream in = mFile.getInputStream();
                Files.copy(in, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map returnMap = new HashMap();
        returnMap.put("filename", returnFilename);
        return returnMap;
    }

    @RequestMapping(value = "dataSource", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DataSource update(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSource dataSource) {
        return uiService.update(dataSource);
    }

    @RequestMapping(value = "dataSource", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDataSource(HttpServletRequest request, HttpServletResponse response) {
        VbUser user = userService.findByUsername(getUser(request));
        if (user == null) {
            return null;
        }
        return uiService.getDataSourceByUser(user);
    }

//    @RequestMapping(value = "dataSource", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    List getDataSource(HttpServletRequest request, HttpServletResponse response) {
//        return uiService.getDataSource();
//    }
    @RequestMapping(value = "dataSource/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DataSource delete(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return uiService.deleteDataSource(id);
    }

    @RequestMapping(value = "dataSet", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DataSet create(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSet dataSet) {
        VbUser user = userService.findByUsername(getUser(request));
        dataSet.setUserId(user);
        dataSet.setAgencyId(user.getAgencyId());
        return uiService.create(dataSet);
    }

    @RequestMapping(value = "dataSet", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DataSet update(HttpServletRequest request, HttpServletResponse response, @RequestBody DataSet dataSet) {
        return uiService.update(dataSet);
    }

    @RequestMapping(value = "dataSetFormulaColumns", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    List<DatasetColumns> createDataSetFormulaColumn(HttpServletRequest request, HttpServletResponse response, @RequestBody DatasetColumnBean dataSetColumn) {
        return uiService.createDataSetFormulaColumn(dataSetColumn);
    }

    @RequestMapping(value = "dataSetFormulaColumns/{datasetColumnId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DatasetColumns deleteDataSetFormulaColumnById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer datasetColumnId) {
        System.out.println("id --> " + datasetColumnId);
        return uiService.deleteDataSetFormulaColumnById(datasetColumnId);
    }

    @RequestMapping(value = "dataSetColumn/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DatasetColumns deleteDataSetColumns(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return uiService.deleteDataSetColumns(id);
    }

    @RequestMapping(value = "getDatasetById/{datasetId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDatasetById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer datasetId) {
        return uiService.getDatasetById(datasetId);
    }

    @RequestMapping(value = "dataSet", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getDataSet(HttpServletRequest request, HttpServletResponse response) {
        VbUser user = userService.findByUsername(getUser(request));
        if (user == null) {
            return null;
        }
        return uiService.getDataSetByUser(user);
    }
//    @RequestMapping(value = "dataSet", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    List getDataSet(HttpServletRequest request, HttpServletResponse response) {
//        return uiService.getDateSet();
//    }

    @RequestMapping(value = "dataSet/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DataSet deleteDataSet(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return uiService.deleteDataSet(id);
    }

//    @RequestMapping(value = "user", method = RequestMethod.GET, produces = "application/json")
//    public @ResponseBody
//    List getUser(HttpServletRequest request, HttpServletResponse response) {
//        return uiService.getUser();
//    }
    @RequestMapping(value = "user", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getAgencyUser(HttpServletRequest request, HttpServletResponse response) {
        VbUser user = userService.findByUsername(getUser(request));
        if (user == null) {
            return null;
        }
        return uiService.getAgencyUser(user);
    }

    @RequestMapping(value = "user", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Object createUser(HttpServletRequest request, HttpServletResponse response, @RequestBody VbUser vbUser) {
        if (vbUser.getAgencyId() == null) {
            VbUser loggedInUser = userService.findByUsername(getUser(request));
            vbUser.setAgencyId(loggedInUser.getAgencyId());
        }
        return uiService.createUser(vbUser);
    }

    @RequestMapping(value = "user", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    VbUser updateUser(HttpServletRequest request, HttpServletResponse response, @RequestBody VbUser vbUser) {
        return uiService.updateUser(vbUser);
    }

    @RequestMapping(value = "user/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    VbUser deleteUser(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return uiService.deleteUser(id);
    }

    @RequestMapping(value = "userAccount", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    UserAccount createProperty(HttpServletRequest request, HttpServletResponse response, @RequestBody UserAccount userAccount) {
        return uiService.createUserAccount(userAccount);
    }

    @RequestMapping(value = "userAccount", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    UserAccount updateProperty(HttpServletRequest request, HttpServletResponse response, @RequestBody UserAccount userAccount) {
        return uiService.updateUserAccount(userAccount);
    }

    @RequestMapping(value = "userAccount", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getUserAccount(HttpServletRequest request, HttpServletResponse response) {
        return uiService.getUserAccount();
    }

    @RequestMapping(value = "getAccount/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getAccountById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return uiService.getAccountById(id);
    }

    @RequestMapping(value = "userAccountByUser", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getUserAccountByUser(HttpServletRequest request, HttpServletResponse response) {
        VbUser user = userService.findByUsername(getUser(request));
        if (user == null) {
            return null;
        }
        return uiService.getUserAccountByUser(user);
    }

    @RequestMapping(value = "userAccount/{userId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getUserAccountById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer userId) {
        return uiService.getUserAccountById(userId);
    }

    @RequestMapping(value = "userAccount/{userAccountId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    UserAccount deleteUserAccount(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer userAccountId) {
        return uiService.deleteUserAccount(userAccountId);
    }

    @RequestMapping(value = "permission", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getPermission(HttpServletRequest request, HttpServletResponse response) {
        return uiService.getPermission();
    }

    @RequestMapping(value = "userPermission", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    UserPermission createPermission(HttpServletRequest request, HttpServletResponse response, @RequestBody UserPermission userPermission) {
        return uiService.createUserPermission(userPermission);
    }

    @RequestMapping(value = "userPermission", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    UserPermission updatePermission(HttpServletRequest request, HttpServletResponse response, @RequestBody UserPermission userPermission) {
        return uiService.updateUserPermission(userPermission);
    }

    @RequestMapping(value = "userPermission", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getUserPermission(HttpServletRequest request, HttpServletResponse response) {
        return uiService.getUserPermission();
    }

    @RequestMapping(value = "userPermission/{userId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List getUserPermissionById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer userId) {
        return uiService.getUserPermissionById(userId);
    }

    @RequestMapping(value = "userPermission/{userPermissionId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    UserPermission deleteUserPermission(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer userPermissionId) {
        return uiService.deleteUserPermission(userPermissionId);
    }

    /*
        code for linkedin - sabari
     */
    @RequestMapping(value = "oauthCode/{accessToken}/{dataSourceType}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String getOauthToken(HttpServletRequest request, HttpServletResponse response, @PathVariable String accessToken, @PathVariable String dataSourceType) throws IOException {

        String oauth;
        if (dataSourceType.equalsIgnoreCase("linkedin")) {
            String url = "https://www.linkedin.com/oauth/v2/accessToken?";
            System.out.println("hurl======" + url);
            String params = "grant_type=authorization_code&code=" + accessToken + "&redirect_uri=http://localhost:8084/VizBoard/fbPost.html&client_id=81kqaac7cnusqy&client_secret=6SrcnKhiX4Yx0Ab4";
            String oauthUrl = url + params;
            System.out.println("oauthurl======");
            System.out.println(url);
            oauth = Rest.postRawForm(url, params);
            System.out.println("oauth==>" + oauth);
            return oauth;
        } else if (dataSourceType.equalsIgnoreCase("facebook")) {
            String url = "https://graph.facebook.com/v2.8/oauth/access_token?";
            String params = "client_id=1631503257146893&redirect_uri=http://localhost:9090/VizBoard/fbPost.html&client_secret=b6659b47ba7b2b11179247bb3cd84f70&code=" + accessToken;
            oauth = Rest.postRawForm(url, params);
            String oauthUrl = url + params;
            System.out.println("oauthurl======");
            System.out.println(oauthUrl);
            System.out.println("oauth==>" + oauth);
            return oauth;
        }
        return null;

    }

    // get facebook datasource
    @RequestMapping(value = "facebookDataSets", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    String getFacebookDataSets(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long accountId;
        accountId = 10201209987716903L;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date();
        String productSegement = "";
//       String  stratDates=dateFormat.format(startDate);
        //facebookService.getAccountPerformance(accountId, startDate, startDate, "day",productSegement);
//        facebookService.getAccountPerformance(accountId,startDate, startDate,'day');

        return null;
    }

    @RequestMapping(value = "currencies", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Currency> getCurrencies(HttpServletRequest request, HttpServletResponse response) {
        List<Currency> currencies = uiService.getCurrencies();
        System.out.println("latest size of currencies" + currencies.size());
        return currencies;
    }

    @RequestMapping(value = "timezones", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Timezone> getTimezones(HttpServletRequest request, HttpServletResponse response) {
        List<Timezone> timezones = uiService.getTimeZones();
        //System.out.println("size of currencies"+currencies.size());
        return timezones;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
