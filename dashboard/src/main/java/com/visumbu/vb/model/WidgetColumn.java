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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author duc-dev-04
 */
@Entity
@Table(name = "widget_column")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WidgetColumn.findAll", query = "SELECT w FROM WidgetColumn w")
    , @NamedQuery(name = "WidgetColumn.findById", query = "SELECT w FROM WidgetColumn w WHERE w.id = :id")
    , @NamedQuery(name = "WidgetColumn.findByWidgetId", query = "SELECT w FROM WidgetColumn w WHERE w.widgetId.id = :id")
    , @NamedQuery(name = "WidgetColumn.findByFieldName", query = "SELECT w FROM WidgetColumn w WHERE w.fieldName = :fieldName")
    , @NamedQuery(name = "WidgetColumn.findByWidget", query = "SELECT w FROM WidgetColumn w WHERE w.widgetId = :widget")
    , @NamedQuery(name = "WidgetColumn.findByDisplayName", query = "SELECT w FROM WidgetColumn w WHERE w.displayName = :displayName")
    , @NamedQuery(name = "WidgetColumn.findBySortPriority", query = "SELECT w FROM WidgetColumn w WHERE w.sortPriority = :sortPriority")
    , @NamedQuery(name = "WidgetColumn.findBySortOrder", query = "SELECT w FROM WidgetColumn w WHERE w.sortOrder = :sortOrder")
    , @NamedQuery(name = "WidgetColumn.findByGroupPriority", query = "SELECT w FROM WidgetColumn w WHERE w.groupPriority = :groupPriority")
    , @NamedQuery(name = "WidgetColumn.findByAgregationFunction", query = "SELECT w FROM WidgetColumn w WHERE w.agregationFunction = :agregationFunction")
    , @NamedQuery(name = "WidgetColumn.findByFunctionParameters", query = "SELECT w FROM WidgetColumn w WHERE w.functionParameters = :functionParameters")
    , @NamedQuery(name = "WidgetColumn.findByFieldType", query = "SELECT w FROM WidgetColumn w WHERE w.fieldType = :fieldType")
    , @NamedQuery(name = "WidgetColumn.findByExpression", query = "SELECT w FROM WidgetColumn w WHERE w.expression = :expression")
    , @NamedQuery(name = "WidgetColumn.findByBaseFieldName", query = "SELECT w FROM WidgetColumn w WHERE w.baseFieldName = :baseFieldName")
    , @NamedQuery(name = "WidgetColumn.findByFieldGenerationFields", query = "SELECT w FROM WidgetColumn w WHERE w.fieldGenerationFields = :fieldGenerationFields")
    , @NamedQuery(name = "WidgetColumn.findByRemarks", query = "SELECT w FROM WidgetColumn w WHERE w.remarks = :remarks")})
