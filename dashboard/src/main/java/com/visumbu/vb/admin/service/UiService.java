/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.admin.dao.UserDao;
import com.visumbu.vb.admin.dao.bean.DataSourceBean;
import com.visumbu.vb.bean.TabWidgetBean;
import com.visumbu.vb.bean.WidgetColumnBean;
import com.visumbu.vb.model.AgencyProduct;
import com.visumbu.vb.model.Dashboard;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.Permission;
import com.visumbu.vb.model.Product;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportType;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.UserAccount;
import com.visumbu.vb.model.UserPermission;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author netphenix
 */
@Service("uiService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UiService {

    @Autowired
    private UiDao uiDao;

    @Autowired
    private UserDao userDao;

    public List<Product> getProduct() {
        return uiDao.read(Product.class);
//        List<Product> product = uiDao.read(Product.class);
//        List<Product> returnList = new ArrayList<>();
//        for (Iterator<Product> iterator = product.iterator(); iterator.hasNext();) {
//            Product product1 = iterator.next();
//            if (product1.getProductName().equalsIgnoreCase("Marketing Data")) {
//                returnList.add(product1);
//            }
//        }
//        return returnList;
    }

    public List<Product> getDealerProduct(Integer dealerId) {
        return uiDao.getDealerProduct(dealerId);
    }

    public List<Dashboard> getDashboards(VbUser user) {
        return uiDao.getDashboards(user);
    }

    public DashboardTabs createDashboardTabs(DashboardTabs dashboardTabs) {
        return (DashboardTabs) uiDao.create(dashboardTabs);
    }

    public DashboardTabs updateTab(DashboardTabs dashboardTab) {
        return (DashboardTabs) uiDao.update(dashboardTab);
    }

    public String updateAgencyProductTab(Integer agencyProductId, String tabOrder) {
        return uiDao.updateTabOrder(agencyProductId, tabOrder);
    }

    public List<DashboardTabs> getAgencyProductTab(Integer agencyProductId) {
        return uiDao.getAgencyProductTab(agencyProductId);
    }
//    public List<DashboardTabs> getDashboardTabs(Integer dbId) {
//        return uiDao.getDashboardTabs(dbId);
//    }

    public List<DashboardTabs> getDashboardTabsByProductDashboard(Integer dashboardId, Integer uId) {
        return uiDao.getDashboardTabsByDbId(dashboardId, uId);
    }

    public DashboardTabs deleteDashboardTab(Integer id) {
        return uiDao.deleteDashboardTab(id);
    }

    public TabWidget createTabWidget(Integer tabId, TabWidget tabWidget) {
        tabWidget.setTabId(uiDao.getTabById(tabId));
        if (tabWidget.getId() != null) {
            TabWidget tabWidgetDb = uiDao.getTabWidgetById(tabWidget.getId());
            if (tabWidget.getWidgetTitle() != null) {
                tabWidgetDb.setWidgetTitle(tabWidget.getWidgetTitle());
            }
            if (tabWidget.getDirectUrl() != null) {
                tabWidgetDb.setDirectUrl(tabWidget.getDirectUrl());
            }
            if (tabWidget.getChartType() != null) {
                tabWidgetDb.setChartType(tabWidget.getChartType());
            }
            return (TabWidget) uiDao.update(tabWidgetDb);
        }
        return (TabWidget) uiDao.create(tabWidget);
    }

    public String updateWidgetUpdateOrder(Integer tabId, String widgetOrder) {
        return uiDao.updateWidgetUpdateOrder(tabId, widgetOrder);
    }

    public TabWidget deleteTabWidget(Integer id) {
        return uiDao.deleteTabWidget(id);
    }

    public List<TabWidget> getTabWidget(Integer tabId) {
        return uiDao.getTabWidget(tabId);
    }

    public List<TabWidget> getReportWidgetByWidgetId(Integer widgetId) {
        return uiDao.getReportWidgetByWidgetId(widgetId);
    }

    public Dashboard getDashboardById(Integer dashboardId) {
        return uiDao.getDashboardById(dashboardId);
    }

    public AgencyProduct getAgencyProductById(Integer agencyProductId) {
        return uiDao.getAgencyProductById(agencyProductId);
    }

    public WidgetColumn addWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        return uiDao.addWidgetColumn(widgetId, widgetColumn);
    }

    public WidgetColumn updateWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        return (WidgetColumn) uiDao.updateWidgetColumn(widgetId, widgetColumn);
    }

    public WidgetColumn deleteWidgetColumn(Integer id) {
        return uiDao.deleteWidgetColumn(id);
    }

    public TabWidget saveTabWidget(Integer tabId, TabWidgetBean tabWidgetBean) {
        TabWidget tabWidget = null;

        if (tabWidgetBean.getId() != null) {
            tabWidget = uiDao.getTabWidgetById(tabWidgetBean.getId());

        } else {
            tabWidget = new TabWidget();
        }

        DataSource dataSource = null;
        if (tabWidgetBean.getDataSourceId() != null) {
            dataSource = uiDao.getDataSourceById(tabWidgetBean.getDataSourceId());

        } else {
            dataSource = new DataSource();
        }

        DataSet dataSet = null;
        if (tabWidgetBean.getDataSourceId() != null) {
            dataSet = uiDao.getDataSetById(tabWidgetBean.getDataSetId());

        } else {
            dataSet = new DataSet();
        }

        tabWidget.setChartType(tabWidgetBean.getChartType());
        tabWidget.setDirectUrl(tabWidgetBean.getDirectUrl());
        tabWidget.setWidgetTitle(tabWidgetBean.getWidgetTitle());
        tabWidget.setProductName(tabWidgetBean.getProductName());
        tabWidget.setProductDisplayName(tabWidgetBean.getProductDisplayName());
        tabWidget.setTableFooter(tabWidgetBean.getTableFooter());
        tabWidget.setZeroSuppression(tabWidgetBean.getZeroSuppression());
        tabWidget.setDateDuration(tabWidgetBean.getDateDuration());
        tabWidget.setCustomRange(tabWidgetBean.getCustomRange());
        tabWidget.setFrequencyDuration(tabWidgetBean.getFrequencyDuration());
        tabWidget.setMaxRecord(tabWidgetBean.getMaxRecord());
        tabWidget.setDatasource(tabWidgetBean.getDatasource());
        tabWidget.setDataset(tabWidgetBean.getDataset());
        tabWidget.setDataSetId(dataSet);
        tabWidget.setDataSourceId(dataSource);
        tabWidget.setWidth(tabWidgetBean.getWidth());
        tabWidget.setContent(tabWidgetBean.getContent());
        TabWidget savedTabWidget = uiDao.saveTabWidget(tabWidget);
        List<WidgetColumnBean> widgetColumns = tabWidgetBean.getWidgetColumns();
        uiDao.deleteWidgetColumns(tabWidget.getId());
        for (Iterator<WidgetColumnBean> iterator = widgetColumns.iterator(); iterator.hasNext();) {
            WidgetColumnBean widgetColumnBean = iterator.next();
            WidgetColumn widgetColumn = new WidgetColumn();
            widgetColumn.setFieldName(widgetColumnBean.getFieldName());
            widgetColumn.setDisplayFormat(widgetColumnBean.getDisplayFormat());
            widgetColumn.setDisplayName(widgetColumnBean.getDisplayName());
            widgetColumn.setSortOrder(widgetColumnBean.getSortOrder());
            widgetColumn.setGroupPriority(widgetColumnBean.getGroupPriority());
            widgetColumn.setAgregationFunction(widgetColumnBean.getAgregationFunction());
            widgetColumn.setxAxis(widgetColumnBean.getxAxis());
            widgetColumn.setyAxis(widgetColumnBean.getyAxis());
            widgetColumn.setWidth(widgetColumnBean.getWidth());
            widgetColumn.setSearch(widgetColumnBean.getSearch());
            widgetColumn.setWrapText(widgetColumnBean.getWrapText());
            widgetColumn.setAlignment(widgetColumnBean.getAlignment());
            widgetColumn.setFieldType(widgetColumnBean.getFieldType());
            Integer columnHide = null;
            if (widgetColumnBean.getGroupPriority() != null && widgetColumnBean.getGroupPriority() != 0) {
                columnHide = 1;
            }
            if (widgetColumnBean.getColumnHide() != null) {
                columnHide = widgetColumnBean.getColumnHide();
            }
            widgetColumn.setColumnHide(columnHide);
            widgetColumn.setWidgetId(savedTabWidget);
            uiDao.saveOrUpdate(widgetColumn);
        }
        return uiDao.getTabWidgetById(tabWidgetBean.getId());
    }

    public ReportType addReportType(ReportType reportTypes) {
        return (ReportType) uiDao.create(reportTypes);
    }

    public ReportType updateReportType(ReportType reportTypes) {
        return (ReportType) uiDao.update(reportTypes);
    }

    public ReportType deleteReportType(Integer reportTypeId) {
        return (ReportType) uiDao.delete(reportTypeId);
    }

    public List getReportType(Integer reportTypeId) {
        return uiDao.readReportType(reportTypeId);
    }

    public Report addReport(Report report) {
        return (Report) uiDao.create(report);
    }

    public Report updateReport(Report report) {
        return (Report) uiDao.update(report);
    }

    public String updateReportOrder(Integer reportId, String widgetOrder) {
        return uiDao.updateReportOrder(reportId, widgetOrder);
    }

    public Report deleteReport(Integer reportId) {
        return uiDao.deleteReport(reportId);
        //return (Report) uiDao.delete(reportId);
    }

    public List getReport() {
        List<Report> report = uiDao.read(Report.class);
        return report;
    }

    public List getAgencyReport(VbUser user) {
        if (user.getAgencyId() == null) {
            List<Report> report = uiDao.read(Report.class);
            return report;
        }
        return uiDao.getAgencyReport(user);
    }

    public ReportWidget createReportWidget(ReportWidget reportWidget) {
        return (ReportWidget) uiDao.create(reportWidget);
    }

    public ReportWidget updateReportWidget(ReportWidget reportWidget) {
        return (ReportWidget) uiDao.update(reportWidget);
    }

