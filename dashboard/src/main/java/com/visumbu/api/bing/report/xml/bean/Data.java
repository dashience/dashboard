/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.bing.report.xml.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author user
 */
@XmlRootElement
public class Data {
    private String value;

    public String getValue() {
        return value;
    }

    @XmlAttribute(name="value")
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Data{" + "value=" + value + '}';
    }
}
