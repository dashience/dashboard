/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.oauth.service;

import com.visumbu.vb.model.TokenDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuth1Template;
import org.springframework.social.oauth1.OAuth1Version;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author dashience
 */
@Service("oauth1Util")
class OAuth1Util {

    @Autowired
    OAuth1ServiceConfig oAuth1configs;
    
    public OAuth1Template oAuth1Template;
    public OAuth1Parameters oAuth1Parameters;
    public OAuthToken requestToken;

    public MultiValueMap<String, Object> generateOAuth1Url(String apiKey, String apiSecret, MultiValueMap<String, Object> returnMap) {
        oAuth1Template = new OAuth1Template(apiKey, apiSecret, oAuth1configs.twitterRequestTokenUrl, oAuth1configs.twitterAuthorizeUrl, oAuth1configs.twitterAuthenticateUrl, oAuth1configs.twitterAccessTokenUrl, OAuth1Version.CORE_10_REVISION_A);
        requestToken = oAuth1Template.fetchRequestToken(oAuth1configs.CALLBACK_URL, null);
        String oAuthUrl = oAuth1Template.buildAuthenticateUrl(requestToken.getValue(), OAuth1Parameters.NONE);
        returnMap.add("authorizeUrl", oAuthUrl);
        return returnMap;
    }

    
    public TokenDetails exchangeForAccessToken(MultiValueMap<String, Object> returnMap) {
        AuthorizedRequestToken setToken = new AuthorizedRequestToken(requestToken, (String) returnMap.getFirst("oauth_verifier"));
        OAuthToken tokenDetails = oAuth1Template.exchangeForAccessToken(setToken, OAuth1Parameters.NONE);
        TokenDetails tokenData = new TokenDetails();
        tokenData.setTokenValue(tokenDetails.getValue());
        tokenData.setTokenSecret(tokenDetails.getSecret());
        return tokenData;
    }

}
