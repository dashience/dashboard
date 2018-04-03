/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.TokenDao;
import com.visumbu.vb.admin.dao.UiDao;
import com.visumbu.vb.admin.oauth.service.TokenTemplate;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.Agency;
import com.visumbu.vb.model.DataSource;
import com.visumbu.vb.model.TokenDetails;
import com.visumbu.vb.model.VbUser;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author Lino
 */
@Service("TokenService")
public class TokenService extends BaseController{

    @Autowired
    private TokenDao tokenDao;
    @Autowired
    UserService userService;
    @Autowired
    UiDao uiDao;

    public void insertTokenDetails(TokenDetails tokenDetail) {
        tokenDao.create(tokenDetail);
    }

    public TokenDetails getTokenObject(MultiValueMap<String, Object> data, TokenTemplate tokenData) {
        TokenDetails tokenDetails = new TokenDetails();
        tokenDetails.setTokenValue(tokenData.getTokenValue());
        tokenDetails.setRefreshToken(tokenData.getRefreshToken());
        tokenDetails.setExpiryDate(Long.toString(tokenData.getExpiryDate()));
        tokenDetails.setScope(tokenData.getScope());
        tokenDetails.setClientId((String) data.getFirst("apiKey"));
        tokenDetails.setClientSecret((String) data.getFirst("apiSecret"));
        tokenDetails.setDataSourceType((String) data.getFirst("source"));
        tokenDetails.setAgencyId((Agency) data.getFirst("agency"));
        tokenDetails.setDataSourceId((DataSource) data.getFirst("dataSource"));
        return tokenDetails;
    }

    public TokenDetails insertIntoDb(MultiValueMap<String, Object> dataMap, TokenTemplate tokenDetails, HttpServletRequest request) throws Exception {
        System.out.println("user-------->"+getUser(request));
        VbUser user = userService.findByUsername(getUser(request));
        String dataSourceId = (String)dataMap.getFirst("dataSourceId");
        DataSource dataSource = uiDao.getDataSourceById(Integer.parseInt(dataSourceId));
        dataMap.add("agency", user.getAgencyId());
        dataMap.add("dataSource", dataSource);
        TokenDetails tokenData = getTokenObject(dataMap, tokenDetails);
        insertTokenDetails(tokenData);
        return tokenData;
    }
}
