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
@Table(name = "report_column")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportColumn.findAll", query = "SELECT r FROM ReportColumn r")
    , @NamedQuery(name = "ReportColumn.findById", query = "SELECT r FROM ReportColumn r WHERE r.id = :id")
    , @NamedQuery(name = "ReportColumn.findByFieldName", query = "SELECT r FROM ReportColumn r WHERE r.fieldName = :fieldName")
    , @NamedQuery(name = "ReportColumn.findByDisplayName", query = "SELECT r FROM ReportColumn r WHERE r.displayName = :displayName")
    , @NamedQuery(name = "ReportColumn.findBySortPriority", query = "SELECT r FROM ReportColumn r WHERE r.sortPriority = :sortPriority")
    , @NamedQuery(name = "ReportColumn.findBySortOrder", query = "SELECT r FROM ReportColumn r WHERE r.sortOrder = :sortOrder")
    , @NamedQuery(name = "ReportColumn.findByGroupPriority", query = "SELECT r FROM ReportColumn r WHERE r.groupPriority = :groupPriority")
    , @NamedQuery(name = "ReportColumn.findByAgregationFunction", query = "SELECT r FROM ReportColumn r WHERE r.agregationFunction = :agregationFunction")
    , @NamedQuery(name = "ReportColumn.findByFunctionParameters", query = "SELECT r FROM ReportColumn r WHERE r.functionParameters = :functionParameters")
    , @NamedQuery(name = "ReportColumn.findByFieldType", query = "SELECT r FROM ReportColumn r WHERE r.fieldType = :fieldType")
    , @NamedQuery(name = "ReportColumn.findByBaseFieldName", query = "SELECT r FROM ReportColumn r WHERE r.baseFieldName = :baseFieldName")
    , @NamedQuery(name = "ReportColumn.findByFieldGenerationFields", query = "SELECT r FROM ReportColumn r WHERE r.fieldGenerationFields = :fieldGenerationFields")
    , @NamedQuery(name = "ReportColumn.findByRemarks", query = "SELECT r FROM ReportColumn r WHERE r.remarks = :remarks")
    , @NamedQuery(name = "ReportColumn.findByXAxis", query = "SELECT r FROM ReportColumn r WHERE r.xAxis = :xAxis")
    , @NamedQuery(name = "ReportColumn.findByXAxisLabel", query = "SELECT r FROM ReportColumn r WHERE r.xAxisLabel = :xAxisLabel")
    , @NamedQuery(name = "ReportColumn.findByYAxis", query = "SELECT r FROM ReportColumn r WHERE r.yAxis = :yAxis")
    , @NamedQuery(name = "ReportColumn.findByYAxisLabel", query = "SELECT r FROM ReportColumn r WHERE r.yAxisLabel = :yAxisLabel")
    , @NamedQuery(name = "ReportColumn.findByDisplayFormat", query = "SELECT r FROM ReportColumn r WHERE r.displayFormat = :displayFormat")
    , @NamedQuery(name = "ReportColumn.findByAlignment", query = "SELECT r FROM ReportColumn r WHERE r.alignment = :alignment")})
public class ReportColumn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 1024)
    @Column(name = "field_name")
    private String fieldName;
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
    @Size(max = 25)
    @Column(name = "x_axis")
    private String xAxis;
    @Size(max = 25)
    @Column(name = "x_axis_label")
    private String xAxisLabel;
    @Size(max = 25)
    @Column(name = "y_axis")
    private String yAxis;
    @Size(max = 25)
    @Column(name = "y_axis_label")
    private String yAxisLabel;
    @Size(max = 25)
    @Column(name = "display_format")
    private String displayFormat;    
    @Column(name = "width")
    private Integer width;
    @Size(max = 25)
    @Column(name = "wrap_text")
    private String wrapText;
    @Size(max = 25)
    @Column(name = "alignment")
    private String alignment;
    @JoinColumn(name = "reportId", referencedColumnName = "id")
    @ManyToOne
    private ReportWidget reportId;

    public ReportColumn() {
    }

    public ReportColumn(Integer id) {
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

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
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

    @XmlTransient
    @JsonIgnore
    public ReportWidget getReportId() {
        return reportId;
    }

    public void setReportId(ReportWidget reportId) {
        this.reportId = reportId;
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
        if (!(object instanceof ReportColumn)) {
            return false;
        }
        ReportColumn other = (ReportColumn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.ReportColumn[ id=" + id + " ]";
    }
    
}
