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
 * @author duc-dev-03
 */
@Entity
@Table(name = "data_source_config_dataset_level")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataSourceConfigDatasetLevel.findAll", query = "SELECT d FROM DataSourceConfigDatasetLevel d")
    , @NamedQuery(name = "DataSourceConfigDatasetLevel.findById", query = "SELECT d FROM DataSourceConfigDatasetLevel d WHERE d.id = :id")
    , @NamedQuery(name = "DataSourceConfigDatasetLevel.findByMetrics", query = "SELECT d FROM DataSourceConfigDatasetLevel d WHERE d.metrics = :metrics")
    , @NamedQuery(name = "DataSourceConfigDatasetLevel.findByAlias", query = "SELECT d FROM DataSourceConfigDatasetLevel d WHERE d.alias = :alias")
    , @NamedQuery(name = "DataSourceConfigDatasetLevel.findByOrderBy", query = "SELECT d FROM DataSourceConfigDatasetLevel d WHERE d.orderBy = :orderBy")})
public class DataSourceConfigDatasetLevel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "metrics")
    private String metrics;
    @Size(max = 255)
    @Column(name = "alias")
    private String alias;
    @Size(max = 255)
    @Column(name = "order_by")
    private String orderBy;
    @JoinColumn(name = "data_set_config_id", referencedColumnName = "id")
    @ManyToOne
    private DataSourceConfigDataset dataSetConfigId;

    public DataSourceConfigDatasetLevel() {
    }

    public DataSourceConfigDatasetLevel(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public DataSourceConfigDataset getDataSetConfigId() {
        return dataSetConfigId;
    }

    public void setDataSetConfigId(DataSourceConfigDataset dataSetConfigId) {
        this.dataSetConfigId = dataSetConfigId;
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
        if (!(object instanceof DataSourceConfigDatasetLevel)) {
            return false;
        }
        DataSourceConfigDatasetLevel other = (DataSourceConfigDatasetLevel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.DataSourceConfigDatasetLevel[ id=" + id + " ]";
    }
    
}
