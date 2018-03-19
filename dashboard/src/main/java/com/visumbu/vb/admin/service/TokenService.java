/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.TokenDao;
import com.visumbu.vb.model.TokenDetails;
import com.visumbu.vb.model.TokenDetailsPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author Lino
 */
@Service("TokenService")
public class TokenService {

    @Autowired
    private TokenDao tokenDao;
    public void insertTokenDetails(TokenDetails tokenDetail) {
        tokenDao.create(tokenDetail);
    }

    public TokenDetails getTokenObject(MultiValueMap<String, Object> data, AccessGrant tokenData) {
        TokenDetails tokenDetails = new TokenDetails();
        TokenDetailsPK tokenDetailsPK = new TokenDetailsPK();
        tokenDetails.setTokenValue(tokenData.getAccessToken());
        tokenDetails.setRefreshToken(tokenData.getRefreshToken());
        tokenDetails.setExpiryDate(Long.toString(tokenData.getExpireTime()));
        tokenDetails.setScope(tokenData.getScope());
        tokenDetails.setClientId((String) data.getFirst("clientId"));
        tokenDetails.setClientSecret((String) data.getFirst("clientSecret"));
        tokenDetails.setDataSourceType((String) data.getFirst("source"));
        tokenDetailsPK.setAgencyId((int) data.getFirst("agencyId"));
        tokenDetailsPK.setId((int) data.getFirst("accountId"));
        tokenDetails.setTokenDetailsPK(tokenDetailsPK);
        return tokenDetails;
    }
}
