/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author duc-dev-03
 */
@Entity
@Table(name = "data_source_config_dataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataSourceConfigDataset.findAll", query = "SELECT d FROM DataSourceConfigDataset d")
    , @NamedQuery(name = "DataSourceConfigDataset.findById", query = "SELECT d FROM DataSourceConfigDataset d WHERE d.id = :id")
    , @NamedQuery(name = "DataSourceConfigDataset.findByDataSetName", query = "SELECT d FROM DataSourceConfigDataset d WHERE d.dataSetName = :dataSetName")})
public class DataSourceConfigDataset implements Serializable {

    @OneToMany(mappedBy = "dataSetConfigId")
    private Collection<DataSourceConfigDatasetFrequency> dataSourceConfigDatasetFrequencyCollection;

    @OneToMany(mappedBy = "dataSetConfigId")
    private Collection<DataSourceConfigDatasetSegment> dataSourceConfigDatasetSegmentCollection;

    @OneToMany(mappedBy = "dataSetConfigId")
    private Collection<DataSourceConfigDatasetLevel> dataSourceConfigDatasetLevelCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "data_set_name")
    private String dataSetName;
    @JoinColumn(name = "data_source_id", referencedColumnName = "id")
    @ManyToOne
    private DataSource dataSourceId;

    public DataSourceConfigDataset() {
    }

    public DataSourceConfigDataset(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataSetName() {
        return dataSetName;
    }

    public void setDataSetName(String dataSetName) {
        this.dataSetName = dataSetName;
    }

    public DataSource getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(DataSource dataSourceId) {
        this.dataSourceId = dataSourceId;
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
        if (!(object instanceof DataSourceConfigDataset)) {
            return false;
        }
        DataSourceConfigDataset other = (DataSourceConfigDataset) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.DataSourceConfigDataset[ id=" + id + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public Collection<DataSourceConfigDatasetLevel> getDataSourceConfigDatasetLevelCollection() {
        return dataSourceConfigDatasetLevelCollection;
    }

    public void setDataSourceConfigDatasetLevelCollection(Collection<DataSourceConfigDatasetLevel> dataSourceConfigDatasetLevelCollection) {
        this.dataSourceConfigDatasetLevelCollection = dataSourceConfigDatasetLevelCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<DataSourceConfigDatasetSegment> getDataSourceConfigDatasetSegmentCollection() {
        return dataSourceConfigDatasetSegmentCollection;
    }

    public void setDataSourceConfigDatasetSegmentCollection(Collection<DataSourceConfigDatasetSegment> dataSourceConfigDatasetSegmentCollection) {
        this.dataSourceConfigDatasetSegmentCollection = dataSourceConfigDatasetSegmentCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<DataSourceConfigDatasetFrequency> getDataSourceConfigDatasetFrequencyCollection() {
        return dataSourceConfigDatasetFrequencyCollection;
    }

    public void setDataSourceConfigDatasetFrequencyCollection(Collection<DataSourceConfigDatasetFrequency> dataSourceConfigDatasetFrequencyCollection) {
        this.dataSourceConfigDatasetFrequencyCollection = dataSourceConfigDatasetFrequencyCollection;
    }
    
}
