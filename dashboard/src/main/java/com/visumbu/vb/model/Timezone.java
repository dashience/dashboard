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
@Table(name = "timezone")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Timezone.findAll", query = "SELECT t FROM Timezone t")
    , @NamedQuery(name = "Timezone.findById", query = "SELECT t FROM Timezone t WHERE t.id = :id")
    , @NamedQuery(name = "Timezone.findByLongName", query = "SELECT t FROM Timezone t WHERE t.longName = :longName")
, @NamedQuery(name = "Timezone.findByDescription", query = "SELECT t FROM Timezone t WHERE t.description = :description")
, @NamedQuery(name = "Timezone.findByShortDescription", query = "SELECT t FROM Timezone t WHERE t.shortDescription = :shortDescription")
, @NamedQuery(name = "Timezone.findByRelativeToGMT", query = "SELECT t FROM Timezone t WHERE t.relativeToGMT = :relativeToGMT")})
public class Timezone implements Serializable {

    @OneToMany(mappedBy = "timeZoneId")
    private Collection<AgencySettings> agencySettingsCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
   /* @Size(max = 150)
    @Column(name = "timezoneTYpe")
    private String timezoneType; */
    
   @Size(max = 150)
    @Column(name = "long_name")
    private String longName;
     @Size(max = 150)
    @Column(name = "description")
    private String description;
        @Size(max = 150)
    @Column(name = "short_description")
    private String shortDescription;
           @Size(max = 150)
    @Column(name = "relative_to_gmt")
    private String relativeToGMT;  

    public Timezone() {
    }

    public Timezone(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getRelativeToGMT() {
        return relativeToGMT;
    }

    public void setRelativeToGMT(String relativeToGMT) {
        this.relativeToGMT = relativeToGMT;
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
        if (!(object instanceof Timezone)) {
            return false;
        }
        Timezone other = (Timezone) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.Timezone[ id=" + id + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public Collection<AgencySettings> getAgencySettingsCollection() {
        return agencySettingsCollection;
    }

    public void setAgencySettingsCollection(Collection<AgencySettings> agencySettingsCollection) {
        this.agencySettingsCollection = agencySettingsCollection;
    }
    
}
