/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author deeta1
 */
public class PropertyReader {

    private String url = "smtp.properties";
    ClassLoader classLoader = PropertyReader.class.getClassLoader();

    File file = new File(classLoader.getResource("mail/smtp.properties").getFile());
    File urlFile = new File(classLoader.getResource("urlChanges/url.properties").getFile());
    FileInputStream fileInput;
    Properties prop = new Properties();

    public String readProperties(String name) {
        String returnProp = null;
        try {
            fileInput = new FileInputStream(file);
            prop.load(fileInput);
            returnProp = prop.getProperty(name);
            fileInput.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnProp;
    }

    public String readUrl(String name) {
        String returnProp = null;
        try {
            fileInput = new FileInputStream(urlFile);
            prop.load(fileInput);
            returnProp = prop.getProperty(name);
            fileInput.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(PropertyReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnProp;
    }
}
