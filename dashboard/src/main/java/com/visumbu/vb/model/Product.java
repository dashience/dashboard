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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author user
 */
@Entity
@Table(name = "product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
    @NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id"),
    @NamedQuery(name = "Product.findByProductName", query = "SELECT p FROM Product p WHERE p.productName = :productName"),
    @NamedQuery(name = "Product.findByRemarks", query = "SELECT p FROM Product p WHERE p.remarks = :remarks")})
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 256)
    @Column(name = "product_name")
    private String productName;
    @Size(max = 1024)
    @Column(name = "remarks")
    private String remarks;
    @Size(max = 255)
    @Column(name = "icon")
    private String icon;
    @Column(name = "show_product")
    private Boolean showProduct;
    @Column(name = "product_order")
    private Integer productOrder;

    @OneToMany(mappedBy = "productId")
    private Collection<Dashboard> dashboardCollection;

    public Product() {
    }

    public Product(Integer id) {
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
    

    @XmlTransient
    @JsonIgnore
    public Collection<Dashboard> getDashboardCollection() {
        return dashboardCollection;
    }

    public void setDashboardCollection(Collection<Dashboard> dashboardCollection) {
        this.dashboardCollection = dashboardCollection;
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
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.Product[ id=" + id + " ]";
    }

}
