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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author arul
 */
@Entity
@Table(name = "product_account_user_template")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductAccountUserTemplate.findAll", query = "SELECT a FROM ProductAccountUserTemplate a")
    , @NamedQuery(name = "ProductAccountUserTemplate.findById", query = "SELECT a FROM ProductAccountUserTemplate a WHERE a.id = :id")
    , @NamedQuery(name = "ProductAccountUserTemplate.findByStatus", query = "SELECT a FROM ProductAccountUserTemplate a WHERE a.status = :status")})
public class ProductAccountUserTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "status")
    private Integer status;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne
    private Account accountId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private VbUser userId;
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    @ManyToOne
    private Agency agencyId;
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    @ManyToOne
    private DashboardTemplate templateId;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne
    private AgencyProduct productId;

    public ProductAccountUserTemplate() {
    }

    public ProductAccountUserTemplate(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Agency getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Agency agencyId) {
        this.agencyId = agencyId;
    }

    public DashboardTemplate getTemplateId() {
        return templateId;
    }

    public void setTemplateId(DashboardTemplate templateId) {
        this.templateId = templateId;
    }

    public AgencyProduct getProductId() {
        return productId;
    }

    public void setProductId(AgencyProduct productId) {
        this.productId = productId;
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
        if (!(object instanceof ProductAccountUserTemplate)) {
            return false;
        }
        ProductAccountUserTemplate other = (ProductAccountUserTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.AccountTemplate[ id=" + id + " ]";
    }
    
}