//    public ReportWidget createReportWidget(Integer reportId, ReportWidget reportWidget) {
//        reportWidget.setReportId(uiDao.getReportById(reportId));
//        if (reportWidget.getId() != null) {
//            ReportWidget reportWidgetDb = uiDao.getReportWidgetById(reportWidget.getId());
//            if (reportWidget.getWidgetTitle() != null) {
//                reportWidgetDb.setWidgetTitle(reportWidget.getWidgetTitle());
//            }
//            if (reportWidget.getDirectUrl() != null) {
//                reportWidgetDb.setDirectUrl(reportWidget.getDirectUrl());
//            }
//            if (reportWidget.getChartType() != null) {
//                reportWidgetDb.setChartType(reportWidget.getChartType());
//            }
//            return (ReportWidget) uiDao.update(reportWidgetDb);
//        }
//        return (ReportWidget) uiDao.create(reportWidget);
//    }
//    public ReportWidget saveReportWidget(Integer reportId, ReportWidgetBean reportWidgetBean) {
//        ReportWidget reportWidget = null;
//        if (reportWidgetBean.getId() != null) {
//            reportWidget = uiDao.getReportWidgetById(reportWidgetBean.getId());
//
//        } else {
//            reportWidget = new ReportWidget();
//        }
//        reportWidget.setChartType(reportWidgetBean.getChartType());
//        reportWidget.setDirectUrl(reportWidgetBean.getDirectUrl());
//        reportWidget.setWidgetTitle(reportWidgetBean.getWidgetTitle());
//        reportWidget.setProductName(reportWidgetBean.getProductName());
//        reportWidget.setProductDisplayName(reportWidgetBean.getProductDisplayName());
//        ReportWidget savedReportWidget = uiDao.saveReportWidget(reportWidget);
//        List<ReportColumnBean> reportColumns = reportWidgetBean.getReportColumns();
//        uiDao.deleteReportColumns(reportWidget.getId());
//        for (Iterator<ReportColumnBean> iterator = reportColumns.iterator(); iterator.hasNext();) {
//            ReportColumnBean reportColumnBean = iterator.next();
//            ReportColumn reportColumn = new ReportColumn();
//            reportColumn.setFieldName(reportColumnBean.getFieldName());
//            reportColumn.setDisplayFormat(reportColumnBean.getDisplayFormat());
//            reportColumn.setDisplayName(reportColumnBean.getDisplayName());
//            reportColumn.setSortOrder(reportColumnBean.getSortOrder());
//            reportColumn.setGroupPriority(reportColumnBean.getGroupPriority());
//            reportColumn.setAgregationFunction(reportColumnBean.getAgregationFunction());
//            reportColumn.setxAxis(reportColumnBean.getxAxis());
//            reportColumn.setyAxis(reportColumnBean.getyAxis());
//            reportColumn.setWidth(reportColumnBean.getWidth());
//            reportColumn.setAlignment(reportColumnBean.getAlignment());
//            reportColumn.setReportId(savedReportWidget);
//            uiDao.saveOrUpdate(reportColumn);
//        }
//        return savedReportWidget;
//    }
    public List<ReportWidget> getReportWidget() {
        List<ReportWidget> reportWidget = uiDao.read(ReportWidget.class);
        return reportWidget;
    }

    public List<ReportWidget> getReportWidget(Integer reportId) {
        return uiDao.getReportWidget(reportId);
    }

    public ReportWidget deleteReportWidget(Integer reportId) {
        return uiDao.deleteReportWidget(reportId);
    }

    public Report getReportById(Integer reportId) {
        return uiDao.getReportById(reportId);
    }

    public DataSource create(DataSource dataSource) {
        return (DataSource) uiDao.create(dataSource);
    }

    public DataSource read(Integer id) {
        return (DataSource) uiDao.read(DataSource.class, id);
    }

    public DataSource delete(Integer id) {
//        DataSource dataSource = read(id);
        return (DataSource) uiDao.deleteDataSource(id);
    }

