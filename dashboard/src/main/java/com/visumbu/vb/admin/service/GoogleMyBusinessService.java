/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.google.api.client.json.JsonParser;
import com.visumbu.vb.utils.Rest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dashience
 */
@Service("googleMyBusinessService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GoogleMyBusinessService {

    private static String gmbAccessToken;

    public List<Map<String, Object>> get(String dataSetReportName, String gmbRefreshToken, String gmbAccountId, String clientId, String clientSecret,
            Date startDate, Date endDate, String timeSegment, String productSegment) {
                System.out.println("access token------->"+gmbAccessToken);
        gmbAccessToken = getAccessToken(gmbAccountId, gmbRefreshToken, clientId, clientSecret);
        System.out.println("access token------->"+gmbAccessToken);
        return null;
    }

    private String getAccessToken(String gmbAccountId, String gmbRefreshToken, String clientId, String clientSecret) {
        try {
            String url = "https://www.googleapis.com/oauth2/v4/token?refresh_token=" + gmbRefreshToken + "&client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=refresh_token";
            String tokenData = Rest.postRawForm(url,"{}");
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(tokenData);
            JSONObject array = (JSONObject) jsonObj;
            String accessToken = (String) array.get("access_token");
            return accessToken;
        } catch (ParseException ex) {
            Logger.getLogger(GoogleMyBusinessService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GoogleMyBusinessService.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error----->"+ex);
        }
        return null;
    }
    private String getLocations(String gmbAccountId) {
        try {
            String url = "https://www.googleapis.com/oauth2/v4/token?refresh_token=";
            String tokenData = Rest.getData(url);
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(tokenData);
            JSONObject array = (JSONObject) jsonObj;
            String accessToken = (String) array.get("access_token");
            return accessToken;
        } catch (ParseException ex) {
            Logger.getLogger(GoogleMyBusinessService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
