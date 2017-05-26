/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao;

import com.visumbu.vb.admin.dao.bean.ProductBean;
import com.visumbu.vb.bean.DatasetColumnBean;
import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.AdwordsCriteria;
import com.visumbu.vb.model.AgencyProduct;
import com.visumbu.vb.model.Currency;
import com.visumbu.vb.model.Dashboard;
import com.visumbu.vb.model.DashboardTabs;
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.DatasetColumns;
import com.visumbu.vb.model.DefaultFieldProperties;
import com.visumbu.vb.model.Product;
import com.visumbu.vb.model.Report;
import com.visumbu.vb.model.ReportColumn;
import com.visumbu.vb.model.ReportType;
import com.visumbu.vb.model.ReportWidget;
import com.visumbu.vb.model.TabWidget;
import com.visumbu.vb.model.Timezone;
import com.visumbu.vb.model.UserAccount;
import com.visumbu.vb.model.UserPermission;
import com.visumbu.vb.model.VbUser;
import com.visumbu.vb.model.WidgetColumn;
import com.visumbu.vb.model.WidgetTag;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

/**
 *
 * @author netphenix
 */
@Transactional
@Repository("uiDao")
public class UiDao extends BaseDao {

    public List<Dashboard> getDashboards(VbUser user) {
        String queryStr = "select d from Dashboard d where d.userId = :user";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("user", user);
        return query.list();
    }

    public List<UserAccount> getUserAccountByUser(VbUser user) {
        System.out.println(user);
        String queryStr = "select d from UserAccount d where (d.userId.status is null or d.userId.status != 'Deleted') and d.accountId.agencyId = d.userId.agencyId and d.userId.id = :userId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("userId", user.getId());
        return query.list();
    }

    public List<DashboardTabs> getAgencyProductTab(Integer agencyProductId) {
        String queryStr = "select d from DashboardTabs d where (d.status is null or d.status != 'Deleted') and d.agencyProductId.id = :agencyProductId order by tabOrder";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("agencyProductId", agencyProductId);
        return query.list();
    }

//    public List<DashboardTabs> getDashboardTabs(Integer dbId) {
//        String queryStr = "select d from DashboardTabs d where (d.status is null or d.status != 'Deleted') and d.dashboardId.id = :dashboardId order by tabOrder";
//        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
//        query.setParameter("dashboardId", dbId);
//        return query.list();
//    }
    public List<DashboardTabs> getDashboardTabsByProduct(Integer productId, Integer userId) {
        String queryStr = "SELECT d FROM DashboardTabs d WHERE (d.status is null or d.status != 'Deleted') and d.dashboardId.productId.id = :productId and d.dashboardId.userId.id = :userId order by tabOrder";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("productId", productId);
        query.setParameter("userId", userId);
        return query.list();
    }

    public List<DashboardTabs> getDashboardTabsByDbId(Integer dashboardId, Integer userId) {
        String queryStr = "SELECT d FROM DashboardTabs d WHERE (d.status is null or d.status != 'Deleted') and d.dashboardId.id = :dashboardId and d.dashboardId.userId.id = :userId order by tabOrder";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dashboardId", dashboardId);
        query.setParameter("userId", userId);
        return query.list();
    }

    public DashboardTabs getDashboardTabById(Integer tabId) {
        DashboardTabs dashboardTabs = (DashboardTabs) sessionFactory.getCurrentSession().get(DashboardTabs.class, tabId);
        return dashboardTabs;
    }

