/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.oauth.service;

import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.VbUser;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author Lino
 */
@Service("oAuthSelector")
public class OAuthSelectorImpl extends BaseController implements OAuthSelector {

    @Autowired
    private OAuth2Util oauth2Util;

    @Autowired
    private OAuth1Util oauth1Util;

    @Autowired
    private UserService userService;

    @Override
    public MultiValueMap<String, Object> generateOAuthUrl(HttpServletRequest request) throws Exception {
        MultiValueMap<String, Object> returnMap = new LinkedMultiValueMap<>();
        String apiKey = request.getParameter("apiKey");
        String apiSecret = request.getParameter("apiSecret");
        String apiSource = request.getParameter("apiSource");
        System.out.println("reached apisource-------->");
        VbUser user = userService.findByUsername(getUser(request));
        returnMap.add("agencyId", user.getAgencyId());
        returnMap.add("apiKey", apiKey);
        returnMap.add("apiSecret", apiSecret);
        returnMap.add("source", apiSource);
        if (apiSource.equals("facebook")) {
            returnMap.add("oauthType", "OAuth2");
            returnMap.add("useParameters", "false");
            return oauth2Util.facebookTokenUtil(apiKey, apiSecret, returnMap);
        } else if (apiSource.equals("linkedin")) {
            Map<String, Object> parameters = new HashMap<>();
            returnMap.add("ExtraCredentials", parameters);
            returnMap.add("oauthType", "OAuth2");
            returnMap.add("useParameters", "true");
                      System.out.println("reached apisource--2------>");
            return oauth2Util.linkedInTokenUtil(apiKey, apiSecret, returnMap);
        } else if (apiSource.equals("twitter")) {
            returnMap.add("oauthType", "OAuth1");
            return oauth1Util.generateOAuth1Url(apiKey, apiSecret, returnMap);
        } else if (apiSource.equalsIgnoreCase("google Analytics")) {
            returnMap.add("oauthType", "OAuth2");
            returnMap.add("useParameters", "true");
            return oauth2Util.gaTokenUtil(apiKey, apiSecret, returnMap);
        }
        return returnMap;
    }

    @Override
    public TokenTemplate getTokenDetails(MultiValueMap<String, Object> dataMap) throws Exception {

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
