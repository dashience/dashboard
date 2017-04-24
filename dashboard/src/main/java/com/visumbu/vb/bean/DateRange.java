/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.vb.bean;

import java.util.Date;

/**
 *
 * @author user
 */
public class DateRange {
    private Date startDate;
    private Date endDate;
    private String frequency;

    public DateRange(Date startDate, Date endDate, String frequency) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.frequency = frequency;
    }

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateRange() {
        System.out.println("This is DateRange Constructor");
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    
}
