/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.admin.dao.UserDao;
import com.visumbu.vb.admin.dao.bean.DataSourceBean;
import com.visumbu.vb.bean.ColumnDef;
import com.visumbu.vb.bean.DashboardTemplateBean;
import com.visumbu.vb.bean.DataSetColumnBean;
import com.visumbu.vb.bean.DataSetReport;
import com.visumbu.vb.bean.JoinDataSetBean;
import com.visumbu.vb.bean.TabWidgetBean;
import com.visumbu.vb.bean.WidgetColumnBean;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.AdwordsCriteria;
import com.visumbu.vb.model.Agency;
import com.visumbu.vb.model.AgencyProduct;
import com.visumbu.vb.model.Currency;
import com.visumbu.vb.model.Dashboard;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.DashboardTemplate;
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.DataSetColumns;
import com.visumbu.vb.model.DataSourceFilter;
import com.visumbu.vb.model.DataSourceSetting;
import com.visumbu.vb.model.DefaultFieldProperties;
import com.visumbu.vb.model.JoinDataSet;
import com.visumbu.vb.model.JoinDataSetCondition;
import com.visumbu.vb.model.Permission;
import com.visumbu.vb.model.Product;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportType;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.TemplateTabs;
import com.visumbu.vb.model.Timezone;
import com.visumbu.vb.model.UserAccount;
import com.visumbu.vb.model.UserPermission;
import com.visumbu.vb.model.UserPreferences;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import com.visumbu.vb.model.WidgetTag;
import com.visumbu.vb.utils.DateUtils;
import com.visumbu.vb.utils.PropertyReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.SerializationUtils;
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

    public List<WidgetColumn> getWidgetColumns(Integer widgetId) {
        return uiDao.getWidgetColumnsByWidgetId(widgetId);
    }

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

    public DashboardTabs createDashboardTabs(DashboardTabs dashboardTabs, Integer templateId, VbUser user) {
        DashboardTabs dashboardTab = (DashboardTabs) uiDao.create(dashboardTabs);
        TemplateTabs templateTab = new TemplateTabs();
        templateTab.setTemplateId(uiDao.getDashboardTemplateById(templateId));
        templateTab.setUserId(user);
        templateTab.setTabId(dashboardTabs);
        uiDao.saveOrUpdate(templateTab);
        return dashboardTab;
    }

    public DashboardTemplate getTemplateById(Integer id) {
        return uiDao.getDashboardTemplateById(id);
    }

    public DashboardTabs updateTab(DashboardTabs dashboardTab) {
        return (DashboardTabs) uiDao.update(dashboardTab);
    }

    public String updateAgencyProductTab(Integer agencyProductId, String tabOrder) {
        return uiDao.updateTabOrder(agencyProductId, tabOrder);
    }

    public List<DashboardTabs> getAgencyProductTab(Integer agencyProductId, Integer accountId, Integer userId) {
        return uiDao.getAgencyProductTab(agencyProductId, accountId, userId);
    }
