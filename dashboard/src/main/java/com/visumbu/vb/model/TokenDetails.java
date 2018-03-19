/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
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
    , @NamedQuery(name = "TokenDetails.findById", query = "SELECT t FROM TokenDetails t WHERE t.tokenDetailsPK.id = :id")
    , @NamedQuery(name = "TokenDetails.findByTokenValue", query = "SELECT t FROM TokenDetails t WHERE t.tokenValue = :tokenValue")
    , @NamedQuery(name = "TokenDetails.findByTokenSecret", query = "SELECT t FROM TokenDetails t WHERE t.tokenSecret = :tokenSecret")
    , @NamedQuery(name = "TokenDetails.findByRefreshToken", query = "SELECT t FROM TokenDetails t WHERE t.refreshToken = :refreshToken")
    , @NamedQuery(name = "TokenDetails.findByScope", query = "SELECT t FROM TokenDetails t WHERE t.scope = :scope")
    , @NamedQuery(name = "TokenDetails.findByAgencyId", query = "SELECT t FROM TokenDetails t WHERE t.tokenDetailsPK.agencyId = :agencyId")
    , @NamedQuery(name = "TokenDetails.findByClientId", query = "SELECT t FROM TokenDetails t WHERE t.clientId = :clientId")
    , @NamedQuery(name = "TokenDetails.findByClientSecret", query = "SELECT t FROM TokenDetails t WHERE t.clientSecret = :clientSecret")
    , @NamedQuery(name = "TokenDetails.findByDataSourceType", query = "SELECT t FROM TokenDetails t WHERE t.dataSourceType = :dataSourceType")})
public class TokenDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TokenDetailsPK tokenDetailsPK;
    @Size(max = 255)
    @Column(name = "token_value")
    private String tokenValue;
    @Size(max = 255)
    @Column(name = "token_secret")
    private String tokenSecret;
    @Lob
    @Size(max = 16777215)
    @Column(name = "expiry_date")
    private String expiryDate;
    @Size(max = 255)
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

    public TokenDetails() {
    }

    public TokenDetails(TokenDetailsPK tokenDetailsPK) {
        this.tokenDetailsPK = tokenDetailsPK;
    }

    public TokenDetails(int id, int agencyId) {
        this.tokenDetailsPK = new TokenDetailsPK(id, agencyId);
    }

    public TokenDetailsPK getTokenDetailsPK() {
        return tokenDetailsPK;
    }

    public void setTokenDetailsPK(TokenDetailsPK tokenDetailsPK) {
        this.tokenDetailsPK = tokenDetailsPK;
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
    public int hashCode() {
        int hash = 0;
        hash += (tokenDetailsPK != null ? tokenDetailsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TokenDetails)) {
            return false;
        }
        TokenDetails other = (TokenDetails) object;
        if ((this.tokenDetailsPK == null && other.tokenDetailsPK != null) || (this.tokenDetailsPK != null && !this.tokenDetailsPK.equals(other.tokenDetailsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.visumbu.vb.model.TokenDetails[ tokenDetailsPK=" + tokenDetailsPK + " ]";
    }
    
}
