/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.visumbu.vb.admin.scheduler.service.SchedulerTemplate;
import com.visumbu.vb.admin.scheduler.service.Sheduler;
import com.visumbu.vb.admin.service.LinkedInService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.Property;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author dashience
 */
@Controller
public class MongoController {

    @Autowired
    private Sheduler scheduler;

    @Autowired
    private LinkedInService linkedIn;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "getNewData", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getData(HttpServletRequest request, HttpServletResponse response) {
        String dataSource = request.getParameter("dataSourceName");
        String accountIdStr = request.getParameter("accountId");
        Integer accountId = Integer.parseInt(accountIdStr);
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        if (dataSource.equalsIgnoreCase("linkedIn")) {
            String companyId = getAccountId(accountProperty, "linkedinCompanyId");
            
            return linkedIn.get(request,Long.parseLong(companyId));
        }
        return null;
    }

    public String getFromMultiValueMap(MultiValueMap valueMap, String key) {
        List<String> dataSourceTypeList = (List<String>) valueMap.get(key);
        if (dataSourceTypeList != null && !dataSourceTypeList.isEmpty()) {
            return dataSourceTypeList.get(0);
        }
        return null;
    }

    private String getAccountId(List<Property> accountProperty, String propertyName) {
        String propertyAccountId = null;
        for (Iterator<Property> iterator = accountProperty.iterator(); iterator.hasNext();) {
            Property property = iterator.next();
            if (property.getPropertyName().equalsIgnoreCase(propertyName)) {
                propertyAccountId = property.getPropertyValue();
            }
        }
        return propertyAccountId;
    }

    @RequestMapping(value = "getDataSource", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getDataSource() {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            DB database = mongoClient.getDB("dashience");
            DBCollection collection = database.getCollection("dataSource");
            List<DBObject> allDataSources = collection.find().toArray();
            mongoClient.close();
            return allDataSources;
        } catch (Exception ex) {
            System.out.println("ex");
        }
        return null;
    }

    @RequestMapping(value = "getDataSets/{dataSetName}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Object getDataSets(HttpServletRequest request, HttpServletResponse response, @PathVariable String dataSetName) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            DB database = mongoClient.getDB("dashience");
            DBCollection collection = database.getCollection("dataSet");
            DBObject query = new BasicDBObject("name", dataSetName);
            List<DBObject> allDataSources = collection.find(query).toArray();
            mongoClient.close();
            return allDataSources;
        } catch (Exception ex) {
            System.out.println("ex");
        }
        return null;
    }

    @RequestMapping(value = "updateDataSets/{dataSetName}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public Object updateDataSets(HttpServletRequest request, HttpServletResponse response, @PathVariable String dataSetName) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            DB database = mongoClient.getDB("dashience");
            DBCollection collection = database.getCollection("dataSet");
            System.out.println("dataSet------->" + request);
            DBObject setQuery = new BasicDBObject("value", request.getAttribute("value"));
            DBObject whereQuery = new BasicDBObject("name", dataSetName);
            WriteResult allDataSources = collection.updateMulti(setQuery, whereQuery);
            mongoClient.close();
            return allDataSources;
        } catch (Exception ex) {
            System.out.println("ex");
        }
        return null;
    }

    @RequestMapping(value = "schedule", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object scheduleMethod(HttpServletRequest request, HttpServletResponse response, @RequestBody SchedulerTemplate schedulerTemplate) {
        try {
            scheduler.setDelay(schedulerTemplate.getCron());
            scheduler.start(schedulerTemplate);
        } catch (Exception ex) {
            System.out.println(ex);
            return "error occured";
        }
        return null;
    }

    @RequestMapping(value = "putDataSource", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public Object updateDataSource(HttpServletRequest request, HttpServletResponse response, @RequestBody String jsonString) {
        System.out.println("in update data source----------->" + jsonString);
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            DB database = mongoClient.getDB("dashience");
            DBCollection collection = database.getCollection("dataSource");
            System.out.println("dataSet------->" + request);
            DBObject dbObject = (DBObject) com.mongodb.util.JSON.parse(jsonString);
            DBObject whereQuery = new BasicDBObject("name", dbObject.get("name"));
            System.out.println("whereStatement--->" + whereQuery);
            BasicDBObject updateFields = new BasicDBObject();
            updateFields.append("name", dbObject.get("name"));
            updateFields.append("oauth", dbObject.get("oauth"));
            BasicDBObject setQuery = new BasicDBObject();
            setQuery.append("$set", updateFields);
            collection.update(whereQuery, setQuery);
            mongoClient.close();
            return "sucess";
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }
}
