/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.TemplateService;
import com.visumbu.vb.admin.service.UiService;
import com.visumbu.vb.admin.service.UserService;
import com.visumbu.vb.controller.BaseController;
import com.visumbu.vb.model.Account;
import com.visumbu.vb.model.AgencyProduct;
import com.visumbu.vb.model.DashboardTemplate;
import com.visumbu.vb.model.Product;
import com.visumbu.vb.model.ProductAccountUserTemplate;
import com.visumbu.vb.model.VbUser;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author arul
 */
@Controller
@RequestMapping("template")
public class TemplateController extends BaseController{
    @Autowired
    private TemplateService templateService;
    @Autowired
    private UserService userService;
    @Autowired
    private UiService uiService;
    
    @RequestMapping(value = "productAccountUserTemplate", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ProductAccountUserTemplate create(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductAccountUserTemplate accountTemplate) {
        VbUser vbUser = userService.findByUsername(getUser(request));
        accountTemplate.setUserId(vbUser);
        return templateService.create(accountTemplate);
    }

    @RequestMapping(value = "productAccountUserTemplate", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    ProductAccountUserTemplate update(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductAccountUserTemplate accountTemplate) {
        return templateService.update(accountTemplate);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List read(HttpServletRequest request, HttpServletResponse response) {
        return templateService.read();
    }
    
    @RequestMapping(value = "getProductTemplate/{productId}/{accountId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    DashboardTemplate getProductAccountUserTemplateById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer productId, @PathVariable Integer accountId) {
        Account account = uiService.getAccountById(accountId);
        AgencyProduct product = uiService.getAgencyProductById(productId);
        VbUser vbUser = userService.findByUsername(getUser(request));
        return templateService.getProductAccountUserTemplateById(vbUser, product, account);
    }

    @RequestMapping(value = "productAccountUserTemplate/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    ProductAccountUserTemplate delete(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return templateService.delete(id);
    }
}
