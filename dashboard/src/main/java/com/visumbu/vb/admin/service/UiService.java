/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.bean.ReportColumnBean;
import com.visumbu.vb.bean.ReportWidgetBean;
import com.visumbu.vb.bean.TabWidgetBean;
import com.visumbu.vb.bean.WidgetColumnBean;
import com.visumbu.vb.model.Dashboard;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.Product;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportColumn;
import com.visumbu.vb.model.ReportType;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    public List<Product> getProduct() {
        List<Product> product = uiDao.read(Product.class);
        List<Product> returnList = new ArrayList<>();
        for (Iterator<Product> iterator = product.iterator(); iterator.hasNext();) {
            Product product1 = iterator.next();
            if (product1.getProductName().equalsIgnoreCase("skyzone")) {
                returnList.add(product1);
            }
        }
        return returnList;
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

    public String updateDashboardTab(Integer dashboardId, String tabOrder) {
        return uiDao.updateTabOrder(dashboardId, tabOrder);
    }

    public List<DashboardTabs> getDashboardTabs(Integer dbId) {
        return uiDao.getDashboardTabs(dbId);
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

    public Dashboard getDashboardById(Integer dashboardId) {
        return uiDao.getDashboardById(dashboardId);
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
        if (tabWidgetBean.getDataSourceId()!= null) {
            dataSource = uiDao.getDataSourceById(tabWidgetBean.getDataSourceId());

        } else {
            dataSource = new DataSource();
        }
        
        DataSet dataSet = null;
        if (tabWidgetBean.getDataSourceId()!= null) {
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
        tabWidget.setMaxRecord(tabWidgetBean.getMaxRecord());
        tabWidget.setDatasource(tabWidgetBean.getDatasource());
        tabWidget.setDataset(tabWidgetBean.getDataset());
        tabWidget.setDataSetId(dataSet);
        tabWidget.setDataSourceId(dataSource);
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

    public Report addReport(Report report, Integer reportTypeId) {
        return uiDao.addReport(report, reportTypeId);
    }

    public Report updateReport(Report report) {
        return (Report) uiDao.update(report);
    }

    public String updateReportOrder(Integer reportId, String widgetOrder) {
        return uiDao.updateReportOrder(reportId, widgetOrder);
    }

    public Report deleteReport(Integer reportId) {
        return (Report) uiDao.delete(reportId);
    }

    public List getReport() {
        List<Report> report = uiDao.read(Report.class);
        return report;
    }

    public ReportWidget createReportWidget(Integer reportId, ReportWidget reportWidget) {
        reportWidget.setReportId(uiDao.getReportById(reportId));
        if (reportWidget.getId() != null) {
            ReportWidget reportWidgetDb = uiDao.getReportWidgetById(reportWidget.getId());
            if (reportWidget.getWidgetTitle() != null) {
                reportWidgetDb.setWidgetTitle(reportWidget.getWidgetTitle());
            }
            if (reportWidget.getDirectUrl() != null) {
                reportWidgetDb.setDirectUrl(reportWidget.getDirectUrl());
            }
            if (reportWidget.getChartType() != null) {
                reportWidgetDb.setChartType(reportWidget.getChartType());
            }
            return (ReportWidget) uiDao.update(reportWidgetDb);
        }
        return (ReportWidget) uiDao.create(reportWidget);
    }

    public ReportWidget saveReportWidget(Integer reportId, ReportWidgetBean reportWidgetBean) {
        ReportWidget reportWidget = null;
        if (reportWidgetBean.getId() != null) {
            reportWidget = uiDao.getReportWidgetById(reportWidgetBean.getId());

        } else {
            reportWidget = new ReportWidget();
        }
        reportWidget.setChartType(reportWidgetBean.getChartType());
        reportWidget.setDirectUrl(reportWidgetBean.getDirectUrl());
        reportWidget.setWidgetTitle(reportWidgetBean.getWidgetTitle());
        reportWidget.setProductName(reportWidgetBean.getProductName());
        reportWidget.setProductDisplayName(reportWidgetBean.getProductDisplayName());
        ReportWidget savedReportWidget = uiDao.saveReportWidget(reportWidget);
        List<ReportColumnBean> reportColumns = reportWidgetBean.getReportColumns();
        uiDao.deleteReportColumns(reportWidget.getId());
        for (Iterator<ReportColumnBean> iterator = reportColumns.iterator(); iterator.hasNext();) {
            ReportColumnBean reportColumnBean = iterator.next();
            ReportColumn reportColumn = new ReportColumn();
            reportColumn.setFieldName(reportColumnBean.getFieldName());
            reportColumn.setDisplayFormat(reportColumnBean.getDisplayFormat());
            reportColumn.setDisplayName(reportColumnBean.getDisplayName());
            reportColumn.setSortOrder(reportColumnBean.getSortOrder());
            reportColumn.setGroupPriority(reportColumnBean.getGroupPriority());
            reportColumn.setAgregationFunction(reportColumnBean.getAgregationFunction());
            reportColumn.setxAxis(reportColumnBean.getxAxis());
            reportColumn.setyAxis(reportColumnBean.getyAxis());
            reportColumn.setWidth(reportColumnBean.getWidth());
            reportColumn.setAlignment(reportColumnBean.getAlignment());
            reportColumn.setReportId(savedReportWidget);
            uiDao.saveOrUpdate(reportColumn);
        }
        return savedReportWidget;
    }

    public List getReportWidget(Integer reportId) {
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
        DataSource dataSource = read(id);
        return (DataSource) uiDao.delete(dataSource);
    }

    public List<DataSource> getDataSource() {
        List<DataSource> dataSource = uiDao.read(DataSource.class);
        return dataSource;
    }

    public DataSource update(DataSource dataSource) {
        return (DataSource) uiDao.update(dataSource);
    }

    public List<DataSet> getDateSet() {
        List<DataSet> dataSet = uiDao.read(DataSet.class);
        return dataSet;
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
        DataSet dataSet = readDataSet(id);
        return (DataSet) uiDao.delete(dataSet);
    }
}
