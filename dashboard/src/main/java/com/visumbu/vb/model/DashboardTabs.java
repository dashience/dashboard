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
@Table(name = "dashboard_tabs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DashboardTabs.findAll", query = "SELECT d FROM DashboardTabs d"),
    @NamedQuery(name = "DashboardTabs.findById", query = "SELECT d FROM DashboardTabs d WHERE d.id = :id"),
    @NamedQuery(name = "DashboardTabs.findByDashboard", 
            query = "SELECT d FROM DashboardTabs d WHERE d.dashboardId = :dashboardId"),
    @NamedQuery(name = "DashboardTabs.findByTabName", query = "SELECT d FROM DashboardTabs d WHERE d.tabName = :tabName"),
    @NamedQuery(name = "DashboardTabs.findByCreatedTime", query = "SELECT d FROM DashboardTabs d WHERE d.createdTime = :createdTime"),
    @NamedQuery(name = "DashboardTabs.findByModifiedTime", query = "SELECT d FROM DashboardTabs d WHERE d.modifiedTime = :modifiedTime"),
    @NamedQuery(name = "DashboardTabs.findByRemarks", query = "SELECT d FROM DashboardTabs d WHERE d.remarks = :remarks"),
    @NamedQuery(name = "DashboardTabs.findByStatus", query = "SELECT d FROM DashboardTabs d WHERE d.status = :status"),
    @NamedQuery(name = "DashboardTabs.findByTabOrder", query = "SELECT d FROM DashboardTabs d WHERE d.tabOrder = :tabOrder")})
public class DashboardTabs implements Serializable {

    @OneToMany(mappedBy = "tabId")
    private Collection<TabWidget> tabWidgetCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 256)
    @Column(name = "tab_name")
    private String tabName;
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;
    @Column(name = "modified_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedTime;
    @Size(max = 1024)
    @Column(name = "remarks")
    private String remarks;
    @Size(max = 64)
    @Column(name = "status")
    private String status;
    @Column(name = "tab_order")
    private Integer tabOrder;
//    @OneToMany(mappedBy = "tabId")
//    private Collection<TabWidget> tabWidgetCollection;
    @JoinColumn(name = "dashboard_id", referencedColumnName = "id")
    @ManyToOne
    private Dashboard dashboardId;
    @JoinColumn(name = "agency_product_id", referencedColumnName = "id")
    @ManyToOne
    private AgencyProduct agencyProductId;

    public DashboardTabs() {
    }

    public DashboardTabs(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTabOrder() {
        return tabOrder;
    }

    public void setTabOrder(Integer tabOrder) {
        this.tabOrder = tabOrder;
    }


    public Dashboard getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Dashboard dashboardId) {
        this.dashboardId = dashboardId;
    }

    public AgencyProduct getAgencyProductId() {
        return agencyProductId;
    }

    public void setAgencyProductId(AgencyProduct agencyProductId) {
        this.agencyProductId = agencyProductId;
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
        if (!(object instanceof DashboardTabs)) {
            return false;
        }
        DashboardTabs other = (DashboardTabs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.DashboardTabs[ id=" + id + " ]";
    }


    }
