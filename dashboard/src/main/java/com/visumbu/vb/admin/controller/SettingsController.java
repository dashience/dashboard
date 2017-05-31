/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.controller;

import com.visumbu.vb.admin.service.SettingsService;
import com.visumbu.vb.model.Settings;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Aj
 */
@Controller
@RequestMapping("settings")
public class SettingsController {
    
    @Autowired
    private SettingsService settingsService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    Settings addVinSettings(HttpServletRequest request, HttpServletResponse response, @RequestBody Settings settings) {
        return settingsService.addVinSettings(settings);
    }

    @RequestMapping(value = "getSettings", method = RequestMethod.GET)
    public @ResponseBody
    List<Settings> getVinSettings(HttpServletRequest request, HttpServletResponse response) {
        return settingsService.getVinSettings();
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody
    Settings updateVinSettings(HttpServletRequest request, HttpServletResponse response, @RequestBody Settings settings) {
        return settingsService.updateVinSettings(settings);
    }
    
}
