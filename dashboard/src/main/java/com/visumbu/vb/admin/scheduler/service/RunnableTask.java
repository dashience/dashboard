/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.scheduler.service;

import com.mongodb.BasicDBObject;
import com.visumbu.vb.utils.Rest;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.visumbu.vb.model.DataSet;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.VbUser;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author dashience
 */
public class RunnableTask implements Runnable {

    private DataSet dataSet;
    private String accountId;
    public String Baseurl = "http://tellyourstory.lino.com:8080/dashboard/admin/getNewData";

    @Override
    public void run() {
        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            DataSource dataSource = dataSet.getDataSourceId();
            VbUser user = dataSet.getUserId();
            String userId = user.getId().toString();
            headers.add("dataSourceName", dataSource.getDataSourceType());
            headers.add("dataSetName", dataSet.getReportName());
            headers.add("accountId", accountId);
            headers.add("userId", userId);
            headers.add("productSegment", dataSet.getProductSegment());
            headers.add("timeSegment", dataSet.getTimeSegment());
            String output = Rest.getData(Baseurl, headers);
            if (output != null) {
                System.out.println("output---->" + output);
                MongoClient mongoClient = new MongoClient("localhost", 27017);
                MongoDatabase database = mongoClient.getDatabase("dashience");
                MongoCollection collection = database.getCollection("linkedIn");
                System.out.println("collection------>" + collection);
                DBObject dataObj = new BasicDBObject("data", BasicDBObject.parse(output));
                DBObject dataInfo = new BasicDBObject("accountId", accountId);
                dataInfo.put("userId", userId);
                dataInfo.put("dataSetId", dataSet.getId());
                dataObj.put("dataInfo", dataInfo);
                System.out.println("dataObj---->" + dataObj);
                collection.insertOne(dataObj);
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
