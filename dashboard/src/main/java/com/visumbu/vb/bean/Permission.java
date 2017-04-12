/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author user
 */
public class Permission {

    private Boolean createUser;
    private Boolean createAccount;
    private Boolean createDataSource;
    private Boolean createDataSet;
    private Boolean grantPermission;
    private Boolean assignAccount;
    private Boolean addTab;
    private Boolean deleteTab;
    private Boolean addWidget;
    private Boolean deleteWidget;
    private Boolean createReport;
    private Boolean deleteReport;
    private Boolean downloadReport;
    private Boolean scheduleReport;
    private Boolean viewReport;

    public Boolean getViewReport() {
        return viewReport;
    }

    public void setViewReport(Boolean viewReport) {
        this.viewReport = viewReport;
    }
        
    public Boolean getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Boolean createUser) {
        this.createUser = createUser;
    }

    public Boolean getCreateAccount() {
        return createAccount;
    }

    public void setCreateAccount(Boolean createAccount) {
        this.createAccount = createAccount;
    }

    public Boolean getCreateDataSource() {
        return createDataSource;
    }

    public void setCreateDataSource(Boolean createDataSource) {
        this.createDataSource = createDataSource;
    }

    public Boolean getCreateDataSet() {
        return createDataSet;
    }

    public void setCreateDataSet(Boolean createDataSet) {
        this.createDataSet = createDataSet;
    }

    public Boolean getGrantPermission() {
        return grantPermission;
    }

    public void setGrantPermission(Boolean grantPermission) {
        this.grantPermission = grantPermission;
    }

    public Boolean getAssignAccount() {
        return assignAccount;
    }

    public void setAssignAccount(Boolean assignAccount) {
        this.assignAccount = assignAccount;
    }

    public Boolean getAddTab() {
        return addTab;
    }

    public void setAddTab(Boolean addTab) {
        this.addTab = addTab;
    }

    public Boolean getDeleteTab() {
        return deleteTab;
    }

    public void setDeleteTab(Boolean deleteTab) {
        this.deleteTab = deleteTab;
    }

    public Boolean getAddWidget() {
        return addWidget;
    }

    public void setAddWidget(Boolean addWidget) {
        this.addWidget = addWidget;
    }

    public Boolean getDeleteWidget() {
        return deleteWidget;
    }

    public void setDeleteWidget(Boolean deleteWidget) {
        this.deleteWidget = deleteWidget;
    }

    public Boolean getCreateReport() {
        return createReport;
    }

    public void setCreateReport(Boolean createReport) {
        this.createReport = createReport;
    }

    public Boolean getDeleteReport() {
        return deleteReport;
    }

    public void setDeleteReport(Boolean deleteReport) {
        this.deleteReport = deleteReport;
    }

    public Boolean getDownloadReport() {
        return downloadReport;
    }

    public void setDownloadReport(Boolean downloadReport) {
        this.downloadReport = downloadReport;
    }

    public Boolean getScheduleReport() {
        return scheduleReport;
    }

    public void setScheduleReport(Boolean scheduleReport) {
        this.scheduleReport = scheduleReport;
    }
    
    
    
    public String setPermission(String permissionName, Boolean permission) {
        try {
             System.out.println("Permission Name " + permissionName + " Permission Value " + permission);
            PropertyUtils.setProperty(this, permissionName, permission);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            // Logger.getLogger(Permission.class.getName()).log(Level.SEVERE, null, ex);
        }
        return permissionName;
    }

    
    public static Permission getDefaultPermission() {
        Permission permission = new Permission();
        
        return permission;
    }
}
