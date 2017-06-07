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
@Table(name = "combined_dataset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CombinedDataset.findAll", query = "SELECT c FROM CombinedDataset c")
    , @NamedQuery(name = "CombinedDataset.findById", query = "SELECT c FROM CombinedDataset c WHERE c.id = :id")
    , @NamedQuery(name = "CombinedDataset.findByDatasetName", query = "SELECT c FROM CombinedDataset c WHERE c.datasetName = :datasetName")
    , @NamedQuery(name = "CombinedDataset.findByOperationType", query = "SELECT c FROM CombinedDataset c WHERE c.operationType = :operationType")})
public class CombinedDataset implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "dataset_name")
    private String datasetName;
    @Size(max = 255)
    @Column(name = "operation_type")
    private String operationType;
    @JoinColumn(name = "dataset_id_first", referencedColumnName = "id")
    @ManyToOne
    private DataSet datasetIdFirst;
    @JoinColumn(name = "dataset_id_second", referencedColumnName = "id")
    @ManyToOne
    private DataSet datasetIdSecond;

    public CombinedDataset() {
    }

    public CombinedDataset(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public DataSet getDatasetIdFirst() {
        return datasetIdFirst;
    }

    public void setDatasetIdFirst(DataSet datasetIdFirst) {
        this.datasetIdFirst = datasetIdFirst;
    }

    public DataSet getDatasetIdSecond() {
        return datasetIdSecond;
    }

    public void setDatasetIdSecond(DataSet datasetIdSecond) {
        this.datasetIdSecond = datasetIdSecond;
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
        if (!(object instanceof CombinedDataset)) {
            return false;
        }
        CombinedDataset other = (CombinedDataset) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.CombinedDataset[ id=" + id + " ]";
    }
    
}
