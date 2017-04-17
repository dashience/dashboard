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
@Table(name = "default_field_properties")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DefaultFieldProperties.findAll", query = "SELECT p FROM DefaultFieldProperties p")
    , @NamedQuery(name = "DefaultFieldProperties.findById", query = "SELECT p FROM DefaultFieldProperties p WHERE p.id = :id")
    , @NamedQuery(name = "DefaultFieldProperties.findByFieldName", query = "SELECT p FROM DefaultFieldProperties p WHERE p.fieldName = :fieldName")})
public class DefaultFieldProperties implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "field_name")
    private String fieldName;
    @Size(max = 255)
    @Column(name = "display_format")
    private String displayFormat;
    @Size(max = 255)
    @Column(name = "data_format")
    private String dataFormat;
    @Size(max = 255)
    @Column(name = "data_type")
    private String dataType;
    @Size(max = 255)
    @Column(name = "display_name")
    private String displayName;
    @Size(max = 255)
    @Column(name = "agregation_function")
    private String agregationFunction;
    

    public DefaultFieldProperties() {
    }

    public DefaultFieldProperties(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAgregationFunction() {
        return agregationFunction;
    }

    public void setAgregationFunction(String agregationFunction) {
        this.agregationFunction = agregationFunction;
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
        if (!(object instanceof DefaultFieldProperties)) {
            return false;
        }
        DefaultFieldProperties other = (DefaultFieldProperties) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.Permission[ id=" + id + " ]";
    }
    
}
