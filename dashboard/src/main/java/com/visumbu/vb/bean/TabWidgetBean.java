/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.UserAccount;
import com.visumbu.vb.model.VbUser;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author duc-dev-04
 */
public class TabWidgetBean {

    private Integer id;
    private String chartType;
    private Short closable;
    private Integer col;
    private String dataset;
    private String datasource;
    private String dimension;
    private String displayColumns;
    private Short editable;
    private String filters;
    private String icon;
    private Integer minHeight;
    private Short minimizable;
    private Integer paginationCount;
    private Short refreshable;
    private Integer row;
    private String sort;
    private String status;
    private String widgetTitle;
    private Integer width;
    private String widthClass;
    private Integer widgetOrder;
    private String directUrl;
    private String productName;
    private Boolean tableFooter;
    private Boolean zeroSuppression;
    private String dateDuration;
    private String customRange;
    private String frequencyDuration;
    private String productDisplayName;
    private Integer maxRecord;
    private Integer dataSetId;
    private Integer dataSourceId;
    private String content;
    private String dateRangeName;
    private String customStartDate;
    private String customEndDate;
    private Integer lastNdays;
    private Integer lastNmonths;
    private Integer lastNweeks;
    private Integer lastNyears;
    private String jsonData;
    private String queryFilter;
    private String isGridLine;
    private List<WidgetColumnBean> widgetColumns;
    private Account accountId;
    private VbUser createdBy;
    private String timeSegment;
    private String productSegment;
    private String networkType;
    private Integer templateUserId;
    private String chartColorOption;
    private String dynamicFilter;
    private String dynamicFilterJsonData;
    private String dynamicFilterAllColumn;

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

    public List<WidgetColumnBean> getWidgetColumns() {
        return widgetColumns;
    }

    public void setWidgetColumns(List<WidgetColumnBean> widgetColumns) {
        this.widgetColumns = widgetColumns;
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

    public Boolean getZeroSuppression() {
        return zeroSuppression;
    }

    public void setZeroSuppression(Boolean zeroSuppression) {
        this.zeroSuppression = zeroSuppression;
    }

    public String getDateDuration() {
        return dateDuration;
    }

    public void setDateDuration(String dateDuration) {
        this.dateDuration = dateDuration;
    }

    public String getProductDisplayName() {
        return productDisplayName;
    }

    public void setProductDisplayName(String productDisplayName) {
        this.productDisplayName = productDisplayName;
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

    public Integer getDataSetId() {
        return dataSetId;
    }

    public void setDataSetId(Integer dataSetId) {
        this.dataSetId = dataSetId;
    }

    public Integer getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Integer dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateRangeName() {
        return dateRangeName;
    }

    public void setDateRangeName(String dateRangeName) {
        this.dateRangeName = dateRangeName;
    }

    public String getCustomStartDate() {
        return customStartDate;
    }

    public void setCustomStartDate(String customStartDate) {
        this.customStartDate = customStartDate;
    }

    public String getCustomEndDate() {
        return customEndDate;
    }

    public void setCustomEndDate(String customEndDate) {
        this.customEndDate = customEndDate;
    }

    public Integer getLastNdays() {
        return lastNdays;
    }

    public void setLastNdays(Integer lastNdays) {
        this.lastNdays = lastNdays;
    }

    public Integer getLastNmonths() {
        return lastNmonths;
    }

    public void setLastNmonths(Integer lastNmonths) {
        this.lastNmonths = lastNmonths;
    }

    public Integer getLastNweeks() {
        return lastNweeks;
    }

    public void setLastNweeks(Integer lastNweeks) {
        this.lastNweeks = lastNweeks;
    }

    public Integer getLastNyears() {
        return lastNyears;
    }

    public void setLastNyears(Integer lastNyears) {
        this.lastNyears = lastNyears;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getQueryFilter() {
        return queryFilter;
    }

    public void setQueryFilter(String queryFilter) {
        this.queryFilter = queryFilter;
    }

    public String getDynamicFilter() {
        return dynamicFilter;
    }

    public void setDynamicFilter(String dynamicFilter) {
        this.dynamicFilter = dynamicFilter;
    }

    public String getDynamicFilterJsonData() {
        return dynamicFilterJsonData;
    }

    public void setDynamicFilterJsonData(String dynamicFilterJsonData) {
        this.dynamicFilterJsonData = dynamicFilterJsonData;
    }

    public String getDynamicFilterAllColumn() {
        return dynamicFilterAllColumn;
    }

    public void setDynamicFilterAllColumn(String dynamicFilterAllColumn) {
        this.dynamicFilterAllColumn = dynamicFilterAllColumn;
    }

    public String getIsGridLine() {
        return isGridLine;
    }

    public void setIsGridLine(String isGridLine) {
        this.isGridLine = isGridLine;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    public VbUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(VbUser createdBy) {
        this.createdBy = createdBy;
    }

    public String getTimeSegment() {
        return timeSegment;
    }

    public void setTimeSegment(String timeSegment) {
        this.timeSegment = timeSegment;
    }

    public String getProductSegment() {
        return productSegment;
    }

    public void setProductSegment(String productSegment) {
        this.productSegment = productSegment;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public Integer getTemplateUserId() {
        return templateUserId;
    }

    public void setTemplateUserId(Integer templateUserId) {
        this.templateUserId = templateUserId;
    }

    public String getChartColorOption() {
        return chartColorOption;
    }

    public void setChartColorOption(String chartColorOption) {
        this.chartColorOption = chartColorOption;
    }

    @Override
    public String toString() {
        return "TabWidgetBean{" + "id=" + id + ", chartType=" + chartType + ", closable=" + closable + ", col=" + col + ", dataset=" + dataset + ", datasource=" + datasource + ", dimension=" + dimension + ", displayColumns=" + displayColumns + ", editable=" + editable + ", filters=" + filters + ", icon=" + icon + ", minHeight=" + minHeight + ", minimizable=" + minimizable + ", paginationCount=" + paginationCount + ", refreshable=" + refreshable + ", row=" + row + ", sort=" + sort + ", status=" + status + ", widgetTitle=" + widgetTitle + ", width=" + width + ", widthClass=" + widthClass + ", widgetOrder=" + widgetOrder + ", directUrl=" + directUrl + ", productName=" + productName + ", tableFooter=" + tableFooter + ", zeroSuppression=" + zeroSuppression + ", dateDuration=" + dateDuration + ", customRange=" + customRange + ", frequencyDuration=" + frequencyDuration + ", productDisplayName=" + productDisplayName + ", maxRecord=" + maxRecord + ", dataSetId=" + dataSetId + ", dataSourceId=" + dataSourceId + ", content=" + content + ", dateRangeName=" + dateRangeName + ", customStartDate=" + customStartDate + ", customEndDate=" + customEndDate + ", lastNdays=" + lastNdays + ", lastNmonths=" + lastNmonths + ", lastNweeks=" + lastNweeks + ", lastNyears=" + lastNyears + ", jsonData=" + jsonData + ", queryFilter=" + queryFilter + ", isGridLine=" + isGridLine + ", widgetColumns=" + widgetColumns + ", accountId=" + accountId + ", createdBy=" + createdBy + ", timeSegment=" + timeSegment + ", productSegment=" + productSegment + ", networkType=" + networkType + '}';
    }

}
