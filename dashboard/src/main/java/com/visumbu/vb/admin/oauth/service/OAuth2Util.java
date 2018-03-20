package com.visumbu.vb.admin.oauth.service;

import com.visumbu.vb.model.TokenDetails;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.visumbu.vb.admin.service.TokenService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service("oauth2Util")
class OAuth2Util {

    @Autowired
    OAuth2ServiceConfig oAuth2configs;
    @Autowired
    TokenService tokenService;

    private OAuth2Template oAuth2Template;
    private OAuth2Parameters oAuth2Parameters;

    public MultiValueMap<String, Object> facebookTokenUtil(String apiKey, String apiSecret, MultiValueMap<String, Object> returnMap) throws Exception {
        Map<String, List<String>> parameters = getParameterMap();
        parameters.put("redirect_uri", Arrays.asList(oAuth2configs.CALLBACK_URL));
        parameters.put("scope", Arrays.asList(""));
        oAuth2Parameters = new OAuth2Parameters(parameters);
        return OAuth2UrlGenerator(apiKey, apiSecret, oAuth2configs.facebookAuthorizeUrl, oAuth2configs.facebookAccessTokenUrl, returnMap);
    }
    

    public MultiValueMap<String, Object> linkedInTokenUtil(String apiKey, String apiSecret, MultiValueMap<String, Object> returnMap) throws Exception {
        Map<String, List<String>> parameters = getParameterMap();
        parameters.put("redirect_uri", Arrays.asList(oAuth2configs.CALLBACK_URL));
        parameters.put("scope", Arrays.asList(""));
        parameters.put("state", Arrays.asList("MNReFWf45A53sdfnet424"));
        oAuth2Parameters = new OAuth2Parameters(parameters);
        return OAuth2UrlGenerator(apiKey, apiSecret, oAuth2configs.linkedInAuthorieUrl, oAuth2configs.linkedInAccessTokenUrl, returnMap);
    }

    public MultiValueMap<String, Object> gaTokenUtil(String apiKey, String apiSecret, MultiValueMap<String, Object> returnMap) throws Exception {
        Map<String, List<String>> parameters = getParameterMap();
        parameters.put("redirect_uri", Arrays.asList(oAuth2configs.CALLBACK_URL));
        parameters.put("scope", Arrays.asList(oAuth2configs.gaScope));
        parameters.put("prompt", Arrays.asList("consent"));
        parameters.put("access_type", Arrays.asList("offline"));
        oAuth2Parameters = new OAuth2Parameters(parameters);
        return OAuth2UrlGenerator(apiKey, apiSecret, oAuth2configs.gaAuthorieUrl, oAuth2configs.gaAccessTokenUrl, returnMap);
    }

    public MultiValueMap<String, Object> OAuth2UrlGenerator(String apiKey, String apiSecret, String authorizeUrl, String accessTokenUrl, MultiValueMap<String, Object> returnMap) throws Exception {
        oAuth2Template = new OAuth2Template(apiKey, apiSecret, authorizeUrl, accessTokenUrl);
        String oauthUrl = oAuth2Template.buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE, oAuth2Parameters);
        returnMap.add("authorizeUrl", oauthUrl);
        return returnMap;
    }

    public TokenDetails exchangeForAccessToken(MultiValueMap<String, Object> dataMap) throws Exception {
        String code = (String) dataMap.getFirst("code");
        boolean useParameters = Boolean.parseBoolean((String) dataMap.getFirst("useParameters"));
        if (useParameters) {
            oAuth2Template.setUseParametersForClientAuthentication(true);
        }
        System.out.println("code--------->"+code);
        System.out.println("oAuth2Parameters--------->"+oAuth2Parameters.getRedirectUri());
        AccessGrant tokenDetails = oAuth2Template.exchangeForAccess(code, oAuth2Parameters.getRedirectUri(), null);
        TokenDetails tokenData = tokenService.getTokenObject(dataMap, tokenDetails);
        tokenService.insertTokenDetails(tokenData);
        return tokenData;
    }

    public void insertExtraDetails(MultiValueMap<String, Object> dataMap) throws Exception{
        HashMap<String, String> extraCredentials = (HashMap<String, String>) dataMap.getFirst("ExtraCredentials");
        if (extraCredentials != null) {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB database = mongoClient.getDB("dashience");
            DBCollection collection = database.getCollection("userDetails");
            DBObject json = new BasicDBObject();
            json.putAll(extraCredentials);
            collection.insert(json);
            mongoClient.close();
        }
    }
    public Map getParameterMap(){
        return new HashMap<>();
    }
}
