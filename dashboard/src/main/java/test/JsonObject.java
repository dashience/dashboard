/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.itextpdf.text.pdf.languages.ArabicLigaturizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author deldot
 */
public class JsonObject {

    public static void main(String args[]) {
        
        printResult();
    }
    
    public static Object objectParsing() {
        JSONObject object=(JSONObject) getObject();
        System.out.println("Company name==>"+object.get("name"));
        return null;
    }
    
    public static List arrayObject()
    {
        JSONObject object=(JSONObject) getObject();
        JSONArray jsonArray=new JSONArray();
        jsonArray.add(object);
        System.out.println("----------------- INSIDE JSON ARRAY OBJECT----");
        System.out.println(jsonArray);
        return jsonArray;
    }
    
    public static Map objectArrayObject()
    {
        JSONObject object=(JSONObject) getObject();
        List objectArray=arrayObject();
        Map<String,Object> map=new HashMap<>();
        map.put("company",objectArray);
        System.out.println("----------------- INSIDE OBJECT OF JSON ARRAY OBJECT----");
        System.out.println(map);
        return map;
    }
    
    public static String printResult()
    {
        JSONObject object=(JSONObject) getObject();
        List objectArray=arrayObject();
        Map<String,Object> objectMap=objectArrayObject();
        System.out.println("-------------- RESULT ----");
//        List<Map> listArrayObject=new ArrayList<>();
        List<Map> listArrayObject = (List<Map>)objectMap.get("company");
//        listArrayObject.add((Map) objectMap.get("company"));
        System.out.println("***********************************************");
        System.out.println(listArrayObject);
        //[{data=[{OTHER=0, WWW=11, API=0, MOBILE=32}]}]
        //[{company=[{"Salary":"50000","Type":"Technology","name":"Ducima analytics"}]}]
        System.out.println("***********************************************");
        for (Iterator<Map> iterator = listArrayObject.iterator(); iterator.hasNext();) {
            Map arrayData = iterator.next();
            System.out.println("Company Name ="+arrayData.get("name"));
            System.out.println("Technology ="+arrayData.get("Type"));
            System.out.println("Salary ="+arrayData.get("Salary"));
        }
        
        return null;
    }
    
    public static Object getObject()
    {
        JSONObject object = new JSONObject();
        object.put("name", "Ducima analytics");
        object.put("Type", "Technology");
        object.put("Salary", "50000");
        return object;
    }
}
