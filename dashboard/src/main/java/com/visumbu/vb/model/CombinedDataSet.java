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
@Table(name = "combined_data_set")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CombinedDataSet.findAll", query = "SELECT c FROM CombinedDataSet c")
    , @NamedQuery(name = "CombinedDataSet.findById", query = "SELECT c FROM CombinedDataSet c WHERE c.id = :id")
    , @NamedQuery(name = "CombinedDataSet.findByOperationType", query = "SELECT c FROM CombinedDataSet c WHERE c.operationType = :operationType")
    , @NamedQuery(name = "CombinedDataSet.findByDataSetName", query = "SELECT c FROM CombinedDataSet c WHERE c.dataSetName = :dataSetName")})
public class CombinedDataSet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "data_set_name")
    private String dataSetName;
    @Size(max = 255)
    @Column(name = "operation_type")
    private String operationType;
    @JoinColumn(name = "data_set_id_first", referencedColumnName = "id")
    @ManyToOne
    private DataSet dataSetIdFirst;
    @JoinColumn(name = "data_set_id_second", referencedColumnName = "id")
    @ManyToOne
    private DataSet dataSetIdSecond;

    public CombinedDataSet() {
    }

    public CombinedDataSet(Integer id) {
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

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public DataSet getDataSetIdFirst() {
        return dataSetIdFirst;
    }

    public void setDataSetIdFirst(DataSet dataSetIdFirst) {
        this.dataSetIdFirst = dataSetIdFirst;
    }

    public DataSet getDataSetIdSecond() {
        return dataSetIdSecond;
    }

    public void setDataSetIdSecond(DataSet dataSetIdSecond) {
        this.dataSetIdSecond = dataSetIdSecond;
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
        if (!(object instanceof CombinedDataSet)) {
            return false;
        }
        CombinedDataSet other = (CombinedDataSet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.CombinedDataSet[ id=" + id + " ]";
    }
    
}
