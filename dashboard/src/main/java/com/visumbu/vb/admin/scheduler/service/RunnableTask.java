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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author dashience
 */
public class RunnableTask implements Runnable {

    public String dataSource;
    public String dataSet;
    public String accountId;
    public String userId;
    public String Baseurl = "http://tellyourstory.lino.com:8080/dashboard/admin/getNewData";

    @Override
    public void run() {
        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("dataSourceName", dataSource);
            headers.add("dataSetName", dataSet);
            headers.add("accountId", accountId);
            String output = Rest.getData("http://tellyourstory.lino.com:8080/dashboard/admin/getNewData?dataSourceName=linkedin&dataSetName=companyProfile&accountId=5&userId=" + userId, null);
            if (output != null) {
                System.out.println("output---->" + output);
                MongoClient mongoClient = new MongoClient("localhost", 27017);
                DB database = mongoClient.getDB("dashience");
                DBCollection collection = database.getCollection("linkedIn");
                System.out.println("collection------>" + collection);
                DBObject dataObj = new BasicDBObject("data", com.mongodb.util.JSON.parse(output));
                DBObject dataInfo = new BasicDBObject("accountId", accountId);
                dataInfo.put("userId", userId);
                dataInfo.put("dataSourceName", dataSource);
                dataInfo.put("dataSetName", dataSet);
                dataObj.put("dataInfo", dataInfo);
                System.out.println("dataObj---->" + dataObj);
                collection.insert(dataObj);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
