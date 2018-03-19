/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.oauth.service;

import com.visumbu.vb.utils.Rest;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 *
 * @author dashience
 */
public class RunnableTask implements Runnable {

    public String dataSource;
    public String dataSet;
    public String Baseurl = "http://localhost:8080/SpringSocial/admin/getData?";

    @Override
    public void run() {
        try {
            String output = Rest.getData(Baseurl + "dataSourceName=" + dataSource + "&dataSetName=" + dataSet, null);
            if (output != null) {
                System.out.println("output---->" + output);
                MongoClient mongoClient = new MongoClient("localhost", 27017);
                DB database = mongoClient.getDB("dashience");
                DBCollection collection = database.getCollection(dataSource);
                DBObject obj = (DBObject) com.mongodb.util.JSON.parse(output);
                collection.insert(obj);
            }
        }
    catch (Exception ex

    
        ) {
            System.out.println(ex);
    }
}
}