    public DashboardTabs deleteDashboardTab(Integer id) {
        String queryStr = "update DashboardTabs d set status = 'Deleted'  where d.id = :dashboardTabId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dashboardTabId", id);
        query.executeUpdate();
        return null;
    }

    public List<TabWidget> getTabWidget(Integer tabId) {
        String queryStr = "select d from TabWidget d where d.tabId.id = :tabId and (status is null or status != 'Deleted') order by widgetOrder";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("tabId", tabId);

        List<TabWidget> tabWidgets = query.list();
        for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
            TabWidget widget = iterator.next();
            widget.setColumns(getColumns(widget));
        }

        return tabWidgets;
    }

    public List<TabWidget> getWidget(Integer widgetId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery("TabWidget.findById");
        query.setParameter("id", widgetId);
        List<TabWidget> tabWidgets = query.list();
        return tabWidgets;
    }

    public List<TabWidget> getReportWidgetByWidgetId(Integer widgetId) {
        String queryStr = "select d from TabWidget d where (status is null or status != 'Deleted') and d.id = :widgetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetId", widgetId);

        List<TabWidget> tabWidgets = query.list();
        for (Iterator<TabWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
            TabWidget widget = iterator.next();
            widget.setColumns(getColumns(widget));
        }

        return tabWidgets;
    }

    public TabWidget getTabWidgetById(Integer widgetId) {
        if (widgetId == null) {
            return null;
        }
        TabWidget tabWidget = (TabWidget) sessionFactory.getCurrentSession().get(TabWidget.class, widgetId);
        System.out.println(tabWidget);
        tabWidget.setColumns(getColumns(tabWidget));
        return tabWidget;
    }

    public Dashboard getDashboardById(Integer dashboardId) {
        return (Dashboard) sessionFactory.getCurrentSession().get(Dashboard.class, dashboardId);
    }

    public AgencyProduct getAgencyProductById(Integer agencyProductId) {
        return (AgencyProduct) sessionFactory.getCurrentSession().get(AgencyProduct.class, agencyProductId);
    }

    private List<WidgetColumn> getColumns(TabWidget widget) {
        String queryStr = "select d from WidgetColumn d where d.widgetId = :widgetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetId", widget);
        return query.list();
    }

    public List<WidgetColumn> getColumnsById(Integer widgetId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery("WidgetColumn.findById");
        query.setParameter("id", widgetId);
        return query.list();
    }

    public List<WidgetColumn> getWidgetColumnsByWidgetId(Integer widgetId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery("WidgetColumn.findByWidgetId");
        query.setParameter("id", widgetId);
        return query.list();
    }

    public List<WidgetTag> getWidgetTagsByWidgetId(Integer widgetId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery("WidgetTag.findByWidgetId");
        query.setParameter("id", widgetId);
        return query.list();
    }

    public WidgetColumn addWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        widgetColumn.setWidgetId(getTabWidgetById(widgetId));
        create(widgetColumn);
        return widgetColumn;
    }

    public WidgetColumn updateWidgetColumn(Integer widgetId, WidgetColumn widgetColumn) {
        if (widgetColumn.getId() != null) {
            widgetColumn.setWidgetId(getTabWidgetById(widgetId));
            update(widgetColumn);
        }
        return widgetColumn;
    }

    public void deleteWidgetColumns(Integer widgetId) {
        String queryStr = "delete from WidgetColumn d where d.widgetId.id = :widgetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetId", widgetId);
        query.executeUpdate();
    }

    public TabWidget deleteTabWidget(Integer id) {
        String queryReport = "delete from ReportWidget d where d.widgetId.id = :widgetId";
        Query querySess = sessionFactory.getCurrentSession().createQuery(queryReport);
        querySess.setParameter("widgetId", id);
        querySess.executeUpdate();

        String queryStr = "delete from WidgetColumn d where d.widgetId.id = :widgetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("widgetId", id);
        query.executeUpdate();

        String queryTag = "delete from WidgetTag d where d.widgetId.id = :widgetId";
        Query querys = sessionFactory.getCurrentSession().createQuery(queryTag);
        querys.setParameter("widgetId", id);
        querys.executeUpdate();

        delete(getTabWidgetById(id));
        return null;
    }

    public WidgetColumn deleteWidgetColumn(Integer id) {
        delete(getTabWidgetColumnById(id));
        return null;
    }

    private WidgetColumn getTabWidgetColumnById(Integer id) {
        WidgetColumn widgetColumn = (WidgetColumn) sessionFactory.getCurrentSession().get(WidgetColumn.class, id);
        return widgetColumn;
    }

    public DashboardTabs getTabById(Integer tabId) {
        DashboardTabs dashboardTab = (DashboardTabs) sessionFactory.getCurrentSession().get(DashboardTabs.class, tabId);
        return dashboardTab;
    }

    public TabWidget saveTabWidget(TabWidget tabWidget) {
        sessionFactory.getCurrentSession().saveOrUpdate(tabWidget);
        return tabWidget;
    }

    public WidgetColumn getWidgetColunmById(Integer id) {
        WidgetColumn widgetColumn = (WidgetColumn) sessionFactory.getCurrentSession().get(WidgetColumn.class, id);
        return widgetColumn;
    }

    public Integer getWidgetCount(Integer id) {
        System.out.println("id ---> " + id);
        Query query = sessionFactory.getCurrentSession().getNamedQuery("TabWidget.findByTabId");
        query.setParameter("tabId", id);
//        Integer count = (Integer) query.list().get(0);
//        String widgetCountQuery = ("SELECT count(t.id) from TabWidget t where t.tabId.id = :tabId");
//        Query query = sessionFactory.getCurrentSession().createQuery(widgetCountQuery);
//        query.setParameter("tabId", id);
//        Integer count =at com.visumbu.vb.admin.dao.UiDao.getWidgetCount(UiDao.java:231) (Integer) query.list().get(0);
        int count = (int) (long) query.list().get(0);
        System.out.println("count ---> " + count);
        return count;
    }

    public void saveOrUpdate(Object object) {
        sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public List readReportType(Integer reportTypeId) {
        ReportType reportTypes = (ReportType) sessionFactory.getCurrentSession().get(ReportType.class, reportTypeId);
        return (List) reportTypes;
    }

    public Report addReport(Report report, Integer reportTypeId) {
        report.setReportTypeId(getReportTypeById(reportTypeId));
        create(report);
        return report;
    }

    private ReportType getReportTypeById(Integer reportTypeId) {
        ReportType reportType = (ReportType) sessionFactory.getCurrentSession().get(ReportType.class, reportTypeId);
        return reportType;
    }

//    public Report getReportById(Integer reportId) {
//        Report report = (Report) sessionFactory.getCurrentSession().get(Report.class, reportId);
//        return report;
//    }
//    public ReportWidget getReportWidgetById(Integer reportId) {
//        ReportWidget reportWidget = (ReportWidget) sessionFactory.getCurrentSession().get(ReportWidget.class, reportId);
//        return reportWidget;
//    }
    public ReportWidget saveReportWidget(ReportWidget reportWidget) {
        sessionFactory.getCurrentSession().saveOrUpdate(reportWidget);
        return reportWidget;
    }

    public void deleteReportColumns(Integer reportId) {
        String queryStr = "delete from ReportColumn d where d.reportId.id = :reportId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("reportId", reportId);
        query.executeUpdate();
    }

//    public List<ReportWidget> getReportWidget(Integer reportId) {
//        String queryStr = "select d from ReportWidget d where d.reportId.id = :reportId";
//        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
//        query.setParameter("reportId", reportId);
//
//        List<ReportWidget> tabWidgets = query.list();
//        for (Iterator<ReportWidget> iterator = tabWidgets.iterator(); iterator.hasNext();) {
//            TabWidget widget = iterator.next().getWidgetId();
//            widget.setColumns(getColumns(widget));
//        }
//        return tabWidgets;
//    }
//    private List<WidgetColumn> getColumns(ReportWidget widget) {
//        String queryStr = "select d from WidgetColumn d where d.widgetId = :widgetId";
//        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
//        query.setParameter("widgetId", widget);
//        return query.list();
//    }
//    public ReportWidget deleteReportWidget(Integer id) {
//        delete(getReportWidgetById(id));
//        return null;
//    }
    public String updateWidgetUpdateOrder(Integer tabId, String widgetOrder) {
        String[] widgetOrderArray = widgetOrder.split(",");
        for (int i = 0; i < widgetOrderArray.length; i++) {
            Integer widgetId = Integer.parseInt(widgetOrderArray[i]);
            TabWidget tabWidget = getTabWidgetById(widgetId);
            tabWidget.setWidgetOrder(i);
            update(tabWidget);
        }
        return "Success";
    }

    public String updateTabOrder(Integer dashboardId, String tabOrder) {
        String[] tabOrderArray = tabOrder.split(",");
        for (int i = 0; i < tabOrderArray.length; i++) {
            Integer tabId = Integer.parseInt(tabOrderArray[i]);
            DashboardTabs dashboardTabs = getDashboardTabById(tabId);
            dashboardTabs.setTabOrder(i);
            update(dashboardTabs);
        }
        return null;
    }

//    public String updateReportOrder(Integer reportId, String widgetOrder) {
//        System.out.println(widgetOrder);
//        String[] reportOrderArray = widgetOrder.split(",");
//        for (int i = 0; i < reportOrderArray.length; i++) {
//            Integer reportWidgetId = Integer.parseInt(reportOrderArray[i]);
//            ReportWidget reportWidget = getReportWidgetById(reportWidgetId);
//            // reportWidget.setWidgetOrder(i);
//        System.out.println(reportWidget);
//            update(reportWidget);
//        }
//        return null;
//    }
//    public Report deleteReport(Integer reportId) {
//        String queryString = "update Report d set status = 'Deleted' where d.id = :reportId";
//        Query querySess = sessionFactory.getCurrentSession().createQuery(queryString);
//        querySess.setParameter("reportId", reportId);
//        querySess.executeUpdate();
//        return null;
//    }
    public DefaultFieldProperties getDefaultFieldProperties(String fieldName) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery("DefaultFieldProperties.findByFieldName");
        query.setParameter("fieldName", fieldName);
        List fieldProperties = query.list();
        if (fieldProperties.size() > 0) {
            return (DefaultFieldProperties) fieldProperties.get(0);
        }
        return null;
    }

    public AdwordsCriteria getAdwordsCriteria(Integer criteriaId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery("AdwordsCriteria.findById");
        query.setParameter("id", criteriaId);
        List data = query.list();
        if (data.size() > 0) {
            return (AdwordsCriteria) data.get(0);
        }
        return null;
    }

    public List<Product> getDealerProduct(Integer dealerId) {
        String queryStr = "select p from DealerProduct dp, Product p where (p.productName = dp.productName or (dp.productName='PPC' and p.productName = 'Paid Search')"
                + " or (p.productName like 'You%Tube%' and dp.productName like 'Video')) and dp.dealerId.id = :dealerId";

        queryStr = "select distinct p.* from product p,  "
                + "(SELECT distinct case when service_name is null then product_name else service_name end service_name "
                + " FROM dealer_product_source dps join  "
                + " dealer_product dp join  "
                + " dealer d  left join  "
                + " dealer_product_service ps on ps.dealer_product_id = dp.id  "
                + " where dp.id = dps.dealer_product_id   "
                + " and dp.dealer_id = d.id  and d.id = :dealerId "
                + " ) a where (a.service_name = p.product_name or (a.service_name like 'YouTube%' and p.product_name='Video') "
                + " or (a.service_name like 'PPC' and p.product_name='Paid Search') "
                + " or (a.service_name like 'Digital Ad Package%' and p.product_name='Paid Search') "
                + " or (a.service_name like 'Facebook%' and p.product_name='Paid Social') "
                + " or (a.service_name like 'Call Tracking' and p.product_name='Paid Search') "
                + " or (a.service_name like '%SEO%' and p.product_name='SEO') "
                + " ) order by p.id";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(queryStr)
                .addScalar("id", IntegerType.INSTANCE)
                .addScalar("product_name", StringType.INSTANCE)
                .addScalar("remarks", StringType.INSTANCE)
                .addScalar("icon", StringType.INSTANCE)
                .addScalar("show_product", StringType.INSTANCE)
                .addScalar("product_order", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ProductBean.class));
        query.setParameter("dealerId", dealerId);
        return query.list();

    }

    public DataSource getDataSourceById(Integer dataSourceId) {
        DataSource dataSource = (DataSource) sessionFactory.getCurrentSession().get(DataSource.class, dataSourceId);
        return dataSource;
    }

    public DataSet getDataSetById(Integer dataSetId) {
        System.out.println("datasetID ---> " + dataSetId);
        DataSet dataSet = (DataSet) sessionFactory.getCurrentSession().get(DataSet.class, dataSetId);
        return dataSet;
    }

    public void removeDsFromDataSet(Integer id) {
        String queryStr = "delete DataSet d where d.dataSourceId.id = :dataSourceId";
//        String queryStr = "update DataSet d set data_source_id=NULL  where d.dataSourceId = :dataSourceId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dataSourceId", id);
        query.executeUpdate();
    }

    public void removeDsFromWidget(Integer id) {
        String queryStr = "update TabWidget d set dataSourceId=NULL, dataSetId=NULL  where d.dataSourceId.id = :dataSourceId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dataSourceId", id);
        query.executeUpdate();
    }

    public DataSource deleteDataSource(Integer id) {
        removeDsFromWidget(id);
        removeDsFromDataSet(id);
        String queryStr = "delete DataSource d where d.id = :dataSourceId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dataSourceId", id);
        query.executeUpdate();
        return null;
    }

    public void removeDataSetFromWidget(Integer id) {
        String queryStr = "update TabWidget d set dataSetId=NULL  where d.dataSetId.id = :dataSetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dataSetId", id);
        query.executeUpdate();
    }

    public DataSet deleteDataSet(Integer id) {
        removeDataSetFromWidget(id);
        String queryStr = "delete DataSet d where d.id = :dataSetId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("dataSetId", id);
        query.executeUpdate();
        return null;
    }

    public List<UserAccount> getUserAccountById(Integer userId) {
        String queryStr = "select d from UserAccount d where d.userId.id = :userId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("userId", userId);
        return query.list();
    }

    public UserAccount deleteUserAccount(Integer userAccountId) {
        String queryString = "delete UserAccount d where d.id = :userAccountId";
        Query querySess = sessionFactory.getCurrentSession().createQuery(queryString);
        querySess.setParameter("userAccountId", userAccountId);
        querySess.executeUpdate();
        return null;
    }

