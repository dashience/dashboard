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
public class DbDataSource {
    private String connectionUrl;
    private String driver;
    private String username;
    private String password;
    private Integer port;
    private String schemaName;
    
    private String query;

    public DbDataSource() {
    }
    
    public DbDataSource(String connectionUrl, String driver) {
        this.connectionUrl = connectionUrl;
        this.driver = driver;
    }

    public DbDataSource(String connectionUrl, String driver, String username, String password) {
        this.connectionUrl = connectionUrl;
        this.driver = driver;
        this.username = username;
        this.password = password;
    }

    public DbDataSource(String connectionUrl, String driver, String username, String password, Integer port) {
        this.connectionUrl = connectionUrl;
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public DbDataSource(String connectionUrl, String driver, String username, String password, Integer port, String schemaName) {
        this.connectionUrl = connectionUrl;
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.port = port;
        this.schemaName = schemaName;
    }

    public DbDataSource(String connectionUrl, String driver, String username, String password, String schemaName) {
        this.connectionUrl = connectionUrl;
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.schemaName = schemaName;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @Override
    public String toString() {
        return "DbDataSource{" + "connectionUrl=" + connectionUrl + ", driver=" + driver + ", username=" + username + ", password=" + password + ", port=" + port + ", schemaName=" + schemaName + ", query=" + query + '}';
    }
}
