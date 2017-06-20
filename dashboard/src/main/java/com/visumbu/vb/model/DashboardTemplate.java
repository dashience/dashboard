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
@Table(name = "dashboard_template")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DashboardTemplate.findAll", query = "SELECT d FROM DashboardTemplate d")
    , @NamedQuery(name = "DashboardTemplate.findById", query = "SELECT d FROM DashboardTemplate d WHERE d.id = :id")
    , @NamedQuery(name = "DashboardTemplate.findByTemplateName", query = "SELECT d FROM DashboardTemplate d WHERE d.templateName = :templateName")})
public class DashboardTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "template_name")
    private String templateName;
    @JoinColumn(name = "agency_product_id", referencedColumnName = "id")
    @ManyToOne
    private AgencyProduct agencyProductId;
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    @ManyToOne
    private Agency agencyId;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private Account accountId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private VbUser userId;

    public DashboardTemplate() {
    }

    public DashboardTemplate(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public AgencyProduct getAgencyProductId() {
        return agencyProductId;
    }

    public void setAgencyProductId(AgencyProduct agencyProductId) {
        this.agencyProductId = agencyProductId;
    }

    public Agency getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Agency agencyId) {
        this.agencyId = agencyId;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    public VbUser getUserId() {
        return userId;
    }

    public void setUserId(VbUser userId) {
        this.userId = userId;
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
        if (!(object instanceof DashboardTemplate)) {
            return false;
        }
        DashboardTemplate other = (DashboardTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.DashboardTemplate[ id=" + id + " ]";
    }
    
}
