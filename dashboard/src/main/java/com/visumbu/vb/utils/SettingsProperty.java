/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import java.util.List;

/**
 *
 * @author deldot
 */

import com.visumbu.vb.model.Settings;
import java.util.Iterator;
public class SettingsProperty {
    
     public static String getSettingsProperty(List<Settings> property, String propertyName) {
        String propertyData = null;
        for (Iterator<Settings> iterator = property.iterator(); iterator.hasNext();) {
            Settings settings = iterator.next();
            if (settings.getPropertyName().equalsIgnoreCase(propertyName)) {
                propertyData = settings.getPropertyValue();
            }
        }
        return propertyData;
    }
  
}
