/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.BasicBSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lino
 */
@Service
public class MongoDbService {

    public List<Document> getDataByAgencyId(int id) {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("dashience");
        MongoCollection collection = database.getCollection("collectData");
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("dataInfo.agencyId", id);
        BasicBSONObject conditionMap = new BasicBSONObject("dataInfo.agencyId", id);
        List<Bson> conditionList = new ArrayList<>();
        BasicDBObject groupFields = new BasicDBObject("_id", "$dataInfo.scheduleId");
        groupFields.put("data", new BasicDBObject("$last", "$data"));
        groupFields.put("dataInfo", new BasicDBObject("$last", "$dataInfo"));
        BasicBSONObject finalmap = new BasicBSONObject();
        finalmap.append("$match", conditionMap);
        BasicBSONObject finalmap2 = new BasicBSONObject();
        finalmap2.append("$group", groupFields);
        Bson o1 = new BasicDBObject(finalmap.toMap());
        Bson o2 = new BasicDBObject(finalmap2.toMap());
        conditionList.add(o1);
        conditionList.add(o2);
        System.out.println("Bson------->" + conditionList);

//	       List<Document> Documents1 = (ArrayList<Document>) collection.find(whereQuery).into(new ArrayList<Document>());
//	       List<Document> Documents1 = (ArrayList<Document>) collection.find(whereQuery).into(new ArrayList<Document>());
//        List<Document> Documents = (List<Document>) collection.distinct("dataInfo.scheduleId", object., Document.class).into(new ArrayList<Document>());
        List<Document> Documents = (List<Document>) collection.aggregate(conditionList).into(new ArrayList<Document>());
        System.out.println("cursor------>" + Documents);
//               collection.distinct("name",);
        return Documents;
    }
}
