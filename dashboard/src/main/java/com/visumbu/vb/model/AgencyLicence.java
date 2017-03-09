/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author duc-dev-04
 */
@Entity
@Table(name = "agency_licence")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgencyLicence.findAll", query = "SELECT a FROM AgencyLicence a")
    , @NamedQuery(name = "AgencyLicence.findById", query = "SELECT a FROM AgencyLicence a WHERE a.id = :id")
    , @NamedQuery(name = "AgencyLicence.findByMaxNoTab", query = "SELECT a FROM AgencyLicence a WHERE a.maxNoTab = :maxNoTab")
    , @NamedQuery(name = "AgencyLicence.findByMaxNoUser", query = "SELECT a FROM AgencyLicence a WHERE a.maxNoUser = :maxNoUser")
    , @NamedQuery(name = "AgencyLicence.findByMaxNoClient", query = "SELECT a FROM AgencyLicence a WHERE a.maxNoClient = :maxNoClient")
    , @NamedQuery(name = "AgencyLicence.findByMaxNoAccount", query = "SELECT a FROM AgencyLicence a WHERE a.maxNoAccount = :maxNoAccount")
    , @NamedQuery(name = "AgencyLicence.findByExpiryDate", query = "SELECT a FROM AgencyLicence a WHERE a.expiryDate = :expiryDate")
    , @NamedQuery(name = "AgencyLicence.findByMaxNoWidgetPerTab", query = "SELECT a FROM AgencyLicence a WHERE a.maxNoWidgetPerTab = :maxNoWidgetPerTab")})
public class AgencyLicence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "max_no_tab")
    private Integer maxNoTab;
    @Column(name = "max_no_user")
    private Integer maxNoUser;
    @Column(name = "max_no_client")
    private Integer maxNoClient;
    @Column(name = "max_no_account")
    private Integer maxNoAccount;
    @Column(name = "expiry_date")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;
    @Column(name = "max_no_widget_per_tab")
    private Integer maxNoWidgetPerTab;
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    @ManyToOne
    private Agency agencyId;

    public AgencyLicence() {
    }

    public AgencyLicence(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaxNoTab() {
        return maxNoTab;
    }

    public void setMaxNoTab(Integer maxNoTab) {
        this.maxNoTab = maxNoTab;
    }

    public Integer getMaxNoUser() {
        return maxNoUser;
    }

    public void setMaxNoUser(Integer maxNoUser) {
        this.maxNoUser = maxNoUser;
    }

    public Integer getMaxNoClient() {
        return maxNoClient;
    }

    public void setMaxNoClient(Integer maxNoClient) {
        this.maxNoClient = maxNoClient;
    }

    public Integer getMaxNoAccount() {
        return maxNoAccount;
    }

    public void setMaxNoAccount(Integer maxNoAccount) {
        this.maxNoAccount = maxNoAccount;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getMaxNoWidgetPerTab() {
        return maxNoWidgetPerTab;
    }

    public void setMaxNoWidgetPerTab(Integer maxNoWidgetPerTab) {
        this.maxNoWidgetPerTab = maxNoWidgetPerTab;
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
        if (!(object instanceof AgencyLicence)) {
            return false;
        }
        AgencyLicence other = (AgencyLicence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.AgencyLicence[ id=" + id + " ]";
    }
    
}
