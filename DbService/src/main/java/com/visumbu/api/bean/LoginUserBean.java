/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.bean;

/**
 *
 * @author user
 */
public class LoginUserBean {
    private String username;
    private String password;
    private String isAdmin;
    private Boolean authenticated;
    private Integer failLoginCount;
    private String errorMessage;
    private String announcement;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Integer getFailLoginCount() {
        return failLoginCount;
    }

    public void setFailLoginCount(Integer failLoginCount) {
        this.failLoginCount = failLoginCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }
    
    
}
