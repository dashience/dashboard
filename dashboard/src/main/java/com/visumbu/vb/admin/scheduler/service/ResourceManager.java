/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.scheduler.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.dao.BaseDao;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.TokenDetails;
import com.visumbu.vb.model.VbUser;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dashience
 */
@Transactional
@Service
public class ResourceManager extends BaseController {

    @Autowired
    protected SessionFactory sessionFactory;

    public List<TokenDetails> getOauthToken(int dataSourceId) {
        System.out.println("dataSourceId------------>"+dataSourceId);
//        Query userquery = sessionFactory.getCurrentSession().getNamedQuery("VbUser.findById");
//        userquery.setParameter("id", userId);
//        VbUser user = (VbUser) userquery.uniqueResult();
        String queryStr = "select d from TokenDetails d where d.dataSourceId.id = :dataSourceId";
        Query query = sessionFactory.getCurrentSession().createQuery(queryStr);
//        query.setParameter("agencyId", user.getAgencyId());
        query.setParameter("dataSourceId", dataSourceId);
        return query.list();
    }

//    public String getOauthToken(String dataSourceName, String field, String expiryDate) {
//        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
//            DB dataBaseName = mongoClient.getDB("dashience");
//            DBCollection collection = dataBaseName.getCollection("tokenData");
//            DBObject query = new BasicDBObject("source", dataSourceName);
//            DBObject tokenJson = collection.findOne(query);
//            DBObject tokenData = (DBObject) tokenJson.get("token");
//            mongoClient.close();
//            String tokenField = null;
//            if (field != null) {
//                tokenField = (String) tokenData.get(field);
//            }
//            if (expiryDate != null) {
//                String tokenExpiry = (String) tokenData.get(field);
//            }
//            return tokenField;
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
//        return null;
//    }
//    public Object getClientDetails(String dataSourceName, String field) {
//        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
//            DB dataBaseName = mongoClient.getDB("dashience");
//            DBCollection collection = dataBaseName.getCollection("userDetails");
//            System.out.println("collection------->" + collection.toString());
//            DBObject query = new BasicDBObject("source", dataSourceName);
//            System.out.println("collection2------->" + collection.toString());
//            DBObject detailsJson = collection.findOne(query);
//            System.out.println("collection3------->" + detailsJson.toString());
//            Object requestedField = detailsJson.get(field);
//            System.out.println("the request field output------>" + requestedField);
//            mongoClient.close();
//            return requestedField;
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
//        return null;
//    }
}
