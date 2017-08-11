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
@Table(name = "data_source_setting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataSourceSetting.findAll", query = "SELECT p FROM DataSourceSetting p")
    , @NamedQuery(name = "DataSourceSetting.findById", query = "SELECT p FROM DataSourceSetting p WHERE p.id = :id")})
public class DataSourceSetting implements Serializable {

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
    @Column(name = "metric_query")
    private String metricQuery;
    @Size(max = 255)
    @Column(name = "metric_name")
    private String metricName;
    @Size(max = 255)
    @Column(name = "order_by")
    private String orderBy;
    @Size(max = 255)
    @Column(name = "group_by")
    private String groupBy;

    public DataSourceSetting() {
    }

    public DataSourceSetting(Integer id) {
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

    public String getMetricQuery() {
        return metricQuery;
    }

    public void setMetricQuery(String metricQuery) {
        this.metricQuery = metricQuery;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
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
        if (!(object instanceof DataSourceSetting)) {
            return false;
        }
        DataSourceSetting other = (DataSourceSetting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.DataSourceSetting[ id=" + id + " ]";
    }

}
