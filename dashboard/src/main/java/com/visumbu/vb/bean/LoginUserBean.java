/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;



/**
 *
 * @author user
 */
public class LoginUserBean {
    private Integer id;
    private String username;
    private String password;
    private String isAdmin;
    private Boolean authenticated;
    private Integer failLoginCount;
    private String errorMessage;
    private String announcement;
    private Permission permission;
    private AgencyBean agencyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public AgencyBean getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(AgencyBean agencyId) {
        this.agencyId = agencyId;
    }

    @Override
    public String toString() {
        return "LoginUserBean{" + "username=" + username + ", password=" + password + ", isAdmin=" + isAdmin + ", authenticated=" + authenticated + ", failLoginCount=" + failLoginCount + ", errorMessage=" + errorMessage + ", announcement=" + announcement + ", permission=" + permission + ", agencyId=" + agencyId + '}';
    }
    
}
