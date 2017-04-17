/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.FieldSettingsService;
import com.visumbu.vb.model.DefaultFieldProperties;
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
 * @author deeta
 */
@Controller
@RequestMapping("fieldSettings")

public class FieldSettingsController {
    @Autowired
    private FieldSettingsService fieldSettingsService;
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    DefaultFieldProperties create(HttpServletRequest request, HttpServletResponse response, @RequestBody DefaultFieldProperties defaultFieldProperties) {
        return fieldSettingsService.create(defaultFieldProperties);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    DefaultFieldProperties update(HttpServletRequest request, HttpServletResponse response, @RequestBody DefaultFieldProperties defaultFieldProperties) {
        return fieldSettingsService.update(defaultFieldProperties);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List read(HttpServletRequest request, HttpServletResponse response) {
        return fieldSettingsService.read();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DefaultFieldProperties delete(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
        return fieldSettingsService.delete(id);
    }
    
    @RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody
    DefaultFieldProperties delete(HttpServletRequest request, HttpServletResponse response, @RequestBody DefaultFieldProperties defaultFieldProperties) {
        return fieldSettingsService.delete(defaultFieldProperties);
    }

   
}
