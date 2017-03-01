/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author duc-dev-04
 */
@Entity
@Table(name = "tab_widget")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TabWidget.findAll", query = "SELECT t FROM TabWidget t")
    , @NamedQuery(name = "TabWidget.findById", query = "SELECT t FROM TabWidget t WHERE t.id = :id")
    , @NamedQuery(name = "TabWidget.findByChartType", query = "SELECT t FROM TabWidget t WHERE t.chartType = :chartType")
    , @NamedQuery(name = "TabWidget.findByClosable", query = "SELECT t FROM TabWidget t WHERE t.closable = :closable")
    , @NamedQuery(name = "TabWidget.findByCol", query = "SELECT t FROM TabWidget t WHERE t.col = :col")
    , @NamedQuery(name = "TabWidget.findByCreatedTime", query = "SELECT t FROM TabWidget t WHERE t.createdTime = :createdTime")
    , @NamedQuery(name = "TabWidget.findByDimension", query = "SELECT t FROM TabWidget t WHERE t.dimension = :dimension")
    , @NamedQuery(name = "TabWidget.findByEditable", query = "SELECT t FROM TabWidget t WHERE t.editable = :editable")
    , @NamedQuery(name = "TabWidget.findByFilters", query = "SELECT t FROM TabWidget t WHERE t.filters = :filters")
    , @NamedQuery(name = "TabWidget.findByIcon", query = "SELECT t FROM TabWidget t WHERE t.icon = :icon")
    , @NamedQuery(name = "TabWidget.findByMinHeight", query = "SELECT t FROM TabWidget t WHERE t.minHeight = :minHeight")
    , @NamedQuery(name = "TabWidget.findByMinimizable", query = "SELECT t FROM TabWidget t WHERE t.minimizable = :minimizable")
    , @NamedQuery(name = "TabWidget.findByPaginationCount", query = "SELECT t FROM TabWidget t WHERE t.paginationCount = :paginationCount")
    , @NamedQuery(name = "TabWidget.findByRefreshable", query = "SELECT t FROM TabWidget t WHERE t.refreshable = :refreshable")
    , @NamedQuery(name = "TabWidget.findByRow", query = "SELECT t FROM TabWidget t WHERE t.row = :row")
    , @NamedQuery(name = "TabWidget.findBySort", query = "SELECT t FROM TabWidget t WHERE t.sort = :sort")
    , @NamedQuery(name = "TabWidget.findByStatus", query = "SELECT t FROM TabWidget t WHERE t.status = :status")
    , @NamedQuery(name = "TabWidget.findByWidgetTitle", query = "SELECT t FROM TabWidget t WHERE t.widgetTitle = :widgetTitle")
    , @NamedQuery(name = "TabWidget.findByWidth", query = "SELECT t FROM TabWidget t WHERE t.width = :width")
    , @NamedQuery(name = "TabWidget.findByWidthClass", query = "SELECT t FROM TabWidget t WHERE t.widthClass = :widthClass")
    , @NamedQuery(name = "TabWidget.findByWidgetOrder", query = "SELECT t FROM TabWidget t WHERE t.widgetOrder = :widgetOrder")
    , @NamedQuery(name = "TabWidget.findByDirectUrl", query = "SELECT t FROM TabWidget t WHERE t.directUrl = :directUrl")
    , @NamedQuery(name = "TabWidget.findByProductDisplayName", query = "SELECT t FROM TabWidget t WHERE t.productDisplayName = :productDisplayName")
    , @NamedQuery(name = "TabWidget.findByProductName", query = "SELECT t FROM TabWidget t WHERE t.productName = :productName")
    , @NamedQuery(name = "TabWidget.findByTableFooter", query = "SELECT t FROM TabWidget t WHERE t.tableFooter = :tableFooter")
    , @NamedQuery(name = "TabWidget.findByDateDuration", query = "SELECT t FROM TabWidget t WHERE t.dateDuration = :dateDuration")
    , @NamedQuery(name = "TabWidget.findByZeroSuppression", query = "SELECT t FROM TabWidget t WHERE t.zeroSuppression = :zeroSuppression")
    , @NamedQuery(name = "TabWidget.findByMaxRecord", query = "SELECT t FROM TabWidget t WHERE t.maxRecord = :maxRecord")
    , @NamedQuery(name = "TabWidget.findByCustomRange", query = "SELECT t FROM TabWidget t WHERE t.customRange = :customRange")
    , @NamedQuery(name = "TabWidget.findByFrequencyDuration", query = "SELECT t FROM TabWidget t WHERE t.frequencyDuration = :frequencyDuration")
    , @NamedQuery(name = "TabWidget.findByDataset", query = "SELECT t FROM TabWidget t WHERE t.dataset = :dataset")
    , @NamedQuery(name = "TabWidget.findByDatasource", query = "SELECT t FROM TabWidget t WHERE t.datasource = :datasource")})
