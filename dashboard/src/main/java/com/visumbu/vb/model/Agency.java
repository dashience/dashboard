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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Type;

/**
 *
 * @author duc-dev-04
 */
@Entity
@Table(name = "agency")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agency.findAll", query = "SELECT a FROM Agency a")
    , @NamedQuery(name = "Agency.findById", query = "SELECT a FROM Agency a WHERE a.id = :id")
    , @NamedQuery(name = "Agency.findByAgencyName", query = "SELECT a FROM Agency a WHERE a.agencyName = :agencyName")
    , @NamedQuery(name = "Agency.findByDescription", query = "SELECT a FROM Agency a WHERE a.description = :description")
    , @NamedQuery(name = "Agency.findByStatus", query = "SELECT a FROM Agency a WHERE a.status = :status")
    , @NamedQuery(name = "Agency.findByEmail", query = "SELECT a FROM Agency a WHERE a.email = :email")})
public class Agency implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "agency_name")
    private String agencyName;
    @Size(max = 4096)
    @Column(name = "description")
    private String description;
    @Size(max = 45)
    @Column(name = "status")
    private String status;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Type(type = "org.hibernate.type.StringClobType")
    @Column(name = "logo")
    private String logo;

    public Agency() {
    }

    public Agency(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
        if (!(object instanceof Agency)) {
            return false;
        }
        Agency other = (Agency) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.Agency[ id=" + id + " ]";
    }
    
}
