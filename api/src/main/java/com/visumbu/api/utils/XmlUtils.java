/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//import net.sf.json.JSON;
//import net.sf.json.xml.XMLSerializer;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author user
 */
public class XmlUtils {

    public static void main(String[] args) throws Exception {

//        InputStream is = new FileInputStream(new File("e:\\jagannathan\\personal\\java - projects\\secondtest.xml"));
//        String xml = IOUtils.toString(is);
        InputStream is = new FileInputStream("E:\\tmp\\bing-6YDFENK2KIBZOGVNXIQEGGP2YKFGDGEY.xml");
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();

        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }

        String fileAsString = sb.toString();
        // System.out.println("Contents : " + fileAsString);

        JSONObject xmlJSONObj = XML.toJSONObject(fileAsString); //.toString();
        String jsonPrettyPrintString = xmlJSONObj.toString(4);
        System.out.println(jsonPrettyPrintString);
        System.out.println(JsonUtils.toMap(xmlJSONObj));
//        XMLSerializer xmlSerializer = new XMLSerializer();
//        JSON json = xmlSerializer.read(xml);
//
//        System.out.println(json.toString(2));
//
//        printJSON(json.toString(2));

    }

    public static JSONObject getAsJson(String filename) {
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String fileAsString = sb.toString();
            // System.out.println("Contents : " + fileAsString);
            JSONObject xmlJSONObj = XML.toJSONObject(fileAsString); //.toString();
            return xmlJSONObj;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XmlUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XmlUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(XmlUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(XmlUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static Object getAsMap(String filename) {
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            String fileAsString = sb.toString();
            // System.out.println("Contents : " + fileAsString);
            JSONObject xmlJSONObj = XML.toJSONObject(fileAsString); //.toString();
            String jsonPrettyPrintString = xmlJSONObj.toString(4);
            System.out.println(jsonPrettyPrintString);
            return JsonUtils.toMap(xmlJSONObj);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XmlUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XmlUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(XmlUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(XmlUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static void printJSON(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();

        try {

            Map<String, Object> jsonInMap = mapper.readValue(jsonString,
                    new TypeReference<Map<String, Object>>() {
                    });

            List<String> keys = new ArrayList<String>(jsonInMap.keySet());

            for (String key : keys) {
                System.out.println(key + ":" + jsonInMap.get(key));
            }

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
