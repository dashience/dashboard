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
import javax.persistence.Lob;
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
@Table(name = "dataset_columns")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DatasetColumns.findAll", query = "SELECT d FROM DatasetColumns d")
    , @NamedQuery(name = "DatasetColumns.findById", query = "SELECT d FROM DatasetColumns d WHERE d.id = :id")
    , @NamedQuery(name = "DatasetColumns.findByDisplayName", query = "SELECT d FROM DatasetColumns d WHERE d.displayName = :displayName")
    , @NamedQuery(name = "DatasetColumns.findByFieldName", query = "SELECT d FROM DatasetColumns d WHERE d.fieldName = :fieldName")
    , @NamedQuery(name = "DatasetColumns.findByFieldType", query = "SELECT d FROM DatasetColumns d WHERE d.fieldType = :fieldType")
    , @NamedQuery(name = "DatasetColumns.findByExpression", query = "SELECT d FROM DatasetColumns d WHERE d.expression = :expression")
    , @NamedQuery(name = "DatasetColumns.findByStatus", query = "SELECT d FROM DatasetColumns d WHERE d.status = :status")
    , @NamedQuery(name = "DatasetColumns.findByFunction", query = "SELECT d FROM DatasetColumns d WHERE d.function = :function")
    , @NamedQuery(name = "DatasetColumns.findByDisplayFormat", query = "SELECT d FROM DatasetColumns d WHERE d.displayFormat = :displayFormat")})
public class DatasetColumns implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "display_name")
    private String displayName;
    @Size(max = 255)
    @Column(name = "field_name")
    private String fieldName;
    @Size(max = 255)
    @Column(name = "field_type")
    private String fieldType;
    @Size(max = 255)
    @Column(name = "expression")
    private String expression;
    @Size(max = 255)
    @Column(name = "status")
    private String status;
    @Size(max = 255)
    @Column(name = "function")
    private String function;
    @Size(max = 255)
    @Column(name = "display_format")
    private String displayFormat;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "formula")
    private String formula;
    @JoinColumn(name = "dataset_id", referencedColumnName = "id")
    @ManyToOne
    private DataSet datasetId;

    public DatasetColumns() {
    }

    public DatasetColumns(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public DataSet getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(DataSet datasetId) {
        this.datasetId = datasetId;
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
        if (!(object instanceof DatasetColumns)) {
            return false;
        }
        DatasetColumns other = (DatasetColumns) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.DatasetColumns[ id=" + id + " ]";
    }
    
}
