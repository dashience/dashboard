/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.oauth.service;

import com.visumbu.vb.admin.service.TokenService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.TokenDetails;
import com.visumbu.vb.model.VbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Lino
 */
@Service
public class TokenDBUtils extends BaseController{

    @Autowired
    TokenService tokenService;
    @Autowired
    private UserService userService;

    public TokenDetails insertIntoDb(MultiValueMap<String, Object> dataMap, TokenTemplate tokenDetails,HttpServletRequest request) throws Exception {
        VbUser user = userService.findByUsername(getUser(request));
        TokenDetails tokenData = tokenService.getTokenObject(dataMap, tokenDetails);
        tokenService.insertTokenDetails(tokenData);
        return tokenData;
    }
}
