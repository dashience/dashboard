/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.api.dashboard.bean;

import java.util.Date;

/**
 *
 * @author user
 */
public class DbDateRange {
    private Date startDate;
    private Date endDate;
    private String rangeName; 
    
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

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    @Override
    public String toString() {
        return "DbDateRange{" + "startDate=" + startDate + ", endDate=" + endDate + ", rangeName=" + rangeName + '}';
    }
}
