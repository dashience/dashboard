/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.admin.service;

import com.visumbu.vb.admin.dao.FieldSettingsDao;
import com.visumbu.vb.model.DefaultFieldProperties;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author deeta
 */
@Service("fieldSettingsService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FieldSettingsService {
     @Autowired
    private FieldSettingsDao fieldSettingsDao;

    public DefaultFieldProperties create(DefaultFieldProperties defaultFieldProperties) {
        return (DefaultFieldProperties) fieldSettingsDao.create(defaultFieldProperties);
    }

    public DefaultFieldProperties read(Integer id) {
        return (DefaultFieldProperties) fieldSettingsDao.read(DefaultFieldProperties.class, id);
    }

    public List<DefaultFieldProperties> read() {
        List<DefaultFieldProperties> defaultFieldProperties = fieldSettingsDao.read(DefaultFieldProperties.class);
        return defaultFieldProperties;
    }

    public DefaultFieldProperties update(DefaultFieldProperties defaultFieldProperties) {
        return (DefaultFieldProperties) fieldSettingsDao.update(defaultFieldProperties);
    }

    public DefaultFieldProperties delete(Integer id) {
        DefaultFieldProperties defaultFieldProperties = read(id);
        return (DefaultFieldProperties) fieldSettingsDao.delete(defaultFieldProperties);
        //return dealer;
    }

    public DefaultFieldProperties delete(DefaultFieldProperties defaultFieldProperties) {
        return (DefaultFieldProperties) fieldSettingsDao.delete(defaultFieldProperties);
    }
}
