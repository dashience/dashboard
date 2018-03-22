/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.oauth.service;

/**
 *
 * @author dashience
 */
public class TokenTemplate {

    private String tokenValue;
    private String tokenSecret;
    private Long expiryDate;
    private String refreshToken;
    private String scope;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
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
    @Override
    public String toString(){
        return "{tokenValue:\""+tokenValue+"\",tokenSecret:\""+tokenSecret+"\",expiryDate:\""+expiryDate+"\",refreshToken:\""+refreshToken+"\",scope:\""+scope+"\"}";
    }
}
