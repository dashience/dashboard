/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.oauth.service;

import com.visumbu.vb.model.TokenDetails;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author dashience
 */
@Service("oAuthSelector")
public class OAuthSelector {

    @Autowired
    private OAuth2Util oauth2Util;

    @Autowired
    private OAuth1Util oauth1Util;

    public MultiValueMap<String, Object> generateView(HttpServletRequest request) throws Exception {
        MultiValueMap<String, Object> returnMap = new LinkedMultiValueMap<>();
        String apiKey = request.getParameter("apiKey");
        String apiSecret = request.getParameter("apiSecret");
        String apiSource = request.getParameter("apiSource");
        returnMap.add("source", apiSource);
        if (apiSource.equals("facebook")) {
            returnMap.add("oauthType", "OAuth2");
            returnMap.add("useParameters", "false");
            return oauth2Util.facebookTokenUtil(apiKey, apiSecret, returnMap);
        } else if (apiSource.equals("linked in")) {
            Map<String,Object> parameters = new HashMap<>();
            parameters.put("companyId", request.getParameter("companyId"));
            parameters.put("source", apiSource);
            returnMap.add("ExtraCredentials", parameters);
            returnMap.add("oauthType", "OAuth2");
            returnMap.add("useParameters", "true");
            return oauth2Util.linkedInTokenUtil(apiKey, apiSecret, returnMap);
        } else if (apiSource.equals("twitter")) {
            returnMap.add("oauthType", "OAuth1");
            return oauth1Util.generateOAuth1Url(apiKey, apiSecret, returnMap);
        } else if (apiSource.equals("googleAnalytics")) {
            returnMap.add("oauthType", "OAuth2");
            returnMap.add("useParameters", "true");
            return oauth2Util.gaTokenUtil(apiKey, apiSecret, returnMap);
        }
        return returnMap;
    }

    public TokenDetails getTokenDetails(MultiValueMap<String, Object> dataMap) throws Exception {

        String oauthType = (String) dataMap.getFirst("oauthType");
        if (oauthType.equalsIgnoreCase("OAuth2")) {
            return oauth2Util.exchangeForAccessToken(dataMap);
        }
        if (oauthType.equalsIgnoreCase("OAuth1")) {
            return oauth1Util.exchangeForAccessToken(dataMap);
        }
        return null;
    }
}
