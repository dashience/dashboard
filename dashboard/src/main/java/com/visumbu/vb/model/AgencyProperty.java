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
 * @author arul
 */
@Entity
@Table(name = "agency_property")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgencyProperty.findAll", query = "SELECT a FROM AgencyProperty a")
    , @NamedQuery(name = "AgencyProperty.findById", query = "SELECT a FROM AgencyProperty a WHERE a.id = :id")
    , @NamedQuery(name = "AgencyProperty.findByPropertyName", query = "SELECT a FROM AgencyProperty a WHERE a.propertyName = :propertyName")
    , @NamedQuery(name = "AgencyProperty.findByStatus", query = "SELECT a FROM AgencyProperty a WHERE a.status = :status")
    , @NamedQuery(name = "AgencyProperty.findByPropertyValue", query = "SELECT a FROM AgencyProperty a WHERE a.propertyValue = :propertyValue")})
public class AgencyProperty implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 4096)
    @Column(name = "property_name")
    private String propertyName;
    @Size(max = 45)
    @Column(name = "status")
    private String status;
    @Size(max = 4096)
    @Column(name = "property_value")
    private String propertyValue;
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    @ManyToOne
    private Agency agencyId;

    public AgencyProperty() {
    }

    public AgencyProperty(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public Agency getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Agency agencyId) {
        this.agencyId = agencyId;
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
        if (!(object instanceof AgencyProperty)) {
            return false;
        }
        AgencyProperty other = (AgencyProperty) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.AgencyProperty[ id=" + id + " ]";
    }
    
}
