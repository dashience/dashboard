/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author dashience
 */
@Entity
@Table(name = "currency")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Currency.findAll", query = "SELECT c FROM Currency c")
    , @NamedQuery(name = "Currency.findById", query = "SELECT c FROM Currency c WHERE c.id = :id")
    , @NamedQuery(name = "Currency.findByCurrency", query = "SELECT c FROM Currency c WHERE c.currency= :currency")
    , @NamedQuery(name = "Currency.findByCurrencyCode", query = "SELECT c FROM Currency c WHERE c.currencyCode = :currencyCode")
    , @NamedQuery(name = "Currency.findByNation", query = "SELECT c FROM Currency c WHERE c.nation = :nation")
 , @NamedQuery(name = "Currency.findByInUsd", query = "SELECT c FROM Currency c WHERE c.inUsd = :inUsd")})
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 200)
    @Column(name = "currency")
    private String currency;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "currency_code")
    private String currencyCode;
    @Size(max = 200)
    @Column(name = "nation")
    private String nation;
    @Size(max = 200)
    @Column(name = "in_usd")
    private String inUsd;
    
    @OneToMany(mappedBy = "currencyId")
    private Collection<AgencySettings> agencySettingsCollection;

    public Currency() {
    }

    public Currency(Integer id) {
        this.id = id;
    }

    public Currency(Integer id, String currencyCode) {
        this.id = id;
        this.currencyCode = currencyCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getInUsd() {
        return inUsd;
    }

    public void setInUsd(String inUsd) {
        this.inUsd = inUsd;
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
        if (!(object instanceof Currency)) {
            return false;
        }
        Currency other = (Currency) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.Currency[ id=" + id + " ]";
    }
    
}
