/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.GoogleMyBusinessService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.Property;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author lino
 */
@Controller
@Scope("session")
@RequestMapping("sessionStorage")
public class SessionStorageController {

    @Autowired
    private GoogleMyBusinessService googleMyBusinessService;

    @Autowired
    private UserService userService;

    @RequestMapping(value ="{accountId}")
    public @ResponseBody
    void getLocationList(@PathVariable Integer accountId) {
        Account account = userService.getAccountId(accountId);
        List<Property> accountProperty = userService.getPropertyByAccountId(account.getId());
        String gmbRefreshToken = getAccountId(accountProperty, "gmbRefreshToken");
        if(gmbRefreshToken == null){
            return;
        }
        String gmbAccountId = getAccountId(accountProperty, "gmbAccountId");
        String gmbClientId = getAccountId(accountProperty, "gmbClientId");
        String gmbClientSecret = getAccountId(accountProperty, "gmbClientSecret");
        System.out.println("gmbRefreshToken ----->" + gmbRefreshToken);
        System.out.println("gmbAccountId ----->" + gmbAccountId);
        System.out.println("gmbClientId ----->" + gmbClientId);
        System.out.println("gmbClientSecret ----->" + gmbClientSecret);
        googleMyBusinessService.getLocations(gmbRefreshToken, gmbAccountId, gmbClientId, gmbClientSecret);
    }

    private String getAccountId(List<Property> accountProperty, String propertyName) {
        String propertyAccountId = null;
        for (Iterator<Property> iterator = accountProperty.iterator(); iterator.hasNext();) {
            Property property = iterator.next();
            if (property.getPropertyName().equalsIgnoreCase(propertyName)) {
                propertyAccountId = property.getPropertyValue();
            }
        }
        return propertyAccountId;
    }
}
