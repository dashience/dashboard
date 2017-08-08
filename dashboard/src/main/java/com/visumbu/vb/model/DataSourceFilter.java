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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author duc-dev-04
 */
@Entity
@Table(name = "data_source_filter")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataSourceFilter.findAll", query = "SELECT p FROM DataSourceFilter p")
    , @NamedQuery(name = "DataSourceFilter.findById", query = "SELECT p FROM DataSourceFilter p WHERE p.id = :id")})
public class DataSourceFilter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "data_source_name")
    private String dataSourceName;
    @Size(max = 255)
    @Column(name = "report_name")
    private String reportName;
    @Size(max = 255)
    @Column(name = "field_name")
    private String fieldName;
    @Size(max = 255)
    @Column(name = "display_name")
    private String displayName;
    @Size(max = 255)
    @Column(name = "field_type")
    private String fieldType;
    @Size(max = 255)
    @Column(name = "dependent_filters")
    private String dependentFilters;
    @Column(name = "order_by")
    private String orderBy;

    public DataSourceFilter() {
    }

    public DataSourceFilter(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
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

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getDependentFilters() {
        return dependentFilters;
    }

    public void setDependentFilters(String dependentFilters) {
        this.dependentFilters = dependentFilters;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
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
        if (!(object instanceof DataSourceFilter)) {
            return false;
        }
        DataSourceFilter other = (DataSourceFilter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.DataSourceFilter[ id=" + id + " ]";
    }

}
