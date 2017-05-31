/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author user
 */
@Entity
@Table(name = "report_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportType.findAll", query = "SELECT r FROM ReportType r"),
    @NamedQuery(name = "ReportType.findById", query = "SELECT r FROM ReportType r WHERE r.id = :id"),
    @NamedQuery(name = "ReportType.findByReportTypeName", query = "SELECT r FROM ReportType r WHERE r.reportTypeName = :reportTypeName"),
    @NamedQuery(name = "ReportType.findByOrder", query = "SELECT r FROM ReportType r WHERE r.order = :order"),
    @NamedQuery(name = "ReportType.findByRemarks", query = "SELECT r FROM ReportType r WHERE r.remarks = :remarks"),
    @NamedQuery(name = "ReportType.findByCreatedTime", query = "SELECT r FROM ReportType r WHERE r.createdTime = :createdTime")})
public class ReportType implements Serializable {
    @Size(max = 256)
    @Column(name = "icon")
    private String icon;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 1024)
    @Column(name = "report_type_name")
    private String reportTypeName;
    @Column(name = "order")
    private Integer order;
    @Size(max = 1024)
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @OneToMany(mappedBy = "reportTypeId")
    private Collection<Report> reportCollection;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private VbUser userId;
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    @ManyToOne
    private VbUser createdBy;

    public ReportType() {
    }

    public ReportType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReportTypeName() {
        return reportTypeName;
    }

    public void setReportTypeName(String reportTypeName) {
        this.reportTypeName = reportTypeName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }


    public VbUser getUserId() {
        return userId;
    }

    public void setUserId(VbUser userId) {
        this.userId = userId;
    }

    public VbUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(VbUser createdBy) {
        this.createdBy = createdBy;
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
        if (!(object instanceof ReportType)) {
            return false;
        }
        ReportType other = (ReportType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.ReportType[ id=" + id + " ]";
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    
}
