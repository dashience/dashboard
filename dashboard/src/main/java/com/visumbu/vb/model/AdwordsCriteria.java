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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@Entity
@Table(name = "adwords_criteria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdwordsCriteria.findAll", query = "SELECT a FROM AdwordsCriteria a"),
    @NamedQuery(name = "AdwordsCriteria.findById", query = "SELECT a FROM AdwordsCriteria a WHERE a.id = :id"),
    @NamedQuery(name = "AdwordsCriteria.findByCriteriaName", query = "SELECT a FROM AdwordsCriteria a WHERE a.criteriaName = :criteriaName"),
    @NamedQuery(name = "AdwordsCriteria.findByCanonicalName", query = "SELECT a FROM AdwordsCriteria a WHERE a.canonicalName = :canonicalName"),
    @NamedQuery(name = "AdwordsCriteria.findByParentId", query = "SELECT a FROM AdwordsCriteria a WHERE a.parentId = :parentId"),
    @NamedQuery(name = "AdwordsCriteria.findByCountryCode", query = "SELECT a FROM AdwordsCriteria a WHERE a.countryCode = :countryCode"),
    @NamedQuery(name = "AdwordsCriteria.findByTargetType", query = "SELECT a FROM AdwordsCriteria a WHERE a.targetType = :targetType"),
    @NamedQuery(name = "AdwordsCriteria.findByCriteriaStatus", query = "SELECT a FROM AdwordsCriteria a WHERE a.criteriaStatus = :criteriaStatus")})
public class AdwordsCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 256)
    @Column(name = "criteria_name")
    private String criteriaName;
    @Size(max = 1024)
    @Column(name = "canonical_name")
    private String canonicalName;
    @Column(name = "parent_id")
    private Integer parentId;
    @Size(max = 16)
    @Column(name = "country_code")
    private String countryCode;
    @Size(max = 32)
    @Column(name = "target_type")
    private String targetType;
    @Size(max = 64)
    @Column(name = "criteria_status")
    private String criteriaStatus;

    public AdwordsCriteria() {
    }

    public AdwordsCriteria(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCriteriaName() {
        return criteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getCriteriaStatus() {
        return criteriaStatus;
    }

    public void setCriteriaStatus(String criteriaStatus) {
        this.criteriaStatus = criteriaStatus;
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
        if (!(object instanceof AdwordsCriteria)) {
            return false;
        }
        AdwordsCriteria other = (AdwordsCriteria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AdwordsCriteria{" + "id=" + id + ", criteriaName=" + criteriaName + ", canonicalName=" + canonicalName + ", parentId=" + parentId + ", countryCode=" + countryCode + ", targetType=" + targetType + ", criteriaStatus=" + criteriaStatus + '}';
    }
    
}
