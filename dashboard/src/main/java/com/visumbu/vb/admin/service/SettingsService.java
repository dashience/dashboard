/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.SettingsDao;
import com.visumbu.vb.model.Settings;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Aj
 */
@Service("settingsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SettingsService {
    
    @Autowired
    private SettingsDao settingsDao;

    public Settings addVinSettings(Settings settings) {
        return (Settings) settingsDao.create(settings);
    }

    public List<Settings> getVinSettings() {
       return settingsDao.read(Settings.class);
    }

    public Settings updateVinSettings(Settings settings) {
        return (Settings) settingsDao.update(settings);
    }
    
}
