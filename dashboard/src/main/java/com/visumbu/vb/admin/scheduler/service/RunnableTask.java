/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.scheduler.service;

import com.mongodb.BasicDBObject;
import com.visumbu.vb.utils.Rest;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.VbUser;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author lino
 */
public class RunnableTask implements Runnable {

    private DataSet dataSet;
    private int accountId;
    private int scheduleId;
    public String Baseurl = "http://lino.com:8080/dashboard/admin/getNewData";

    @Override
    public void run() {
        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            DataSource dataSource = dataSet.getDataSourceId();
            Integer dataSourceId = dataSource.getId();
            VbUser user = dataSet.getUserId();
            int agencyId = dataSet.getAgencyId().getId();
            String userId = user.getId().toString();
            headers.add("dataSourceName", dataSource.getDataSourceType());
            headers.add("dataSetName", dataSet.getReportName());
            headers.add("accountId", Integer.toString(accountId));
            headers.add("userId", userId);
            headers.add("productSegment", dataSet.getProductSegment());
            headers.add("timeSegment", dataSet.getTimeSegment());
            headers.add("dataSourceId", Integer.toString(dataSourceId));
            String output = Rest.getData(Baseurl, headers);
            if (output != null) {
                System.out.println("output---->" + output);
                MongoClient mongoClient = new MongoClient("localhost", 27017);
                MongoDatabase database = mongoClient.getDatabase("dashience");
                MongoCollection collection = database.getCollection("collectData");
                System.out.println("collection------>" + collection);
                JSONParser parser = new JSONParser();
                JSONArray json = (JSONArray) parser.parse(output);
                DBObject dataObj = new BasicDBObject("data", json);
                DBObject dataInfo = new BasicDBObject("accountId", accountId);
                System.out.println("till account------------->");
                dataInfo.put("userId", userId);
                dataInfo.put("agencyId", agencyId);
                dataInfo.put("dataSetId", dataSet.getId());
                dataInfo.put("dataSourceName", dataSource.getDataSourceType());
                dataInfo.put("dataSetName", dataSet.getReportName());
                dataInfo.put("scheduleId", scheduleId);
                System.out.println("till account-----------g-->");
                dataObj.put("dataInfo", dataInfo);
                System.out.println("dataObj---->" + dataObj);
                collection.insertOne(new Document(dataObj.toMap()));
                mongoClient.close();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
}
