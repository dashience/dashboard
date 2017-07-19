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
@Table(name = "settings")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Settings.findAll", query = "SELECT s FROM Settings s")
    , @NamedQuery(name = "Settings.findById", query = "SELECT s FROM Settings s WHERE s.id = :id")
    , @NamedQuery(name = "Settings.findByGroupName", query = "SELECT s FROM Settings s WHERE s.groupName = :groupName")
    , @NamedQuery(name = "Settings.findByPropertyName", query = "SELECT s FROM Settings s WHERE s.propertyName = :propertyName")
    , @NamedQuery(name = "Settings.findByPropertyType", query = "SELECT s FROM Settings s WHERE s.propertyType = :propertyType")
    , @NamedQuery(name = "Settings.findByPropertyValue", query = "SELECT s FROM Settings s WHERE s.propertyValue = :propertyValue")
    , @NamedQuery(name = "Settings.findByRemarks", query = "SELECT s FROM Settings s WHERE s.remarks = :remarks")})
public class Settings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "group_name")
    private String groupName;
    @Size(max = 255)
    @Column(name = "property_name")
    private String propertyName;
    @Size(max = 255)
    @Column(name = "property_type")
    private String propertyType;
    @Size(max = 255)
    @Column(name = "property_value")
    private String propertyValue;
    @Size(max = 255)
    @Column(name = "remarks")
    private String remarks;
    @Size(max = 255)
    @Column(name = "default_chart_color")
    private String defaultChartColor;
    public Settings() {
    }

    public Settings(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDefaultChartColor() {
        return defaultChartColor;
    }

    public void setDefaultChartColor(String defaultChartColor) {
        this.defaultChartColor = defaultChartColor;
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
        if (!(object instanceof Settings)) {
            return false;
        }
        Settings other = (Settings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.Settings[ id=" + id + " ]";
    }
    
}
