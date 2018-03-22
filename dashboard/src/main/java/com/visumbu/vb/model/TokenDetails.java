/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dashience
 */
@Entity
@Table(name = "token_details")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TokenDetails.findAll", query = "SELECT t FROM TokenDetails t")
    , @NamedQuery(name = "TokenDetails.findById", query = "SELECT t FROM TokenDetails t WHERE t.id = :id")
    , @NamedQuery(name = "TokenDetails.findByTokenValue", query = "SELECT t FROM TokenDetails t WHERE t.tokenValue = :tokenValue")
    , @NamedQuery(name = "TokenDetails.findByTokenSecret", query = "SELECT t FROM TokenDetails t WHERE t.tokenSecret = :tokenSecret")
    , @NamedQuery(name = "TokenDetails.findByRefreshToken", query = "SELECT t FROM TokenDetails t WHERE t.refreshToken = :refreshToken")
    , @NamedQuery(name = "TokenDetails.findByScope", query = "SELECT t FROM TokenDetails t WHERE t.scope = :scope")
    , @NamedQuery(name = "TokenDetails.findByAgencyId", query = "SELECT t FROM TokenDetails t WHERE t.agencyId = :agencyId")
    , @NamedQuery(name = "TokenDetails.findByClientId", query = "SELECT t FROM TokenDetails t WHERE t.clientId = :clientId")
    , @NamedQuery(name = "TokenDetails.findByClientSecret", query = "SELECT t FROM TokenDetails t WHERE t.clientSecret = :clientSecret")
    , @NamedQuery(name = "TokenDetails.findByDataSourceType", query = "SELECT t FROM TokenDetails t WHERE t.dataSourceType = :dataSourceType")})
public class TokenDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "token_value")
    private String tokenValue;
    @Size(max = 1000)
    @Column(name = "token_secret")
    private String tokenSecret;
    @Size(max = 255)
    @Column(name = "expiry_date")
    private String expiryDate;
    @Size(max = 1000)
    @Column(name = "refresh_token")
    private String refreshToken;
    @Size(max = 255)
    @Column(name = "scope")
    private String scope;
    @Size(max = 255)
    @Column(name = "client_id")
    private String clientId;
    @Size(max = 255)
    @Column(name = "client_secret")
    private String clientSecret;
    @Size(max = 250)
    @Column(name = "data_source_type")
    private String dataSourceType;
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    @ManyToOne
    private Agency agencyId;


    public Agency getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Agency agencyId) {
        this.agencyId = agencyId;
    }

    public TokenDetails() {
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TokenDetails)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.TokenDetails[ id=" + id + " ]";
    }

}
