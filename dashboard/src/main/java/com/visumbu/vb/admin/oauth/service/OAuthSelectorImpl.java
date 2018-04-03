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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    OAuth2ServiceConfig oAuth2configs;

    @Override
    public MultiValueMap<String, Object> generateOAuthUrl(HttpServletRequest request) throws Exception {
        String apiSource = request.getParameter("apiSource");
        String apiKey = request.getParameter("apiKey");
        String apiSecret = request.getParameter("apiSecret");
        String dataSourceId = request.getParameter("dataSourceId");
        Map<String, List<String>> parameters = new HashMap<>();
        Map<String, String> oauthUrls = new HashMap<>();
        if (apiSource.equals("facebook")) {
            parameters.put("redirect_uri", Arrays.asList(oAuth2configs.CALLBACK_URL));
            parameters.put("scope", Arrays.asList(""));

            oauthUrls.put("authorizeUrl", oAuth2configs.facebookAuthorizeUrl);
            oauthUrls.put("accessTokenUrl", oAuth2configs.facebookAccessTokenUrl);
            return oauth2ReturnMap(apiSource, apiKey, apiSecret, dataSourceId, parameters, oauthUrls);
        } else if (apiSource.equals("linkedin")) {
            parameters.put("redirect_uri", Arrays.asList(oAuth2configs.CALLBACK_URL));
            parameters.put("scope", Arrays.asList(""));
            parameters.put("state", Arrays.asList("MNReFWf45A53sdfnet424"));

            oauthUrls.put("authorizeUrl", oAuth2configs.linkedInAuthorieUrl);
            oauthUrls.put("accessTokenUrl", oAuth2configs.linkedInAccessTokenUrl);

            return oauth2ReturnMap(apiSource, apiKey, apiSecret, dataSourceId, parameters, oauthUrls);
        } else if (apiSource.equalsIgnoreCase("google Analytics")) {
            parameters.put("redirect_uri", Arrays.asList(oAuth2configs.CALLBACK_URL));
            parameters.put("scope", Arrays.asList(oAuth2configs.gaScope));
            parameters.put("prompt", Arrays.asList("consent"));
            parameters.put("access_type", Arrays.asList("offline"));

            oauthUrls.put("authorizeUrl", oAuth2configs.gaAuthorieUrl);
            oauthUrls.put("accessTokenUrl", oAuth2configs.gaAccessTokenUrl);

            return oauth2ReturnMap(apiSource, apiKey, apiSecret, dataSourceId, parameters, oauthUrls);
        }
//        else if (apiSource.equals("twitter")) {
//            return oauth1Util.generateOAuth1Url(apiKey, apiSecret, returnMap);
//        }
        return null;
    }

    public MultiValueMap<String, Object> oauth2ReturnMap(String apiSource, String apiKey, String apiSecret, String dataSourceId, Map<String, List<String>> parameters, Map<String, String> oauthUrls) throws Exception {
        MultiValueMap<String, Object> returnMap = new LinkedMultiValueMap<>();
        Map<String, Object> resultMap = oauth2Util.getOAuth2Token(apiKey, apiSecret, parameters, oauthUrls);
        returnMap.add("apiKey", apiKey);
        returnMap.add("apiSecret", apiSecret);
        returnMap.add("source", apiSource);
        returnMap.add("dataSourceId", dataSourceId);
        returnMap.add("oauthType", "OAuth2");
        returnMap.add("useParameters", "true");
        returnMap.add("authorizeUrl", resultMap.get("authorizeUrl"));
        return returnMap;
    }
    public MultiValueMap<String, Object> oauth1ReturnMap(String apiSource, String apiKey, String apiSecret, String dataSourceId, Map<String, List<String>> parameters, Map<String, String> oauthUrls) throws Exception {
        MultiValueMap<String, Object> returnMap = new LinkedMultiValueMap<>();
        Map<String, Object> resultMap = oauth2Util.getOAuth2Token(apiKey, apiSecret, parameters, oauthUrls);
        returnMap.add("apiKey", apiKey);
        returnMap.add("apiSecret", apiSecret);
        returnMap.add("source", apiSource);
        returnMap.add("dataSourceId", dataSourceId);
        returnMap.add("oauthType", "OAuth1");
        returnMap.add("authorizeUrl", resultMap.get("authorizeUrl"));
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
