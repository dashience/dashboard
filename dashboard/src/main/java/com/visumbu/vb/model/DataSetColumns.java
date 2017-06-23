/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author deeta1
 */
@Entity
@Table(name = "data_set_columns")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataSetColumns.findAll", query = "SELECT d FROM DataSetColumns d")
    , @NamedQuery(name = "DataSetColumns.findById", query = "SELECT d FROM DataSetColumns d WHERE d.id = :id")
    , @NamedQuery(name = "DataSetColumns.findByDisplayName", query = "SELECT d FROM DataSetColumns d WHERE d.displayName = :displayName")
    , @NamedQuery(name = "DataSetColumns.findByFieldName", query = "SELECT d FROM DataSetColumns d WHERE d.fieldName = :fieldName")
    , @NamedQuery(name = "DataSetColumns.findByFieldType", query = "SELECT d FROM DataSetColumns d WHERE d.fieldType = :fieldType")
    , @NamedQuery(name = "DataSetColumns.findByExpression", query = "SELECT d FROM DataSetColumns d WHERE d.expression = :expression")
    , @NamedQuery(name = "DataSetColumns.findByStatus", query = "SELECT d FROM DataSetColumns d WHERE d.status = :status")
    , @NamedQuery(name = "DataSetColumns.findByDisplayFormat", query = "SELECT d FROM DataSetColumns d WHERE d.displayFormat = :displayFormat")
    , @NamedQuery(name = "DataSetColumns.findByFunctionName", query = "SELECT d FROM DataSetColumns d WHERE d.functionName = :functionName")
    , @NamedQuery(name = "DataSetColumns.findByColumnName", query = "SELECT d FROM DataSetColumns d WHERE d.columnName = :columnName")
    , @NamedQuery(name = "DataSetColumns.findByBaseField", query = "SELECT d FROM DataSetColumns d WHERE d.baseField = :baseField")
    , @NamedQuery(name = "DataSetColumns.findByDateRangeName", query = "SELECT d FROM DataSetColumns d WHERE d.dateRangeName = :dateRangeName")
    , @NamedQuery(name = "DataSetColumns.findByCustomStartDate", query = "SELECT d FROM DataSetColumns d WHERE d.customStartDate = :customStartDate")
    , @NamedQuery(name = "DataSetColumns.findByCustomEndDate", query = "SELECT d FROM DataSetColumns d WHERE d.customEndDate = :customEndDate")
    , @NamedQuery(name = "DataSetColumns.findByLastNdays", query = "SELECT d FROM DataSetColumns d WHERE d.lastNdays = :lastNdays")
    , @NamedQuery(name = "DataSetColumns.findByLastNweeks", query = "SELECT d FROM DataSetColumns d WHERE d.lastNweeks = :lastNweeks")
    , @NamedQuery(name = "DataSetColumns.findByLastNmonths", query = "SELECT d FROM DataSetColumns d WHERE d.lastNmonths = :lastNmonths")
    , @NamedQuery(name = "DataSetColumns.findByLastNyears", query = "SELECT d FROM DataSetColumns d WHERE d.lastNyears = :lastNyears")})
public class DataSetColumns implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "display_name")
    private String displayName;
    @Size(max = 255)
    @Column(name = "field_name")
    private String fieldName;
    @Size(max = 255)
    @Column(name = "field_type")
    private String fieldType;
    @Size(max = 255)
    @Column(name = "expression")
    private String expression;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Size(max = 255)
    @Column(name = "display_format")
    private String displayFormat;
    @Size(max = 255)
    @Column(name = "function_name")
    private String functionName;
    @Size(max = 255)
    @Column(name = "column_name")
    private String columnName;
    @Size(max = 255)
    @Column(name = "base_field")
    private String baseField;
    @Size(max = 255)
    @Column(name = "date_range_name")
    private String dateRangeName;
    @Size(max = 255)
    @Column(name = "custom_start_date")
    private String customStartDate;
    @Size(max = 255)
    @Column(name = "custom_end_date")
    private String customEndDate;
    @Column(name = "last_ndays")
    private Integer lastNdays;
    @Column(name = "last_nweeks")
    private Integer lastNweeks;
    @Column(name = "last_nmonths")
    private Integer lastNmonths;
    @Column(name = "sort_priority")
    private Integer sortPriority;
    @Column(name = "last_nyears")
    private Integer lastNyears;
    @JoinColumn(name = "data_set_id", referencedColumnName = "id")
    @ManyToOne
    private DataSet dataSetId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private VbUser userId;
    @JoinColumn(name = "widget_id", referencedColumnName = "id")
    @ManyToOne
    private TabWidget widgetId;

    public DataSetColumns() {
    }

    public DataSetColumns(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getBaseField() {
        return baseField;
    }

    public void setBaseField(String baseField) {
        this.baseField = baseField;
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

    public Integer getLastNweeks() {
        return lastNweeks;
    }

    public void setLastNweeks(Integer lastNweeks) {
        this.lastNweeks = lastNweeks;
    }

    public Integer getLastNmonths() {
        return lastNmonths;
    }

    public void setLastNmonths(Integer lastNmonths) {
        this.lastNmonths = lastNmonths;
    }

    public Integer getLastNyears() {
        return lastNyears;
    }

    public void setLastNyears(Integer lastNyears) {
        this.lastNyears = lastNyears;
    }

    public DataSet getDataSetId() {
        return dataSetId;
    }

    public void setDataSetId(DataSet dataSetId) {
        this.dataSetId = dataSetId;
    }

    public Integer getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(Integer sortPriority) {
        this.sortPriority = sortPriority;
    }

    public VbUser getUserId() {
        return userId;
    }

    public void setUserId(VbUser userId) {
        this.userId = userId;
    }

    public TabWidget getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(TabWidget widgetId) {
        this.widgetId = widgetId;
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
        if (!(object instanceof DataSetColumns)) {
            return false;
        }
        DataSetColumns other = (DataSetColumns) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.DataSetColumns[ id=" + id + " ]";
    }

}
