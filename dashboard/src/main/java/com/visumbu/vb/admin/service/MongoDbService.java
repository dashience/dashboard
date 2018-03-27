/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lino
 */
@Service
public class MongoDbService {

    public  List<Document> getDataByAgencyId(int id) {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("dashience");
        MongoCollection collection = database.getCollection("collectData");
        BasicDBObject whereQuery = new BasicDBObject();
	whereQuery.put("dataInfo.agencyId",id);
	       List<Document> Documents = (ArrayList<Document>) collection.find(whereQuery).into(new ArrayList<Document>());
               System.out.println("cursor------>"+Documents);
               return Documents;
    }
}
