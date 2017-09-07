/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import com.visumbu.vb.bean.map.auth.SecurityAuthBean;
import com.visumbu.vb.bean.map.auth.SecurityTokenBean;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author vsamraj
 */
public class Rest {

    private final static String USER_AGENT = "ApiAgent";

    public static String getData(String urlString) {
        return getData(urlString, null);
    }

    public static String getData(String url, MultiValueMap<String, String> params) {
        String urlString = url;
        if (params != null) {
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();
            urlString = uriComponents.toUriString();
        }
        String returnStr = "";
        try {
            System.out.println("urlString:" + urlString);
            URL httpUrl = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("if condition");
                System.out.println(urlString);
                System.out.println("Code ---->" + conn.getResponseCode() + " Message ----> " + conn.getResponseMessage());
                return null;
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + conn.getResponseCode());
            } else {
                System.out.println("else condition");
                System.out.println(urlString);
                System.out.println("Code ---->" + conn.getResponseCode() + " Message ----> " + conn.getResponseMessage());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                returnStr += output;
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnStr;
    }

    public static String getData(String url, MultiValueMap<String, String> params, Map<String, String> header) {
        String urlString = url;
        if (params != null) {
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).build();
            urlString = uriComponents.toUriString();
        }
        String returnStr = "";
        try {
            System.out.println(urlString);
            URL httpUrl = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            for (Map.Entry<String, String> entrySet : header.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                conn.setRequestProperty(key, value);
            }
            conn.setRequestMethod("GET");

            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println(urlString);
                System.out.println("Code ---->" + conn.getResponseCode() + " Message ----> " + conn.getResponseMessage());
                return null;
//                throw new RuntimeException("Failed : HTTP error code : "
//                        + conn.getResponseCode());
            } else {
                System.out.println(urlString);
                System.out.println("Code ---->" + conn.getResponseCode() + " Message ----> " + conn.getResponseMessage());

            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                returnStr += output;
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnStr;
    }

    public static String postRawForm(String postUrl, String postParams) throws IOException {
        String returnStr = null;
        URL obj = new URL(postUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("charset", "utf-8");
        con.setUseCaches(false);
        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(postParams.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            System.out.println(response.toString());
            returnStr = response.toString();
        } else {
            System.out.println("POST request not worked");
        }
        return returnStr;
    }

    public static void main(String args[]) {
        try {
            String output = postRawForm(Settings.getSecurityTokenUrl(), "client_id=f8f06d06436f4104ade219fd7d535654&client_secret=ba082149c90f41c49e86f4862e22e980&grant_type=password&scope=FullControl&username=admin&password=admin123");
            ObjectMapper mapper = new ObjectMapper();
            SecurityTokenBean token = mapper.readValue(output, SecurityTokenBean.class);

            Map<String, String> accessHeader = new HashMap<>();
            accessHeader.put("Authorization", token.getAccessToken());
            String dataOut = getData(Settings.getSecurityAuthUrl() + "?Userid=00959ecd-41c5-4b92-8bd9-78c26d10486c", null, accessHeader);
            SecurityAuthBean authData = mapper.readValue(dataOut, SecurityAuthBean.class);

            System.out.println(authData);

        } catch (IOException ex) {
            Logger.getLogger(Rest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