//    public List<DataSource> getDataSource() {
//        List<DataSource> dataSource = uiDao.read(DataSource.class);
//        return dataSource;
//    }
    public List<DataSource> getDataSourceByUser(VbUser user) {
        return uiDao.getDataSourceByUser(user);
    }

    public DataSource update(DataSource dataSource) {
        return (DataSource) uiDao.update(dataSource);
    }

//    public List<DataSet> getDataSet() {
//        List<DataSet> dataSet = uiDao.read(DataSet.class);
//        return dataSet;
//    }
    public List<DataSet> getDataSetByUser(VbUser user) {
        return uiDao.getDataSetByUser(user);
    }

    public DataSet create(DataSet dataSet) {
        return (DataSet) uiDao.create(dataSet);
    }

    public DataSet update(DataSet dataSet) {
        return (DataSet) uiDao.update(dataSet);
    }

    public DataSet readDataSet(Integer id) {
        return (DataSet) uiDao.read(DataSet.class, id);
    }

    public DataSet deleteDataSet(Integer id) {
        return (DataSet) uiDao.deleteDataSet(id);
    }

    public DataSource deleteDataSource(Integer id) {

        return (DataSource) uiDao.deleteDataSource(id);
    }

    public DataSource saveDataSource(DataSourceBean dataSource) {
        try {
            DataSource dbDataSource = new DataSource();
            BeanUtils.copyProperties(dbDataSource, dataSource);
            uiDao.create(dbDataSource);

        } catch (IllegalAccessException ex) {
            Logger.getLogger(UiService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(UiService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<VbUser> getUser() {
        List<VbUser> vbUser = uiDao.read(VbUser.class);
        return vbUser;
    }

    public List<VbUser> getAgencyUser(VbUser user) {
        if (user.getAgencyId() == null) {
            System.out.print("Agence Id Null");
            return userDao.read();
        }
        return uiDao.getUsersByAgencyUser(user);
    }

    public VbUser createUser(VbUser vbUser) {
        return (VbUser) uiDao.create(vbUser);
    }

    public VbUser updateUser(VbUser vbUser) {
        return (VbUser) uiDao.update(vbUser);
    }

    public VbUser readUser(Integer id) {
        return (VbUser) uiDao.read(VbUser.class, id);
    }

    public VbUser deleteUser(Integer id) {
        //VbUser vbUser = readUser(id);
        return uiDao.deleteUser(id);
    }

    public UserAccount createUserAccount(UserAccount userAccount) {
        return (UserAccount) uiDao.create(userAccount);
    }

    public UserAccount updateUserAccount(UserAccount userAccount) {
        return (UserAccount) uiDao.update(userAccount);
    }

    public List<UserAccount> getUserAccount() {
        List<UserAccount> userAccount = uiDao.read(UserAccount.class);
        return userAccount;
    }

    public List<UserAccount> getUserAccountByUser(VbUser user) {
        return uiDao.getUserAccountByUser(user);
    }

    public List<UserAccount> getUserAccountById(Integer userId) {
        return uiDao.getUserAccountById(userId);
    }

    public UserAccount deleteUserAccount(Integer userAccountId) {
        return uiDao.deleteUserAccount(userAccountId);
    }

//    public List getUserAccountId(Integer userId) {
//        return uiDao.getUserAccountId(userId);
//    }
    public List<Permission> getPermission() {
        List<Permission> permission = uiDao.read(Permission.class);
        return permission;
    }

    public UserPermission createUserPermission(UserPermission userPermission) {
        return (UserPermission) uiDao.create(userPermission);
    }

    public UserPermission updateUserPermission(UserPermission userPermission) {
        return (UserPermission) uiDao.update(userPermission);
    }

    public List<UserPermission> getUserPermission() {
        List<UserPermission> userPermission = uiDao.read(UserPermission.class);
        return userPermission;
    }

    public List getUserPermissionById(Integer userId) {
        return uiDao.getUserPermission(userId);
    }

    public UserPermission deleteUserPermission(Integer userPermissionId) {
        return uiDao.deleteUserPermission(userPermissionId);
    }
}
