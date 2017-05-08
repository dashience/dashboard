/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.visumbu.api.admin.service.DbDataSetService;
import com.visumbu.api.admin.service.UserService;
import com.visumbu.api.bean.DbDataSource;
import com.visumbu.api.bean.LoginUserBean;
import com.visumbu.api.utils.ApiUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author jp
 */
@Controller
@RequestMapping("dataSet")
public class DbDataSetController {

    @Autowired
    private UserService userService;

    @Autowired
    private DbDataSetService dbDataSetService;

    @RequestMapping(value = "getData", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    Map getData(HttpServletRequest request, HttpServletResponse response) {
        String connectionUrl = request.getParameter("connectionUrl");
        String driver = request.getParameter("driver");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Integer port = ApiUtils.toInteger(request.getParameter("port"));
        String schemaName = request.getParameter("schema");

        String query = request.getParameter("query");
        System.out.println("Query ===> " + query);
        String fieldsOnly = request.getParameter("fieldsOnly");
        Map returnMap = new HashMap();

        Map<String, String[]> parameterMap = request.getParameterMap();

        DbDataSource dataSource = new DbDataSource(connectionUrl, driver, username, password, port, schemaName);
        System.out.println(dataSource);
        
        dataSource.setQuery(query);
        returnMap.put("columnDefs", dbDataSetService.getMeta(dataSource, parameterMap));
        if (fieldsOnly != null) {
            return returnMap;
        }
        returnMap.put("data", dbDataSetService.getData(dataSource, parameterMap));
        return returnMap;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
