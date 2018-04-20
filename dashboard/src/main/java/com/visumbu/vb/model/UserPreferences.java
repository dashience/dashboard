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
@Table(name = "user_preferences")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserPreferences.findAll", query = "SELECT u FROM UserPreferences u")
    , @NamedQuery(name = "UserPreferences.findById", query = "SELECT u FROM UserPreferences u WHERE u.id = :id")
    , @NamedQuery(name = "UserPreferences.findByUserId", query = "SELECT u FROM UserPreferences u WHERE u.userId = :userId")
    , @NamedQuery(name = "UserPreferences.findByOptionName", query = "SELECT u FROM UserPreferences u WHERE u.optionName = :optionName")
    , @NamedQuery(name = "UserPreferences.findByOptionValue", query = "SELECT u FROM UserPreferences u WHERE u.optionValue = :optionValue")})
public class UserPreferences implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "option_name")
    private String optionName;
    @Size(max = 255)
    @Column(name = "option_value")
    private String optionValue;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private VbUser userId;

    public UserPreferences() {
    }

    public UserPreferences(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
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
        if (!(object instanceof UserPreferences)) {
            return false;
        }
        UserPreferences other = (UserPreferences) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.UserPreferences[ id=" + id + " ]";
    }

}