//    public List getUserAccountId(Integer userId) {
//        String queryStr = "select d from AccountUser d where d.userId.id = :userId";
//        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
//        query.setParameter("userId", userId);           
//        return query.list();
//    }
    public List getUserPermission(Integer userId) {
        String queryStr = "select d from UserPermission d where (d.userId.status is null or d.userId.status != 'Deleted') and d.userId.id = :userId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("userId", userId);
        return query.list();
    }

    public UserPermission deleteUserPermission(Integer userPermissionId) {
        String queryString = "delete UserPermission d where d.id = :userPermissionId";
        Query querySess = sessionFactory.getCurrentSession().createQuery(queryString);
        querySess.setParameter("userPermissionId", userPermissionId);
        querySess.executeUpdate();
        return null;
    }

    public List<VbUser> getUsersByAgencyUser(VbUser user) {
        String queryStr = "select d from VbUser d where (d.agencyId.status is null or d.agencyId.status != 'Deleted') and (d.status is null or d.status != 'Deleted') and d.agencyId.id = :agencyId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("agencyId", user.getAgencyId().getId());
        return query.list();
    }

    public VbUser deleteUser(Integer id) {
        String queryString = "update VbUser d set status = 'Deleted' where d.id = :userId";
        Query querySess = sessionFactory.getCurrentSession().createQuery(queryString);
        querySess.setParameter("userId", id);
        querySess.executeUpdate();
        return null;
    }

    public List<DataSet> getDataSetByUser(VbUser user) {
        String queryStr = "select d from DataSet d where d.userId.id = :userId and d.agencyId = :agencyId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("userId", user.getId());
        query.setParameter("agencyId", user.getAgencyId());
        return query.list();
    }

    public List<DataSource> getDataSourceByUser(VbUser user) {
        String queryStr = "select d from DataSource d where d.userId.id = :userId and d.agencyId = :agencyId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("userId", user.getId());
        query.setParameter("agencyId", user.getAgencyId());
        return query.list();
    }

