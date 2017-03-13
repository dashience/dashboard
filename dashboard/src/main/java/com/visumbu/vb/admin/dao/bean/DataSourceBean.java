/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.dao.bean;

import com.visumbu.vb.model.Agency;
import com.visumbu.vb.model.VbUser;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.Size;

/**
 *
 * @author duc-dev-04
 */
public class DataSourceBean {
    private Integer id;
    private String name;
    private String connectionString;
    private String userName;
    private String password;
    private String sqlDriver;
    private String dataSourceType;
    private String sourceFile;
    private String sourceFileName;
    private VbUser userId;
    private Agency agencyId;

    public String getSourceFileName() {
        return sourceFileName;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSqlDriver() {
        return sqlDriver;
    }

    public void setSqlDriver(String sqlDriver) {
        this.sqlDriver = sqlDriver;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public VbUser getUserId() {
        return userId;
    }

    public void setUserId(VbUser userId) {
        this.userId = userId;
    }

    public Agency getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Agency agencyId) {
        this.agencyId = agencyId;
    }
    
    @Override
    public String toString() {
        return "DataSourceBean{" + "id=" + id + ", name=" + name + ", connectionString=" + connectionString + ", userName=" + userName + ", password=" + password + ", sqlDriver=" + sqlDriver + ", dataSourceType=" + dataSourceType + ", sourceFile=" + sourceFile + ", sourceFileName=" + sourceFileName + ", userId=" + userId + ", agencyId=" + agencyId + '}';
    }
}
