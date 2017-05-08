/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.admin.controller;

import com.visumbu.api.admin.service.UserService;
import com.visumbu.api.bean.LoginUserBean;
import com.visumbu.api.utils.Rest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@RequestMapping("proxy")
public class ProxyController {

    @RequestMapping(value = "getJson", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    void getJson(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getParameter("url");
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entrySet : parameterMap.entrySet()) {
            try {
                String key = entrySet.getKey();
                String[] value = entrySet.getValue();
                MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
                valueMap.put(key, Arrays.asList(value));
                String data = Rest.getData(url, valueMap);
                response.getOutputStream().write(data.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public @ResponseBody
    void get(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getParameter("url");
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entrySet : parameterMap.entrySet()) {
            try {
                String key = entrySet.getKey();
                String[] value = entrySet.getValue();
                MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
                valueMap.put(key, Arrays.asList(value));
                String data = Rest.getData(url, valueMap);
                response.getOutputStream().write(data.getBytes());
            } catch (IOException ex) {
                Logger.getLogger(ProxyController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        e.printStackTrace();
    }
}