//    public List<Report> getAgencyReport(VbUser user) {
//        String queryStr = "select d from Report d where (d.status is null or d.status != 'Deleted') and d.agencyId.id = :agencyId";
//        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
//        query.setParameter("agencyId", user.getAgencyId().getId());
//        return query.list();
//    }    
    public List<WidgetTag> getTagWidgetByWidgetId(Integer widgetId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery("WidgetTag.findByWidgetId");
        query.setParameter("id", widgetId);
        return query.list();
    }

    public List<DatasetColumns> getDatasetColumnsByDatasetId(Integer datasetId) {
        String queryStr = "SELECT d FROM DatasetColumns d where d.datasetId.id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("id", datasetId);
        return query.list();
    }

    public List<DatasetColumns> createDataSetFormulaColumn(DatasetColumnBean dataSetColumn) {
        DataSet dataset = getDataSetById(dataSetColumn.getDatasetId());
        List<DatasetColumns> datasetList = new ArrayList();
        System.out.println("create columns function");
        List<DatasetColumnBean> datasetColumnList = dataSetColumn.getTableColumns();
        System.out.println("datasetColumnList ----> " + datasetColumnList);
        for (Iterator<DatasetColumnBean> datasetColumnBean = datasetColumnList.iterator(); datasetColumnBean.hasNext();) {
            System.out.println("create Data set columns ----> ");
            DatasetColumnBean datasetColumn = datasetColumnBean.next();
            System.out.println(datasetColumn.getId() + "____________" + dataSetColumn.getId());
            if (!Objects.equals(datasetColumn.getId(), dataSetColumn.getId())) {
                System.out.println("if");
                DatasetColumns datasetFields = new DatasetColumns();
                System.out.println(datasetColumn.getFieldName() + " : " + datasetColumn.getDisplayName() + " ; " + datasetColumn.getFieldType());
                datasetFields.setId(datasetColumn.getId());
                datasetFields.setExpression(datasetColumn.getExpression());
                datasetFields.setFieldName(datasetColumn.getFieldName());
                datasetFields.setDisplayName(datasetColumn.getDisplayName());
                datasetFields.setDisplayFormat(datasetColumn.getDisplayFormat());
                datasetFields.setStatus(datasetColumn.getStatus());
                datasetFields.setFunctionName(datasetColumn.getFunctionName());
                datasetFields.setFieldType(datasetColumn.getFieldType());
                datasetFields.setDatasetId(dataset);
                saveOrUpdate(datasetFields);
                datasetList.add(datasetFields);
            } else if (datasetColumn.getId() == null && dataSetColumn.getId() == null) {
                System.out.println("else if");
                DatasetColumns datasetFields = new DatasetColumns();
                System.out.println(datasetColumn.getFieldName() + " : " + datasetColumn.getDisplayName() + " ; " + datasetColumn.getFieldType());
                datasetFields.setId(datasetColumn.getId());
                datasetFields.setExpression(datasetColumn.getExpression());
                datasetFields.setFieldName(datasetColumn.getFieldName());
                datasetFields.setDisplayName(datasetColumn.getDisplayName());
                datasetFields.setDisplayFormat(datasetColumn.getDisplayFormat());
                datasetFields.setStatus(datasetColumn.getStatus());
                datasetFields.setFunctionName(datasetColumn.getFunctionName());
                datasetFields.setFieldType(datasetColumn.getFieldType());
                datasetFields.setDatasetId(dataset);
                saveOrUpdate(datasetFields);
                datasetList.add(datasetFields);
            }
        }
        DatasetColumns datasetColumns = new DatasetColumns();
        datasetColumns.setId(dataSetColumn.getId());
        datasetColumns.setExpression(dataSetColumn.getExpression());
        datasetColumns.setFieldName(dataSetColumn.getFieldName());
        datasetColumns.setFieldType(dataSetColumn.getFieldType());
        datasetColumns.setDisplayName(dataSetColumn.getDisplayName());
        datasetColumns.setDisplayFormat(dataSetColumn.getDisplayFormat());
        datasetColumns.setStatus(dataSetColumn.getStatus());
        datasetColumns.setFunctionName(dataSetColumn.getFunctionName());
        datasetColumns.setDatasetId(dataset);
        saveOrUpdate(datasetColumns);
        datasetList.add(datasetColumns);
        return datasetList;
    }

    public List<Currency> getCurrenciesTypes() {
        // System.out.println("dao is calling....");
        String queryStr = "SELECT c FROM Currency c";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        return query.list();
    }

    public List<Timezone> getTimezoneTypes() {
        // System.out.println("dao is calling....");
        String queryStr = "SELECT t FROM Timezone t";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        return query.list();

    }

    public TabWidget getWidgetByIdAndDataSetId(Integer widgetId, Integer datasetId) {
        String queryStr = "Select t FROM TabWidget t where t.dataSetId.id = :datasetId and id = :id";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
        query.setParameter("id", widgetId);
        query.setParameter("datasetId", datasetId);
        List tabWidgetData = query.list();
        if(tabWidgetData == null || tabWidgetData.isEmpty()){
            return null;
        } 
        System.out.println("tabWidgetData ---> "+tabWidgetData);
        TabWidget tabWidget = (TabWidget) tabWidgetData.get(0);
        tabWidget.setColumns(getColumns(tabWidget));
        return tabWidget;
    }

}