//    public List<DashboardTabs> getDashboardTabs(Integer dbId) {
//        return uiDao.getDashboardTabs(dbId);
//    }

    public List<DashboardTabs> getDashboardTabsByProductDashboard(Integer dashboardId, Integer uId) {
        return uiDao.getDashboardTabsByDbId(dashboardId, uId);
    }

    public DashboardTabs deleteDashboardTab(Integer id) {
        uiDao.deleteTabFromTemplate(id);
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

    public List<TabWidget> getTabWidget(Integer tabId, Integer accountId) {
        return uiDao.getTabWidget(tabId, accountId);
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

//    public WidgetColumn getWidgetColumn(Integer widgetId) {
//        return uiDao.getWidgetColumn(widgetId);
//    }
    public WidgetColumn updateWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        return (WidgetColumn) uiDao.updateWidgetColumn(widgetId, widgetColumn);
    }

    public WidgetColumn deleteWidgetColumn(Integer id) {
        return uiDao.deleteWidgetColumn(id);
    }

    public DataSet getDataSetById(Integer dataSetId) {
        return uiDao.getDataSetById(dataSetId);
    }

    public TabWidget saveTabWidget(Integer tabId, TabWidgetBean tabWidgetBean) {
        VbUser createByUserId = tabWidgetBean.getCreatedBy();
        Integer createByUserIdInt = null;
        if (createByUserId != null) {
            createByUserIdInt = createByUserId.getId();
        }
        Integer currentUserId = tabWidgetBean.getTemplateUserId();
        System.out.println("createdBy---->" + createByUserIdInt);
        System.out.println("currentUserId---->" + currentUserId);
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
        if (tabWidgetBean.getDataSetId() != null) {
            dataSet = uiDao.getDataSetById(tabWidgetBean.getDataSetId());

        } else {
            dataSet = new DataSet();
        }

        String dateRangeName = tabWidgetBean.getDateRangeName();

        String startDate = null;
        String endDate = null;
        Integer lastNdays = null;
        Integer lastNmonths = null;
        Integer lastNweeks = null;
        Integer lastNyears = null;
        if (dateRangeName != null) {

            if (tabWidgetBean.getLastNdays() != null) {
                lastNdays = tabWidgetBean.getLastNdays();
                System.out.println("Last N days ----> " + lastNdays);
            } else if (dateRangeName.equalsIgnoreCase("Last 0 Days")) {
                lastNdays = 0;
            }
            if (tabWidgetBean.getLastNmonths() != null) {
                lastNmonths = tabWidgetBean.getLastNmonths();
                System.out.println("Last N months ----> " + lastNmonths);
            } else if (dateRangeName.equalsIgnoreCase("Last 0 Months")) {
                lastNmonths = 0;
            }
            if (tabWidgetBean.getLastNweeks() != null) {
                lastNweeks = tabWidgetBean.getLastNweeks();
                System.out.println("Last N weeks ----> " + lastNweeks);

            } else if (dateRangeName.equalsIgnoreCase("Last 0 Weeks")) {
                lastNweeks = 0;
            }
            if (tabWidgetBean.getLastNyears() != null) {
                lastNyears = tabWidgetBean.getLastNyears();
                System.out.println("Last N years ----> " + lastNyears);
            } else if (dateRangeName.equalsIgnoreCase("Last 0 Years")) {
                lastNyears = 0;
            }

            System.out.println("dateRangename ----> " + dateRangeName);

            if (dateRangeName.equalsIgnoreCase("Custom")) {
                startDate = tabWidgetBean.getCustomStartDate();
                endDate = tabWidgetBean.getCustomEndDate();
            } else if (dateRangeName.equalsIgnoreCase("Select Date Duration")) {
                startDate = null;
                endDate = null;
            } else if (dateRangeName.equalsIgnoreCase("None")) {
                startDate = null;
                endDate = null;
            }
        }
        System.out.println("dateRange start Date-----> " + startDate);
        System.out.println("dateRange End Date-----> " + endDate);
        tabWidget.setTabId(uiDao.getTabById(tabId));
        tabWidget.setWidth(tabWidgetBean.getWidth());
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
        if (!tabWidgetBean.getChartType().equalsIgnoreCase("text")) {
            tabWidget.setDatasource(tabWidgetBean.getDatasource());
            tabWidget.setDataset(tabWidgetBean.getDataset());
            tabWidget.setDataSetId(dataSet);
            tabWidget.setDataSourceId(dataSource);
        }
        tabWidget.setWidth(tabWidgetBean.getWidth());
        tabWidget.setContent(tabWidgetBean.getContent());
        tabWidget.setDateRangeName(tabWidgetBean.getDateRangeName());
        tabWidget.setCustomStartDate(startDate);
        tabWidget.setCustomEndDate(endDate);
        tabWidget.setLastNdays(lastNdays);
        tabWidget.setLastNmonths(lastNmonths);
        tabWidget.setLastNweeks(lastNweeks);
        tabWidget.setLastNyears(lastNyears);
        tabWidget.setIsGridLine(tabWidgetBean.getIsGridLine());
        tabWidget.setQueryFilter(tabWidgetBean.getQueryFilter());
        tabWidget.setJsonData(tabWidgetBean.getJsonData());
        tabWidget.setAccountId(tabWidgetBean.getAccountId());
        tabWidget.setCreatedBy(tabWidgetBean.getCreatedBy());
        tabWidget.setTimeSegment(tabWidgetBean.getTimeSegment());
        tabWidget.setProductSegment(tabWidgetBean.getProductSegment());
        tabWidget.setNetworkType(tabWidgetBean.getNetworkType());
        tabWidget.setChartColorOption(tabWidgetBean.getChartColorOption());
        tabWidget.setIcon(tabWidgetBean.getIcon());
//        tabWidget.setCustomStartDate(tabWidgetBean.getCustomStartDate());
//        tabWidget.setCustomEndDate(tabWidgetBean.getCustomEndDate());
//        tabWidget.setLastNdays(tabWidgetBean.getLastNdays());
//        tabWidget.setLastNmonths(tabWidgetBean.getLastNmonths());
//        tabWidget.setLastNweeks(tabWidgetBean.getLastNweeks());
//        tabWidget.setLastNyears(tabWidgetBean.getLastNyears());

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
            widgetColumn.setExpression(widgetColumnBean.getExpression());
            widgetColumn.setGroupPriority(widgetColumnBean.getGroupPriority());
            widgetColumn.setAgregationFunction(widgetColumnBean.getAgregationFunction());
            widgetColumn.setxAxis(widgetColumnBean.getxAxis());
            widgetColumn.setyAxis(widgetColumnBean.getyAxis());
            widgetColumn.setWidth(widgetColumnBean.getWidth());
            widgetColumn.setSearch(widgetColumnBean.getSearch());
            widgetColumn.setWrapText(widgetColumnBean.getWrapText());
            widgetColumn.setAlignment(widgetColumnBean.getAlignment());
            widgetColumn.setFieldType(widgetColumnBean.getFieldType());
            widgetColumn.setGroupField(widgetColumnBean.getGroupField());
            widgetColumn.setCombinationType(widgetColumnBean.getCombinationType());
            widgetColumn.setCategory(widgetColumnBean.getCategory());
            Integer columnHide = null;
            if (widgetColumnBean.getGroupPriority() != null && widgetColumnBean.getGroupPriority() != 0) {
                columnHide = 1;
            }
            if (widgetColumnBean.getColumnHide() != null) {
                columnHide = widgetColumnBean.getColumnHide();
            }
            widgetColumn.setColumnHide(columnHide);
            widgetColumn.setWidgetId(savedTabWidget);
            widgetColumn.setDerivedId(widgetColumnBean.getDerivedId());
            uiDao.saveOrUpdate(widgetColumn);
        }
        TabWidget returnMsg = null;
        if (createByUserIdInt == currentUserId) {
            return uiDao.getTabWidgetById(savedTabWidget.getId());
        } else {
            return returnMsg;
        }
    }

    public DashboardTabs duplicateTab(Integer tabId, VbUser userId) {
        DashboardTabs dashboardTab = uiDao.getTabById(tabId);
        DashboardTabs newDashboardTab = new DashboardTabs();
        newDashboardTab.setTabName(dashboardTab.getTabName());
        newDashboardTab.setUserId(userId);
        newDashboardTab.setTabOrder(dashboardTab.getTabOrder());
        uiDao.saveOrUpdate(newDashboardTab);
        List<TabWidget> widgets = uiDao.getWidgetsByTab(tabId);
        for (Iterator<TabWidget> iterator = widgets.iterator(); iterator.hasNext();) {
            TabWidget widget = iterator.next();
            System.out.println(widget.getId());
            System.out.println(newDashboardTab.getId());
            TabWidget tabWidget = duplicateWidget(widget.getId(), newDashboardTab.getId());
            if (tabWidget == null) {
                continue;
            }
            System.out.println(tabWidget);
            tabWidget.setTabId(newDashboardTab);
            uiDao.saveOrUpdate(tabWidget);
        }
        return newDashboardTab;
    }

    public TabWidget duplicateWidget(Integer widgetId, Integer tabId) {

        int id = 0;
        TabWidget tabWidgetBean = uiDao.getWidgetById(widgetId);
        TabWidget tabWidget = new TabWidget();
        tabWidget.setId(null);
        tabWidget.setTabId(tabWidgetBean.getTabId());
        Integer count = uiDao.getWidgetCount(tabId);
        tabWidget.setWidth(tabWidgetBean.getWidth());
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
        tabWidget.setDataSetId(tabWidgetBean.getDataSetId());
        tabWidget.setDataSourceId(tabWidgetBean.getDataSourceId());
        tabWidget.setWidgetOrder(count + 1);
        tabWidget.setWidth(tabWidgetBean.getWidth());
        tabWidget.setMinHeight(tabWidgetBean.getMinHeight());
        tabWidget.setContent(tabWidgetBean.getContent());
        tabWidget.setDateRangeName(tabWidgetBean.getDateRangeName());
        tabWidget.setCustomStartDate(tabWidgetBean.getCustomStartDate());
        tabWidget.setCustomEndDate(tabWidgetBean.getCustomEndDate());
        tabWidget.setLastNdays(tabWidgetBean.getLastNdays());
        tabWidget.setLastNmonths(tabWidgetBean.getLastNmonths());
        tabWidget.setLastNweeks(tabWidgetBean.getLastNweeks());
        tabWidget.setLastNyears(tabWidgetBean.getLastNyears());
        tabWidget.setIsGridLine(tabWidgetBean.getIsGridLine());
        tabWidget.setQueryFilter(tabWidgetBean.getQueryFilter());
        tabWidget.setJsonData(tabWidgetBean.getJsonData());
        tabWidget.setAccountId(tabWidgetBean.getAccountId());
        tabWidget.setCreatedBy(tabWidgetBean.getCreatedBy());
        tabWidget.setTimeSegment(tabWidgetBean.getTimeSegment());
        tabWidget.setProductSegment(tabWidgetBean.getProductSegment());
        tabWidget.setNetworkType(tabWidgetBean.getNetworkType());
        tabWidget.setChartColorOption(tabWidgetBean.getChartColorOption());
        tabWidget.setIcon(tabWidgetBean.getIcon());
        TabWidget savedTabWidget = uiDao.saveTabWidget(tabWidget);
        id = savedTabWidget.getId();
        System.out.println("new Widget Id ---> " + id);
        List<WidgetColumn> widgetColumns = uiDao.getWidgetColumnsByWidgetId(widgetId);
        System.out.println("widgetColumns ---> " + widgetColumns);
        List<WidgetTag> widgetTags = uiDao.getWidgetTagsByWidgetId(widgetId);
        System.out.println("widgetTags ---> " + widgetTags);
        for (Iterator<WidgetTag> iterate = widgetTags.iterator(); iterate.hasNext();) {
            WidgetTag tagWidgetBean = iterate.next();
            WidgetTag widgetTag = new WidgetTag();
            widgetTag.setStatus(tagWidgetBean.getStatus());
            widgetTag.setTagId(tagWidgetBean.getTagId());
            widgetTag.setWidgetId(savedTabWidget);
            widgetTag.setStatus(tagWidgetBean.getStatus());
            uiDao.saveOrUpdate(widgetTag);
        }
        DataSet dataSet = tabWidgetBean.getDataSetId();
        if (dataSet != null) {
            System.out.println("datasetId --> " + dataSet.getId());
            List<DataSetColumns> dataSetColumns = uiDao.getDataSetColumn(dataSet.getId(), widgetId);
            System.out.println("dataSetColumns --> " + dataSetColumns);
            for (Iterator<WidgetColumn> iterate = widgetColumns.iterator(); iterate.hasNext();) {
                WidgetColumn widgetColumnBean = iterate.next();
                WidgetColumn widgetColumn = new WidgetColumn();
                widgetColumn.setFieldName(widgetColumnBean.getFieldName());
                widgetColumn.setDisplayFormat(widgetColumnBean.getDisplayFormat());
                widgetColumn.setDisplayName(widgetColumnBean.getDisplayName());
                widgetColumn.setSortOrder(widgetColumnBean.getSortOrder());
                widgetColumn.setGroupPriority(widgetColumnBean.getGroupPriority());
                widgetColumn.setExpression(widgetColumnBean.getExpression());
                widgetColumn.setAgregationFunction(widgetColumnBean.getAgregationFunction());
                widgetColumn.setxAxis(widgetColumnBean.getxAxis());
                widgetColumn.setyAxis(widgetColumnBean.getyAxis());
                widgetColumn.setWidth(widgetColumnBean.getWidth());
                widgetColumn.setSearch(widgetColumnBean.getSearch());
                widgetColumn.setWrapText(widgetColumnBean.getWrapText());
                widgetColumn.setAlignment(widgetColumnBean.getAlignment());
                widgetColumn.setFieldType(widgetColumnBean.getFieldType());
                widgetColumn.setGroupField(widgetColumnBean.getGroupField());
                widgetColumn.setCombinationType(widgetColumnBean.getCombinationType());
                widgetColumn.setCategory(widgetColumnBean.getCategory());
                widgetColumn.setIsLocation(widgetColumnBean.getIsLocation());
                widgetColumn.setIsLatitude(widgetColumnBean.getIsLatitude());
                widgetColumn.setIsLongitude(widgetColumnBean.getIsLongitude());
                
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

            // Duplicate Data Set Columns for a widget
            for (Iterator<DataSetColumns> iterator1 = dataSetColumns.iterator(); iterator1.hasNext();) {
                DataSetColumns dataSetColumn = iterator1.next();
                DataSetColumns newDataSetColumn = new DataSetColumns();
                newDataSetColumn.setId(null);
                newDataSetColumn.setFieldName(dataSetColumn.getFieldName());
                newDataSetColumn.setFieldType(dataSetColumn.getFieldType());
                newDataSetColumn.setDataSetId(dataSetColumn.getDataSetId());
                newDataSetColumn.setDisplayFormat(dataSetColumn.getDisplayFormat());
                newDataSetColumn.setDisplayName(dataSetColumn.getDisplayName());
                newDataSetColumn.setExpression(dataSetColumn.getExpression());
                newDataSetColumn.setFunctionName(dataSetColumn.getFunctionName());
                newDataSetColumn.setBaseField(dataSetColumn.getBaseField());
                newDataSetColumn.setColumnName(dataSetColumn.getColumnName());
                newDataSetColumn.setCustomStartDate(dataSetColumn.getCustomStartDate());
                newDataSetColumn.setCustomEndDate(dataSetColumn.getCustomEndDate());
                newDataSetColumn.setDateRangeName(dataSetColumn.getDateRangeName());
                newDataSetColumn.setLastNdays(dataSetColumn.getLastNdays());
                newDataSetColumn.setLastNdays(dataSetColumn.getLastNdays());
                newDataSetColumn.setLastNmonths(dataSetColumn.getLastNmonths());
                newDataSetColumn.setLastNweeks(dataSetColumn.getLastNweeks());
                newDataSetColumn.setLastNyears(dataSetColumn.getLastNyears());
//            if (dataSetColumn.getUserId() != null) {
                newDataSetColumn.setUserId(dataSetColumn.getUserId());
                newDataSetColumn.setWidgetId(savedTabWidget);
//            }
                uiDao.saveOrUpdate(newDataSetColumn);
            }
        }
        return uiDao.getTabWidgetById(id);
    }

    public List<WidgetTag> getTagWidget(Integer widgetId) {
        return uiDao.getTagWidgetByWidgetId(widgetId);
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

//    public Report addReport(Report report) {
//        return (Report) uiDao.create(report);
//    }
//
//    public Report updateReport(Report report) {
//        return (Report) uiDao.update(report);
//    }
//    public String updateReportOrder(Integer reportId, String widgetOrder) {
//        return uiDao.updateReportOrder(reportId, widgetOrder);
//    }
//    public Report deleteReport(Integer reportId) {
//        return uiDao.deleteReport(reportId);
//        //return (Report) uiDao.delete(reportId);
//    }
    public List getReport() {
        List<Report> report = uiDao.read(Report.class);
        return report;
    }

//    public List getAgencyReport(VbUser user) {
//        if (user.getAgencyId() == null) {
//            List<Report> report = uiDao.read(Report.class);
//            return report;
//        }
//        return uiDao.getAgencyReport(user);
//    }
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

//    public List<ReportWidget> getReportWidget(Integer reportId) {
//        return uiDao.getReportWidget(reportId);
//    }
//    public ReportWidget deleteReportWidget(Integer reportId) {
//        return uiDao.deleteReportWidget(reportId);
//    }
//    public Report getReportById(Integer reportId) {
//        return uiDao.getReportById(reportId);
//    }
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

    public List getPublishDataSetByUser(VbUser user) {
        return uiDao.getPublishDataSetByUser(user);
    }

    public DataSet create(DataSet dataSet, String joinDataSetId) {
        Integer joinDataSetIdInt = null;
        if (joinDataSetId != null) {
            joinDataSetIdInt = Integer.parseInt(joinDataSetId);
            dataSet.setJoinDataSetId(uiDao.getJoinDataSetById(joinDataSetIdInt));
        }
        Boolean activeStatus = Boolean.parseBoolean(dataSet.getPublish());
        if (activeStatus == true) {
            dataSet.setPublish("Active");
        } else {
            dataSet.setPublish("InActive");
        }
        uiDao.create(dataSet);
        return dataSet;
    }

    public DataSet update(DataSet dataSet) {
        Boolean activeStatus = Boolean.parseBoolean(dataSet.getPublish());
        if (activeStatus == true) {
            dataSet.setPublish("Active");
        } else {
            dataSet.setPublish("InActive");
        }
        return (DataSet) uiDao.update(dataSet);
    }

    public DataSet readDataSet(Integer id) {
        return (DataSet) uiDao.read(DataSet.class, id);
    }

    public void deleteDataSet(Integer id) {
        uiDao.deleteDataSet(id);
    }

    public DataSetColumns deleteDataSetColumns(Integer id) {
        return (DataSetColumns) uiDao.deleteDataSetColumns(id);
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

    public HashMap createUser(VbUser vbUser) {
        String returnMsg = null;
        Boolean isSuccess = null;
        HashMap returnMap = new HashMap();
        List findUser = userDao.findUserNameByUser(vbUser.getUserName());
        if (findUser.isEmpty()) {
            uiDao.create(vbUser);
            returnMsg = "Success";
            isSuccess = true;

        } else {
//            Iterator<VbUser> userList = userDao.findByUserName(vbUser.getUserName()).iterator();
            returnMsg = "User Already Exist";
            isSuccess = false;
        }
        returnMap.put("message", returnMsg);
        returnMap.put("status", isSuccess);
        return returnMap;
    }

    public VbUser updateUser(VbUser vbUser) {
        return (VbUser) uiDao.update(vbUser);
    }

    public VbUser readUser(Integer id) {
        return (VbUser) uiDao.read(VbUser.class, id);
    }

    public VbUser deleteUser(Integer id) {
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

    public Account getAccountById(Integer id) {
        return uiDao.getAccountById(id);
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

    public UserAccount findUserAccountById(UserAccount accountId) {
        UserAccount userAccount = uiDao.findUserAccountById(accountId);
        if (userAccount != null) {
            return userAccount;
        }
        return null;
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

    public DataSource getDataSourceById(Integer dataSourceIdInt) {
        return uiDao.getDataSourceById(dataSourceIdInt);
    }

    public DefaultFieldProperties getDefaultFieldProperties(String fieldName) {
        return uiDao.getDefaultFieldProperties(fieldName);
    }

    public AdwordsCriteria getAdwordsCriteria(Integer criteriaId) {
        return uiDao.getAdwordsCriteria(criteriaId);
    }

    public TabWidget getWidgetById(Integer widgetId) {
        return uiDao.getTabWidgetById(widgetId);
    }

    public TabWidget getWidgetByIdAndDataSetId(Integer widgetId, Integer datasetId) {
        return uiDao.getWidgetByIdAndDataSetId(widgetId, datasetId);
    }

    public List<DataSetColumns> createDataSetFormulaColumn(DataSetColumnBean dataSetColumnBean) {
        DataSet dataSet = uiDao.getDataSetById(dataSetColumnBean.getDataSetId());
        List<DataSetColumns> dataSetList = new ArrayList();
        List<DataSetColumnBean> dataSetColumnList = dataSetColumnBean.getTableColumns();
        for (Iterator<DataSetColumnBean> dataSetColumnBeanIterator = dataSetColumnList.iterator(); dataSetColumnBeanIterator.hasNext();) {
            DataSetColumnBean allDataSetColumn = dataSetColumnBeanIterator.next();
            System.out.println(allDataSetColumn.getId() + "____________" + dataSetColumnBean.getId());
            if (allDataSetColumn.getId() == null && dataSetColumnBean.getId() == null) {
                System.out.println("if");
                DataSetColumns dataSetFields = new DataSetColumns();
                dataSetFields.setId(allDataSetColumn.getId());
                dataSetFields.setExpression(allDataSetColumn.getExpression());
                dataSetFields.setFieldName(allDataSetColumn.getFieldName());
                dataSetFields.setDisplayName(allDataSetColumn.getDisplayName());
                dataSetFields.setDisplayFormat(allDataSetColumn.getDisplayFormat());
                dataSetFields.setStatus(allDataSetColumn.getStatus());
                dataSetFields.setFunctionName(allDataSetColumn.getFunctionName());
                dataSetFields.setColumnName(allDataSetColumn.getColumnName());
                dataSetFields.setBaseField(allDataSetColumn.getBaseField());
                dataSetFields.setDateRangeName(allDataSetColumn.getDateRangeName());
                dataSetFields.setCustomStartDate(allDataSetColumn.getCustomStartDate());
                dataSetFields.setCustomEndDate(allDataSetColumn.getCustomEndDate());
                dataSetFields.setLastNdays(allDataSetColumn.getLastNdays());
                dataSetFields.setLastNmonths(allDataSetColumn.getLastNmonths());
                dataSetFields.setLastNweeks(allDataSetColumn.getLastNweeks());
                dataSetFields.setLastNyears(allDataSetColumn.getLastNyears());
                dataSetFields.setFieldType(allDataSetColumn.getFieldType());
                dataSetFields.setSortPriority(allDataSetColumn.getSortPriority());
                dataSetFields.setDataSetId(dataSet);
                dataSetFields.setWidgetId(allDataSetColumn.getWidgetId());
                dataSetFields.setUserId(allDataSetColumn.getUserId());
                uiDao.saveOrUpdate(dataSetFields);
                dataSetList.add(dataSetFields);
            } else if (!Objects.equals(allDataSetColumn.getId(), dataSetColumnBean.getId())) {
                System.out.println("else if");
                DataSetColumns dataSetFields = new DataSetColumns();
                dataSetFields.setId(allDataSetColumn.getId());
                dataSetFields.setExpression(allDataSetColumn.getExpression());
                dataSetFields.setFieldName(allDataSetColumn.getFieldName());
                dataSetFields.setDisplayName(allDataSetColumn.getDisplayName());
                dataSetFields.setDisplayFormat(allDataSetColumn.getDisplayFormat());
                dataSetFields.setStatus(allDataSetColumn.getStatus());
                dataSetFields.setFunctionName(allDataSetColumn.getFunctionName());
                dataSetFields.setColumnName(allDataSetColumn.getColumnName());
                dataSetFields.setBaseField(allDataSetColumn.getBaseField());
                dataSetFields.setDateRangeName(allDataSetColumn.getDateRangeName());
                dataSetFields.setCustomStartDate(allDataSetColumn.getCustomStartDate());
                dataSetFields.setCustomEndDate(allDataSetColumn.getCustomEndDate());
                dataSetFields.setLastNdays(allDataSetColumn.getLastNdays());
                dataSetFields.setLastNmonths(allDataSetColumn.getLastNmonths());
                dataSetFields.setLastNweeks(allDataSetColumn.getLastNweeks());
                dataSetFields.setLastNyears(allDataSetColumn.getLastNyears());
                dataSetFields.setFieldType(allDataSetColumn.getFieldType());
                dataSetFields.setSortPriority(allDataSetColumn.getSortPriority());
                dataSetFields.setDataSetId(dataSet);
                dataSetFields.setWidgetId(allDataSetColumn.getWidgetId());
                dataSetFields.setUserId(allDataSetColumn.getUserId());
                uiDao.saveOrUpdate(dataSetFields);
                dataSetList.add(dataSetFields);
            }
        }

        DataSetColumns dataSetColumns = new DataSetColumns();
        dataSetColumns.setId(dataSetColumnBean.getId());
        dataSetColumns.setExpression(dataSetColumnBean.getExpression());
        dataSetColumns.setFieldName(dataSetColumnBean.getFieldName());
        dataSetColumns.setFieldType(dataSetColumnBean.getFieldType());
        dataSetColumns.setDisplayName(dataSetColumnBean.getDisplayName());
        dataSetColumns.setDisplayFormat(dataSetColumnBean.getDisplayFormat());
        dataSetColumns.setStatus(dataSetColumnBean.getStatus());
        dataSetColumns.setFunctionName(dataSetColumnBean.getFunctionName());
        dataSetColumns.setColumnName(dataSetColumnBean.getColumnName());
        dataSetColumns.setBaseField(dataSetColumnBean.getBaseField());
        dataSetColumns.setDateRangeName(dataSetColumnBean.getDateRangeName());
        dataSetColumns.setCustomStartDate(dataSetColumnBean.getCustomStartDate());
        dataSetColumns.setCustomEndDate(dataSetColumnBean.getCustomEndDate());
        dataSetColumns.setLastNdays(dataSetColumnBean.getLastNdays());
        dataSetColumns.setSortPriority(dataSetColumnBean.getSortPriority());
        dataSetColumns.setLastNmonths(dataSetColumnBean.getLastNmonths());
        dataSetColumns.setLastNweeks(dataSetColumnBean.getLastNweeks());
        dataSetColumns.setLastNyears(dataSetColumnBean.getLastNyears());
        dataSetColumns.setDataSetId(dataSet);
        dataSetColumns.setUserId(dataSetColumnBean.getUserId());
        dataSetColumns.setWidgetId(dataSetColumnBean.getWidgetId());
        uiDao.saveOrUpdate(dataSetColumns);
        dataSetList.add(dataSetColumns);
        return dataSetList;
    }

    public List<DataSetColumns> createWidgetColumn(DataSetColumnBean dataSetColumnBean, VbUser user, Integer widgetId) {
        List<DataSetColumnBean> dataSetColumnList = dataSetColumnBean.getTableColumns();
        List<DataSetColumns> dataSetColumn = new ArrayList<>();
        for (Iterator<DataSetColumnBean> dataSetColumnBeanIterator = dataSetColumnList.iterator(); dataSetColumnBeanIterator.hasNext();) {
            DataSetColumnBean allDataSetColumn = dataSetColumnBeanIterator.next();
            DataSet dataSet = null;
            if (allDataSetColumn.getDataSetId() != null) {
                dataSet = uiDao.getDataSetById(allDataSetColumn.getDataSetId());
            } else {
                dataSet = new DataSet();
            }
            if (widgetId != null) {
                TabWidget tabWidget = uiDao.getTabWidgetById(widgetId);
                dataSet = tabWidget.getDataSetId();
                DataSetColumns checkDbForColumn = uiDao.getDataSetColumn(allDataSetColumn.getFieldName(), dataSet);
                if (checkDbForColumn != null) {
                    if (allDataSetColumn.getExpression() != null && !allDataSetColumn.getExpression().isEmpty()) {
                        checkDbForColumn.setExpression(allDataSetColumn.getExpression());
                    }
                    if (allDataSetColumn.getFieldName() != null && !allDataSetColumn.getFieldName().isEmpty()) {
                        checkDbForColumn.setFieldName(allDataSetColumn.getFieldName());
                    }

                    if (allDataSetColumn.getDisplayName() != null && !allDataSetColumn.getDisplayName().isEmpty()) {
                        checkDbForColumn.setDisplayName(allDataSetColumn.getDisplayName());
                    }
                    if (allDataSetColumn.getDisplayFormat() != null && !allDataSetColumn.getDisplayFormat().isEmpty()) {
                        checkDbForColumn.setDisplayFormat(allDataSetColumn.getDisplayFormat());
                    }
                    checkDbForColumn.setStatus(allDataSetColumn.getStatus());
                    checkDbForColumn.setFunctionName(allDataSetColumn.getFunctionName());
                    checkDbForColumn.setColumnName(allDataSetColumn.getColumnName());
                    checkDbForColumn.setBaseField(allDataSetColumn.getBaseField());
                    checkDbForColumn.setDateRangeName(allDataSetColumn.getDateRangeName());
                    checkDbForColumn.setCustomStartDate(allDataSetColumn.getCustomStartDate());
                    checkDbForColumn.setCustomEndDate(allDataSetColumn.getCustomEndDate());
                    checkDbForColumn.setLastNdays(allDataSetColumn.getLastNdays());
                    checkDbForColumn.setLastNmonths(allDataSetColumn.getLastNmonths());
                    checkDbForColumn.setLastNweeks(allDataSetColumn.getLastNweeks());
                    checkDbForColumn.setLastNyears(allDataSetColumn.getLastNyears());
                    checkDbForColumn.setCategory(allDataSetColumn.getCategory());
                    if (allDataSetColumn.getFieldType() != null && !allDataSetColumn.getFieldType().isEmpty()) {
                        checkDbForColumn.setFieldType(allDataSetColumn.getFieldType());
                    }
                    checkDbForColumn.setSortPriority(allDataSetColumn.getSortPriority());
                    if (widgetId != null) {
                        checkDbForColumn.setWidgetId(tabWidget);
                        checkDbForColumn.setDataSetId(tabWidget.getDataSetId());
                        checkDbForColumn.setUserId(allDataSetColumn.getUserId());
                    }
                    uiDao.saveOrUpdate(checkDbForColumn);
                    continue;
                }
            }
            System.out.println(allDataSetColumn.getId() + "____________" + dataSetColumnBean.getId());
            if (allDataSetColumn.getId() == null && dataSetColumnBean.getId() == null) {
                System.out.println("if");
                DataSetColumns dataSetFields = new DataSetColumns();
                dataSetFields.setId(allDataSetColumn.getId());
                dataSetFields.setExpression(allDataSetColumn.getExpression());
                dataSetFields.setFieldName(allDataSetColumn.getFieldName());
                dataSetFields.setDisplayName(allDataSetColumn.getDisplayName());
                dataSetFields.setDisplayFormat(allDataSetColumn.getDisplayFormat());
                dataSetFields.setStatus(allDataSetColumn.getStatus());
                dataSetFields.setFunctionName(allDataSetColumn.getFunctionName());
                dataSetFields.setColumnName(allDataSetColumn.getColumnName());
                dataSetFields.setBaseField(allDataSetColumn.getBaseField());
                dataSetFields.setDateRangeName(allDataSetColumn.getDateRangeName());
                dataSetFields.setCustomStartDate(allDataSetColumn.getCustomStartDate());
                dataSetFields.setCustomEndDate(allDataSetColumn.getCustomEndDate());
                dataSetFields.setLastNdays(allDataSetColumn.getLastNdays());
                dataSetFields.setLastNmonths(allDataSetColumn.getLastNmonths());
                dataSetFields.setLastNweeks(allDataSetColumn.getLastNweeks());
                dataSetFields.setLastNyears(allDataSetColumn.getLastNyears());
                dataSetFields.setFieldType(allDataSetColumn.getFieldType());
                dataSetFields.setSortPriority(allDataSetColumn.getSortPriority());
                dataSetFields.setCategory(allDataSetColumn.getCategory());
//                DataSet dataSet = uiDao.getDataSetById(allDataSetColumn.getDataSetId());
                dataSetFields.setDataSetId(dataSet);
                if (widgetId != null) {
                    TabWidget tabWidget = uiDao.getTabWidgetById(widgetId);
                    dataSetFields.setWidgetId(tabWidget);
                    dataSetFields.setDataSetId(tabWidget.getDataSetId());
                    dataSetFields.setUserId(allDataSetColumn.getUserId());
                }
                uiDao.saveOrUpdate(dataSetFields);
                dataSetColumn.add(dataSetFields);
            } else if (!Objects.equals(allDataSetColumn.getId(), dataSetColumnBean.getId())) {
                System.out.println("else if");
                DataSetColumns dataSetFields = new DataSetColumns();
                dataSetFields.setId(allDataSetColumn.getId());
                dataSetFields.setExpression(allDataSetColumn.getExpression());
                dataSetFields.setFieldName(allDataSetColumn.getFieldName());
                dataSetFields.setDisplayName(allDataSetColumn.getDisplayName());
                dataSetFields.setDisplayFormat(allDataSetColumn.getDisplayFormat());
                dataSetFields.setStatus(allDataSetColumn.getStatus());
                dataSetFields.setFunctionName(allDataSetColumn.getFunctionName());
                dataSetFields.setColumnName(allDataSetColumn.getColumnName());
                dataSetFields.setBaseField(allDataSetColumn.getBaseField());
                dataSetFields.setDateRangeName(allDataSetColumn.getDateRangeName());
                dataSetFields.setCustomStartDate(allDataSetColumn.getCustomStartDate());
                dataSetFields.setCustomEndDate(allDataSetColumn.getCustomEndDate());
                dataSetFields.setLastNdays(allDataSetColumn.getLastNdays());
                dataSetFields.setLastNmonths(allDataSetColumn.getLastNmonths());
                dataSetFields.setLastNweeks(allDataSetColumn.getLastNweeks());
                dataSetFields.setLastNyears(allDataSetColumn.getLastNyears());
                dataSetFields.setFieldType(allDataSetColumn.getFieldType());
                dataSetFields.setSortPriority(allDataSetColumn.getSortPriority());
                dataSetFields.setCategory(allDataSetColumn.getCategory());
                dataSetFields.setDataSetId(dataSet);
                if (allDataSetColumn.getUserId() != null) {
                    TabWidget tabWidget = uiDao.getTabWidgetById(widgetId);
                    dataSetFields.setWidgetId(tabWidget);
                    dataSetFields.setUserId(allDataSetColumn.getUserId());
                }
                uiDao.saveOrUpdate(dataSetFields);
                dataSetColumn.add(dataSetFields);
            }
        }
        return dataSetColumn;
    }

    public DataSetColumns getDataSetColumn(String fieldName, Integer dataSetId) {
        DataSet dataSet = getDataSetById(dataSetId);

        DataSetColumns dataSetColumn = uiDao.getDataSetColumn(fieldName, dataSet);
        return dataSetColumn;
    }

    public DataSetColumns createDataSetColumnByDataSet(DataSetColumnBean dataSetColumnBean, VbUser user) {
        DataSet dataSet = null;
        if (dataSetColumnBean.getDataSetId() != null) {
            dataSet = getDataSetById(dataSetColumnBean.getDataSetId());
        } else {
            dataSet = new DataSet();
        }
        String dataSetFieldName = dataSetColumnBean.getFieldName();
        DataSetColumns checkDbForColumn = uiDao.getDataSetColumn(dataSetFieldName, dataSet);

//        if (checkDbForColumn != null) {
        if (dataSetColumnBean.getExpression() != null && !dataSetColumnBean.getExpression().isEmpty()) {
            checkDbForColumn.setExpression(dataSetColumnBean.getExpression());
        }
        if (dataSetColumnBean.getFieldName() != null && !dataSetColumnBean.getFieldName().isEmpty()) {
            System.out.println(dataSetColumnBean.getFieldName());
            checkDbForColumn.setFieldName(dataSetColumnBean.getFieldName());
        }

        if (dataSetColumnBean.getDisplayName() != null && !dataSetColumnBean.getDisplayName().isEmpty()) {
            checkDbForColumn.setDisplayName(dataSetColumnBean.getDisplayName());
        }
        if (dataSetColumnBean.getDisplayFormat() != null && !dataSetColumnBean.getDisplayFormat().isEmpty()) {
            checkDbForColumn.setDisplayFormat(dataSetColumnBean.getDisplayFormat());
        }
        checkDbForColumn.setStatus(dataSetColumnBean.getStatus());
        checkDbForColumn.setFunctionName(dataSetColumnBean.getFunctionName());
        checkDbForColumn.setColumnName(dataSetColumnBean.getColumnName());
        checkDbForColumn.setBaseField(dataSetColumnBean.getBaseField());
        checkDbForColumn.setDateRangeName(dataSetColumnBean.getDateRangeName());
        checkDbForColumn.setCustomStartDate(dataSetColumnBean.getCustomStartDate());
        checkDbForColumn.setCustomEndDate(dataSetColumnBean.getCustomEndDate());
        checkDbForColumn.setLastNdays(dataSetColumnBean.getLastNdays());
        checkDbForColumn.setLastNmonths(dataSetColumnBean.getLastNmonths());
        checkDbForColumn.setLastNweeks(dataSetColumnBean.getLastNweeks());
        checkDbForColumn.setLastNyears(dataSetColumnBean.getLastNyears());
        checkDbForColumn.setDataFormat(dataSetColumnBean.getDataFormat());
        if (dataSetColumnBean.getFieldType() != null && !dataSetColumnBean.getFieldType().isEmpty()) {
            checkDbForColumn.setFieldType(dataSetColumnBean.getFieldType());
        }
        checkDbForColumn.setCategory(dataSetColumnBean.getCategory());
        checkDbForColumn.setSortPriority(dataSetColumnBean.getSortPriority());
        checkDbForColumn.setWidgetId(checkDbForColumn.getWidgetId());
        checkDbForColumn.setDataSetId(checkDbForColumn.getDataSetId());
        checkDbForColumn.setUserId(checkDbForColumn.getUserId());
        uiDao.saveOrUpdate(checkDbForColumn);
//        } else{

//        }
        return checkDbForColumn;
    }

    public List<JoinDataSetCondition> createJoinDataSet(JoinDataSetBean joinDataSetBean) {
        JoinDataSet joinDataSet = new JoinDataSet();
        List<JoinDataSetCondition> returnList = new ArrayList<>();
        joinDataSet.setId(joinDataSetBean.getId());
        joinDataSet.setDataSetName(joinDataSetBean.getDataSetName());
        joinDataSet.setDataSetIdFirst(readDataSet(joinDataSetBean.getDataSetIdFirst()));
        joinDataSet.setDataSetIdSecond(readDataSet(joinDataSetBean.getDataSetIdSecond()));
        joinDataSet.setOperationType(joinDataSetBean.getOperationType());
        uiDao.saveOrUpdate(joinDataSet);
        List<JoinDataSetBean> conditionFields = joinDataSetBean.getConditionFields();
        for (Iterator<JoinDataSetBean> iterator = conditionFields.iterator(); iterator.hasNext();) {
            JoinDataSetBean joinDataSetData = iterator.next();
            JoinDataSetCondition joinDataSetCondition = new JoinDataSetCondition();
            joinDataSetCondition.setJoinDataSetId(joinDataSet);
            joinDataSetCondition.setId(joinDataSetData.getConditionId());
            joinDataSetCondition.setColumnName(joinDataSetData.getColumnName());
            joinDataSetCondition.setConditionFieldFirst(joinDataSetData.getConditionFieldFirst());
            joinDataSetCondition.setConditionFieldSecond(joinDataSetData.getConditionFieldSecond());
            uiDao.saveOrUpdate(joinDataSetCondition);
            returnList.add(joinDataSetCondition);
        }
        return returnList;
    }

    public List<Currency> getCurrencies() {
        return uiDao.getCurrenciesTypes();
    }

    public List<Timezone> getTimeZones() {
        return uiDao.getTimezoneTypes();
    }

    public DataSetColumns deleteDataSetFormulaColumnById(Integer dataSetColumnId) {
        DataSetColumns dataSetColumn = (DataSetColumns) uiDao.read(DataSetColumns.class, dataSetColumnId);
        return (DataSetColumns) uiDao.delete(dataSetColumn);
    }

    public List<DataSetColumns> getDataSetColumnsByDataSetId(Integer dataSetId, Integer userId) {
        return uiDao.getDataSetColumnsByDataSetId(dataSetId, userId);
    }

    public List<JoinDataSetCondition> getJoinDataSetConditionById(Integer id) {
        return uiDao.getJoinDataSetConditionById(id);
    }

    public List<JoinDataSetCondition> deleteJoinDataSetConditionById(Integer conditionId, Integer joinDataSetId) {
        return uiDao.deleteJoinDataSetConditionById(conditionId, joinDataSetId);
    }

    public DashboardTemplate createDashboardTemplate(DashboardTemplateBean template, VbUser userId, Integer productId) {
        Integer templateId = template.getId();
        DashboardTemplate dashboardTemplate;
        if (templateId != null) {
            dashboardTemplate = uiDao.getDashboardTemplateById(templateId);
        } else {
            dashboardTemplate = new DashboardTemplate();
            dashboardTemplate.setAgencyId(userId.getAgencyId());
            dashboardTemplate.setUserId(userId);
            dashboardTemplate.setAgencyProductId(uiDao.getAgencyProductById(productId));
        }
        dashboardTemplate.setTemplateName(template.getTemplateName());
        uiDao.saveOrUpdate(dashboardTemplate);

        String[] tabs = template.getTabIds().split(",");
        uiDao.deleteTemplateTabs(dashboardTemplate.getId());
        for (int i = 0; i < tabs.length; i++) {
            String tabIdStr = tabs[i];
            Integer tabId = Integer.parseInt(tabIdStr);
            // DashboardTabs dashboardTab = uiDao.getTabById(tabId);
            DashboardTabs duplicateTab = duplicateTab(tabId, userId);
            TemplateTabs templateTab = new TemplateTabs();
            templateTab.setTemplateId(dashboardTemplate);
            templateTab.setUserId(userId);
            templateTab.setTabId(duplicateTab);
            uiDao.saveOrUpdate(templateTab);
        }
        return dashboardTemplate;
    }

    public DataSource createDataSourceForJoinDataSet(DataSourceBean dataSource) {
        List<DataSource> joinDataSourceList = uiDao.getJoinDataSource(dataSource.getUserId());
        System.out.println("dataSource" + dataSource.getName());
        DataSource newDataSource = new DataSource();
        if (joinDataSourceList.size() > 0) {
            System.out.println("if update --->");
            DataSource joinDataSource = joinDataSourceList.get(0);
            newDataSource.setId(joinDataSource.getId());
            newDataSource.setName(joinDataSource.getName());
            newDataSource.setDataSourceType("join");
            newDataSource.setUserId(joinDataSource.getUserId());
            newDataSource.setAgencyId(joinDataSource.getAgencyId());
            uiDao.saveOrUpdate(newDataSource);
        } else {
            System.out.println("else create ---> ");
            newDataSource.setName(dataSource.getName());
            newDataSource.setDataSourceType("join");
            newDataSource.setUserId(dataSource.getUserId());
            newDataSource.setAgencyId(dataSource.getAgencyId());
            uiDao.saveOrUpdate(newDataSource);
        }
        return newDataSource;
    }

    public List<DashboardTemplate> getTemplateId(Integer accountId, Integer productId, Integer userId) {
        return uiDao.getTemplateId(accountId, productId, userId);
    }

    public List<DashboardTemplate> getTemplateByAgencyId(Integer agencyId) {
        return uiDao.getTemplateByAgencyId(agencyId);
    }

//    public DataSetColumns addWidgetColumnToDataSetColumnList(WidgetColumn widgetColumn) {
//        DataSetColumns dataSetColumn = new DataSetColumns();
//        dataSetColumn.setBaseField(widgetColumn.getBaseFieldName());
//        dataSetColumn.setExpression(widgetColumn.getExpression());
//        dataSetColumn.setDisplayName(widgetColumn.getDisplayName());
//        dataSetColumn.setDisplayFormat(widgetColumn.getDisplayFormat());
//        dataSetColumn.setFieldName(widgetColumn.getFieldName());
//        dataSetColumn.setFieldType(widgetColumn.getFieldType());
//        return dataSetColumn;
//    }
    public List<DataSetColumns> getDataSetColumns(Integer datasetId, Integer widgetId) {
        return uiDao.getDataSetColumnOfAll(datasetId, widgetId);
    }

    public DataSetColumns getDataSetColumn(String fieldName, ColumnDef columnDef, Integer userId, Integer dataSetId, Integer widgetId) {
        DataSetColumns column = uiDao.getDataSetColumn(fieldName, userId, dataSetId, widgetId);
        if (column == null && dataSetId != null && widgetId != null) {
            column = uiDao.createDataSetColumn(columnDef, dataSetId, userId, widgetId);
        }
        return column;
    }

    public List getTabByTemplateId(Integer templateId) {
        return uiDao.getTabByTemplateId(templateId);
    }

    public List<DashboardTemplate> getDefaultTemplate(Integer agencyId) {
        return uiDao.getDefaultTemplate(agencyId);
    }

    public UserPreferences updateChartColor(UserPreferences userPreferences) {
        uiDao.saveOrUpdate(userPreferences);
        return userPreferences;
    }

    //added by Paramvir for theme settings
    public UserPreferences updateThemeSettings(UserPreferences userPreferences) {
        VbUser user = userPreferences.getUserId();
        Integer userId = user.getId();
        UserPreferences userPreferencesList = uiDao.getThemeByUserId(user);
        if (userPreferencesList != null) {
            userPreferences.setId(userPreferencesList.getId());
        }
        uiDao.saveOrUpdate(userPreferences);
        return userPreferences;
    }

    public UserPreferences getThemeByUserId(VbUser userId) {
        return uiDao.getThemeByUserId(userId);
    }

    public UserPreferences addChartColor(UserPreferences userPreferences) {
        uiDao.saveOrUpdate(userPreferences);
        return userPreferences;
    }

    public UserPreferences getChartColorByUserId(VbUser vbUser) {
        return uiDao.getChartColorByUserId(vbUser);
    }

    public List<DashboardTemplate> getTemplates(VbUser user, Agency agency, AgencyProduct agencyProduct) {
        return uiDao.getTemplates(user, agency, agencyProduct);
    }

    public List<DashboardTemplate> getUserTemplate(VbUser user) {
        return uiDao.getUserTemplate(user);
    }

    public void deleteUserTemplate(Integer templateId) {
        uiDao.deleteUserTemplate(templateId);
    }

    public DashboardTemplate updateSharedTemplateStatus(DashboardTemplate dashboardTemplate, Integer templateId) {
        DashboardTemplate template = getTemplateById(templateId);
        template.setShared(dashboardTemplate.getShared());
        uiDao.saveOrUpdate(template);
        return template;
    }

    public TabWidget editWidgetSize(Integer widgetId, Integer width) {
        TabWidget widget = getWidgetById(widgetId);
        widget.setWidth(width);
        uiDao.saveOrUpdate(widget);
        return widget;
    }

    public List<DataSetColumns> saveDataSetColumns(DataSet dataSet, TabWidget tabWidget, List<DataSetColumnBean> dataSetColumnBeans) {
        List<DataSetColumns> dataSetColumns = new ArrayList<>();
        for (Iterator<DataSetColumnBean> iterator = dataSetColumnBeans.iterator(); iterator.hasNext();) {
            DataSetColumnBean columnBean = iterator.next();
            DataSetColumns dataSetColumnFromDb = uiDao.getDataSetColumn(columnBean.getFieldName(), dataSet);
            if (dataSetColumnFromDb == null) {
                continue;
            }
            DataSetColumns dataSetColumn = new DataSetColumns();
            dataSetColumn.setFieldName(columnBean.getFieldName());
            dataSetColumn.setFieldType(columnBean.getFieldType());
            dataSetColumn.setDataSetId(dataSet);
            dataSetColumn.setDisplayFormat(columnBean.getDisplayFormat());
            dataSetColumn.setDisplayName(columnBean.getDisplayName());
            dataSetColumn.setDataFormat(columnBean.getDataFormat());
            if (tabWidget != null) {
                dataSetColumn.setWidgetId(tabWidget);
            }
            uiDao.saveOrUpdate(dataSetColumn);
            dataSetColumns.add(dataSetColumn);
        }
        return dataSetColumns;
    }

    public JoinDataSet getJoinDataSetById(Integer joinDataSetId) {
        return uiDao.getJoinDataSetById(joinDataSetId);
    }

    public List getDataSetReports(Integer dataSourceId) {
        DataSource dataSource = uiDao.getDataSourceById(dataSourceId);
        return getDataSetReports(dataSource.getName());
    }

    public List getDataSetReports(String dataSourceType) {
        return uiDao.getDataSetReport(dataSourceType);
    }

    public List<DataSourceFilter> getDataSourceFilter(String dataSourceName, String reportName) {
        return uiDao.getDataSourceFilters(dataSourceName, reportName);
    }

    public String getDataSourceQuery(String dataSource, String dataSetReportName, String level, String segment, String frequency, Date startDate, Date endDate) {
        List<String> select = new ArrayList<>();
        List<String> groupBy = new ArrayList<>();
        List<String> orderBy = new ArrayList<>();
        System.out.println("Data Source Name " + dataSource);
        System.out.println("Data Set Report Name " + dataSetReportName);
        String dateField = readProperty("datasource/datasource.properties", dataSource + "." + dataSetReportName + ".datefield");
        List<DataSourceSetting> dataSourceSettings = uiDao.getDataSourceSettings(dataSource, dataSetReportName);

        for (Iterator<DataSourceSetting> iterator = dataSourceSettings.iterator(); iterator.hasNext();) {
            DataSourceSetting dataSourceSetting = iterator.next();
            String metricQuery = dataSourceSetting.getMetricQuery();
            String metricName = dataSourceSetting.getMetricName();
            String orderByForMetric = dataSourceSetting.getOrderBy();
            String groupByForMetric = dataSourceSetting.getGroupBy();
            if (metricQuery == null && metricName == null) {
                continue;
            }
            String metricQueryName = (metricQuery == null ? metricName : metricQuery) + " " + (metricName == null ? metricQuery : metricName);
            select.add(metricQueryName);
            if (groupByForMetric != null && !groupByForMetric.trim().isEmpty()) {
                groupBy.add(groupByForMetric);
            }

            if (orderByForMetric != null && !orderByForMetric.trim().isEmpty()) {
                orderBy.add(orderByForMetric);
            }

        }

        if (level != null && !level.equalsIgnoreCase("Overall")) {
            groupBy.add(level);
            select.add(level);
        }

        if (segment != null && !segment.equalsIgnoreCase("none")) {
            groupBy.add(segment);
            select.add(segment);

        }
        if (frequency != null && !frequency.equalsIgnoreCase("none")) {
            groupBy.add(frequency);
            select.add(frequency);

        }
        String selectQry = String.join(",", select);
        String groupQry = String.join(",", groupBy);
        String orderQry = String.join(",", orderBy);

        String orderByAppender = "";
        if (orderQry != null && !(orderQry.trim().isEmpty())) {
            orderByAppender = " order by " + orderQry;
        }
        String groupByAppender = "";
        if (groupQry != null && !(groupQry.trim().isEmpty())) {
            groupByAppender = " group by " + groupQry;
        }
        String tableName = dataSetReportName;

        String startDateStr = DateUtils.dateToString(startDate, "yyyy-MM-dd");
        String endDateStr = DateUtils.dateToString(endDate, "yyyy-MM-dd");
        String whereCondition = "";
        if (dateField != null) {
            whereCondition = " where " + dateField + " between '" + startDateStr + "' and '" + endDateStr + "' ";
        }
        String queryStr = "select " + selectQry + " from " + tableName + whereCondition + groupByAppender + " " + orderByAppender;
        return queryStr;

    }

    public Map<String, List<DataSetReport>> getReportDetails(String dataSourceName, String dataSetReport) {
        ClassLoader classLoader = PropertyReader.class.getClassLoader();
        File file = new File(classLoader.getResource("datasource/datasource.properties").getFile());
        String levelPropertyValue = readProperty(file, dataSourceName + "." + dataSetReport + ".level");
        String segmentsPropertyValue = readProperty(file, dataSourceName + "." + dataSetReport + ".segment");
        String frequencyPropertyValue = readProperty(file, dataSourceName + "." + dataSetReport + ".frequency");
        List<DataSetReport> levelsForReport = new ArrayList<>();
        List<DataSetReport> segmentsForReport = new ArrayList<>();
        List<DataSetReport> frequencyForReport = new ArrayList<>();

        String[] frequencyList = frequencyPropertyValue.split(";");
        for (int i = 0; i < frequencyList.length; i++) {
            String levels = frequencyList[i];
            String[] level = levels.split(",");
            DataSetReport segmentDataSetReport = new DataSetReport();
            segmentDataSetReport.setType(level[0]);
            segmentDataSetReport.setName(level[1]);
            frequencyForReport.add(segmentDataSetReport);
        }
        String[] segmentsList = segmentsPropertyValue.split(";");
        for (int i = 0; i < segmentsList.length; i++) {
            String levels = segmentsList[i];
            String[] level = levels.split(",");
            DataSetReport segmentDataSetReport = new DataSetReport();
            segmentDataSetReport.setType(level[0]);
            segmentDataSetReport.setName(level[1]);
            segmentsForReport.add(segmentDataSetReport);
        }
        String[] levelsList = levelPropertyValue.split(";");
        for (int i = 0; i < levelsList.length; i++) {
            String levels = levelsList[i];
            String[] level = levels.split(",");
            DataSetReport levelDataSetReport = new DataSetReport();
            levelDataSetReport.setType(level[0]);
            levelDataSetReport.setName(level[1]);
            levelsForReport.add(levelDataSetReport);
        }

        Map<String, List<DataSetReport>> returnMap = new HashMap();
        returnMap.put("levels", levelsForReport);
        returnMap.put("segments", segmentsForReport);
        returnMap.put("frequency", frequencyForReport);
        return returnMap;
    }

    public String readProperty(String filename, String name) {
        ClassLoader classLoader = PropertyReader.class.getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        return readProperty(file, name);
    }

    public String readProperty(File file, String name) {
        FileInputStream fileInput;
        Properties prop = new Properties();
        String returnProp = null;
        try {
            fileInput = new FileInputStream(file);
            prop.load(fileInput);
            returnProp = prop.getProperty(name);
            fileInput.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnProp;
    }

    public void deleteDerivedColumn(String deleteColumns, Integer widgetId) {
        String[] columnArray = deleteColumns.split(",");
        for (int i = 0; i < columnArray.length; i++) {
            uiDao.deleteDerivedColumn(columnArray[i], widgetId);
            uiDao.deleteWidgetColumn(columnArray[i], widgetId);
        }
    }

    public UserPreferences getChartColorById(Integer userId) {
        return uiDao.getChartColorById(userId);
    }

    public List<DashboardTemplate> getTemplates(Integer userId, Integer agencyId, AgencyProduct agencyProduct) {
        return uiDao.getTemplates(userId, agencyId, agencyProduct);
    }
}
