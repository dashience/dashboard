/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author dashience
 */
@Embeddable
public class TokenDetailsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "agency_id")
    private int agencyId;

    public TokenDetailsPK() {
    }

    public TokenDetailsPK(int id, int agencyId) {
        this.id = id;
        this.agencyId = agencyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) agencyId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TokenDetailsPK)) {
            return false;
        }
        TokenDetailsPK other = (TokenDetailsPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.agencyId != other.agencyId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.TokenDetailsPK[ id=" + id + ", agencyId=" + agencyId + " ]";
    }
    
}