public class WidgetColumn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 1024)
    @Column(name = "field_name")
    private String fieldName;
    @Column(name = "is_location")
    private Boolean isLocation;
    @Column(name = "is_latitude")
    private Boolean isLatitude;
    @Column(name = "is_longitude")
    private Boolean isLongitude;
    @Size(max = 128)
    @Column(name = "x_axis")
    private String xAxis;
    @Size(max = 128)
    @Column(name = "y_axis")
    private String yAxis;
    @Size(max = 128)
    @Column(name = "y_axis_label")
    private String yAxisLabel;
    @Size(max = 128)
    @Column(name = "x_axis_label")
    private String xAxisLabel;
    @Size(max = 1024)
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "sort_priority")
    private Integer sortPriority;
    @Size(max = 20)
    @Column(name = "sort_order")
    private String sortOrder;
    @Column(name = "group_priority")
    private Integer groupPriority;
    @Size(max = 20)
    @Column(name = "agregation_function")
    private String agregationFunction;
    @Size(max = 2096)
    @Column(name = "function_parameters")
    private String functionParameters;
    @Size(max = 32)
    @Column(name = "field_type")
    private String fieldType;
    @Size(max = 1024)
    @Column(name = "expression")
    private String expression;
    @Size(max = 1024)
    @Column(name = "base_field_name")
    private String baseFieldName;
    @Lob
    @Size(max = 65535)
    @Column(name = "field_generation_function")
    private String fieldGenerationFunction;
    @Size(max = 4096)
    @Column(name = "field_generation_fields")
    private String fieldGenerationFields;
    @Size(max = 1024)
    @Column(name = "remarks")
    private String remarks;
    @JoinColumn(name = "widget_id", referencedColumnName = "id")
    @ManyToOne
    private TabWidget widgetId;
    @Size(max = 32)
    @Column(name = "display_format")
    private String displayFormat;
    @Column(name = "width")
    private Integer width;
    @Size(max = 32)
    @Column(name = "wrap_text")
    private String wrapText;
    @Size(max = 32)
    @Column(name = "alignment")
    private String alignment;
    @Size(max = 32)
    @Column(name = "column_hide")
    private Integer columnHide;
    @Column(name = "search")
    private Boolean search;
    @Size(max = 255)
    @Column(name = "combination_type")
    private String combinationType;
    @Column(name = "derived_id")
    private Integer derivedId;

    @Size(max = 32)
    @Column(name = "category")
    private String category;

    @Size(max = 128)
    @Column(name = "group_field")
    private String groupField;

    public WidgetColumn() {
    }

    public WidgetColumn(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getSortPriority() {
        return sortPriority;
    }

    public void setSortPriority(Integer sortPriority) {
        this.sortPriority = sortPriority;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getGroupPriority() {
        return groupPriority;
    }

    public void setGroupPriority(Integer groupPriority) {
        this.groupPriority = groupPriority;
    }

    public String getAgregationFunction() {
        return agregationFunction;
    }

    public void setAgregationFunction(String agregationFunction) {
        this.agregationFunction = agregationFunction;
    }

    public String getFunctionParameters() {
        return functionParameters;
    }

    public void setFunctionParameters(String functionParameters) {
        this.functionParameters = functionParameters;
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

    public String getBaseFieldName() {
        return baseFieldName;
    }

    public void setBaseFieldName(String baseFieldName) {
        this.baseFieldName = baseFieldName;
    }

    public String getFieldGenerationFunction() {
        return fieldGenerationFunction;
    }

    public void setFieldGenerationFunction(String fieldGenerationFunction) {
        this.fieldGenerationFunction = fieldGenerationFunction;
    }

    public String getFieldGenerationFields() {
        return fieldGenerationFields;
    }

    public void setFieldGenerationFields(String fieldGenerationFields) {
        this.fieldGenerationFields = fieldGenerationFields;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getWrapText() {
        return wrapText;
    }

    public void setWrapText(String wrapText) {
        this.wrapText = wrapText;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public Integer getColumnHide() {
        return columnHide;
    }

    public void setColumnHide(Integer columnHide) {
        this.columnHide = columnHide;
    }

    public Boolean getSearch() {
        return search;
    }

    public void setSearch(Boolean search) {
        this.search = search;
    }

    public String getGroupField() {
        return groupField;
    }

    public void setGroupField(String groupField) {
        this.groupField = groupField;
    }

    public String getCombinationType() {
        return combinationType;
    }

    public void setCombinationType(String combinationType) {
        this.combinationType = combinationType;
    }

    public Integer getDerivedId() {
        return derivedId;
    }

    public void setDerivedId(Integer derivedId) {
        this.derivedId = derivedId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsLocation() {
        return isLocation;
    }

    public void setIsLocation(Boolean isLocation) {
        this.isLocation = isLocation;
    }

    public Boolean getIsLatitude() {
        return isLatitude;
    }

    public void setIsLatitude(Boolean isLatitude) {
        this.isLatitude = isLatitude;
    }

    public Boolean getIsLongitude() {
        return isLongitude;
    }

    public void setIsLongitude(Boolean isLongitude) {
        this.isLongitude = isLongitude;
    }

    @XmlTransient
    @JsonIgnore
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
        if (!(object instanceof WidgetColumn)) {
            return false;
        }
        WidgetColumn other = (WidgetColumn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.WidgetColumn[ id=" + id + " ]";
    }

}
