/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean.map.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author user
 */
public class SecurityTokenBean {
    
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("scope")
    private String scope;
    
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("user_guid")
    private String userGuid;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    @Override
    public String toString() {
        return "SecurityTokenBean{" + "accessToken=" + accessToken + ", scope=" + scope + ", refreshToken=" + refreshToken + ", userGuid=" + userGuid + '}';
    }

}
