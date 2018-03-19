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
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author user
 */
@Entity
@Table(name = "dealer_product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DealerProduct.findAll", query = "SELECT d FROM DealerProduct d"),
    @NamedQuery(name = "DealerProduct.findById", query = "SELECT d FROM DealerProduct d WHERE d.id = :id"),
    @NamedQuery(name = "DealerProduct.findByProductName", query = "SELECT d FROM DealerProduct d WHERE d.productName = :productName"),
    @NamedQuery(name = "DealerProduct.findByBudget", query = "SELECT d FROM DealerProduct d WHERE d.budget = :budget"),
    @NamedQuery(name = "DealerProduct.findByStartDate", query = "SELECT d FROM DealerProduct d WHERE d.startDate = :startDate"),
    @NamedQuery(name = "DealerProduct.findByEndDate", query = "SELECT d FROM DealerProduct d WHERE d.endDate = :endDate")})
public class DealerProduct implements Serializable {
    @Size(max = 255)
    @Column(name = "map_status")
    private String mapStatus;
    @Size(max = 8)
    @Column(name = "status")
    private String status;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 256)
    @Column(name = "product_name")
    private String productName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "budget")
    private Double budget;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @JoinColumn(name = "dealer_id", referencedColumnName = "id")
    @ManyToOne
    private Dealer dealerId;
    @OneToMany(mappedBy = "dealerProductId")
    private Collection<DealerProductSource> dealerProductSourceCollection;
    @OneToMany(mappedBy = "dealerProductId")
    private Collection<DealerProductService> dealerProductServiceCollection;

    public DealerProduct() {
    }

    public DealerProduct(Integer id) {
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

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Dealer getDealerId() {
        return dealerId;
    }

    public void setDealerId(Dealer dealerId) {
        this.dealerId = dealerId;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<DealerProductSource> getDealerProductSourceCollection() {
        return dealerProductSourceCollection;
    }

    public void setDealerProductSourceCollection(Collection<DealerProductSource> dealerProductSourceCollection) {
        this.dealerProductSourceCollection = dealerProductSourceCollection;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<DealerProductService> getDealerProductServiceCollection() {
        return dealerProductServiceCollection;
    }

    public void setDealerProductServiceCollection(Collection<DealerProductService> dealerProductServiceCollection) {
        this.dealerProductServiceCollection = dealerProductServiceCollection;
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
        if (!(object instanceof DealerProduct)) {
            return false;
        }
        DealerProduct other = (DealerProduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.wa.model.DealerProduct[ id=" + id + " ]";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMapStatus() {
        return mapStatus;
    }

    public void setMapStatus(String mapStatus) {
        this.mapStatus = mapStatus;
    }
    
}
