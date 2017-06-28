/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;

/**
 *
 * @author duc-dev-04
 */
@Entity
@Table(name = "agency_product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgencyProduct.findAll", query = "SELECT a FROM AgencyProduct a")
    , @NamedQuery(name = "AgencyProduct.findById", query = "SELECT a FROM AgencyProduct a WHERE a.id = :id")
    , @NamedQuery(name = "AgencyProduct.findByProductName", query = "SELECT a FROM AgencyProduct a WHERE a.productName = :productName")
    , @NamedQuery(name = "AgencyProduct.findByShowProduct", query = "SELECT a FROM AgencyProduct a WHERE a.showProduct = :showProduct")
    , @NamedQuery(name = "AgencyProduct.findByProductOrder", query = "SELECT a FROM AgencyProduct a WHERE a.productOrder = :productOrder")
    , @NamedQuery(name = "AgencyProduct.findByStatus", query = "SELECT a FROM AgencyProduct a WHERE a.status = :status")})
public class AgencyProduct implements Serializable {

    @OneToMany(mappedBy = "agencyProductId")
    private Collection<DashboardTemplate> dashboardTemplateCollection;

    @Type(type = "org.hibernate.type.StringClobType")
    @Column(name = "icon")
    private String icon;
    @OneToMany(mappedBy = "agencyProductId")
    private Collection<DashboardTabs> dashboardTabsCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 4096)
    @Column(name = "product_name")
    private String productName;
    @Column(name = "show_product")
    private Boolean showProduct;
    @Column(name = "product_order")
    private Integer productOrder;
    @Size(max = 45)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    @ManyToOne
    private Agency agencyId;
    @JoinColumn(name = "template_id", referencedColumnName = "id")
    @ManyToOne
    private DashboardTemplate templateId;

    public AgencyProduct() {
    }

    public AgencyProduct(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public Boolean getShowProduct() {
        return showProduct;
    }

    public void setShowProduct(Boolean showProduct) {
        this.showProduct = showProduct;
    }

    public Integer getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(Integer productOrder) {
        this.productOrder = productOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AgencyProduct)) {
            return false;
        }
        AgencyProduct other = (AgencyProduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.AgencyProduct[ id=" + id + " ]";
    }   

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    
}
