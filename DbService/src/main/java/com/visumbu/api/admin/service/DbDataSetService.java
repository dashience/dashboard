/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.service;

import com.visumbu.api.bean.ColumnDef;
import com.visumbu.api.bean.DbDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Varghees Samraj
 */
@Service("dbDataSetService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DbDataSetService {

    public List<Map> getData(DbDataSource dataSource, Map<String, String[]> parameters, String queryString) {
        List<Map> results = null;
        Connection connection = null;
        try {
            connection = getDbConnection(dataSource);
            QueryRunner query = new QueryRunner();
            // ResultSetHandler
            MapListHandler handler = new MapListHandler();
            System.out.println("datasource ---->" + dataSource.getQuery());
            System.out.println("Query String ---> " + queryString);
            results = (List) query.query(connection, queryString, handler);
        } catch (SQLException ex) {
            Logger.getLogger(DbDataSetService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DbDataSetService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return results;
    }

    public List<ColumnDef> getMeta(DbDataSource dataSource, Map<String, String[]> parameters, String queryString) {
        List<ColumnDef> columnDefs = new ArrayList<>();
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        try {
            connection = getDbConnection(dataSource);
            if (queryString.endsWith(";")) {
                queryString = queryString.substring(0, queryString.length() - 1);
            }
            System.out.println("Query String ==> " + queryString + " limit 1;");
            prepareStatement = connection.prepareStatement(queryString + " limit 1;");
            ResultSetMetaData metaData = prepareStatement.getMetaData();
            System.out.println("Test Data" + metaData);

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String type = "string";
                if (metaData.getColumnTypeName(i).equalsIgnoreCase("int")) {
                    type = "number";
                }
                if (metaData.getColumnTypeName(i).equalsIgnoreCase("varchar")) {
                    type = "string";
                }

                ColumnDef columnDef = new ColumnDef(metaData.getColumnName(i), type, metaData.getColumnLabel(i));
                System.out.println(columnDef);
                columnDefs.add(columnDef);
            }
            ParameterMetaData parameterMetaData = prepareStatement.getParameterMetaData();
            System.out.println("Test Data" + parameterMetaData);
            return columnDefs;
            // ParameterMetaData 
        } catch (SQLException ex) {
            Logger.getLogger(DbDataSetService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
                prepareStatement.close();
            } catch (SQLException | NullPointerException ex) {
                Logger.getLogger(DbDataSetService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return columnDefs;

    }

    private Connection getDbConnection(DbDataSource dataSource) {
        try {
            Connection connection = null;
            String url = dataSource.getConnectionUrl(); // "jdbc:mysql://localhost:3306/test";
            String driver = dataSource.getDriver(); // "com.mysql.jdbc.Driver";
            String user = dataSource.getUsername(); // "root";
            String password = dataSource.getPassword(); //"";
            DbUtils.loadDriver(driver);
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection ==> " + connection);
            return connection;
        } catch (SQLException ex) {
            Logger.getLogger(DbDataSetService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] argv) {
        try {
            String connectionUrl = "jdbc:mysql://localhost:3306/fr_data";
            String driver = "com.mysql.jdbc.Driver";
            String username = "root";
            String password = "root";
            Integer port = 3306;
            String schemaName = "test";
            DbDataSource dataSource = new DbDataSource(connectionUrl, driver, username, password, port, schemaName);

            String query = "select id myId, dealer_name from dealer where dealer_id = '$id$'";
            dataSource.setQuery(query);
            Connection connection = DriverManager.getConnection(connectionUrl, username, password);
            System.out.println("Connection ==> " + connection);
            PreparedStatement prepareStatement = connection.prepareStatement(dataSource.getQuery());
            System.out.println("Prepare Statement ==> " + prepareStatement);
            System.out.println("Meta Data ==> " + prepareStatement.getMetaData());
            ResultSetMetaData metaData = prepareStatement.getMetaData();
            System.out.println("Test Data " + metaData.getColumnType(1));
            List<ColumnDef> columnDefs = new ArrayList<>();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                ColumnDef columnDef = new ColumnDef(metaData.getColumnName(i), metaData.getColumnLabel(i), metaData.getColumnTypeName(i));
                System.out.println(columnDef);
                columnDefs.add(columnDef);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbDataSetService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