public class TabWidget implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "chart_type")
    private String chartType;
    @Column(name = "closable")
    private Short closable;
    @Column(name = "col")
    private Integer col;
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Size(max = 255)
    @Column(name = "dimension")
    private String dimension;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "display_columns")
    private String displayColumns;
    @Column(name = "editable")
    private Short editable;
    @Size(max = 255)
    @Column(name = "filters")
    private String filters;
    @Size(max = 255)
    @Column(name = "icon")
    private String icon;
    @Column(name = "min_height")
    private Integer minHeight;
    @Column(name = "minimizable")
    private Short minimizable;
    @Column(name = "pagination_count")
    private Integer paginationCount;
    @Column(name = "refreshable")
    private Short refreshable;
    @Column(name = "row")
    private Integer row;
    @Size(max = 255)
    @Column(name = "sort")
    private String sort;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Size(max = 255)
    @Column(name = "widget_title")
    private String widgetTitle;
    @Column(name = "width")
    private Integer width;
    @Size(max = 255)
    @Column(name = "width_class")
    private String widthClass;
    @Column(name = "widget_order")
    private Integer widgetOrder;
    @Size(max = 4095)
    @Column(name = "direct_url")
    private String directUrl;
    @Size(max = 255)
    @Column(name = "product_display_name")
    private String productDisplayName;
    @Size(max = 255)
    @Column(name = "productName")
    private String productName;
    @Size(max = 45)
    @Column(name = "table_footer")
    private Boolean tableFooter;
    @Size(max = 255)
    @Column(name = "date_duration")
    private String dateDuration;
    @Column(name = "zero_suppression")
    private Boolean zeroSuppression;
    @Column(name = "max_record")
    private Integer maxRecord;
    
    
    @Size(max = 255)
    @Column(name = "frequency_duration")
    private String frequencyDuration;
    @Size(max = 255)
    @Column(name = "custom_range")
    private String customRange;
    
    @Lob
    @Size(max = 65535)
    @Column(name = "content")
    private String content;
    
    @Size(max = 255)
    @Column(name = "dataset")
    private String dataset;
    @Size(max = 255)
    @Column(name = "datasource")
    private String datasource;
    @JoinColumn(name = "data_set_id", referencedColumnName = "id")
    @ManyToOne
    private DataSet dataSetId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private VbUser createdBy;
    @JoinColumn(name = "data_source_id", referencedColumnName = "id")
    @ManyToOne
    private DataSource dataSourceId;
    @JoinColumn(name = "tab_id", referencedColumnName = "id")
    @ManyToOne
    private DashboardTabs tabId;
    
    @Transient
    private List<WidgetColumn> columns;

    @Transient
    private List<Map<String, Object>> data;

    public TabWidget() {
    }

    public TabWidget(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public Short getClosable() {
        return closable;
    }

    public void setClosable(Short closable) {
        this.closable = closable;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getDisplayColumns() {
        return displayColumns;
    }

    public void setDisplayColumns(String displayColumns) {
        this.displayColumns = displayColumns;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
    
    public Short getEditable() {
        return editable;
    }

    public void setEditable(Short editable) {
        this.editable = editable;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Integer minHeight) {
        this.minHeight = minHeight;
    }

    public Short getMinimizable() {
        return minimizable;
    }

    public void setMinimizable(Short minimizable) {
        this.minimizable = minimizable;
    }

    public Integer getPaginationCount() {
        return paginationCount;
    }

    public void setPaginationCount(Integer paginationCount) {
        this.paginationCount = paginationCount;
    }

    public Short getRefreshable() {
        return refreshable;
    }

    public void setRefreshable(Short refreshable) {
        this.refreshable = refreshable;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWidgetTitle() {
        return widgetTitle;
    }

    public void setWidgetTitle(String widgetTitle) {
        this.widgetTitle = widgetTitle;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getWidthClass() {
        return widthClass;
    }

    public void setWidthClass(String widthClass) {
        this.widthClass = widthClass;
    }

    public Integer getWidgetOrder() {
        return widgetOrder;
    }

    public void setWidgetOrder(Integer widgetOrder) {
        this.widgetOrder = widgetOrder;
    }

    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
    }

    public String getProductDisplayName() {
        return productDisplayName;
    }

    public void setProductDisplayName(String productDisplayName) {
        this.productDisplayName = productDisplayName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Boolean getTableFooter() {
        return tableFooter;
    }

    public void setTableFooter(Boolean tableFooter) {
        this.tableFooter = tableFooter;
    }

    public String getDateDuration() {
        return dateDuration;
    }

    public void setDateDuration(String dateDuration) {
        this.dateDuration = dateDuration;
    }

    public Boolean getZeroSuppression() {
        return zeroSuppression;
    }

    public void setZeroSuppression(Boolean zeroSuppression) {
        this.zeroSuppression = zeroSuppression;
    }

    public Integer getMaxRecord() {
        return maxRecord;
    }

    public void setMaxRecord(Integer maxRecord) {
        this.maxRecord = maxRecord;
    }

    public String getCustomRange() {
        return customRange;
    }

    public void setCustomRange(String customRange) {
        this.customRange = customRange;
    }

    public String getFrequencyDuration() {
        return frequencyDuration;
    }

    public void setFrequencyDuration(String frequencyDuration) {
        this.frequencyDuration = frequencyDuration;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }   

    public VbUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(VbUser createdBy) {
        this.createdBy = createdBy;
    }
    
    public DataSet getDataSetId() {
        return dataSetId;
    }

    public void setDataSetId(DataSet dataSetId) {
        this.dataSetId = dataSetId;
    }

    public DataSource getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(DataSource dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public DashboardTabs getTabId() {
        return tabId;
    }

    public void setTabId(DashboardTabs tabId) {
        this.tabId = tabId;
    }
    
     public List<WidgetColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<WidgetColumn> columns) {
        this.columns = columns;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TabWidget)) {
            return false;
        }
        TabWidget other = (TabWidget) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.TabWidget[ id=" + id + " ]";
    }
    
}
