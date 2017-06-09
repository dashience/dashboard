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
@Table(name = "combined_data_set_condition")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CombinedDataSetCondition.findAll", query = "SELECT c FROM CombinedDataSetCondition c")
    , @NamedQuery(name = "CombinedDataSetCondition.findById", query = "SELECT c FROM CombinedDataSetCondition c WHERE c.id = :id")
    , @NamedQuery(name = "CombinedDataSetCondition.findByConditionFieldFirst", query = "SELECT c FROM CombinedDataSetCondition c WHERE c.conditionFieldFirst = :conditionFieldFirst")
    , @NamedQuery(name = "CombinedDataSetCondition.findByConditionFieldSecond", query = "SELECT c FROM CombinedDataSetCondition c WHERE c.conditionFieldSecond = :conditionFieldSecond")})
public class CombinedDataSetCondition implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "condition_field_first")
    private String conditionFieldFirst;
    @Size(max = 255)
    @Column(name = "condition_field_second")
    private String conditionFieldSecond;
    @JoinColumn(name = "combined_data_set_id", referencedColumnName = "id")
    @ManyToOne
    private CombinedDataSet combinedDataSetId;

    public CombinedDataSetCondition() {
    }

    public CombinedDataSetCondition(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConditionFieldFirst() {
        return conditionFieldFirst;
    }

    public void setConditionFieldFirst(String conditionFieldFirst) {
        this.conditionFieldFirst = conditionFieldFirst;
    }

    public String getConditionFieldSecond() {
        return conditionFieldSecond;
    }

    public void setConditionFieldSecond(String conditionFieldSecond) {
        this.conditionFieldSecond = conditionFieldSecond;
    }

    public CombinedDataSet getCombinedDataSetId() {
        return combinedDataSetId;
    }

    public void setCombinedDataSetId(CombinedDataSet combinedDataSetId) {
        this.combinedDataSetId = combinedDataSetId;
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
        if (!(object instanceof CombinedDataSetCondition)) {
            return false;
        }
        CombinedDataSetCondition other = (CombinedDataSetCondition) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.CombinedDataSetCondition[ id=" + id + " ]";
    }
    
}
